/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

/**
 *
 * @author danya
 */
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FrameUI extends JFrame{
    
    // store list of skills
    private List<Skill> skills = new ArrayList<>();
 
    // Left side of UI (list of skills)
    private DefaultListModel<Skill> listModel = new DefaultListModel<>();
    private JList<Skill> skillList            = new JList<>(listModel);
 
    // Right side of UI (details)
    // set at first each label with (-)
    private JLabel nameLabel       = new JLabel("-");
    private JLabel categoryLabel   = new JLabel("-");
    private JLabel statusLabel     = new JLabel("-");
    private JLabel scoreLabel      = new JLabel("-");
    private JLabel lastUsedLabel   = new JLabel("-");
    private JLabel usageLabel      = new JLabel("-");
    private JLabel strategyLabel   = new JLabel("-");
    private JLabel messageLabel    = new JLabel("Select a skill from the list.");
    private JButton logBtn         = new JButton("Log Usage Today");
 
    public FrameUI() {
        // window/frame
        setTitle("SkillTracker");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
 
        createUI();
        loadDemo();
        refreshList();
    }
 
    private void createUI() {
        setLayout(new BorderLayout(5, 5));
 
        // top bar
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // button to add skills
        JButton addBtn = new JButton("Add Skill");
        // dialog box appears when clicked
        addBtn.addActionListener(e -> openAddDialog());
        topBar.add(new JLabel("SkillTracker  |  "));
        topBar.add(addBtn);
        add(topBar, BorderLayout.NORTH);
 
        // left side (My Skills)
        skillList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        skillList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) showSkills();
        });
        JScrollPane listScroll = new JScrollPane(skillList);
        listScroll.setPreferredSize(new Dimension(240, 0));
        listScroll.setBorder(BorderFactory.createTitledBorder("My Skills"));
        add(listScroll, BorderLayout.WEST);
 
        // right side (Skill Details)
        JPanel detail = new JPanel();
        detail.setLayout(new BoxLayout(detail, BoxLayout.Y_AXIS));
        detail.setBorder(BorderFactory.createTitledBorder("Skill Details"));
 
        detail.add(row("Skill Name:",  nameLabel));
        detail.add(row("Category:",    categoryLabel));
        detail.add(row("Status:",      statusLabel));
        detail.add(row("Score:",       scoreLabel));
        detail.add(row("Last Used:",   lastUsedLabel));
        detail.add(row("Times Logged:", usageLabel));
        detail.add(row("Strategy:",    strategyLabel));
        detail.add(Box.createVerticalStrut(10));
        detail.add(row("Note:", messageLabel));
        detail.add(Box.createVerticalStrut(15));
 
        logBtn.setEnabled(false);
        logBtn.addActionListener(e -> LogUsage());
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRow.add(logBtn);
        detail.add(btnRow);
 
        add(detail, BorderLayout.CENTER);
    }
 
    // method that creates a labeled row used in adding detail to avoid repetition
    private JPanel row(String key, JLabel value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JLabel k = new JLabel(key);
        k.setPreferredSize(new Dimension(110, 20));
        k.setFont(k.getFont().deriveFont(Font.BOLD));
        p.add(k);
        p.add(value);
        return p;
    }
 
    // Opens the Add Skill dialog
    private void openAddDialog() {
        JTextField nameField     = new JTextField(15);
        JTextField categoryField = new JTextField(15);
        String[] strategyOptions = {"Time-Based Decay", "Frequency-Based Decay"};
        JComboBox<String> strategyBox = new JComboBox<>(strategyOptions);
 
        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.add(new JLabel("Skill Name:"));   form.add(nameField);
        form.add(new JLabel("Category:"));     form.add(categoryField);
        form.add(new JLabel("Strategy:"));     form.add(strategyBox);
 
        int result = JOptionPane.showConfirmDialog(this, form,
                "Add New Skill", JOptionPane.OK_CANCEL_OPTION);
 
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Skill name cannot be empty.");
                return;
            }
            String cat = categoryField.getText().trim();
            if (cat.isEmpty()) cat = "General";
 
            // ** calss the chosen strategy
            DecayStrategy strategy = strategyBox.getSelectedIndex() == 0
                    ? new TimeBasedDecay()
                    : new FrequencyBasedDecay();
 
            Skill s = new Skill(name, cat, new SkillEvaluator(strategy));
            skills.add(s);
            refreshList();
            skillList.setSelectedIndex(listModel.size() - 1);
        }
    }
 
    // Updates the skill details panel when a skill is clicked
    private void showSkills() {
        Skill s = skillList.getSelectedValue();
        if (s == null) return;
 
        nameLabel.setText(s.getName());
        categoryLabel.setText(s.getCategory());
        statusLabel.setText(s.getState().getStatus());
        scoreLabel.setText((int) s.getScore() + " / 100");
        lastUsedLabel.setText(s.getLastUsedFormatted());
        usageLabel.setText(String.valueOf(s.getUsageCount()));
        strategyLabel.setText(s.getEvaluatorContext().getStrategy().toString());
        messageLabel.setText(s.getState().toString());
        logBtn.setEnabled(true);
    }
 
    // Called when "Log Usage Today" is clicked
    private void LogUsage() {
        Skill s = skillList.getSelectedValue();
        if (s == null) return;
        s.logUsage();
        refreshList();
        showSkills();
        JOptionPane.showMessageDialog(this,
            s.getName() + " logged!\nStatus is now: " + s.getState().getStatus(),
            "Usage Logged", JOptionPane.INFORMATION_MESSAGE);
    }
 
    // Rebuilds the list from the skills ArrayList
    private void refreshList() {
        int idx = skillList.getSelectedIndex();
        listModel.clear();
        for (Skill s : skills) {
            s.recomputeScore();
            listModel.addElement(s);
        }
        if (idx >= 0 && idx < listModel.size())
            skillList.setSelectedIndex(idx);
    }
 
    // 3 pre-loaded demo skills at different decay stages
    private void loadDemo() {
        Skill java = new Skill("Java", "Programming",
                new SkillEvaluator(new TimeBasedDecay()));
        skills.add(java);
 
        Skill sql = new Skill("SQL", "Database",
                new SkillEvaluator(new FrequencyBasedDecay()));
        sql.simulateLastUsed(LocalDate.now().minusDays(40), 5);
        skills.add(sql);
 
        Skill swift = new Skill("Swift", "Mobile",
                new SkillEvaluator(new TimeBasedDecay()));
        swift.simulateLastUsed(LocalDate.now().minusDays(85), 2);
        skills.add(swift);
    }
}

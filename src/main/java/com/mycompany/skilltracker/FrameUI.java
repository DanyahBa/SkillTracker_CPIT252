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
import java.util.List;

public class FrameUI extends JFrame{

    // to call queries that has the one instance of connection SINGLETON
    private SkillQuery db = new SkillQuery();

    // logged in user's id from login frame
    private int userId;

    // SUBJECT is storing skills and notifies observers when changes happen
    private Subject tracker = new Subject();

    // this OBSERVER counts how many skills are fresh, fading, outdated and updats when SUBJECT notifies
    private SummaryObserver summary;

    // this OBSERVER shows a warning when SUBJECT notifies change and there is skill fading
    private AlertObserver   alert = new AlertObserver();

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
    private JLabel freshCount    = new JLabel("Fresh: 0");
    private JLabel fadingCount   = new JLabel("Fading: 0");
    private JLabel outdatedCount = new JLabel("Outdated: 0");

    public FrameUI(int userId) {
        this.userId = userId;
        // window/frame
        setTitle("SkillTracker");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // attach OBSERVERS to the SUBJECT
        summary = new SummaryObserver(tracker.getSkills());
        tracker.attach(summary);
        tracker.attach(alert);

        createUI();
        loadFromDatabase(); // shared connection
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

        // bottom (summary)
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 6));
        bottomBar.setBorder(BorderFactory.createTitledBorder("Summary"));
        freshCount.setForeground(new Color(39, 174, 96));
        fadingCount.setForeground(new Color(200, 120, 0));
        outdatedCount.setForeground(new Color(192, 57, 43));
        bottomBar.add(freshCount);
        bottomBar.add(fadingCount);
        bottomBar.add(outdatedCount);
        add(bottomBar, BorderLayout.SOUTH);
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

    // loads this user skills from the database
    private void loadFromDatabase() {
        List<Skill> saved = db.loadSkills(userId); // from query class that has shared connection
        for (Skill s : saved) {
            tracker.addSkill(s); // SUBJECT adds skill + notifies observers
        }
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

            // ** calss the chosen STRATEGY
            DecayStrategy strategy = strategyBox.getSelectedIndex() == 0
                    ? new TimeBasedDecay()
                    : new FrequencyBasedDecay();

            Skill s = new Skill(name, cat, new SkillEvaluator(strategy));
            tracker.addSkill(s); // SUBJECT adds a skill + notifies observers automatic -> by calling update
            db.saveSkill(userId, s);
            refreshList();
            skillList.setSelectedIndex(listModel.size() - 1);
        }
    }

    // Updates the skill details panel when a skill is clicked
    private void showSkills() {
        Skill s = skillList.getSelectedValue();
        if (s == null)
            return;

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
        if (s == null)
            return;
        tracker.logSkillUsage(s); // SUBJECT handles log + notify observers automatic -> by calling update
        db.updateSkill(userId, s);
        refreshList();
        showSkills();
        updateSummaryBar();
    }

    private void refreshList() {
        int idx = skillList.getSelectedIndex();
        listModel.clear();
        for (Skill s : tracker.getSkills()) {
            listModel.addElement(s);
        }
        if (idx >= 0 && idx < listModel.size())
            skillList.setSelectedIndex(idx);
        updateSummaryBar();
    }

    private void updateSummaryBar() {
        freshCount.setText("Fresh: "    + summary.getFresh());
        fadingCount.setText("Fading: "  + summary.getFading());
        outdatedCount.setText("Outdated: " + summary.getOutdated());
    }
}
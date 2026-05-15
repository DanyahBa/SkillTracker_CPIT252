/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;
import javax.swing.*;
import java.awt.*;

// Login UI frame
public class LoginFrame extends JFrame{

    private JTextField phoneField = new JTextField(15);
    private SkillQuery db    = new SkillQuery();

    public LoginFrame() {
        setTitle("Login");
        setSize(350, 180);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Welcome to SkillTracker", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        form.add(new JLabel("Phone Number:"));
        form.add(phoneField);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> onLogin());
        form.add(new JLabel(""));
        form.add(loginBtn);
        add(form, BorderLayout.CENTER);
    }

    private void onLogin() {
        String phone = phoneField.getText().trim();

        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your phone number.");
            return;
        }

        // uses SkillQuery class to check if phone exists in DB
        int userId = db.login(phone);

        if (userId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Phone number not found.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            // phone found so open main app with this user skills
            new FrameUI(userId).setVisible(true);
            dispose();
        }
    }
}

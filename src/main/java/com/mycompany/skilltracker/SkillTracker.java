/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.skilltracker;

/**
 *
 * @author danya
 */
import javax.swing.SwingUtilities;

public class SkillTracker {

    public static void main(String[] args) {

        // databse setup
        SkillDB.setup();

        // used swing to make a desktop app
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
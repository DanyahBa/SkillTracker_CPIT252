/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

/**
 *
 * @author maryh
 */
import javax.swing.JOptionPane;

// Concrete Observer (AlertObserver) shows warning when skill is fading/outdated
public class AlertObserver implements SkillObserver{
    
    @Override
    public void update(Skill s){
        // get status of this skill
        String status = s.getState().getStatus();
        // notifies and gives a warning that a skill is fading
        if (status.equals("Fading") || status.equals("Outdated")) {
            JOptionPane.showMessageDialog(null,
                "Warning: " + s.getName() + " is " + status + "!\n" +
                s.getState().toString(),
                "Skill Alert", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    @Override
    public String getName() {
        return "Alert Observer";
    }
}

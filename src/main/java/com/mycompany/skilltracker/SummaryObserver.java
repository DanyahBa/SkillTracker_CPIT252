/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

import java.util.List;

/**
 *
 * @author maryh
 */

// Concrete Observer (SummaryObserver) track skills count and their state
public class SummaryObserver implements SkillObserver{
    
    private int fresh    = 0;
    private int fading   = 0;
    private int outdated = 0;
    
    private List<Skill> skills;

    public SummaryObserver(List<Skill> skills) {
        this.skills = skills;
    }

    @Override
    public void update(Skill skill) {
        // Recount all skills every time any skill changes so make all 0'z
        fresh = fading = outdated = 0;
        for (Skill s : skills) {
            String status = s.getState().getStatus();
            // start incrementing
            if (status.equals("Fresh"))    fresh++;
            else if (status.equals("Fading"))   fading++;
            else if (status.equals("Outdated")) outdated++;
        }
    }
    
    // to get its count later
    public int getFresh(){
        return fresh;
    }
    public int getFading(){
        return fading;
    }
    public int getOutdated(){
        return outdated;
    }

    @Override
    public String getName() {
        return "Summary Observer";
    }
}

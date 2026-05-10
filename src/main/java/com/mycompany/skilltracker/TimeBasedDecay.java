/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

/**
 *
 * @author danya
 */
import java.time.LocalDate;
 
// Concrete Strategy (TimeBasedDecay) evaluating scores based on days since last use
public class TimeBasedDecay implements DecayStrategy {
 
    // takes last date the skill was used
    @Override
    public double computeScore(LocalDate lastUsed, int usageCount) {
        
        // if not used return (0)
        if (lastUsed == null)
            return 0.0;
        
        // calculates days since last used the skill and today
        // gets the date and convert to total days number 
        long days = LocalDate.now().toEpochDay() - lastUsed.toEpochDay();
        
        // if skill was used today
        if (days <= 0) 
            return 100.0;
        // if skill used before 90 days
        if (days >= 90)
            return 0.0;
        // if between 1 and 90 calculates score
        // 100% - forgotten percentage
        return 100.0 - (days / 90.0) * 100.0;
    }
 
    @Override
    public String toString() {
        return "Time-Based Decay";
    }
}

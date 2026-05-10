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
 
// Concrete Strategy (FrequencyBasedDecay) evaluation score consider both how recent and the usage frequency
public class FrequencyBasedDecay implements DecayStrategy {
 
    // takes last date the skill was used and number of times the skill was used
    @Override
    public double computeScore(LocalDate lastUsed, int usageCount) {
        
        // if not used return (0)
        if (lastUsed == null)
            return 0.0;
        
        // calculates days since last used the skill and today
        // gets the date and convert to total days number 
        long days = LocalDate.now().toEpochDay() - lastUsed.toEpochDay();
 
        // how recent the skill was used
        // if more than 90 = 0.0
        // if between 1 and 90 calculate remaining percentage
        double recency   = (days >= 90) ? 0.0 : (1.0 - days / 90.0) * 100.0;
        // calculates score based on how often skill was practiced
        // 20 uses for full freq score
        double frequency = Math.min(usageCount / 15.0, 1.0) * 100.0;
 
        // combines both with recency being more important
        return (recency * 0.70) + (frequency * 0.30);
    }
 
    @Override
    public String toString() {
        return "Frequency-Based Decay";
    }
}

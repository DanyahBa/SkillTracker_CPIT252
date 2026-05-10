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

// Context classfor strategy pattern
public class SkillEvaluator {
    
    private DecayStrategy strategy;
    
    // constructor that takes DecayStrategy in its argument
    public SkillEvaluator(DecayStrategy strategy) {
        this.strategy = strategy;
    }
    
    public DecayStrategy getStrategy() {
        return strategy;
    }
    
    // this is what gets called from the context and based on the strategy it calls a certain algorithm
    public double computeScore(LocalDate lastUsed, int usageCount) {
        return strategy.computeScore(lastUsed, usageCount);
    }
}

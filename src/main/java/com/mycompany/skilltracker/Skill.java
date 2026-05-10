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
import java.time.format.DateTimeFormatter;

// Context class for state design pattern
// state is what is being tracked and computed
public class Skill {
    
    private String name;
    private String category;
    private LocalDate lastUsed;
    private int usageCount;
    private SkillState state;
    private SkillEvaluator evaluatorContext;
    private double score;
 
    public Skill(String name, String category, SkillEvaluator evaluatorContext) {
        this.name             = name;
        this.category         = category;
        this.evaluatorContext = evaluatorContext;
        this.lastUsed         = LocalDate.now();
        this.usageCount       = 1;
        this.state            = new FreshState();
        recomputeScore();
    }
 
    // Log that the skill was practiced in todays date
    public void logUsage() {
        this.lastUsed = LocalDate.now();
        this.usageCount++;
        recomputeScore();
    }
 
    // Recompute score using the current strategy selectes, then let the state evaluate
    public void recomputeScore() {
        this.score = evaluatorContext.computeScore(lastUsed, usageCount);
        state.evaluate(this);
    }
 
    // Called by concrete states to transition the states
    public void setState(SkillState newState) {
        this.state = newState;
    }
 
    // Used to set up demo skills with already defined data
    public void simulateLastUsed(LocalDate date, int count) {
        this.lastUsed   = date;
        this.usageCount = count;
        recomputeScore();
    }
 
    public String getName(){
        return name;
    }
    public String getCategory(){
        return category;
    }
    public LocalDate getLastUsed(){
        return lastUsed;
    }
    public int getUsageCount(){
        return usageCount;
    }
    public SkillState getState(){
        return state;
    }
    public double getScore(){
        return score;
    }
    public SkillEvaluator getEvaluatorContext(){
        return evaluatorContext;
    }
 
    public String getLastUsedFormatted() {
        return lastUsed.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }
 
    @Override
    public String toString() {
        return name + " [" + state.getStatus() + "] - " + (int) score + "%";
    }
}

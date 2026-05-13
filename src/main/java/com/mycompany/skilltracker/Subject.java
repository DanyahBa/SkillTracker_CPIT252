/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author maryhan
 */

// Subject class in the Observer pattern
// store skill list and notifies observers when skills change 
public class Subject {
    
    private List<Skill>         skills    = new ArrayList<>();
    private List<SkillObserver> observers = new ArrayList<>();

    // Attach observer
    public void attach(SkillObserver observer) {
        observers.add(observer);
    }

    // Detach observer
    public void detach(SkillObserver observer) {
        observers.remove(observer);
    }

    // Notify all OBSERVERS that a skill has changed
    public void notifyObservers(Skill skill) {
        for (SkillObserver o : observers) {
            o.update(skill);
        }
    }
    
    // Add a skill and notify observers
    public void addSkill(Skill skill) {
        skills.add(skill);
        notifyObservers(skill);
    }

    // Log usage of a skill and notify observers
    public void logSkillUsage(Skill skill) {
        skill.logUsage();
        notifyObservers(skill);
    }

    public List<Skill> getSkills() {
        return skills;
    }
}

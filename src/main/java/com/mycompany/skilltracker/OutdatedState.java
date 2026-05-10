/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

/**
 *
 * @author danya
 */

// concrete state OutdateshState which is the third state
public class OutdatedState implements SkillState {
 
    // if score greater than 60 then next state which is fresh
    // else if greater than 25 means state becomes fading
    @Override
    public void evaluate(Skill skill) {
        if (skill.getScore() >= 60)
            skill.setState(new FreshState());
        else if (skill.getScore() >= 25)
            skill.setState(new FadingState());
    }
 
    @Override
    public String getStatus() {
        return "Outdated";
    }
 
    @Override
    public String toString() {
        return "This skill is outdated! Practice as soon as possible.";
    }
}

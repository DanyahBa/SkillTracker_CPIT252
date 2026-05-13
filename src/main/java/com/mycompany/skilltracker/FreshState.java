/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

/**
 *
 * @author danya
 */

// concrete state FreshState which is the first state
public class FreshState implements SkillState{
    
    // if score less than 60 then next state which is starting to fade
    @Override
    public void evaluate(Skill skill) {
        if (skill.getScore() > 25 && skill.getScore() < 60)
            skill.setState(new FadingState());
        else if (skill.getScore() < 25)
            skill.setState(new OutdatedState());
    }
 
    @Override
    public String getStatus() {
        return "Fresh";
    }
 
    @Override
    public String toString() {
        return "Great job! This skill is actively maintained.";
    }
}

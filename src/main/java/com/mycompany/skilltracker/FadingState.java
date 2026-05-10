/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

/**
 *
 * @author danya
 */

// concrete state FadinghState which is the second state
public class FadingState implements SkillState {
 
    // if score greater than 60 then next state which is fresh
    // else if less than 25 means state becoming outdated state
    @Override
    public void evaluate(Skill skill) {
        if (skill.getScore() >= 60)
            skill.setState(new FreshState());
        else if (skill.getScore() < 25)
            skill.setState(new OutdatedState());
    }
 
    @Override
    public String getStatus() {
        return "Fading";
    }
 
    @Override
    public String toString() {
        return "This skill is starting to fade. Practice it soon!";
    }
}

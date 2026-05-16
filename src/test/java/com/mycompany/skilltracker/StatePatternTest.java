/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;



import org.junit.jupiter.api.Test; 
import static org.junit.Assert.*;
import java.lang.reflect.Modifier;
import java.time.LocalDate;

public class StatePatternTest {

   // SkillState must be an interface
    @Test
    public void testSkillStateIsInterface() {
        assertTrue(Modifier.isInterface(SkillState.class.getModifiers()));
    }
 
    // All 3 states must implement SkillState
    @Test
    public void testAllStatesImplementInterface() {
        assertTrue(FreshState.class.getInterfaces()[0].equals(SkillState.class));
        assertTrue(FadingState.class.getInterfaces()[0].equals(SkillState.class));
        assertTrue(OutdatedState.class.getInterfaces()[0].equals(SkillState.class));
    }
 
    // Each state must return the correct status name
    @Test
    public void testStateStatusNames() {
        assertEquals("Fresh",    new FreshState().getStatus());
        assertEquals("Fading",   new FadingState().getStatus());
        assertEquals("Outdated", new OutdatedState().getStatus());
    }
 
    // A new skill used today should be Fresh
    @Test
    public void testNewSkillIsFresh() {
        Skill skill = new Skill("Java", "Programming", new SkillEvaluator(new TimeBasedDecay()));
        assertEquals("Fresh", skill.getState().getStatus());
    }
 
    // A skill unused for 45 days should become Fading
    @Test
    public void testSkillBecomeFading() {
        Skill skill = new Skill("Java", "Programming", new SkillEvaluator(new TimeBasedDecay()));
        skill.simulateLastUsed(LocalDate.now().minusDays(45), 1);
        assertEquals("Fading", skill.getState().getStatus());
    }
 
    // A skill unused for 90 days should become Outdated
    @Test
    public void testSkillBecomeOutdated() {
        Skill skill = new Skill("Java", "Programming", new SkillEvaluator(new TimeBasedDecay()));
        skill.simulateLastUsed(LocalDate.now().minusDays(90), 1);
        assertEquals("Outdated", skill.getState().getStatus());
    }
 
    // Logging usage on an outdated skill should bring it back to Fresh
    @Test
    public void testSkillRecoveryToFresh() {
        Skill skill = new Skill("Java", "Programming", new SkillEvaluator(new TimeBasedDecay()));
        skill.simulateLastUsed(LocalDate.now().minusDays(90), 1);
        skill.logUsage();
        assertEquals("Fresh", skill.getState().getStatus());
    }
}
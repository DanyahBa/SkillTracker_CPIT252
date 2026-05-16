/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;



import org.junit.jupiter.api.Test; 
import static org.junit.Assert.*;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ObserverPatternTest {

    // SkillObserver must be an interface
    @Test
    public void testSkillObserverIsInterface() {
        assertTrue(Modifier.isInterface(SkillObserver.class.getModifiers()));
    }
 
    // Both observers must implement SkillObserver
    @Test
    public void testObserversImplementInterface() {
        assertTrue(AlertObserver.class.getInterfaces()[0].equals(SkillObserver.class));
        assertTrue(SummaryObserver.class.getInterfaces()[0].equals(SkillObserver.class));
    }
 
    // Each observer must return the correct name
    @Test
    public void testObserverNames() {
        assertEquals("Alert Observer",   new AlertObserver().getName());
        assertEquals("Summary Observer", new SummaryObserver(new ArrayList<>()).getName());
    }
 
    // Subject must have attach, detach, and notifyObservers methods
    @Test
    public void testSubjectHasRequiredMethods() {
        boolean hasAttach = false, hasDetach = false, hasNotify = false;
        for (java.lang.reflect.Method m : Subject.class.getDeclaredMethods()) {
            if (m.getName().equals("attach"))          hasAttach = true;
            if (m.getName().equals("detach"))          hasDetach = true;
            if (m.getName().equals("notifyObservers")) hasNotify = true;
        }
        assertTrue(hasAttach);
        assertTrue(hasDetach);
        assertTrue(hasNotify);
    }
 
    // SummaryObserver should correctly count a Fresh skill
    @Test
    public void testSummaryCountsFreshSkill() {
        List<Skill> skills = new ArrayList<>();
        Subject subject = new Subject();
        SummaryObserver summary = new SummaryObserver(skills);
        subject.attach(summary);
 
        Skill skill = new Skill("Java", "Programming", new SkillEvaluator(new TimeBasedDecay()));
        skills.add(skill);
        subject.addSkill(skill);
 
        assertEquals(1, summary.getFresh());
        assertEquals(0, summary.getFading());
        assertEquals(0, summary.getOutdated());
    }
 
    // SummaryObserver should correctly count a Fading skill
    @Test
    public void testSummaryCountsFadingSkill() {
        List<Skill> skills = new ArrayList<>();
        Subject subject = new Subject();
        SummaryObserver summary = new SummaryObserver(skills);
        subject.attach(summary);
 
        Skill skill = new Skill("Java", "Programming", new SkillEvaluator(new TimeBasedDecay()));
        skill.simulateLastUsed(LocalDate.now().minusDays(45), 1);
        skills.add(skill);
        subject.addSkill(skill);
 
        assertEquals(1, summary.getFading());
    }
 
    // SummaryObserver should correctly count an Outdated skill
    @Test
    public void testSummaryCountsOutdatedSkill() {
        List<Skill> skills = new ArrayList<>();
        Subject subject = new Subject();
        SummaryObserver summary = new SummaryObserver(skills);
        subject.attach(summary);
 
        Skill skill = new Skill("Java", "Programming", new SkillEvaluator(new TimeBasedDecay()));
        skill.simulateLastUsed(LocalDate.now().minusDays(90), 1);
        skills.add(skill);
        subject.addSkill(skill);
 
        assertEquals(1, summary.getOutdated());
    }
 
    // Subject getSkills() should return all added skills
    @Test
    public void testSubjectGetSkills() {
        Subject subject = new Subject();
        SkillEvaluator evaluator = new SkillEvaluator(new TimeBasedDecay());
        subject.addSkill(new Skill("Java",   "Programming", evaluator));
        subject.addSkill(new Skill("Python", "Programming", evaluator));
        assertEquals(2, subject.getSkills().size());
    }
}
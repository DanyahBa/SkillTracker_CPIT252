/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

/**
 *
 * @author maryh
 */

import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Modifier;
import java.time.LocalDate;

public class StrategyPatternTest {

   // DecayStrategy must be an interface
    @Test
    public void testDecayStrategyIsInterface() {
        assertTrue(Modifier.isInterface(DecayStrategy.class.getModifiers()));
    }
 
    // TimeBasedDecay must implement DecayStrategy
    @Test
    public void testTimeBasedDecayImplementsInterface() {
        Class<?>[] interfaces = TimeBasedDecay.class.getInterfaces();
        assertTrue(interfaces[0].equals(DecayStrategy.class));
    }
 
    // FrequencyBasedDecay must implement DecayStrategy
    @Test
    public void testFrequencyBasedDecayImplementsInterface() {
        Class<?>[] interfaces = FrequencyBasedDecay.class.getInterfaces();
        assertTrue(interfaces[0].equals(DecayStrategy.class));
    }
 
    // Skill used today should score 100
    @Test
    public void testTimeBasedScoreToday() {
        TimeBasedDecay strategy = new TimeBasedDecay();
        assertEquals(100.0, strategy.computeScore(LocalDate.now(), 1), 0.01);
    }
 
    // Null date should score 0
    @Test
    public void testTimeBasedScoreNullDate() {
        TimeBasedDecay strategy = new TimeBasedDecay();
        assertEquals(0.0, strategy.computeScore(null, 0), 0.01);
    }
 
    // Skill not used for 90 days should score 0
    @Test
    public void testTimeBasedScoreOutdated() {
        TimeBasedDecay strategy = new TimeBasedDecay();
        assertEquals(0.0, strategy.computeScore(LocalDate.now().minusDays(90), 1), 0.01);
    }
 
    // Frequency score should always be between 0 and 100
    @Test
    public void testFrequencyScoreRange() {
        FrequencyBasedDecay strategy = new FrequencyBasedDecay();
        double score = strategy.computeScore(LocalDate.now().minusDays(30), 5);
        assertTrue(score >= 0.0 && score <= 100.0);
    }
 
    // SkillEvaluator should return the same strategy passed to it
    @Test
    public void testSkillEvaluatorGetStrategy() {
        DecayStrategy strategy = new TimeBasedDecay();
        SkillEvaluator evaluator = new SkillEvaluator(strategy);
        assertEquals(strategy, evaluator.getStrategy());
    }
 
    // SkillEvaluator should correctly compute score using its strategy
    @Test
    public void testSkillEvaluatorComputesScore() {
        SkillEvaluator evaluator = new SkillEvaluator(new TimeBasedDecay());
        assertEquals(100.0, evaluator.computeScore(LocalDate.now(), 1), 0.01);
    }
}
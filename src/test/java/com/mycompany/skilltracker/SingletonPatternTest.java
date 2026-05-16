/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;

/**
 *
 * @author maryh
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;                          
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


// DatabaseConnection should restrict instantiation to a single instance
public class SingletonPatternTest {
 
 
    // Only one instance should ever be created
    @Test
    public void testOnlyOneInstance() {
        DatabaseConnection first  = DatabaseConnection.getInstance();
        DatabaseConnection second = DatabaseConnection.getInstance();
        assertEquals(first, second);
    }
 
    // Constructor must be private so no one can use 'new'
    @Test
    public void testConstructorIsPrivate() {
        Constructor[] constructors = DatabaseConnection.class.getDeclaredConstructors();
        for (Constructor c : constructors) {
            assertTrue(Modifier.isPrivate(c.getModifiers()));
        }
    }
 
    // The instance field must be private and static
    @Test
    public void testInstanceFieldIsPrivateAndStatic() {
        boolean found = false;
        for (Field f : DatabaseConnection.class.getDeclaredFields()) {
            if (f.getType() == DatabaseConnection.class) {
                found = true;
                assertTrue(Modifier.isPrivate(f.getModifiers()));
                assertTrue(Modifier.isStatic(f.getModifiers()));
            }
        }
        assertTrue(found);
    }
}
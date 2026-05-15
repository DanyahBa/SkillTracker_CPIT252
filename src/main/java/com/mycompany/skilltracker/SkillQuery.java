/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.skilltracker;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// database logic
public class SkillQuery {

    // SIGLETON shared connection
    private Connection conn =
            DatabaseConnection.getInstance().getConnection();

    // login method that checks if phone number exists in DB
    public int login(String phone) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM users WHERE phone = ?");
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return rs.getInt("id");

        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return -1;
    }

    // load demo skills created
    public List<Skill> loadSkills(int userId) {
        List<Skill> skills = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM skills WHERE user_id = ?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DecayStrategy strategy;
                if (rs.getString("strategy").equals("Time-Based Decay"))
                    strategy = new TimeBasedDecay();
                else
                    strategy = new FrequencyBasedDecay();

                Skill skill = new Skill(
                        rs.getString("name"),
                        rs.getString("category"),
                        new SkillEvaluator(strategy)
                );

                skill.simulateLastUsed(
                        LocalDate.parse(rs.getString("last_used")),
                        rs.getInt("usage_count")
                );

                skills.add(skill);
            }
        } catch (SQLException e) {
            System.out.println("Load skills error: " + e.getMessage());
        }
        return skills;
    }


    // save skill method after user adds skill
    // inserts a new skill into the database for a specific user
    public void saveSkill(int userId, Skill skill) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO skills " +
                            "(user_id, name, category, strategy, last_used, usage_count, state) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, userId);
            ps.setString(2, skill.getName());
            ps.setString(3, skill.getCategory());
            ps.setString(4, skill.getEvaluatorContext().getStrategy().toString());
            ps.setString(5, skill.getLastUsed().toString());
            ps.setInt(6, skill.getUsageCount());
            ps.setString(7, skill.getState().getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Save skill error: " + e.getMessage());
        }
    }

    // update skill after user logs usage
    // updates last_used, usage_count, and state in the database
    public void updateSkill(int userId, Skill skill) {
        try {
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE skills SET last_used = ?, usage_count = ?, state = ? " +
                            "WHERE user_id = ? AND name = ?");
            ps.setString(1, skill.getLastUsed().toString());
            ps.setInt(2, skill.getUsageCount());
            ps.setString(3, skill.getState().getStatus());
            ps.setInt(4, userId);
            ps.setString(5, skill.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update skill error: " + e.getMessage());
        }
    }
}
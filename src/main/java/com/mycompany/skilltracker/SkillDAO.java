package com.mycompany.skilltracker;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class SkillDAO {

    public void saveSkill(Skill skill) {

        String sql = "INSERT INTO skills "
                + "(name, category, last_used, usage_count, score, status, strategy) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {

            Connection con =
                    DatabaseConnection.getInstance().getConnection();

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, skill.getName());
            ps.setString(2, skill.getCategory());
            ps.setDate(3, java.sql.Date.valueOf(skill.getLastUsed()));
            ps.setInt(4, skill.getUsageCount());
            ps.setDouble(5, skill.getScore());
            ps.setString(6, skill.getState().getStatus());
            ps.setString(7,
                    skill.getEvaluatorContext()
                            .getStrategy()
                            .toString());

            ps.executeUpdate();

            System.out.println("Skill Saved!");

        } catch (Exception e) {


            System.out.println("Database not available.");
        }
        }
    }

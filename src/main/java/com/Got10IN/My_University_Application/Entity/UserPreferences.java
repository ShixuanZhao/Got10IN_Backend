package com.Got10IN.My_University_Application.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class UserPreferences {
    private String fieldOfInterests;
    private String mbti;
    private String weatherPreferences;
    private String locationPreferences;
    private String lifestyle;
    private String collegeVibe;
    private String collegeTypes;
    private String idealTuition;
    private String gpaRange;
    private Map<String, String> otherImportantFactors;
    private String rankingFor;
    private Map<String, String> importanceOfPreferences;
}
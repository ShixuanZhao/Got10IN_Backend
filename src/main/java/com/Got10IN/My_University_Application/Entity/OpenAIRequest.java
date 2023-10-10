package com.Got10IN.My_University_Application.Entity;

import lombok.Data;

@Data
public class OpenAIRequest {
    private String model;
    private String prompt;
    private int max_tokens;
    private double temperature;
}


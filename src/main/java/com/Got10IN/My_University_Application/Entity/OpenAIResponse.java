package com.Got10IN.My_University_Application.Entity;

import lombok.Data;

import java.util.List;

@Data
public class OpenAIResponse {
    public String id;
    public String object;
    public long created;
    public String model;
    public List<Choice> choices;
    public Usage usage;

    public static class Choice {
        public String text;
        public int index;
        public Object logprobs;
        public String finish_reason;

    }

    public static class Usage {
        public int prompt_tokens;
        public int completion_tokens;
        public int total_tokens;

    }

}


package com.Got10IN.My_University_Application.Controller;

import com.Got10IN.My_University_Application.Entity.CollegeRankingResponse;
import com.Got10IN.My_University_Application.Entity.UserPreferences;
import com.Got10IN.My_University_Application.Service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/preferences")
public class PreferencesController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/rankColleges")
    public ResponseEntity<List<CollegeRankingResponse>> rankColleges(@RequestBody UserPreferences preferences) {
        try {
            // Validate preferences
            if (preferences == null) {
                return ResponseEntity.badRequest().build();
            }

            // Send preferences to OpenAI API
            String rankingResponse = openAIService.getCollegeRankings(preferences);
            List<CollegeRankingResponse> colleges = new ArrayList<>();
            Pattern pattern = Pattern.compile("(\\d+)\\. (.+)");
            Matcher matcher = pattern.matcher(rankingResponse);

            while (matcher.find()) {
                int ranking = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);

                CollegeRankingResponse college = new CollegeRankingResponse();
                college.setRanking(ranking);
                college.setCollegeNames(name);

                colleges.add(college);
            }
            //TODO
            // Store rankings in Firebase
            return ResponseEntity.ok(colleges);
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

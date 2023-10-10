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

@RestController
@RequestMapping("/preferences")
public class PreferencesController {

    @Autowired
    private OpenAIService openAIService;

    @PostMapping("/rankColleges")
    public ResponseEntity<String> rankColleges(@RequestBody UserPreferences preferences) {
        try {
            // Validate preferences
            if (preferences == null) {
                return ResponseEntity.badRequest().build();
            }

            // Send preferences to OpenAI API
            CollegeRankingResponse rankingResponse = openAIService.getCollegeRankings(preferences);

            //TODO
            // Store rankings in Firebase

            return ResponseEntity.ok(rankingResponse.getCollegeNames());
        } catch (Exception e) {
            // Handle exceptions and return an appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

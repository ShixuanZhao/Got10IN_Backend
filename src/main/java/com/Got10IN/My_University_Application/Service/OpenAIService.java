package com.Got10IN.My_University_Application.Service;

import com.Got10IN.My_University_Application.Entity.CollegeRankingResponse;
import com.Got10IN.My_University_Application.Entity.OpenAIRequest;
import com.Got10IN.My_University_Application.Entity.OpenAIResponse;
import com.Got10IN.My_University_Application.Entity.UserPreferences;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAIService {

    @Value("${apiKey}")
    private String openAIApiKey;

    @Value("${endpoint}")
    private String openAIEndpoint;

    public CollegeRankingResponse getCollegeRankings(UserPreferences preferences) {
        // Construct the request headers with your OpenAI API key
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAIApiKey);
        // Construct the request payload
        OpenAIRequest request = new OpenAIRequest();
        String fieldOfInterests = preferences.getFieldOfInterests();
        String mbti = preferences.getMbti();
        String weatherPreferences = preferences.getWeatherPreferences();
        String locationPreferences = preferences.getLocationPreferences();
        String lifestyle = preferences.getLifestyle();
        String collegeVibe = preferences.getCollegeVibe();
        String collegeTypes = preferences.getCollegeTypes();
        String idealTuition = preferences.getIdealTuition();
        String gpaRange = preferences.getGpaRange();
        Map<String, String> otherImportantFactors = preferences.getOtherImportantFactors();
        String otherImportantFactorsString = otherImportantFactors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));
        String rankingFor = preferences.getRankingFor();
        Map<String, String> importanceOfPreferences = preferences.getImportanceOfPreferences();
        String importanceOfPreferencesString = importanceOfPreferences.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        String prefix = "Generate the top 10 US college ranking based on the following preferences of that student.\n" +
                "Rules:\n" +
                "Don’t respond with anything else but the actual school name(Don’t say anything else).\n" +
                "Do not generate severely depending on one preference, it should be holistic(for example: Don’t make every college from the West Coast just because I said I like sunny weather). \n";
        String prompt = prefix + " fieldOfInterests: " + fieldOfInterests + ",mbti:" + mbti + " ,weatherPreferences" + weatherPreferences +
                " ,locationPreferences" + locationPreferences + " ,lifestyle" + lifestyle + " ,collegeVibe"
                + collegeVibe+ " ,collegeTypes" + collegeTypes+ " ,idealTuition" + idealTuition +
                " ,gpaRange" + gpaRange + " ,rankingFor" + rankingFor  + " ,otherImportantFactorsString" + otherImportantFactorsString + " ,importanceOfPreferencesString" + importanceOfPreferencesString;
        request.setModel("gpt-3.5-turbo-instruct");
        request.setPrompt(prompt);
        request.setMax_tokens(100);
        request.setTemperature(0);
        //String requestBody = "{\"model\":\"gpt-3.5-turbo-instruct\",\"prompt\":\"" + prompt + "\",\"max_tokens\":7,\"temperature\":0}";
        // Create the request entity
        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);

        // Create a RestTemplate to make the POST request
        RestTemplate restTemplate = new RestTemplate();

        // Send the POST request to the OpenAI API
        ResponseEntity<OpenAIResponse> responseEntity = restTemplate.exchange(
                openAIEndpoint,
                HttpMethod.POST,
                entity,
                OpenAIResponse.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Parse the response and extract the college rankings
            OpenAIResponse response = responseEntity.getBody();
            String collegeNames = response.getChoices().get(0).text;
            CollegeRankingResponse collegeRankingResponse = new CollegeRankingResponse();
            collegeRankingResponse.setCollegeNames(collegeNames);
            return collegeRankingResponse;
        } else {
            // Handle the error appropriately based on your application's requirements
            throw new RuntimeException("Error communicating with OpenAI API");
        }
    }
}

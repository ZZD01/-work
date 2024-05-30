package cn.ikarosx.restaurant.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class GPT3Util {
    public static String sendPost(String data) throws JSONException{
        System.out.println(data);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + "sk-IOC81gCsUjpHLlabXd8ZBy6rT8spWdWkJR8kJlKJtgu8Xlhj");
        httpHeaders.add("Content-Type", "application/json");

        String escapedData = JSONObject.quote(data);
//        System.out.println(escapedData);
//        String requestJson = String.format(
//                "{\n" +
//                        "\"model\": \"gpt-3.5-turbo\",\n" +
//                        "\"temperature\": 0,\n" +
//                        "\"max_tokens\": 2048,\n" +
//                        "\"messages\": [{\"role\": \"user\", \"content\": %s}]\n" +
//                        "}", escapedData
//        );
        String requestJson = String.format(
                "{\n" +
                        "\"model\": \"gpt-3.5-turbo\",\n" +
                        "\"temperature\": 0,\n" +
                        "\"max_tokens\": 2048,\n" +
                        "\"messages\": [{\"role\": \"user\", \"content\": %s}]\n" +
                        "}",escapedData
        );
//        System.out.println(requestJson);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.chatanywhere.tech/v1/chat/completions", entity, String.class);
        System.out.println(response.getBody());
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray choices = jsonObject.getJSONArray("choices");
//        String text = choices.getJSONObject(0).getJSONObject("message").getString("content");
        return choices.getJSONObject(0).getJSONObject("message").getString("content");
    }
}
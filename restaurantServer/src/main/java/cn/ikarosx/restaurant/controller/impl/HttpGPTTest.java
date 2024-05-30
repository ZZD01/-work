package cn.ikarosx.restaurant.controller.impl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class HttpGPTTest {
    @Test
    public static String sendPost(String data) throws JSONException {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + "sk-DVtX85h0amfb3SFF3TfMdZ0hwSl8jRrLUmCUp5WiRoZ9AhXG");
        httpHeaders.add("Content-Type", "application/json");

        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", data);

        JSONArray messages = new JSONArray();
        messages.put(message);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("temperature", 0);
        requestBody.put("max_tokens", 2048);
        requestBody.put("messages", messages);

        String requestJson = requestBody.toString();
        System.out.println(requestJson);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, httpHeaders);
        ResponseEntity<String> response = restTemplate.postForEntity("https://api.chatanywhere.tech/v1/chat/completions", entity, String.class);
        System.out.println(response.getBody());
        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONArray choices = jsonObject.getJSONArray("choices");
        String text = choices.getJSONObject(0).getJSONObject("message").getString("content");
        return text;
    }

    public static void main(String[] args) throws JSONException {
        System.out.println(sendPost("介绍一下中国"));
    }
}
//package cn.ikarosx.restaurant.controller;
//
//import cn.ikarosx.restaurant.util.GPT3Util;
//import org.json.JSONException;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//import java.util.Map;
//import org.json.JSONObject;
//
//@RestController
//@RequestMapping("/api")
//public class GPTController {
//
//    @PostMapping("/TalkToGPT")
//    public String TalkToGPT(@RequestBody List<Map<String, String>> messages) {
//        // 1. 创建一个新的JSONObject
//        JSONObject jsonObject = new JSONObject();
//
//        // 2. 将messages添加到JSONObject
//        try {
//            jsonObject.put("messages", messages);
//        } catch (JSONException e) {
//           throw new RuntimeException(e);
//        }
//
//        // 3. 将JSONObject转换为字符串
//        String jsonContent = jsonObject.toString();
//
//        System.out.println(jsonContent);
//
//        // 4. 使用GPT3Util.sendPost方法生成内容
//        String generatedContent = "";
//        try {
//            generatedContent = GPT3Util.sendPost(jsonContent);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        // 5. 返回生成的内容
//        return generatedContent;
//    }
//}

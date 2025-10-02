package com.example.siliconflowmod.api;

import com.example.siliconflowmod.SiliconFlowMod;
import com.example.siliconflowmod.config.ModConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class SiliconFlowAPI {
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
    private static final Gson gson = new Gson();
    
    public static String chat(String message) {
        try {
            // 构建硅基流动API请求体
            JsonObject requestBody = new JsonObject();
            requestBody.addProperty("model", ModConfig.MODEL);
            
            // 构建消息数组
            JsonArray messages = new JsonArray();
            JsonObject messageObj = new JsonObject();
            messageObj.addProperty("role", "user");
            messageObj.addProperty("content", message);
            messages.add(messageObj);
            
            requestBody.add("messages", messages);
            requestBody.addProperty("stream", false);
            requestBody.addProperty("max_tokens", 2048);
            
            String requestBodyString = requestBody.toString();
            SiliconFlowMod.LOGGER.info("发送请求到硅基流动 API");
            
            // 构建HTTP请求
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ModConfig.API_URL))
                    .header("Authorization", "Bearer " + ModConfig.API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBodyString))
                    .build();
            
            // 发送请求
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            SiliconFlowMod.LOGGER.info("API 响应状态: {}", response.statusCode());
            
            if (response.statusCode() == 200) {
                // 解析硅基流动API响应
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                
                if (jsonResponse.has("choices") && jsonResponse.getAsJsonArray("choices").size() > 0) {
                    return jsonResponse.getAsJsonArray("choices")
                            .get(0).getAsJsonObject()
                            .getAsJsonObject("message")
                            .get("content").getAsString();
                } else {
                    return "❌ API 返回格式异常，无 choices 字段";
                }
            } else {
                SiliconFlowMod.LOGGER.error("硅基流动 API 请求失败: {} - {}", response.statusCode(), response.body());
                return "❌ 硅基流动 API 请求失败: " + response.statusCode();
            }
            
        } catch (Exception e) {
            SiliconFlowMod.LOGGER.error("调用硅基流动 API 时出错", e);
            return "❌ 出错: " + e.getMessage();
        }
    }
}

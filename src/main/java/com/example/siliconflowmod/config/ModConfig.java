package com.example.siliconflowmod.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("siliconflow_mod.json");
    
    // 硅基流动 API 配置
    public static String API_KEY = "sk-cuyyxofsxjvwbanxnmpnquwegwfxxqacyfyaxybfejuadcvg";
    public static String API_URL = "https://api.siliconflow.cn/v1/chat/completions";
    public static String MODEL = "deepseek-ai/DeepSeek-V2";
    
    public static void loadConfig() {
        if (!Files.exists(CONFIG_PATH)) {
            saveConfig();
            return;
        }
        
        try {
            String content = Files.readString(CONFIG_PATH);
            ConfigData data = GSON.fromJson(content, ConfigData.class);
            
            API_KEY = data.apiKey;
            API_URL = data.apiUrl != null ? data.apiUrl : API_URL;
            MODEL = data.model != null ? data.model : MODEL;
            
        } catch (IOException e) {
            SiliconFlowMod.LOGGER.error("加载配置失败", e);
        }
    }
    
    public static void saveConfig() {
        try {
            ConfigData data = new ConfigData();
            data.apiKey = API_KEY;
            data.apiUrl = API_URL;
            data.model = MODEL;
            
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, GSON.toJson(data));
            
        } catch (IOException e) {
            SiliconFlowMod.LOGGER.error("保存配置失败", e);
        }
    }
    
    private static class ConfigData {
        String apiKey;
        String apiUrl;
        String model;
    }
}

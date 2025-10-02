package com.example.siliconflowmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiliconFlowMod implements ModInitializer {
    public static final String MOD_ID = "siliconflowmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("硅基流动 AI Mod 初始化!");
        
        // 注册命令
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            AICommand.register(dispatcher);
        });
        
        // 加载配置
        ModConfig.loadConfig();
    }
}

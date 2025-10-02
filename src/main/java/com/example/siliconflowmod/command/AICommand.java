package com.example.siliconflowmod.command;

import com.example.siliconflowmod.api.SiliconFlowAPI;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.*;

public class AICommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("ai")
            .then(argument("message", StringArgumentType.greedyString())
                .executes(context -> {
                    String message = StringArgumentType.getString(context, "message");
                    ServerCommandSource source = context.getSource();
                    
                    // 先发送等待消息
                    source.sendMessage(Text.literal("💭 正在询问硅基流动 AI..."));
                    
                    // 异步处理API调用，避免阻塞游戏线程
                    new Thread(() -> {
                        String response = SiliconFlowAPI.chat(message);
                        
                        // 回到游戏主线程发送消息
                        source.getServer().execute(() -> {
                            // 将响应分成多行，避免消息过长
                            String[] lines = response.split("\n");
                            for (String line : lines) {
                                if (!line.trim().isEmpty()) {
                                    source.sendMessage(Text.literal("🤖 " + line.trim()));
                                }
                            }
                        });
                    }).start();
                    
                    return Command.SINGLE_SUCCESS;
                })
            )
        );

        // 添加一个简化的命令别名
        dispatcher.register(literal("ask")
            .then(argument("message", StringArgumentType.greedyString())
                .executes(context -> {
                    String message = StringArgumentType.getString(context, "message");
                    ServerCommandSource source = context.getSource();
                    
                    source.sendMessage(Text.literal("💭 正在询问硅基流动 AI..."));
                    
                    new Thread(() -> {
                        String response = SiliconFlowAPI.chat(message);
                        
                        source.getServer().execute(() -> {
                            String[] lines = response.split("\n");
                            for (String line : lines) {
                                if (!line.trim().isEmpty()) {
                                    source.sendMessage(Text.literal("🤖 " + line.trim()));
                                }
                            }
                        });
                    }).start();
                    
                    return Command.SINGLE_SUCCESS;
                })
            )
        );
    }
}

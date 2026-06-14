package yifei.randomheight.manager;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yifei.randomheight.util.HeightRandomizer;

import java.util.Random;

public class ScaleManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("randomheight");
    
    private static Boolean pehkuiAvailable = null;
    private static final Random random = new Random();
    private static HeightRandomizer heightRandomizer;

    private static void checkPehkui() {
        if (pehkuiAvailable != null) return;
        
        LOGGER.info("[RandomHeight] Attempting to detect Pehkui API...");
        
        try {
            if (!ModList.get().isLoaded("pehkui")) {
                LOGGER.warn("[RandomHeight] Pehkui mod not loaded (ModList check failed)");
                pehkuiAvailable = false;
                return;
            }
            
            LOGGER.info("[RandomHeight] Pehkui mod is loaded according to ModList");
            pehkuiAvailable = true;
            
        } catch (Exception e) {
            LOGGER.warn("[RandomHeight] Error detecting Pehkui: " + e.getClass().getName() + " - " + e.getMessage());
            pehkuiAvailable = false;
        }
    }

    public static boolean isPehkuiAvailable() {
        checkPehkui();
        return pehkuiAvailable;
    }

    public static float getRandomScale(double min, double max) {
        if (heightRandomizer == null) {
            heightRandomizer = new HeightRandomizer(min, max);
        }
        return heightRandomizer.next();
    }

    public static void setPlayerScale(ServerPlayer player, float scale) {
        checkPehkui();
        
        if (!pehkuiAvailable) {
            player.sendSystemMessage(Component.translatable("randomheight.pehkui_incompatible"));
            return;
        }

        try {
            boolean success = executePehkuiCommands(player, scale);
            if (success) {
                player.sendSystemMessage(Component.translatable("randomheight.scale_changed", scale));
            }
        } catch (Exception e) {
            LOGGER.error("[RandomHeight] Error setting scale: " + e.getMessage(), e);
            player.sendSystemMessage(Component.translatable("randomheight.set_scale_failed", e.getMessage()));
        }
    }
    
    private static boolean executePehkuiCommands(ServerPlayer player, float scale) {
        String widthCommand = "scale set pehkui:width " + scale + " @s";
        String heightCommand = "scale set pehkui:height " + scale + " @s";
        
        LOGGER.info("[RandomHeight] Executing commands: /" + widthCommand + ", /" + heightCommand);
        
        try {
            executeCommand(player, widthCommand);
            executeCommand(player, heightCommand);
            LOGGER.info("[RandomHeight] Commands executed successfully");
            return true;
        } catch (Exception e) {
            LOGGER.error("[RandomHeight] Failed to execute commands: " + e.getMessage(), e);
            return false;
        }
    }
    
    private static void executeCommand(ServerPlayer player, String command) throws Exception {
        try {
            MinecraftServer server = player.getServer();
            if (server == null) {
                throw new RuntimeException("Server is null");
            }
            
            CommandSourceStack source = player.createCommandSourceStack();
            CommandDispatcher<CommandSourceStack> dispatcher = server.getCommands().getDispatcher();
            
            ParseResults<CommandSourceStack> parseResults = dispatcher.parse(command, source);
            dispatcher.execute(parseResults);
            
            LOGGER.info("[RandomHeight] Command executed successfully: /" + command);
            
        } catch (Exception e) {
            LOGGER.error("[RandomHeight] Failed to execute command: " + e.getMessage());
            throw e;
        }
    }
}
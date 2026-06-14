package yifei.randomheight.manager;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.ModList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import virtuoel.pehkui.api.ScaleData;
import virtuoel.pehkui.api.ScaleTypes;
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
            
            ScaleTypes.BASE.getClass();
            LOGGER.info("[RandomHeight] Pehkui API detected successfully!");
            pehkuiAvailable = true;
            
        } catch (NoClassDefFoundError e) {
            LOGGER.warn("[RandomHeight] PehkuiApi class not found (NoClassDefFoundError): " + e.getMessage());
            pehkuiAvailable = false;
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

    public static void setPlayerScale(LocalPlayer player, float scale) {
        checkPehkui();
        
        if (!pehkuiAvailable) {
            player.sendSystemMessage(Component.translatable("randomheight.pehkui_incompatible"));
            return;
        }

        try {
            ScaleData baseScaleData = ScaleTypes.BASE.getScaleData(player);
            ScaleData widthScaleData = ScaleTypes.WIDTH.getScaleData(player);
            ScaleData heightScaleData = ScaleTypes.HEIGHT.getScaleData(player);
            
            baseScaleData.setScale(scale);
            widthScaleData.setScale(scale);
            heightScaleData.setScale(scale);
            
            player.sendSystemMessage(Component.translatable("randomheight.scale_changed", scale));
            
        } catch (Exception e) {
            LOGGER.error("[RandomHeight] Error setting scale: " + e.getMessage(), e);
            player.sendSystemMessage(Component.translatable("randomheight.set_scale_failed", e.getMessage()));
        }
    }
}
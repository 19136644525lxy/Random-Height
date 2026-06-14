package yifei.randomheight.events;

import yifei.randomheight.config.RandomHeightConfig;
import yifei.randomheight.manager.ScaleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TickHandler {
    private int tickCounter = 0;
    private int countdownTick = 0;
    private boolean inCountdown = false;
    private boolean firstJoin = true;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;
        
        if (firstJoin) {
            firstJoin = false;
            if (!ScaleManager.isPehkuiAvailable()) {
                player.sendSystemMessage(Component.translatable("randomheight.pehkui_warning"));
            }
        }

        if (!RandomHeightConfig.isEnabled()) return;

        int intervalTicks = RandomHeightConfig.getIntervalSeconds() * 20;
        int countdownTicks = RandomHeightConfig.getCountdownSeconds() * 20;

        tickCounter++;

        if (RandomHeightConfig.isCountdownEnabled()) {
            if (tickCounter >= intervalTicks - countdownTicks && tickCounter < intervalTicks) {
                if (!inCountdown) {
                    inCountdown = true;
                    countdownTick = 0;
                }
                
                countdownTick++;
                int remainingSeconds = (intervalTicks - tickCounter) / 20;
                
                if (remainingSeconds > 0 && countdownTick % 20 == 0) {
                    if (remainingSeconds <= RandomHeightConfig.getCountdownSeconds()) {
                        player.sendSystemMessage(Component.translatable("randomheight.countdown_message", remainingSeconds));
                    }
                }
            }
        }

        if (tickCounter >= intervalTicks) {
            tickCounter = 0;
            inCountdown = false;
            applyRandomScale(player);
        }
    }

    public static void applyRandomScale(LocalPlayer player) {
        float scale = ScaleManager.getRandomScale(
            RandomHeightConfig.getMinScale(), 
            RandomHeightConfig.getMaxScale()
        );
        
        ScaleManager.setPlayerScale(player, scale);
        
        player.sendSystemMessage(Component.translatable("randomheight.scale_changed", scale));
    }

    public static void forceApplyRandomScale() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            applyRandomScale(player);
        }
    }

    public void resetTimer() {
        tickCounter = 0;
        countdownTick = 0;
        inCountdown = false;
    }
}
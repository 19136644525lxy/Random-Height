package yifei.randomheight.events;

import yifei.randomheight.config.RandomHeightConfig;
import yifei.randomheight.manager.ScaleManager;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class TickHandler {
    private int tickCounter = 0;
    private Map<ServerPlayer, Integer> countdownMap = new HashMap<>();
    private Map<ServerPlayer, Boolean> firstJoinMap = new HashMap<>();

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (!RandomHeightConfig.isEnabled()) return;

        int intervalTicks = RandomHeightConfig.getIntervalSeconds() * 20;

        tickCounter++;

        if (tickCounter >= intervalTicks) {
            tickCounter = 0;
            applyRandomScaleToAllPlayers(event.getServer());
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        
        if (!(event.player instanceof ServerPlayer serverPlayer)) return;

        if (firstJoinMap.getOrDefault(serverPlayer, true)) {
            firstJoinMap.put(serverPlayer, false);
            
            Component pehkuiStatus = Component.translatable(
                "randomheight.welcome.pehkui", 
                ScaleManager.isPehkuiAvailable() 
                    ? Component.translatable("randomheight.yes").getString() 
                    : Component.translatable("randomheight.no").getString()
            );
            serverPlayer.sendSystemMessage(pehkuiStatus);
            
            Component githubLink = Component.translatable("randomheight.welcome.github")
                .withStyle(style -> style.withClickEvent(new ClickEvent(
                    ClickEvent.Action.OPEN_URL, 
                    "https://github.com/19136644525lxy/Random-Height"
                )));
            serverPlayer.sendSystemMessage(githubLink);
            
            Component thanks = Component.translatable("randomheight.welcome.thanks");
            serverPlayer.sendSystemMessage(thanks);
        }

        if (!RandomHeightConfig.isEnabled()) return;
        if (!RandomHeightConfig.isCountdownEnabled()) return;

        int intervalTicks = RandomHeightConfig.getIntervalSeconds() * 20;
        int countdownTicks = RandomHeightConfig.getCountdownSeconds() * 20;

        int playerTick = tickCounter;
        if (playerTick >= intervalTicks - countdownTicks && playerTick < intervalTicks) {
            int remainingSeconds = (intervalTicks - playerTick) / 20;
            
            if (remainingSeconds > 0 && playerTick % 20 == 0) {
                if (remainingSeconds <= RandomHeightConfig.getCountdownSeconds()) {
                    serverPlayer.sendSystemMessage(Component.translatable("randomheight.countdown_message", remainingSeconds));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            firstJoinMap.put(serverPlayer, true);
            countdownMap.put(serverPlayer, 0);
        }
    }

    private void applyRandomScaleToAllPlayers(net.minecraft.server.MinecraftServer server) {
        if (server == null) return;

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            applyRandomScale(player);
        }
    }

    public static void applyRandomScale(ServerPlayer player) {
        float scale = ScaleManager.getRandomScale(
            RandomHeightConfig.getMinScale(), 
            RandomHeightConfig.getMaxScale()
        );

        ScaleManager.setPlayerScale(player, scale);
    }

    public static void forceApplyRandomScale(ServerPlayer player) {
        applyRandomScale(player);
    }

    public void resetTimer() {
        tickCounter = 0;
    }
}
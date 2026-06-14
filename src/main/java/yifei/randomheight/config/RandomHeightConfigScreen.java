package yifei.randomheight.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "randomheight", value = Dist.CLIENT)
public class RandomHeightConfigScreen {
    
    public static KeyMapping CONFIG_KEY = new KeyMapping(
        "key.randomheight.config",
        GLFW.GLFW_KEY_K,
        "key.categories.randomheight"
    );
    
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        event.register(CONFIG_KEY);
    }
    
    @SubscribeEvent
    public static void onKeyInput(ScreenEvent.KeyPressed event) {
        if (event.getKeyCode() == CONFIG_KEY.getKey().getValue() && 
            event.getModifiers() == 0 &&
            Minecraft.getInstance().player != null) {
            Minecraft.getInstance().setScreen(createConfigScreen(Minecraft.getInstance().screen));
        }
    }
    
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("randomheight.config.title"))
            .setSavingRunnable(RandomHeightConfig::saveToFile);
        
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        
        ConfigCategory general = builder.getOrCreateCategory(
            Component.translatable("randomheight.config.category.general"));
        
        general.addEntry(entryBuilder.startBooleanToggle(
            Component.translatable("randomheight.config.enabled"),
            RandomHeightConfig.isEnabled())
            .setDefaultValue(true)
            .setSaveConsumer(RandomHeightConfig::setEnabled)
            .setTooltip(Component.translatable("randomheight.config.enabled.tooltip"))
            .build());
        
        general.addEntry(entryBuilder.startIntSlider(
            Component.translatable("randomheight.config.interval"),
            RandomHeightConfig.getIntervalSeconds(),
            10, 3600)
            .setDefaultValue(180)
            .setSaveConsumer(RandomHeightConfig::setIntervalSeconds)
            .setTextGetter(value -> Component.translatable(
                "randomheight.config.interval.format", value))
            .setTooltip(Component.translatable("randomheight.config.interval.tooltip"))
            .build());
        
        general.addEntry(entryBuilder.startBooleanToggle(
            Component.translatable("randomheight.config.countdown.enabled"),
            RandomHeightConfig.isCountdownEnabled())
            .setDefaultValue(true)
            .setSaveConsumer(RandomHeightConfig::setCountdownEnabled)
            .setTooltip(Component.translatable("randomheight.config.countdown.enabled.tooltip"))
            .build());
        
        general.addEntry(entryBuilder.startIntSlider(
            Component.translatable("randomheight.config.countdown.time"),
            RandomHeightConfig.getCountdownSeconds(),
            1, 60)
            .setDefaultValue(10)
            .setSaveConsumer(RandomHeightConfig::setCountdownSeconds)
            .setTextGetter(value -> Component.translatable(
                "randomheight.config.countdown.time.format", value))
            .setTooltip(Component.translatable("randomheight.config.countdown.time.tooltip"))
            .build());
        
        ConfigCategory scale = builder.getOrCreateCategory(
            Component.translatable("randomheight.config.category.scale"));
        
        scale.addEntry(entryBuilder.startDoubleField(
            Component.translatable("randomheight.config.scale.min"),
            RandomHeightConfig.getMinScale())
            .setDefaultValue(0.1)
            .setMin(0.1)
            .setMax(5.0)
            .setSaveConsumer(RandomHeightConfig::setMinScale)
            .setTooltip(Component.translatable("randomheight.config.scale.min.tooltip"))
            .build());
        
        scale.addEntry(entryBuilder.startDoubleField(
            Component.translatable("randomheight.config.scale.max"),
            RandomHeightConfig.getMaxScale())
            .setDefaultValue(5.0)
            .setMin(0.1)
            .setMax(5.0)
            .setSaveConsumer(RandomHeightConfig::setMaxScale)
            .setTooltip(Component.translatable("randomheight.config.scale.max.tooltip"))
            .build());
        
        return builder.build();
    }
}
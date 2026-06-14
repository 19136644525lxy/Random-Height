package yifei.randomheight;

import yifei.randomheight.commands.RandomHeightCommand;
import yifei.randomheight.config.RandomHeightConfig;
import yifei.randomheight.events.TickHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RandomHeightMod.MOD_ID)
public class RandomHeightMod {
    public static final String MOD_ID = "randomheight";

    public RandomHeightMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TickHandler());
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, RandomHeightConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        RandomHeightConfig.loadConfig();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        RandomHeightCommand.register(event.getDispatcher());
    }
}
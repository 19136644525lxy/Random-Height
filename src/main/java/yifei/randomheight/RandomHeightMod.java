package yifei.randomheight;

import yifei.randomheight.commands.RandomHeightCommand;
import yifei.randomheight.config.RandomHeightConfig;
import yifei.randomheight.config.RandomHeightConfigScreen;
import yifei.randomheight.events.TickHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("randomheight")
public class RandomHeightMod {
    
    public RandomHeightMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new TickHandler());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        RandomHeightConfig.loadFromFile();
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new RandomHeightConfigScreen());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        RandomHeightCommand.register(event.getDispatcher());
    }
}
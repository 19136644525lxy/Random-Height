package yifei.randomheight.commands;

import yifei.randomheight.config.RandomHeightConfig;
import yifei.randomheight.events.TickHandler;
import yifei.randomheight.manager.ScaleManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class RandomHeightCommand {
    private static final String YES_KEY = "randomheight.yes";
    private static final String NO_KEY = "randomheight.no";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> randomHeightNode = dispatcher.register(
            Commands.literal("randomheight")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("enable")
                    .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(RandomHeightCommand::setEnabled)))
                .then(Commands.literal("interval")
                    .then(Commands.argument("seconds", IntegerArgumentType.integer(10, 3600))
                        .executes(RandomHeightCommand::setInterval)))
                .then(Commands.literal("scale")
                    .then(Commands.argument("min", DoubleArgumentType.doubleArg(0.1D, 5.0D))
                        .then(Commands.argument("max", DoubleArgumentType.doubleArg(0.1D, 5.0D))
                            .executes(RandomHeightCommand::setScaleRange)))
                .then(Commands.literal("countdown")
                    .then(Commands.argument("value", BoolArgumentType.bool())
                        .executes(RandomHeightCommand::setCountdownEnabled)))
                .then(Commands.literal("countdown_time")
                    .then(Commands.argument("seconds", IntegerArgumentType.integer(1, 60))
                        .executes(RandomHeightCommand::setCountdownTime)))
                .then(Commands.literal("force")
                    .executes(RandomHeightCommand::forceRandomHeight))
                .then(Commands.literal("config")
                    .executes(RandomHeightCommand::showConfig))
        ));

        dispatcher.register(Commands.literal("rh").redirect(randomHeightNode));
    }

    private static int setEnabled(CommandContext<CommandSourceStack> context) {
        boolean value = BoolArgumentType.getBool(context, "value");
        RandomHeightConfig.setEnabled(value);
        String key = value ? "randomheight.enabled" : "randomheight.disabled";
        context.getSource().sendSuccess(() -> Component.translatable(key), true);
        return 1;
    }

    private static int setInterval(CommandContext<CommandSourceStack> context) {
        int seconds = IntegerArgumentType.getInteger(context, "seconds");
        RandomHeightConfig.setIntervalSeconds(seconds);
        context.getSource().sendSuccess(() -> Component.translatable("randomheight.interval_set", seconds), true);
        return 1;
    }

    private static int setScaleRange(CommandContext<CommandSourceStack> context) {
        double min = DoubleArgumentType.getDouble(context, "min");
        double max = DoubleArgumentType.getDouble(context, "max");
        if (min > max) {
            context.getSource().sendFailure(Component.translatable("randomheight.error.min_max"));
            return 0;
        }
        RandomHeightConfig.setMinScale(min);
        RandomHeightConfig.setMaxScale(max);
        context.getSource().sendSuccess(() -> Component.translatable("randomheight.scale_range_set", min, max), true);
        return 1;
    }

    private static int setCountdownEnabled(CommandContext<CommandSourceStack> context) {
        boolean value = BoolArgumentType.getBool(context, "value");
        RandomHeightConfig.setCountdownEnabled(value);
        String key = value ? "randomheight.countdown_enabled" : "randomheight.countdown_disabled";
        context.getSource().sendSuccess(() -> Component.translatable(key), true);
        return 1;
    }

    private static int setCountdownTime(CommandContext<CommandSourceStack> context) {
        int seconds = IntegerArgumentType.getInteger(context, "seconds");
        RandomHeightConfig.setCountdownSeconds(seconds);
        context.getSource().sendSuccess(() -> Component.translatable("randomheight.countdown_time_set", seconds), true);
        return 1;
    }

    private static int forceRandomHeight(CommandContext<CommandSourceStack> context) {
        if (!ScaleManager.isPehkuiAvailable()) {
            context.getSource().sendFailure(Component.translatable("randomheight.pehkui_not_installed"));
            return 0;
        }
        TickHandler.forceApplyRandomScale();
        return 1;
    }

    private static int showConfig(CommandContext<CommandSourceStack> context) {
        Component header = Component.translatable("randomheight.config.header");
        Component enabled = Component.translatable("randomheight.config.enabled", 
            RandomHeightConfig.isEnabled() ? Component.translatable(YES_KEY).getString() : Component.translatable(NO_KEY).getString());
        Component interval = Component.translatable("randomheight.config.interval", RandomHeightConfig.getIntervalSeconds());
        Component minScale = Component.translatable("randomheight.config.min_scale", RandomHeightConfig.getMinScale());
        Component maxScale = Component.translatable("randomheight.config.max_scale", RandomHeightConfig.getMaxScale());
        Component countdown = Component.translatable("randomheight.config.countdown", 
            RandomHeightConfig.isCountdownEnabled() ? Component.translatable(YES_KEY).getString() : Component.translatable(NO_KEY).getString());
        Component countdownTime = Component.translatable("randomheight.config.countdown_time", RandomHeightConfig.getCountdownSeconds());
        Component pehkui = Component.translatable("randomheight.config.pehkui", 
            ScaleManager.isPehkuiAvailable() ? Component.translatable(YES_KEY).getString() : Component.translatable(NO_KEY).getString());

        Component message = header.copy()
            .append("\n").append(enabled)
            .append("\n").append(interval)
            .append("\n").append(minScale)
            .append("\n").append(maxScale)
            .append("\n").append(countdown)
            .append("\n").append(countdownTime)
            .append("\n").append(pehkui);

        context.getSource().sendSuccess(() -> message, false);
        return 1;
    }
}
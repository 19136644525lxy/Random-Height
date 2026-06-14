package yifei.randomheight.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class RandomHeightConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue INTERVAL_SECONDS;
    public static final ForgeConfigSpec.DoubleValue MIN_SCALE;
    public static final ForgeConfigSpec.DoubleValue MAX_SCALE;
    public static final ForgeConfigSpec.BooleanValue ENABLED;
    public static final ForgeConfigSpec.BooleanValue ENABLE_COUNTDOWN;
    public static final ForgeConfigSpec.IntValue COUNTDOWN_SECONDS;

    static {
        BUILDER.comment("Random Height Mod Configuration");

        INTERVAL_SECONDS = BUILDER
            .comment("随机身高切换间隔时间（秒）")
            .defineInRange("interval_seconds", 180, 10, 3600);

        MIN_SCALE = BUILDER
            .comment("最小缩放比例")
            .defineInRange("min_scale", 0.5, 0.1, 2.0);

        MAX_SCALE = BUILDER
            .comment("最大缩放比例")
            .defineInRange("max_scale", 2.0, 0.1, 5.0);

        ENABLED = BUILDER
            .comment("是否启用随机身高功能")
            .define("enabled", true);

        ENABLE_COUNTDOWN = BUILDER
            .comment("是否启用切换倒计时提示")
            .define("enable_countdown", true);

        COUNTDOWN_SECONDS = BUILDER
            .comment("切换前倒计时时间（秒）")
            .defineInRange("countdown_seconds", 10, 1, 60);

        SPEC = BUILDER.build();
    }

    private static int intervalSeconds;
    private static double minScale;
    private static double maxScale;
    private static boolean enabled;
    private static boolean enableCountdown;
    private static int countdownSeconds;

    public static void loadConfig() {
        intervalSeconds = INTERVAL_SECONDS.get();
        minScale = MIN_SCALE.get();
        maxScale = MAX_SCALE.get();
        enabled = ENABLED.get();
        enableCountdown = ENABLE_COUNTDOWN.get();
        countdownSeconds = COUNTDOWN_SECONDS.get();
    }

    public static int getIntervalSeconds() { return intervalSeconds; }
    public static void setIntervalSeconds(int value) { intervalSeconds = value; }

    public static double getMinScale() { return minScale; }
    public static void setMinScale(double value) { minScale = value; }

    public static double getMaxScale() { return maxScale; }
    public static void setMaxScale(double value) { maxScale = value; }

    public static boolean isEnabled() { return enabled; }
    public static void setEnabled(boolean value) { enabled = value; }

    public static boolean isCountdownEnabled() { return enableCountdown; }
    public static void setCountdownEnabled(boolean value) { enableCountdown = value; }

    public static int getCountdownSeconds() { return countdownSeconds; }
    public static void setCountdownSeconds(int value) { countdownSeconds = value; }
}
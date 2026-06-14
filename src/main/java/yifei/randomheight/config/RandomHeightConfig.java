package yifei.randomheight.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraftforge.common.ForgeConfigSpec;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class RandomHeightConfig {
    private static final String CONFIG_FILE_NAME = "randomheight.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static int intervalSeconds = 180;
    private static double minScale = 0.1;
    private static double maxScale = 5.0;
    private static boolean enabled = true;
    private static boolean countdownEnabled = true;
    private static int countdownSeconds = 10;

    public static void loadFromFile() {
        File configFile = getConfigFile();
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                
                if (json.has("interval_seconds")) {
                    intervalSeconds = json.get("interval_seconds").getAsInt();
                }
                if (json.has("min_scale")) {
                    minScale = json.get("min_scale").getAsDouble();
                }
                if (json.has("max_scale")) {
                    maxScale = json.get("max_scale").getAsDouble();
                }
                if (json.has("enabled")) {
                    enabled = json.get("enabled").getAsBoolean();
                }
                if (json.has("countdown_enabled")) {
                    countdownEnabled = json.get("countdown_enabled").getAsBoolean();
                }
                if (json.has("countdown_seconds")) {
                    countdownSeconds = json.get("countdown_seconds").getAsInt();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            saveToFile();
        }
    }

    public static void saveToFile() {
        File configFile = getConfigFile();
        try (FileWriter writer = new FileWriter(configFile)) {
            JsonObject json = new JsonObject();
            json.addProperty("interval_seconds", intervalSeconds);
            json.addProperty("min_scale", minScale);
            json.addProperty("max_scale", maxScale);
            json.addProperty("enabled", enabled);
            json.addProperty("countdown_enabled", countdownEnabled);
            json.addProperty("countdown_seconds", countdownSeconds);
            GSON.toJson(json, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getConfigFile() {
        File gameDir = new File(".");
        File configDir = new File(gameDir, "config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        return new File(configDir, CONFIG_FILE_NAME);
    }

    public static int getIntervalSeconds() { return intervalSeconds; }
    public static void setIntervalSeconds(int value) { 
        intervalSeconds = Math.max(10, Math.min(3600, value)); 
    }

    public static double getMinScale() { return minScale; }
    public static void setMinScale(double value) { 
        minScale = Math.max(0.1, Math.min(5.0, value)); 
    }

    public static double getMaxScale() { return maxScale; }
    public static void setMaxScale(double value) { 
        maxScale = Math.max(0.1, Math.min(5.0, value)); 
    }

    public static boolean isEnabled() { return enabled; }
    public static void setEnabled(boolean value) { enabled = value; }

    public static boolean isCountdownEnabled() { return countdownEnabled; }
    public static void setCountdownEnabled(boolean value) { countdownEnabled = value; }

    public static int getCountdownSeconds() { return countdownSeconds; }
    public static void setCountdownSeconds(int value) { 
        countdownSeconds = Math.max(1, Math.min(60, value)); 
    }
}
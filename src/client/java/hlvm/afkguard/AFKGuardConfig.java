package hlvm.afkguard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration manager for AFK Guard mod.
 * Loads and saves config to config/afkguard.json.
 * Works independently of Cloth Config.
 */
public class AFKGuardConfig {
    private static AFKGuardConfig instance;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("afkguard.json");

    // Config values with defaults
    private int autoAfkDelaySeconds = 30;
    private boolean showStatusMessages = true;
    private boolean ignorePlayerDamage = true;

    private AFKGuardConfig() {
    }

    public static AFKGuardConfig getInstance() {
        if (instance == null) {
            instance = new AFKGuardConfig();
            instance.load();
        }
        return instance;
    }

    /**
     * Load config from file, or create default if not exists.
     */
    public void load() {
        if (Files.exists(CONFIG_PATH)) {
            try {
                String json = Files.readString(CONFIG_PATH);
                AFKGuardConfig loaded = GSON.fromJson(json, AFKGuardConfig.class);
                if (loaded != null) {
                    this.autoAfkDelaySeconds = loaded.autoAfkDelaySeconds;
                    this.showStatusMessages = loaded.showStatusMessages;
                    this.ignorePlayerDamage = loaded.ignorePlayerDamage;
                }
                AFKGuard.LOGGER.info("Loaded config from {}", CONFIG_PATH);
            } catch (IOException e) {
                AFKGuard.LOGGER.error("Failed to load config", e);
            }
        } else {
            save(); // Create default config
        }
    }

    /**
     * Save current config to file.
     */
    public void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            String json = GSON.toJson(this);
            Files.writeString(CONFIG_PATH, json);
            AFKGuard.LOGGER.info("Saved config to {}", CONFIG_PATH);
        } catch (IOException e) {
            AFKGuard.LOGGER.error("Failed to save config", e);
        }
    }

    // Getters
    public int getAutoAfkDelaySeconds() {
        return autoAfkDelaySeconds;
    }

    public long getAutoAfkDelayMs() {
        return autoAfkDelaySeconds * 1000L;
    }

    public boolean shouldShowStatusMessages() {
        return showStatusMessages;
    }

    public boolean shouldIgnorePlayerDamage() {
        return ignorePlayerDamage;
    }

    // Setters (with save)
    public void setAutoAfkDelaySeconds(int seconds) {
        this.autoAfkDelaySeconds = Math.max(1, seconds);
        save();
    }

    public void setShowStatusMessages(boolean show) {
        this.showStatusMessages = show;
        save();
    }

    public void setIgnorePlayerDamage(boolean ignore) {
        this.ignorePlayerDamage = ignore;
        save();
    }
}

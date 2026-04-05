package hlvm.afkguard;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * Central state manager for AFK Guard mod.
 * Tracks AFK guard status, auto-AFK detection, and player position.
 */
public class AFKGuardState {
    private static AFKGuardState instance;

    private boolean afkGuardEnabled = false;
    private boolean autoAfkEnabled = false;
    private boolean wasAutoEnabled = false; // Tracks if guard was auto-enabled
    private long lastInputTime = System.currentTimeMillis();

    // Get delay from config

    // Damage tracking
    private boolean lastDamageWasPlayer = false;
    private long lastDamageTime = 0;

    private AFKGuardState() {
    }

    public static AFKGuardState getInstance() {
        if (instance == null) {
            instance = new AFKGuardState();
        }
        return instance;
    }

    public boolean isAfkGuardEnabled() {
        return afkGuardEnabled;
    }

    public boolean isAutoAfkEnabled() {
        return autoAfkEnabled;
    }

    public void toggleAfkGuard() {
        afkGuardEnabled = !afkGuardEnabled;
        wasAutoEnabled = false; // Manual toggle clears auto flag
        if (AFKGuardConfig.getInstance().shouldShowStatusMessages()) {
            showStatusMessage(afkGuardEnabled ? "§a[AFK Guard] Enabled" : "§c[AFK Guard] Disabled");
        }
    }

    public void toggleAutoAfk() {
        autoAfkEnabled = !autoAfkEnabled;
        if (!autoAfkEnabled) {
            // Reset timer and auto flag when disabling
            lastInputTime = System.currentTimeMillis();
            wasAutoEnabled = false;
        }
        if (AFKGuardConfig.getInstance().shouldShowStatusMessages()) {
            showStatusMessage(autoAfkEnabled ? "§a[Auto-AFK] Enabled" : "§c[Auto-AFK] Disabled");
        }
    }

    public void setAfkGuardEnabled(boolean enabled) {
        this.afkGuardEnabled = enabled;
    }

    public void registerInput() {
        lastInputTime = System.currentTimeMillis();

        // If guard was auto-enabled, disable it on input
        if (afkGuardEnabled && wasAutoEnabled) {
            afkGuardEnabled = false;
            wasAutoEnabled = false;
            if (AFKGuardConfig.getInstance().shouldShowStatusMessages()) {
                showStatusMessage("§c[AFK Guard] Auto-disabled on player input");
            }
        }
    }

    public void setLastDamageWasPlayer(boolean wasPlayer) {
        this.lastDamageWasPlayer = wasPlayer;
        this.lastDamageTime = System.currentTimeMillis();
    }

    public boolean wasLastDamagePlayer() {
        // Only consider valid if recent (e.g., within 1 second)
        return lastDamageWasPlayer && (System.currentTimeMillis() - lastDamageTime < 1000);
    }

    /**
     * Called every client tick to check for auto-AFK timeout.
     * When auto-AFK is enabled:
     * - Enables AFK Guard after delay of no input
     */
    public void tick() {
        if (!autoAfkEnabled) {
            return;
        }

        // Check if delay has passed since last input
        if (!afkGuardEnabled) {
            long idleTime = System.currentTimeMillis() - lastInputTime;
            long delayMs = AFKGuardConfig.getInstance().getAutoAfkDelayMs();
            if (idleTime >= delayMs) {
                afkGuardEnabled = true;
                wasAutoEnabled = true;
                int delaySec = AFKGuardConfig.getInstance().getAutoAfkDelaySeconds();
                if (AFKGuardConfig.getInstance().shouldShowStatusMessages()) {
                    showStatusMessage("§a[AFK Guard] Auto-enabled after " + delaySec + "s idle");
                }
            }
        }
    }

    /**
     * Reset state when player disconnects.
     */
    public void onDisconnect() {
        afkGuardEnabled = false;
        wasAutoEnabled = false;
        lastInputTime = System.currentTimeMillis();
    }

    private void showStatusMessage(String message) {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            client.player.sendSystemMessage(Component.literal(message));
        }
    }
}

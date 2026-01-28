package hlvm.afkguard;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

/**
 * Central state manager for AFK Guard mod.
 * Tracks AFK guard status, auto-AFK detection, and player position.
 */
public class AFKGuardState {
    private static AFKGuardState instance;

    private boolean afkGuardEnabled = false;
    private boolean autoAfkEnabled = false;
    private boolean wasAutoEnabled = false; // Tracks if guard was auto-enabled
    private long lastMoveTime = System.currentTimeMillis();
    private Vec3d lastPosition = null;

    // 30 seconds in milliseconds
    private static final long AUTO_AFK_DELAY_MS = 30_000;

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
        showStatusMessage(afkGuardEnabled ? "§a[AFK Guard] Enabled" : "§c[AFK Guard] Disabled");
    }

    public void toggleAutoAfk() {
        autoAfkEnabled = !autoAfkEnabled;
        if (!autoAfkEnabled) {
            // Reset timer and auto flag when disabling
            lastMoveTime = System.currentTimeMillis();
            wasAutoEnabled = false;
        }
        showStatusMessage(autoAfkEnabled ? "§a[Auto-AFK] Enabled" : "§c[Auto-AFK] Disabled");
    }

    public void setAfkGuardEnabled(boolean enabled) {
        this.afkGuardEnabled = enabled;
    }

    /**
     * Called every client tick to track player position for auto-AFK.
     * When auto-AFK is enabled:
     * - Enables AFK Guard after 30s of no movement
     * - Disables AFK Guard when player moves (if it was auto-enabled)
     */
    public void tick() {
        if (!autoAfkEnabled) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;

        if (player == null) {
            return;
        }

        Vec3d currentPos = new Vec3d(player.getX(), player.getY(), player.getZ());

        if (lastPosition == null) {
            lastPosition = currentPos;
            lastMoveTime = System.currentTimeMillis();
            return;
        }

        // Check if player has moved (with small tolerance for floating point)
        double distanceMoved = currentPos.squaredDistanceTo(lastPosition);
        boolean playerMoved = distanceMoved > 0.001;

        if (playerMoved) {
            // Player moved, reset timer
            lastPosition = currentPos;
            lastMoveTime = System.currentTimeMillis();

            // If guard was auto-enabled, disable it on movement
            if (afkGuardEnabled && wasAutoEnabled) {
                afkGuardEnabled = false;
                wasAutoEnabled = false;
                showStatusMessage("§c[AFK Guard] Auto-disabled (player moved)");
            }
        } else {
            // Player hasn't moved, check if 30 seconds have passed
            if (!afkGuardEnabled) {
                long idleTime = System.currentTimeMillis() - lastMoveTime;
                if (idleTime >= AUTO_AFK_DELAY_MS) {
                    afkGuardEnabled = true;
                    wasAutoEnabled = true;
                    showStatusMessage("§a[AFK Guard] Auto-enabled after 30s idle");
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
        lastPosition = null;
        lastMoveTime = System.currentTimeMillis();
    }

    private void showStatusMessage(String message) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.sendMessage(Text.literal(message), true);
        }
    }
}

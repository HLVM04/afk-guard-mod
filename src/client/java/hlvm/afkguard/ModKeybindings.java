package hlvm.afkguard;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * Registers and manages keybindings for AFK Guard mod.
 */
public class ModKeybindings {
    private static boolean wasToggleAfkDown;
    private static boolean wasToggleAutoDown;

    public static void register() {
        wasToggleAfkDown = false;
        wasToggleAutoDown = false;
    }

    public static boolean consumeToggleAfkGuard() {
        return consumeKeyPress(GLFW.GLFW_KEY_G, true);
    }

    public static boolean consumeToggleAutoAfk() {
        return consumeKeyPress(GLFW.GLFW_KEY_H, false);
    }

    private static boolean consumeKeyPress(int key, boolean afkToggle) {
        Minecraft client = Minecraft.getInstance();
        if (client == null || client.getWindow() == null) {
            return false;
        }

        boolean isDown = InputConstants.isKeyDown(client.getWindow(), key);
        boolean wasDown = afkToggle ? wasToggleAfkDown : wasToggleAutoDown;
        boolean pressed = isDown && !wasDown;

        if (afkToggle) {
            wasToggleAfkDown = isDown;
        } else {
            wasToggleAutoDown = isDown;
        }

        return pressed;
    }
}

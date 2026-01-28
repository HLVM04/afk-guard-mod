package hlvm.afkguard;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

/**
 * Registers and manages keybindings for AFK Guard mod.
 */
public class ModKeybindings {
    private static KeyBinding toggleAfkGuardKey;
    private static KeyBinding toggleAutoAfkKey;

    // Create custom category for AFK Guard
    private static final KeyBinding.Category CATEGORY = KeyBinding.Category.create(
            Identifier.of(AFKGuard.MOD_ID, "keybinds"));

    public static void register() {
        toggleAfkGuardKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.afkguard.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                CATEGORY));

        toggleAutoAfkKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.afkguard.toggle_auto",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                CATEGORY));
    }

    public static KeyBinding getToggleAfkGuardKey() {
        return toggleAfkGuardKey;
    }

    public static KeyBinding getToggleAutoAfkKey() {
        return toggleAutoAfkKey;
    }
}

package hlvm.afkguard.config;

import net.minecraft.client.gui.screens.Screen;

/**
 * Placeholder cloth-config integration for Mojang mappings migration.
 *
 * The previous cloth-config API wiring was intermediary-mapped and fails to
 * compile in this Mojang-mapped source set. Returning null keeps Mod Menu
 * integration safe while command-based configuration remains available.
 */
public class ClothConfigIntegration {

    public static Screen createConfigScreen(Screen parent) {
        return null;
    }
}

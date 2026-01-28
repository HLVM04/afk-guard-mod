package hlvm.afkguard.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import hlvm.afkguard.AFKGuard;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Mod Menu integration for AFK Guard.
 * Provides config screen factory that uses Cloth Config when available.
 */
public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // Check if Cloth Config is installed
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return parent -> {
                try {
                    return ClothConfigIntegration.createConfigScreen(parent);
                } catch (NoClassDefFoundError | Exception e) {
                    AFKGuard.LOGGER.warn("Failed to create Cloth Config screen: {}", e.getMessage());
                    return null;
                }
            };
        }
        // No config screen without Cloth Config - users can use /afkguard commands
        return parent -> null;
    }
}

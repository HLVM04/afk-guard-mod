package hlvm.afkguard.config;

import hlvm.afkguard.AFKGuardConfig;
import hlvm.afkguard.ModKeybindings;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

/**
 * Cloth Config integration for AFK Guard settings GUI.
 * This class is only loaded if Cloth Config is present.
 */
public class ClothConfigIntegration {

        /**
         * Creates and returns a Cloth Config screen for AFK Guard settings.
         * 
         * @param parent The parent screen to return to
         * @return The config screen
         */
        public static Screen createConfigScreen(Screen parent) {
                AFKGuardConfig config = AFKGuardConfig.getInstance();

                ConfigBuilder builder = ConfigBuilder.create()
                                .setParentScreen(parent)
                                .setTitle(Text.translatable("config.afkguard.title"))
                                .setSavingRunnable(config::save);

                ConfigEntryBuilder entryBuilder = builder.entryBuilder();

                // Single category for all settings
                ConfigCategory category = builder
                                .getOrCreateCategory(Text.translatable("config.afkguard.category.general"));

                // Auto-AFK delay
                category.addEntry(entryBuilder
                                .startIntField(Text.translatable("config.afkguard.autoAfkDelay"),
                                                config.getAutoAfkDelaySeconds())
                                .setDefaultValue(30)
                                .setMin(1)
                                .setMax(3600)
                                .setTooltip(Text.translatable("config.afkguard.autoAfkDelay.tooltip"))
                                .setSaveConsumer(config::setAutoAfkDelaySeconds)
                                .build());

                // Show status messages
                category.addEntry(entryBuilder
                                .startBooleanToggle(Text.translatable("config.afkguard.showStatusMessages"),
                                                config.shouldShowStatusMessages())
                                .setDefaultValue(true)
                                .setTooltip(Text.translatable("config.afkguard.showStatusMessages.tooltip"))
                                .setSaveConsumer(config::setShowStatusMessages)
                                .build());

                // Ignore Player Damage
                category.addEntry(entryBuilder
                                .startBooleanToggle(Text.translatable("config.afkguard.ignorePlayerDamage"),
                                                config.shouldIgnorePlayerDamage())
                                .setDefaultValue(true)
                                .setTooltip(Text.translatable("config.afkguard.ignorePlayerDamage.tooltip"))
                                .setSaveConsumer(config::setIgnorePlayerDamage)
                                .build());

                // Toggle AFK Guard keybind
                KeyBinding toggleAfkKey = ModKeybindings.getToggleAfkGuardKey();
                category.addEntry(entryBuilder
                                .startKeyCodeField(Text.translatable("key.afkguard.toggle"),
                                                toggleAfkKey.getDefaultKey())
                                .setDefaultValue(toggleAfkKey.getDefaultKey())
                                .setKeySaveConsumer(key -> toggleAfkKey.setBoundKey(key))
                                .setTooltip(Text.translatable("config.afkguard.keybind.toggle.tooltip"))
                                .build());

                // Toggle Auto-AFK keybind
                KeyBinding toggleAutoKey = ModKeybindings.getToggleAutoAfkKey();
                category.addEntry(entryBuilder
                                .startKeyCodeField(Text.translatable("key.afkguard.toggle_auto"),
                                                toggleAutoKey.getDefaultKey())
                                .setDefaultValue(toggleAutoKey.getDefaultKey())
                                .setKeySaveConsumer(key -> toggleAutoKey.setBoundKey(key))
                                .setTooltip(Text.translatable("config.afkguard.keybind.toggle_auto.tooltip"))
                                .build());

                return builder.build();
        }
}

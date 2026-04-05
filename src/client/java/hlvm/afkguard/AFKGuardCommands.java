package hlvm.afkguard;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.network.chat.Component;

/**
 * Registers client-side commands for configuring AFK Guard.
 * Allows configuration without Cloth Config / Mod Menu.
 */
public class AFKGuardCommands {

        public static void register() {
                ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                        dispatcher.register(
                                        ClientCommands.literal("afkguard")
                                                        // Show current config
                                                        .then(ClientCommands.literal("config")
                                                                        .executes(context -> {
                                                                                AFKGuardConfig config = AFKGuardConfig
                                                                                                .getInstance();
                                                                                context.getSource().sendFeedback(Component
                                                                                                .literal("§6=== AFK Guard Config ==="));
                                                                                context.getSource().sendFeedback(
                                                                                                Component.literal(
                                                                                                                "§7Auto-AFK delay: §f"
                                                                                                                                + config.getAutoAfkDelaySeconds()
                                                                                                                                + " seconds"));
                                                                                context.getSource().sendFeedback(Component
                                                                                                .literal("§7Status messages: §f"
                                                                                                                + (config.shouldShowStatusMessages()
                                                                                                                                ? "enabled"
                                                                                                                                : "disabled")));
                                                                                context.getSource().sendFeedback(Component
                                                                                                .literal("§7Ignore player damage: §f"
                                                                                                                + (config.shouldIgnorePlayerDamage()
                                                                                                                                ? "enabled"
                                                                                                                                : "disabled")));
                                                                                return 1;
                                                                        }))
                                                        // Set auto-AFK delay
                                                        .then(ClientCommands.literal("delay")
                                                                        .then(ClientCommands.argument("seconds",
                                                                                        IntegerArgumentType.integer(1,
                                                                                                        3600))
                                                                                        .executes(context -> {
                                                                                                int seconds = IntegerArgumentType
                                                                                                                .getInteger(context,
                                                                                                                                "seconds");
                                                                                                AFKGuardConfig.getInstance()
                                                                                                                .setAutoAfkDelaySeconds(
                                                                                                                                seconds);
                                                                                                context.getSource()
                                                                                                                .sendFeedback(Component
                                                                                                                                .literal("§aAuto-AFK delay set to "
                                                                                                                                                + seconds
                                                                                                                                                + " seconds"));
                                                                                                return 1;
                                                                                        })))
                                                        // Toggle status messages
                                                        .then(ClientCommands.literal("messages")
                                                                        .then(ClientCommands.argument("enabled",
                                                                                        BoolArgumentType.bool())
                                                                                        .executes(context -> {
                                                                                                boolean enabled = BoolArgumentType
                                                                                                                .getBool(context,
                                                                                                                                "enabled");
                                                                                                AFKGuardConfig.getInstance()
                                                                                                                .setShowStatusMessages(
                                                                                                                                enabled);
                                                                                                context.getSource()
                                                                                                                .sendFeedback(Component
                                                                                                                                .literal(
                                                                                                                                                "§aStatus messages "
                                                                                                                                                                + (enabled ? "enabled"
                                                                                                                                                                                : "disabled")));
                                                                                                return 1;
                                                                                        })))
                                                        // Toggle ignore player damage
                                                        .then(ClientCommands.literal("ignoreplayerdamage")
                                                                        .then(ClientCommands.argument("enabled",
                                                                                        BoolArgumentType.bool())
                                                                                        .executes(context -> {
                                                                                                boolean enabled = BoolArgumentType
                                                                                                                .getBool(context,
                                                                                                                                "enabled");
                                                                                                AFKGuardConfig.getInstance()
                                                                                                                .setIgnorePlayerDamage(
                                                                                                                                enabled);
                                                                                                context.getSource()
                                                                                                                .sendFeedback(Component
                                                                                                                                .literal(
                                                                                                                                                "§aIgnore player damage "
                                                                                                                                                                + (enabled ? "enabled"
                                                                                                                                                                                : "disabled")));
                                                                                                return 1;
                                                                                        })))
                                                        // Show help
                                                        .executes(context -> {
                                                                context.getSource().sendFeedback(Component.literal(
                                                                                "§6=== AFK Guard Commands ==="));
                                                                context.getSource()
                                                                                .sendFeedback(Component.literal(
                                                                                                "§e/afkguard config §7- Show current config"));
                                                                context.getSource().sendFeedback(
                                                                                Component.literal("§e/afkguard delay <seconds> §7- Set auto-AFK delay"));
                                                                context.getSource().sendFeedback(
                                                                                Component.literal("§e/afkguard messages <true|false> §7- Toggle status messages"));
                                                                context.getSource().sendFeedback(
                                                                                Component.literal("§e/afkguard ignoreplayerdamage <true|false> §7- Toggle ignore player damage"));
                                                                return 1;
                                                        }));
                });
        }
}

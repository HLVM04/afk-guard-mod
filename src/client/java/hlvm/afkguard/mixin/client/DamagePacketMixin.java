package hlvm.afkguard.mixin.client;

import hlvm.afkguard.AFKGuardConfig;
import hlvm.afkguard.AFKGuardState;
import hlvm.afkguard.AFKGuard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to intercept damage packets and identify if the attacker is a player.
 */
@Mixin(ClientPacketListener.class)
public class DamagePacketMixin {

    @Inject(method = "handleDamageEvent", at = @At("HEAD"))
    private void onEntityDamage(ClientboundDamageEventPacket packet, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        ClientLevel world = client.level;

        if (world == null || client.player == null)
            return;

        // Check if the damaged entity is the local player
        if (packet.entityId() == client.player.getId()) {
            // Get the source entity (attacker)
            int sourceId = packet.sourceCauseId();
            Entity source = world.getEntity(sourceId);

            boolean damagedByPlayer = source instanceof Player;

            AFKGuardState state = AFKGuardState.getInstance();

            // Update state
            if (state.isAfkGuardEnabled()) {
                boolean ignore = AFKGuardConfig.getInstance().shouldIgnorePlayerDamage();

                if (ignore && damagedByPlayer) {
                    return; // Ignore damage if it was from a player and config is enabled
                }

                // Disable AFK guard to prevent repeated disconnects
                state.setAfkGuardEnabled(false);

                // Disconnect from server
                var networkHandler = client.getConnection();
                if (networkHandler != null) {
                    var connection = networkHandler.getConnection();
                    if (connection != null) {
                        connection.disconnect(Component.literal("AFK Guard: Disconnected due to damage!"));
                    }
                }
            }
        }
    }
}

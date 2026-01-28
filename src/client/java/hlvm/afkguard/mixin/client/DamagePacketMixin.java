package hlvm.afkguard.mixin.client;

import hlvm.afkguard.AFKGuardConfig;
import hlvm.afkguard.AFKGuardState;
import hlvm.afkguard.AFKGuard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityDamageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to intercept damage packets and identify if the attacker is a player.
 */
@Mixin(ClientPlayNetworkHandler.class)
public class DamagePacketMixin {

    @Inject(method = "onEntityDamage", at = @At("HEAD"))
    private void onEntityDamage(EntityDamageS2CPacket packet, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientWorld world = client.world;

        if (world == null || client.player == null)
            return;

        // Check if the damaged entity is the local player
        if (packet.entityId() == client.player.getId()) {
            // Get the source entity (attacker)
            int sourceId = packet.sourceCauseId();
            Entity source = world.getEntityById(sourceId);

            boolean damagedByPlayer = source instanceof PlayerEntity;

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
                var networkHandler = client.getNetworkHandler();
                if (networkHandler != null) {
                    var connection = networkHandler.getConnection();
                    if (connection != null) {
                        connection.disconnect(Text.literal("AFK Guard: Disconnected due to damage!"));
                    }
                }
            }
        }
    }
}

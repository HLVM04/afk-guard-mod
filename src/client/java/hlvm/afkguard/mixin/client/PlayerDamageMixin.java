package hlvm.afkguard.mixin.client;

import hlvm.afkguard.AFKGuardState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to detect when the local player's health decreases.
 * Uses setHealth which is called client-side when health is synchronized.
 * Triggers disconnect if AFK Guard is enabled and health decreased.
 */
@Mixin(LivingEntity.class)
public abstract class PlayerDamageMixin {

    @Shadow
    public abstract float getHealth();

    @Inject(method = "setHealth", at = @At("HEAD"))
    private void onHealthChange(float health, CallbackInfo ci) {
        // Check if this is the local player
        LivingEntity self = (LivingEntity) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || self != client.player) {
            return;
        }

        // Get current health before it changes
        float currentHealth = this.getHealth();

        // Only trigger if health is decreasing (taking damage)
        if (health >= currentHealth) {
            return;
        }

        AFKGuardState state = AFKGuardState.getInstance();
        if (state.isAfkGuardEnabled()) {
            // Disable AFK guard to prevent repeated disconnects
            state.setAfkGuardEnabled(false);

            // Disconnect from server
            if (client.getNetworkHandler() != null && client.getNetworkHandler().getConnection() != null) {
                client.getNetworkHandler().getConnection().disconnect(
                        Text.literal("AFK Guard: Disconnected due to damage!"));
            }
        }
    }
}

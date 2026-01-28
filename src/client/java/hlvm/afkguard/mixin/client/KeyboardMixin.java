package hlvm.afkguard.mixin.client;

import hlvm.afkguard.AFKGuardState;
import net.minecraft.client.Keyboard;
import net.minecraft.client.input.KeyInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(method = "onKey", at = @At("HEAD"))
    private void onKey(long window, int action, KeyInput input, CallbackInfo ci) {
        // Only track generic key presses (action 1 = press, 2 = repeat)
        if (action != 1) { // 0 is release
            AFKGuardState.getInstance().registerInput();
        }
    }
}

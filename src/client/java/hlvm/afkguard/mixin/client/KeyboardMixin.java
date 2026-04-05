package hlvm.afkguard.mixin.client;

import hlvm.afkguard.AFKGuardState;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {
    @Inject(method = "keyPress", at = @At("HEAD"))
    private void onKey(long window, int action, KeyEvent input, CallbackInfo ci) {
        // Only track generic key presses (action 1 = press, 2 = repeat)
        if (action != 0) { // 0 is release
            AFKGuardState.getInstance().registerInput();
        }
    }
}

package hlvm.afkguard.mixin.client;

import hlvm.afkguard.AFKGuardState;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {
    @Inject(method = "onMove", at = @At("HEAD"))
    private void onCursorPos(long window, double x, double y, CallbackInfo ci) {
        AFKGuardState.getInstance().registerInput();
    }

    @Inject(method = "onButton", at = @At("HEAD"))
    private void onMouseButton(long window, MouseButtonInfo input, int action, CallbackInfo ci) {
        if (action != 0) { // 0 is release
            AFKGuardState.getInstance().registerInput();
        }
    }
}

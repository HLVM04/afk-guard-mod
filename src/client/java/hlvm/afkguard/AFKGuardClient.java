package hlvm.afkguard;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class AFKGuardClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// Initialize config
		AFKGuardConfig.getInstance();

		// Register keybindings
		ModKeybindings.register();

		// Register client commands
		AFKGuardCommands.register();

		// Register tick event for keybind handling and auto-AFK
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			// Handle keybind presses
			if (ModKeybindings.consumeToggleAfkGuard()) {
				AFKGuardState.getInstance().toggleAfkGuard();
			}

			if (ModKeybindings.consumeToggleAutoAfk()) {
				AFKGuardState.getInstance().toggleAutoAfk();
			}

			// Tick the state for auto-AFK detection
			AFKGuardState.getInstance().tick();
		});

		AFKGuard.LOGGER.info("AFK Guard client initialized!");
	}
}
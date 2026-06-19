package uranium.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import uranium.ModBlockEntities;
import uranium.ModMenuType;
import uranium.ReactorBlockEntity;
import java.util.HashMap;
import java.util.Map;

public class UraniumAgeClient implements ClientModInitializer {

	private final Map<BlockPos, ReactorHumSoundInstance> playing = new HashMap<>();

	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		BlockEntityRenderers.register(ModBlockEntities.Reactor_Block_Entity, ActiveBlockReactorOverlay::new);
		MenuScreens.register(ModMenuType.REACTOR_BLOCK, ReactorScreen::new);

		ReactorBlockEntity.START_SOUND = pos -> {
			ReactorHumSoundInstance existing = playing.get(pos);
			if (existing == null || !Minecraft.getInstance().getSoundManager().isActive(existing)){
				ReactorHumSoundInstance instance = new ReactorHumSoundInstance(pos);
				playing.put(pos, instance);
				Minecraft.getInstance().getSoundManager().play(instance);
			}
		};

		ReactorBlockEntity.STOP_SOUND = pos -> {
			ReactorHumSoundInstance instance = playing.remove(pos);
			if (instance != null) {
				Minecraft.getInstance().getSoundManager().stop(instance);
			}
		};
	}
}
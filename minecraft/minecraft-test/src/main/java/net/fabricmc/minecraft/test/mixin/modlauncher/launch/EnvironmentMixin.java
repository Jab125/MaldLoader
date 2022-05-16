package net.fabricmc.minecraft.test.mixin.modlauncher.launch;

import cpw.mods.modlauncher.Environment;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import me.hydos.copecw.DummyModuleLayerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

@Mixin(value = Environment.class, remap = false)
public class EnvironmentMixin {
	private static final DummyModuleLayerManager LAYER_MANAGER = new DummyModuleLayerManager();

	/**
	 * @author hYdos
	 * @reason Replace with dummy class because modules are useless
	 */
	@Overwrite
	public Optional<IModuleLayerManager> findModuleLayerManager() {
		return Optional.of(LAYER_MANAGER);
	}
}

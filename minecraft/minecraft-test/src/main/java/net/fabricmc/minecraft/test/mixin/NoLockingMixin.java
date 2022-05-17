package net.fabricmc.minecraft.test.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "net.minecraftforge.registries.NamespacedDefaultedWrapper")
public class NoLockingMixin {
	/**
	 * @author Jab125
	 * @reason REGISTRY
	 */
	@Overwrite
	public void lock(){  }
}

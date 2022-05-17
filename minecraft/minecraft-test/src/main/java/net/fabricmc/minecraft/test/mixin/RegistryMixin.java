package net.fabricmc.minecraft.test.mixin;

import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Registry.class)
public class RegistryMixin {
	/**
	 * @author Jab125
	 * @reason Let me REGISTER it please
	 */
	@Overwrite
	public static void freezeRegistries() {
	}
}

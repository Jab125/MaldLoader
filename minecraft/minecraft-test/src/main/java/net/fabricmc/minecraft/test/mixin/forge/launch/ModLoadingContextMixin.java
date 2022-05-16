package net.fabricmc.minecraft.test.mixin.forge.launch;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ModLoadingContext.class, remap = false)
public class ModLoadingContextMixin {
	@Inject(method = "setActiveContainer", at = @At("HEAD"), cancellable = true)
	public void ignoreNullContainer(ModContainer container, CallbackInfo ci) {
		if (container == null) ci.cancel();
	}
}

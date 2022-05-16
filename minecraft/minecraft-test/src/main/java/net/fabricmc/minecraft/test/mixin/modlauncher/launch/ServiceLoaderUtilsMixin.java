package net.fabricmc.minecraft.test.mixin.modlauncher.launch;

import cpw.mods.modlauncher.util.ServiceLoaderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ServiceLoaderUtils.class, remap = false)
public class ServiceLoaderUtilsMixin {

	@Inject(method = "fileNameFor", at = @At("HEAD"), cancellable = true)
	private static void emptyFile(Class<?> clazz, CallbackInfoReturnable<String> cir) {
		cir.setReturnValue("MISSING FILE");
	}
}

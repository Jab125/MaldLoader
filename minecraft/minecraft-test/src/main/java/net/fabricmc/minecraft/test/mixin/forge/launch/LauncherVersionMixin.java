package net.fabricmc.minecraft.test.mixin.forge.launch;

import net.minecraftforge.fml.loading.JarVersionLookupHandler;
import net.minecraftforge.fml.loading.LauncherVersion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = JarVersionLookupHandler.class, remap = false)
public class LauncherVersionMixin {
	@Inject(method = "getImplementationVersion(Ljava/lang/Class;)Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
	private static void setThatSoThatItWIllSHUTUP(Class<?> clazz, CallbackInfoReturnable<Optional<String>> cir) {
		if (clazz == LauncherVersion.class) {
			cir.setReturnValue(Optional.of("38.0.17"));
		}
	}
}

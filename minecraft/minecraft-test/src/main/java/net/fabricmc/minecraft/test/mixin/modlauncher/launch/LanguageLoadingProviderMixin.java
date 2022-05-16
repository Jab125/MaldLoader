package net.fabricmc.minecraft.test.mixin.modlauncher.launch;

import java.util.Optional;
import java.util.ServiceLoader;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LanguageLoadingProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = LanguageLoadingProvider.class, remap = false)
public abstract class LanguageLoadingProviderMixin {

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/util/ServiceLoader;load(Ljava/lang/ModuleLayer;Ljava/lang/Class;)Ljava/util/ServiceLoader;"))
	private <S> ServiceLoader<S> loadServiceNoModule(ModuleLayer layer, Class<S> service) {
		return ServiceLoader.load(service);
	}

	@Redirect(method = "lambda$loadLanguageProviders$4", at = @At(value = "INVOKE", target = "Ljava/util/Optional;orElse(Ljava/lang/Object;)Ljava/lang/Object;"))
	private <T> Object fallbackVersionsIfPossible(Optional<String> instance, T other) {
		if(instance.isEmpty()) {
			return FMLLoader.versionInfo().forgeVersion().split("\\.")[0];
		} else {
			return instance.get();
		}
	}
}

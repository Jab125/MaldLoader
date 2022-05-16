package net.fabricmc.minecraft.test.mixin.modlauncher.launch;

import java.util.ServiceLoader;

import net.minecraftforge.fml.loading.moddiscovery.ModDiscoverer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ModDiscoverer.class, remap = false)
public class ModDiscovererMixin {

	@Redirect(method = "<init>(Ljava/util/Map;)V", at = @At(value = "INVOKE", target = "Ljava/util/ServiceLoader;load(Ljava/lang/ModuleLayer;Ljava/lang/Class;)Ljava/util/ServiceLoader;"))
	private <S> ServiceLoader<S> loadNonModuleService(ModuleLayer layer, Class<S> service) {
		return ServiceLoader.load(service);
	}
}

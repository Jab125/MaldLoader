package net.fabricmc.minecraft.test.mixin;

import java.util.Map;
import java.util.ServiceLoader;

import cpw.mods.modlauncher.TransformationServiceDecorator;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "cpw/mods/modlauncher/TransformationServicesHandler")
public class TransformationServicesHandlerMixin {

	@Shadow
	@Final
	private static Logger LOGGER;

	@Shadow
	private Map<String, TransformationServiceDecorator> serviceLookup;

	@Redirect(method = "lambda$discoverServices$18", at = @At(value = "INVOKE", target = "Ljava/util/ServiceLoader;load(Ljava/lang/ModuleLayer;Ljava/lang/Class;)Ljava/util/ServiceLoader;"))
	private static <S> ServiceLoader<S> loadServicesWithoutModules(ModuleLayer layer, Class<S> service) {
		return ServiceLoader.load(service);
	}
}

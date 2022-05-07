package net.fabricmc.minecraft.test.mixin.modlauncher.launch;

import cpw.mods.jarhandling.SecureJar;
import cpw.mods.modlauncher.LaunchPluginHandler;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

@Mixin(LaunchPluginHandler.class)
public class LaunchPluginHandlerMixin {

	@Shadow
	@Final
	private Map<String, ILaunchPluginService> plugins;

	@Redirect(method = "lambda$new$0", at = @At(value = "INVOKE", target = "Ljava/util/ServiceLoader;load(Ljava/lang/ModuleLayer;Ljava/lang/Class;)Ljava/util/ServiceLoader;"))
	private static <S> ServiceLoader<S> getServicesWithoutModules(ModuleLayer layer, Class<S> service) {
		return ServiceLoader.load(service);
	}

	/**
	 * @author hYdos
	 * @reason dont enable mixin plugin
	 */
	@Overwrite
	void offerScanResultsToPlugins(List<SecureJar> scanResults) {
		plugins.forEach((n, p) -> {
			if (!n.equals("mixin")) {
				p.addResources(scanResults);
			}
		});
	}
}

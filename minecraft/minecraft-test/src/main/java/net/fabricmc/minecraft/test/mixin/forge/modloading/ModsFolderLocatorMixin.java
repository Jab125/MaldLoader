package net.fabricmc.minecraft.test.mixin.forge.modloading;

import net.minecraftforge.fml.loading.LogMarkers;
import net.minecraftforge.fml.loading.ModDirTransformerDiscoverer;
import net.minecraftforge.fml.loading.StringUtils;
import net.minecraftforge.fml.loading.moddiscovery.AbstractJarFileLocator;
import net.minecraftforge.fml.loading.moddiscovery.ModsFolderLocator;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static cpw.mods.modlauncher.api.LamdbaExceptionUtils.uncheck;

@Mixin(ModsFolderLocator.class)
public abstract class ModsFolderLocatorMixin extends AbstractJarFileLocator {

	@Shadow
	@Final
	private static Logger LOGGER;

	@Shadow
	@Final
	private Path modFolder;

	@Shadow
	@Final
	private static String SUFFIX;

	@Override
	public Stream<Path> scanCandidates() {
		LOGGER.debug(LogMarkers.SCAN, "Scanning mods dir {} for mods", this.modFolder);
		var excluded = ModDirTransformerDiscoverer.allExcluded();

		return uncheck(() -> Files.list(this.modFolder))
				.filter(p -> !excluded.contains(p) && StringUtils.toLowerCase(p.getFileName().toString()).endsWith(SUFFIX))
				.filter(p -> {
					try(JarFile jar = new JarFile(p.toFile())) {
						return jar.stream().filter(jarEntry -> jarEntry.getName().equals("fabric.mod.json")).toList().size() == 0;
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				})
				.sorted(Comparator.comparing(path -> StringUtils.toLowerCase(path.getFileName().toString())));
	}
}

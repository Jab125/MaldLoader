package net.fabricmc.minecraft.test.mixin;

import net.minecraftforge.fml.loading.targets.CommonUserdevLaunchHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.File;

@Mixin(CommonUserdevLaunchHandler.class)
public class CommonUserdevLaunchHandlerMixin {

	@Redirect(method = "getMinecraftPaths", at = @At(value = "INVOKE", target = "Ljava/lang/System;getProperty(Ljava/lang/String;)Ljava/lang/String;"))
	private String getClasspath(String key) {
		if(key.equals("legacyClassPath")) {
			key = "java.class.path";

			String[] cp = System.getProperty(key).split(File.pathSeparator);
			String newCp = "";
			for (String lib : cp) {
				if(lib.contains("spi")) continue;
				newCp += lib + File.pathSeparator;
			}
			return newCp;
		}

		return System.getProperty(key);
	}
}

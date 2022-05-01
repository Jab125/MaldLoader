package net.fabricmc.minecraft.test.mixin;

import cpw.mods.jarhandling.impl.Jar;
import cpw.mods.niofs.union.UnionFileSystemProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.file.spi.FileSystemProvider;
import java.util.List;

@Mixin(Jar.class)
public class JarMixin {

	@Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/nio/file/spi/FileSystemProvider;installedProviders()Ljava/util/List;"))
	private static List<FileSystemProvider> whyCpw() {
		return List.of(new UnionFileSystemProvider());
	}
}

package net.fabricmc.minecraft.test.mixin.forge.launch;

import net.minecraftforge.fml.Bindings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ServiceLoader;

@Mixin(value = Bindings.class, remap = false)
public class BindingsMixin {

	@Redirect(method = "lambda$new$0", at = @At(value = "INVOKE", target = "Ljava/util/ServiceLoader;load(Ljava/lang/ModuleLayer;Ljava/lang/Class;)Ljava/util/ServiceLoader;"))
	private static <S> ServiceLoader<S> loadServiceNoModule(ModuleLayer layer, Class<S> service) {
		return ServiceLoader.load(service);
	}
}

/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.minecraft.test.mixin;

import java.lang.module.Configuration;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import cpw.mods.modlauncher.ModuleLayerHandler;
import cpw.mods.modlauncher.api.IModuleLayerManager;
import me.hydos.copecw.KnotModuleClassLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.impl.launch.knot.KnotClassLoader;

@Mixin(value = ModuleLayerHandler.class, remap = false)
public abstract class ModuleLayerHandlerMixin {

	@Shadow
	@Final
	private EnumMap<IModuleLayerManager.Layer, ?> completedLayers;

	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Ljava/lang/Class;getClassLoader()Ljava/lang/ClassLoader;"))
	private ClassLoader nullCope(Class<?> clazz) {
		return new KnotModuleClassLoader((KnotClassLoader) this.getClass().getClassLoader());
	}

	@Inject(method = "getLayer", at = @At("HEAD"), cancellable = true)
	private void checkExtraLayers(IModuleLayerManager.Layer layer, CallbackInfoReturnable<Optional<?>> cir) {
		this.completedLayers.forEach((layer1, layerInfo) -> {
			if (layer1.toString().equals(layer.toString())) {
				cir.setReturnValue(Optional.of(ModuleLayer.empty()));
			}
		});
	}

	@SuppressWarnings("InvalidInjectorMethodSignature")
	@Redirect(method = "lambda$buildLayer$3", at = @At(value = "INVOKE", target = "Lcpw/mods/modlauncher/ModuleLayerHandler$LayerInfo;layer()Ljava/lang/ModuleLayer;"))
	private static ModuleLayer emptyLayer(@Coerce Object instance) {
		return ModuleLayer.empty();
	}

	@Redirect(method = "lambda$buildLayer$4", at = @At(value = "INVOKE", target = "Ljava/lang/ModuleLayer;parents()Ljava/util/List;"))
	private static List<?> emptyList(ModuleLayer instance) {
		return List.of();
	}

	@Redirect(method = "buildLayer(Lcpw/mods/modlauncher/api/IModuleLayerManager$Layer;Ljava/util/function/BiFunction;)Lcpw/mods/modlauncher/ModuleLayerHandler$LayerInfo;", at = @At(value = "INVOKE", target = "Ljava/lang/ModuleLayer;defineModules(Ljava/lang/module/Configuration;Ljava/util/List;Ljava/util/function/Function;)Ljava/lang/ModuleLayer$Controller;"))
	private ModuleLayer.Controller dontDefineModulesNoMoreJ9Yes(Configuration layer, List<ModuleLayer> e, Function<String, ClassLoader> cf) {
		return null;
	}

	@Redirect(method = "buildLayer(Lcpw/mods/modlauncher/api/IModuleLayerManager$Layer;Ljava/util/function/BiFunction;)Lcpw/mods/modlauncher/ModuleLayerHandler$LayerInfo;", at = @At(value = "INVOKE", target = "Ljava/lang/ModuleLayer$Controller;layer()Ljava/lang/ModuleLayer;"))
	private ModuleLayer stopFuckingUsingModulesCpw(ModuleLayer.Controller instance) {
		return ModuleLayer.empty();
	}
}

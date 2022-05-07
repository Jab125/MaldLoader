package net.fabricmc.minecraft.test.mixin.minecraft.crash;

import net.minecraft.util.WinNativeModuleUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(WinNativeModuleUtil.class)
public class WinNativeModuleUtilMixin {

	@Inject(method = "collectNativeModules", at = @At("HEAD"), cancellable = true)
	private static void thisIsntAnOverwriteISwear(CallbackInfoReturnable<List<WinNativeModuleUtil.NativeModule>> cir) {
		cir.setReturnValue(List.of());
	}
}

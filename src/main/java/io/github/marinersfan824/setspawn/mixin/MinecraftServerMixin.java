package io.github.marinersfan824.setspawn.mixin;

import io.github.marinersfan824.setspawn.SetSpawn;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "prepareWorlds", at = @At(value = "HEAD"))
    public void setSpawn_worldGenStarted(CallbackInfo ci) {
        if (SetSpawn.config.isEnabled()) {
            SetSpawn.shouldModifySpawn = true;
        }
    }
}

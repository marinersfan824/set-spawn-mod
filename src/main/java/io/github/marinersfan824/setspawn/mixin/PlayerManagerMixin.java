package io.github.marinersfan824.setspawn.mixin;

import io.github.marinersfan824.setspawn.Seed;
import io.github.marinersfan824.setspawn.SetSpawn;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "method_12827", at = @At("HEAD"))
    public void onPlayerConnect(ClientConnection clientConnection, ServerPlayerEntity serverPlayerEntity, CallbackInfo ci) {
        if (SetSpawn.shouldModifySpawn) {
            SetSpawn.shouldModifySpawn = false;
            Seed seedObject = SetSpawn.findSeedObjectFromLong(serverPlayerEntity.getWorld().getSeed());
            String response;
            if (seedObject != null) {
                int xFloor = MathHelper.floor(seedObject.getX());
                int zFloor = MathHelper.floor(seedObject.getZ());
                if ((Math.abs(xFloor - serverPlayerEntity.world.getSpawnPos().getX()) > serverPlayerEntity.world.getGameRules().getInt("spawnRadius")) ||
                        (Math.abs(zFloor - serverPlayerEntity.world.getSpawnPos().getZ()) > serverPlayerEntity.world.getGameRules().getInt("spawnRadius"))) {
                    SetSpawn.shouldSendErrorMessage = true;
                    response = "The X or Z coordinates given (" + seedObject.getX() + ", " + seedObject.getZ() + ") are more than 10 blocks away from the world spawn. Not overriding player spawnpoint";
                    SetSpawn.errorMessage = response;
                    SetSpawn.LOGGER.warn(response);
                } else {
                    BlockPos spawnPos = new BlockPos(xFloor, serverPlayerEntity.world.getTopPosition(new BlockPos(xFloor, 3, zFloor)).getY(), zFloor);
                    SetSpawn.shouldSendErrorMessage = false;
                    SetSpawn.LOGGER.info("Spawning player at: " + seedObject.getX() + " " + spawnPos.getY() + " " + seedObject.getZ());
                    serverPlayerEntity.refreshPositionAndAngles(spawnPos, 0.0F, 0.0F);
                }
            }
        }
        if (SetSpawn.shouldSendErrorMessage) {
            Text message = new LiteralText("§c" + SetSpawn.errorMessage + " This run is not verifiable.");
            serverPlayerEntity.sendMessage(message, false);
        }
        SetSpawn.shouldSendErrorMessage = false;
    }
}

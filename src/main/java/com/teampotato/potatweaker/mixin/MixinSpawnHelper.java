package com.teampotato.potatweaker.mixin;

import com.teampotato.potatweaker.Potatweaker;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnHelper.class)
public abstract class MixinSpawnHelper {
    @Redirect(method = "spawnEntitiesInChunk(Lnet/minecraft/entity/SpawnGroup;Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/SpawnHelper$Checker;Lnet/minecraft/world/SpawnHelper$Runner;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/SpawnHelper;isValidSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/mob/MobEntity;D)Z"))
    private static boolean onSpawn(ServerWorld world, MobEntity entity, double squaredDistance) {
        if (Potatweaker.shouldCancel(entity)) {
            return false;
        } else {
            return SpawnHelperAccess.isValidSpawn(world, entity, squaredDistance);
        }
    }

    @Redirect(method = "populateEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldView;)Z"))
    private static boolean onSpawn(MobEntity instance, WorldView world) {
        if (Potatweaker.shouldCancel(instance)) {
            return false;
        } else {
            return instance.canSpawn(world);
        }
    }
}

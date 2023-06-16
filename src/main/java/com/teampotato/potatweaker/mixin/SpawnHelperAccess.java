package com.teampotato.potatweaker.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.SpawnHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnHelper.class)
@SuppressWarnings("unused")
public interface SpawnHelperAccess {
    @Invoker("isValidSpawn")
    static boolean isValidSpawn(ServerWorld world, MobEntity entity, double squaredDistance) {
        throw new RuntimeException();
    }
}

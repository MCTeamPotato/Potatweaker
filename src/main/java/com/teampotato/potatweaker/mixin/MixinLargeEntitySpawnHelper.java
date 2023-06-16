package com.teampotato.potatweaker.mixin;

import com.teampotato.potatweaker.Potatweaker;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LargeEntitySpawnHelper.class)
public abstract class MixinLargeEntitySpawnHelper {
    @Redirect(method = "trySpawnAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/MobEntity;canSpawn(Lnet/minecraft/world/WorldView;)Z", ordinal = 0))
    private static boolean canSpawn(MobEntity mobEntity, WorldView world) {
        if (Potatweaker.shouldCancel(mobEntity)) {
            return false;
        } else {
            return mobEntity.canSpawn(world);
        }
    }
}

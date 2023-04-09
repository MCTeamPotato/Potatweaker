package com.teampotato.potatweaker;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void ctrlSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Entity replaced = event.getEntity();
        Level world = replaced.level;
        if (world.isClientSide || Config.REPLACED.get().isEmpty()) return;
        String type = replaced.getType().toString();

        int index = Config.REPLACED.get().indexOf(type.split("\\.")[1] + ":" + type.split("\\.")[2]);
        if (index == -1) return;

        boolean removalMatchesChance = Config.REMOVAL_MATCHES_CHANCE.get().get(index);
        boolean isRemoval = Config.REMOVE.get().get(index);
        int replaceChance = Config.REPLACE_CHANCE.get().get(index);

        if (isRemoval) {
            if (removalMatchesChance) {
                if (replaceChance > world.getRandom().nextInt(101)) event.setSpawnCancelled(true);
            } else {
                event.setSpawnCancelled(true);
            }
        } else {
            if (replaceChance > world.getRandom().nextInt(101)) {
                event.setSpawnCancelled(true);
                summonHelper(Objects.requireNonNull(world.getServer()), index, replaced);
            }
        }
    }

    private static void summonHelper(MinecraftServer server, Integer index, Entity replaced) {
        Vec3 pos = replaced.position();

        String dim = replaced.level.dimension().location().toString();
        String replacer = Config.REPLACER.get().get(index);
        String nbt = Config.NBT.get().get(index);

        String cmd = "/execute in " + dim + " run summon " + replacer + " " + pos.x + " " + pos.y + " " + pos.z + " {" + nbt + "}";

        server.getCommands().performPrefixedCommand(server.createCommandSourceStack().withSuppressedOutput(), cmd);
    }
}

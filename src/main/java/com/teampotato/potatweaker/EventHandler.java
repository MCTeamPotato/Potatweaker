package com.teampotato.potatweaker;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void ctrlSpawn(LivingSpawnEvent.CheckSpawn event) {
        Entity replaced = event.getEntity();
        World world = replaced.level;
        if (world.isClientSide || Config.REPLACED.get().isEmpty() || replaced.getType().getRegistryName() == null) return;

        int index = Config.REPLACED.get().indexOf(replaced.getType().getRegistryName().toString());
        if (index == -1) return;

        boolean removalMatchesChance = Config.REMOVAL_MATCHES_CHANCE.get().get(index);
        boolean isRemoval = Config.REMOVE.get().get(index);
        int replaceChance = Config.REPLACE_CHANCE.get().get(index);

        if (isRemoval) {
            if (removalMatchesChance) {
                if (replaceChance > world.getRandom().nextInt(101)) event.setResult(Event.Result.DENY);
            } else {
                event.setResult(Event.Result.DENY);
            }
        } else {
            if (replaceChance > world.getRandom().nextInt(101)) {
                event.setResult(Event.Result.DENY);
                summonHelper(Objects.requireNonNull(world.getServer()), index, replaced);
            }
        }
    }

    private static void summonHelper(MinecraftServer server, Integer index, Entity replaced) {
        Vector3d pos = replaced.position();

        String dim = replaced.level.dimension().location().toString();
        String replacer = Config.REPLACER.get().get(index);
        String nbt = Config.NBT.get().get(index);

        String cmd = "/execute in " + dim + " run summon " + replacer + " " + pos.x + " " + pos.y + " " + pos.z + " {" + nbt + "}";

        server.getCommands().performCommand(server.createCommandSourceStack().withSuppressedOutput(), cmd);
    }
}

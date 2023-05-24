package com.teampotato.potatweaker;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = "potatweaker")
public class EventHandler {
    @SubscribeEvent
    public static void ctrlSpawn(LivingSpawnEvent.CheckSpawn event) {
        Entity replaced = event.getEntity();
        Level world = replaced.level;
        if (world.isClientSide || Config.REPLACED.get().isEmpty()) return;
        ResourceLocation type = ForgeRegistries.ENTITY_TYPES.getKey(replaced.getType());
        if (type == null) return;

        int index = Config.REPLACED.get().indexOf(type.toString());
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
            if (replaceChance > world.getRandom().nextInt(101) && world.getServer() != null) {
                event.setResult(Event.Result.DENY);
                summonHelper(world.getServer(), index, replaced);
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

package com.teampotato.potatweaker;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = "potatweaker")
public class EventHandler {
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    @SubscribeEvent
    public static void ctrlSpawn(@NotNull EntityJoinWorldEvent event) {
        Entity replaced = event.getEntity();
        World world = replaced.getCommandSenderWorld();
        MinecraftServer server = world.getServer();
        if (world.isClientSide || Config.REPLACED.get().isEmpty() || server == null || replaced.getTags().contains("potatweaker.spawned")) return;
        ResourceLocation type = replaced.getType().getRegistryName();
        if (type == null) return;

        int index = Config.REPLACED.get().indexOf(type.toString());
        if (index == -1) return;

        boolean removalMatchesChance = Config.REMOVAL_MATCHES_CHANCE.get().get(index);
        boolean isRemoval = Config.REMOVE.get().get(index);
        int replaceChance = Config.REPLACE_CHANCE.get().get(index);

        if (isRemoval) {
            if (removalMatchesChance) {
                if (replaceChance > random.nextInt(101)) event.setCanceled(true);
            } else {
                event.setCanceled(true);
            }
        } else {
            if (replaceChance > random.nextInt(101) && world.getServer() != null) {
                event.setCanceled(true);
                summonHelper(server, index, replaced);
            }
        }
        if (!event.isCanceled()) replaced.addTag("potatweaker.spawned");
    }

    private static void summonHelper(@NotNull MinecraftServer server, Integer index, @NotNull Entity replaced) {
        Vector3d pos = replaced.position();

        String dim = replaced.getCommandSenderWorld().dimension().location().toString();
        String replacer = Config.REPLACER.get().get(index);
        String nbt = Config.NBT.get().get(index);
        boolean useNBT = Config.USE_NBT_WHEN_REPLACING.get().get(index);

        String cmd;
        if (useNBT) {
            cmd = "/execute in " + dim + " run summon " + replacer + " " + pos.x + " " + pos.y + " " + pos.z + " {" + nbt + "}";
        } else {
            cmd = "/execute in " + dim + " run summon " + replacer + " " + pos.x + " " + pos.y + " " + pos.z;
        }

        server.getCommands().performCommand(server.createCommandSourceStack().withSuppressedOutput(), cmd);
    }
}

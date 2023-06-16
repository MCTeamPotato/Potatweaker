package com.teampotato.potatweaker;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class Potatweaker implements ModInitializer {
	@Override
	public void onInitialize() {
		ModLoadingContext.registerConfig("potatweaker", ModConfig.Type.COMMON, Config.COMMON_CONFIG);
	}

	private static void summonHelper(MinecraftServer server, int index, Entity replaced) {
		Vec3d pos = replaced.getPos();

		String dim = replaced.world.getRegistryKey().getValue().toString();
		String replacer = Config.REPLACER.get().get(index);
		String nbt = Config.NBT.get().get(index);

		String cmd = "/execute in " + dim + " run summon " + replacer + " " + pos.x + " " + pos.y + " " + pos.z + " {" + nbt + "}";

		server.getCommandManager().executeWithPrefix(server.getCommandSource().withSilent(), cmd);
	}

	public static boolean shouldCancel(Entity replaced) {
		World world = replaced.world;
		if (world.isClient || Config.REPLACED.get().isEmpty()) return false;
		Identifier type = Registry.ENTITY_TYPE.getId(replaced.getType());

		int index = Config.REPLACED.get().indexOf(type.toString());
		if (index == -1) return false;

		boolean removalMatchesChance = Config.REMOVAL_MATCHES_CHANCE.get().get(index);
		boolean isRemoval = Config.REMOVE.get().get(index);
		int replaceChance = Config.REPLACE_CHANCE.get().get(index);

		if (isRemoval) {
			if (removalMatchesChance) {
				return replaceChance > world.getRandom().nextInt(101);
			} else {
				return true;
			}
		} else {
			if (replaceChance > world.getRandom().nextInt(101) && world.getServer() != null) {
				summonHelper(world.getServer(), index, replaced);
				return true;
			}
		}

		return false;
	}
}

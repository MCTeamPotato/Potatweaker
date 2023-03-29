package com.teampotato.potatweaker;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import static com.teampotato.potatweaker.Config.COMMON_CONFIG;

@Mod("potatweaker")
public class Potatweaker {
    public Potatweaker() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        MinecraftForge.EVENT_BUS.register(this);
    }
}

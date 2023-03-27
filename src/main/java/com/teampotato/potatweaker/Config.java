package com.teampotato.potatweaker;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> REPLACED, REPLACER;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> REPLACE_CHANCE;
    public static ForgeConfigSpec.ConfigValue<List<? extends Boolean>> REMOVE, REMOVAL_MATCHES_CHANCE;

    static {
        List<? extends String> entityToReplace = Lists.newArrayList("minecraft:stone");
        List<? extends String> replacer = Lists.newArrayList("minecraft:cobblestone");
        List<? extends Integer> replaceChance = Lists.newArrayList(50);
        List<? extends Boolean> replaceToAir = Lists.newArrayList(false);
        List<? extends Boolean> chanceReplaceToAir = Lists.newArrayList(false);

        Predicate<Object> stringValidator = a -> a instanceof String;
        Predicate<Object> intValidator = b -> b instanceof Integer;
        Predicate<Object> booleanValidator = c -> c instanceof Boolean;

        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Potatweaker").push("general");
        REPLACED = CONFIG_BUILDER.defineList("replaced", entityToReplace, stringValidator);
        REPLACER = CONFIG_BUILDER.defineList("replacer", replacer, stringValidator);
        REPLACE_CHANCE = CONFIG_BUILDER.defineList("replace chance", replaceChance, intValidator);
        REMOVE = CONFIG_BUILDER.defineList("remove", replaceToAir, booleanValidator);
        REMOVAL_MATCHES_CHANCE = CONFIG_BUILDER.defineList("removal matches chance", chanceReplaceToAir, booleanValidator);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

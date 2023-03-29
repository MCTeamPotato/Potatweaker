package com.teampotato.potatweaker;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.function.Predicate;

public class Config {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> REPLACED, REPLACER, NBT;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> REPLACE_CHANCE;
    public static ForgeConfigSpec.ConfigValue<List<? extends Boolean>> REMOVE, REMOVAL_MATCHES_CHANCE;

    static {
        List<? extends String> entityToReplace = Lists.newArrayList();
        List<? extends String> replacer = Lists.newArrayList();
        List<? extends String> nbt = Lists.newArrayList();
        List<? extends Integer> replaceChance = Lists.newArrayList();
        List<? extends Boolean> replaceToAir = Lists.newArrayList();
        List<? extends Boolean> chanceReplaceToAir = Lists.newArrayList();

        Predicate<Object> stringValidator = a -> a instanceof String;
        Predicate<Object> intValidator = b -> b instanceof Integer;
        Predicate<Object> booleanValidator = c -> c instanceof Boolean;

        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Potatweaker").push("General");
        REPLACED = CONFIG_BUILDER
                .comment("Entities that will be replaced or removed.")
                .defineList("the replaced", entityToReplace, stringValidator);
        REPLACER = CONFIG_BUILDER
                .comment("Replacer entities.")
                .defineList("the replacers", replacer, stringValidator);
        NBT = CONFIG_BUILDER
                .comment("The NBTs of the replacers. You don't need to write { } here.")
                .defineList("replacer entity NBT", nbt, stringValidator);
        REPLACE_CHANCE = CONFIG_BUILDER
                .comment("Chance of the replacement or removal.",
                        "This value will be compared with a random int between 0 and 100.",
                        "If this value is bigger than the random, replacement or removal will happen.")
                .defineList("the replacement chance", replaceChance, intValidator);
        REMOVE = CONFIG_BUILDER
                .comment("Mark the replacement as removal.")
                .defineList("is removal", replaceToAir, booleanValidator);
        REMOVAL_MATCHES_CHANCE = CONFIG_BUILDER
                .comment("Let the removal follows replacement chance")
                .defineList("remove by chance", chanceReplaceToAir, booleanValidator);
        CONFIG_BUILDER.pop();
        COMMON_CONFIG = CONFIG_BUILDER.build();
    }
}

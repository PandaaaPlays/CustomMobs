package ca.pandaaa.custommobs.custommobs.options;

import org.bukkit.DyeColor;
import org.bukkit.entity.Cat;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Llama;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Villager;

import java.util.HashMap;
import java.util.Map;

public class NextOptions {

    public static DyeColor getNextDyeColor(DyeColor currentColor) {
        Map<DyeColor, DyeColor> next = new HashMap<>();
        next.put(null, DyeColor.WHITE);
        next.put(DyeColor.WHITE, DyeColor.ORANGE);
        next.put(DyeColor.ORANGE, DyeColor.MAGENTA);
        next.put(DyeColor.MAGENTA, DyeColor.LIGHT_BLUE);
        next.put(DyeColor.LIGHT_BLUE, DyeColor.YELLOW);
        next.put(DyeColor.YELLOW, DyeColor.LIME);
        next.put(DyeColor.LIME, DyeColor.PINK);
        next.put(DyeColor.PINK, DyeColor.GRAY);
        next.put(DyeColor.GRAY, DyeColor.LIGHT_GRAY);
        next.put(DyeColor.LIGHT_GRAY, DyeColor.CYAN);
        next.put(DyeColor.CYAN, DyeColor.PURPLE);
        next.put(DyeColor.PURPLE, DyeColor.BLUE);
        next.put(DyeColor.BLUE, DyeColor.BROWN);
        next.put(DyeColor.BROWN, DyeColor.GREEN);
        next.put(DyeColor.GREEN, DyeColor.RED);
        next.put(DyeColor.RED, DyeColor.BLACK);
        next.put(DyeColor.BLACK, DyeColor.WHITE);

        return next.get(currentColor);
    }

    public static Cat.Type getNextCatType(Cat.Type currentType) {
        Map<Cat.Type, Cat.Type> next = new HashMap<>();
        next.put(null, Cat.Type.TABBY);
        next.put(Cat.Type.TABBY, Cat.Type.BLACK);
        next.put(Cat.Type.BLACK, Cat.Type.RED);
        next.put(Cat.Type.RED, Cat.Type.SIAMESE);
        next.put(Cat.Type.SIAMESE, Cat.Type.BRITISH_SHORTHAIR);
        next.put(Cat.Type.BRITISH_SHORTHAIR, Cat.Type.CALICO);
        next.put(Cat.Type.CALICO, Cat.Type.PERSIAN);
        next.put(Cat.Type.PERSIAN, Cat.Type.RAGDOLL);
        next.put(Cat.Type.RAGDOLL, Cat.Type.WHITE);
        next.put(Cat.Type.WHITE, Cat.Type.JELLIE);
        next.put(Cat.Type.JELLIE, Cat.Type.ALL_BLACK);
        next.put(Cat.Type.ALL_BLACK, Cat.Type.TABBY);

        return next.get(currentType);
    }

    public static Fox.Type getNextFoxType(Fox.Type currentType) {
        Map<Fox.Type, Fox.Type> next = new HashMap<>();
        next.put(null, Fox.Type.RED);
        next.put(Fox.Type.RED, Fox.Type.SNOW);
        next.put(Fox.Type.SNOW, Fox.Type.RED);

        return next.get(currentType);
    }

    public static Horse.Color getNextHorseColor(Horse.Color currentColor) {
        Map<Horse.Color, Horse.Color> next = new HashMap<>();
        next.put(null, Horse.Color.WHITE);
        next.put(Horse.Color.WHITE, Horse.Color.CREAMY);
        next.put(Horse.Color.CREAMY, Horse.Color.CHESTNUT);
        next.put(Horse.Color.CHESTNUT, Horse.Color.BROWN);
        next.put(Horse.Color.BROWN, Horse.Color.BLACK);
        next.put(Horse.Color.BLACK, Horse.Color.GRAY);
        next.put(Horse.Color.GRAY, Horse.Color.DARK_BROWN);
        next.put(Horse.Color.DARK_BROWN, Horse.Color.WHITE);

        return next.get(currentColor);
    }

    public static Llama.Color getNextLlamaColor(Llama.Color currentColor) {
        Map<Llama.Color, Llama.Color> next = new HashMap<>();
        next.put(null, Llama.Color.CREAMY);
        next.put(Llama.Color.CREAMY, Llama.Color.WHITE);
        next.put(Llama.Color.WHITE, Llama.Color.BROWN);
        next.put(Llama.Color.BROWN, Llama.Color.GRAY);
        next.put(Llama.Color.GRAY, Llama.Color.CREAMY);

        return next.get(currentColor);
    }

    public static Panda.Gene getNextPandaGene(Panda.Gene currentGene) {
        Map<Panda.Gene, Panda.Gene> next = new HashMap<>();
        next.put(null, Panda.Gene.NORMAL);
        next.put(Panda.Gene.NORMAL, Panda.Gene.LAZY);
        next.put(Panda.Gene.LAZY, Panda.Gene.WORRIED);
        next.put(Panda.Gene.WORRIED, Panda.Gene.PLAYFUL);
        next.put(Panda.Gene.PLAYFUL, Panda.Gene.BROWN);
        next.put(Panda.Gene.BROWN, Panda.Gene.WEAK);
        next.put(Panda.Gene.WEAK, Panda.Gene.AGGRESSIVE);
        next.put(Panda.Gene.AGGRESSIVE, Panda.Gene.NORMAL);

        return next.get(currentGene);
    }

    public static Horse.Style getNextHorseStyle(Horse.Style currentStyle) {
        Map<Horse.Style, Horse.Style> next = new HashMap<>();
        next.put(null, Horse.Style.NONE);
        next.put(Horse.Style.NONE, Horse.Style.WHITE);
        next.put(Horse.Style.WHITE, Horse.Style.WHITEFIELD);
        next.put(Horse.Style.WHITEFIELD, Horse.Style.WHITE_DOTS);
        next.put(Horse.Style.WHITE_DOTS, Horse.Style.BLACK_DOTS);
        next.put(Horse.Style.BLACK_DOTS, Horse.Style.NONE);

        return next.get(currentStyle);
    }

    public static Axolotl.Variant getNextAxolotlVariant(Axolotl.Variant currentVariant) {
        Map<Axolotl.Variant, Axolotl.Variant> next = new HashMap<>();
        next.put(null, Axolotl.Variant.LUCY);
        next.put(Axolotl.Variant.LUCY, Axolotl.Variant.WILD);
        next.put(Axolotl.Variant.WILD, Axolotl.Variant.GOLD);
        next.put(Axolotl.Variant.GOLD, Axolotl.Variant.CYAN);
        next.put(Axolotl.Variant.CYAN, Axolotl.Variant.BLUE);
        next.put(Axolotl.Variant.BLUE, Axolotl.Variant.LUCY);

        return next.get(currentVariant);
    }

    public static Frog.Variant getNextFrogVariant(Frog.Variant currentVariant) {
        Map<Frog.Variant, Frog.Variant> next = new HashMap<>();
        next.put(null, Frog.Variant.TEMPERATE);
        next.put(Frog.Variant.TEMPERATE, Frog.Variant.WARM);
        next.put(Frog.Variant.WARM, Frog.Variant.COLD);
        next.put(Frog.Variant.COLD, Frog.Variant.TEMPERATE);

        return next.get(currentVariant);
    }

    public static Parrot.Variant getNextParrotVariant(Parrot.Variant currentVariant) {
        Map<Parrot.Variant, Parrot.Variant> next = new HashMap<>();
        next.put(null, Parrot.Variant.RED);
        next.put(Parrot.Variant.RED, Parrot.Variant.BLUE);
        next.put(Parrot.Variant.BLUE, Parrot.Variant.GREEN);
        next.put(Parrot.Variant.GREEN, Parrot.Variant.CYAN);
        next.put(Parrot.Variant.CYAN, Parrot.Variant.GRAY);
        next.put(Parrot.Variant.GRAY, Parrot.Variant.RED);

        return next.get(currentVariant);
    }

    public static Rabbit.Type getNextRabbitType(Rabbit.Type currentType) {
        Map<Rabbit.Type, Rabbit.Type> next = new HashMap<>();
        next.put(null, Rabbit.Type.BROWN);
        next.put(Rabbit.Type.BROWN, Rabbit.Type.WHITE);
        next.put(Rabbit.Type.WHITE, Rabbit.Type.BLACK);
        next.put(Rabbit.Type.BLACK, Rabbit.Type.BLACK_AND_WHITE);
        next.put(Rabbit.Type.BLACK_AND_WHITE, Rabbit.Type.GOLD);
        next.put(Rabbit.Type.GOLD, Rabbit.Type.SALT_AND_PEPPER);
        next.put(Rabbit.Type.SALT_AND_PEPPER, Rabbit.Type.THE_KILLER_BUNNY);
        next.put(Rabbit.Type.THE_KILLER_BUNNY, Rabbit.Type.BROWN);

        return next.get(currentType);
    }

    public static Villager.Type getNextVillagerType(Villager.Type currentType) {
        Map<Villager.Type, Villager.Type> next = new HashMap<>();
        next.put(null, Villager.Type.DESERT);
        next.put(Villager.Type.DESERT, Villager.Type.JUNGLE);
        next.put(Villager.Type.JUNGLE, Villager.Type.PLAINS);
        next.put(Villager.Type.PLAINS, Villager.Type.SAVANNA);
        next.put(Villager.Type.SAVANNA, Villager.Type.SNOW);
        next.put(Villager.Type.SNOW, Villager.Type.SWAMP);
        next.put(Villager.Type.SWAMP, Villager.Type.TAIGA);
        next.put(Villager.Type.TAIGA, Villager.Type.DESERT);

        return next.get(currentType);
    }

    public static Villager.Profession getNextVillagerProfession(Villager.Profession currentProfession) {
        Map<Villager.Profession, Villager.Profession> next = new HashMap<>();
        next.put(Villager.Profession.NONE, Villager.Profession.ARMORER);
        next.put(Villager.Profession.ARMORER, Villager.Profession.BUTCHER);
        next.put(Villager.Profession.BUTCHER, Villager.Profession.CARTOGRAPHER);
        next.put(Villager.Profession.CARTOGRAPHER, Villager.Profession.CLERIC);
        next.put(Villager.Profession.CLERIC, Villager.Profession.FARMER);
        next.put(Villager.Profession.FARMER, Villager.Profession.FISHERMAN);
        next.put(Villager.Profession.FISHERMAN, Villager.Profession.FLETCHER);
        next.put(Villager.Profession.FLETCHER, Villager.Profession.LEATHERWORKER);
        next.put(Villager.Profession.LEATHERWORKER, Villager.Profession.LIBRARIAN);
        next.put(Villager.Profession.LIBRARIAN, Villager.Profession.MASON);
        next.put(Villager.Profession.MASON, Villager.Profession.NITWIT);
        next.put(Villager.Profession.NITWIT, Villager.Profession.SHEPHERD);
        next.put(Villager.Profession.SHEPHERD, Villager.Profession.TOOLSMITH);
        next.put(Villager.Profession.TOOLSMITH, Villager.Profession.WEAPONSMITH);
        next.put(Villager.Profession.WEAPONSMITH, Villager.Profession.NONE);

        return next.get(currentProfession);
    }
}

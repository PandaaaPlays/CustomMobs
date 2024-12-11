Get-Content "OriginalSoundEnum.txt" | ForEach-Object {
    $_ -replace '\(.*', ''
} | Set-Content "SoundNameEnum.txt"

Get-Content "OriginalMaterialEnum.txt" | ForEach-Object {
    $_ -replace '\(.*', ''
} | Set-Content "MaterialNameEnum.txt"



# Input files
$materialsFile = "MaterialNameEnum.txt"
$soundsFile = "SoundNameEnum.txt"

# Read materials and sounds
$materials = Get-Content $materialsFile
$sounds = Get-Content $soundsFile

# Create a mapping for materials
$materialSet = $materials | ForEach-Object { $_.ToUpper() }

# Initialize Enum output
$output = @"
import org.bukkit.Material;
import org.bukkit.Sound;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum SoundEnum {`n
"@

# Process each sound
foreach ($sound in $sounds) {
    $matchingMaterial = $null

    # Extract category and sound name
    $category = $sound.Split('_')[0]
    if($category -notin ("BLOCK", "MUSIC", "ENTITY", "AMBIENT", "ITEM")) {
        $category = "OTHERS"
    }

    switch($sound) {
        "AMBIENT_CAVE" { $matchingMaterial = "STONE" }
        "AMBIENT_NETHER_WASTES_ADDITIONS" { $matchingMaterial = "NETHERRACK" }
        "AMBIENT_NETHER_WASTES_LOOP" { $matchingMaterial = "NETHERRACK" }
        "AMBIENT_NETHER_WASTES_MOOD" { $matchingMaterial = "NETHERRACK" }
        "BLOCK_ANVIL_HIT" { $matchingMaterial = "ANVIL" }
        "BLOCK_ANVIL_LAND" { $matchingMaterial = "ANVIL" }
        "BLOCK_BAMBOO_SAPLING_BREAK" { $matchingMaterial = "BAMBOO" }
        "BLOCK_BAMBOO_SAPLING_HIT" { $matchingMaterial = "BAMBOO" }
        "BLOCK_BAMBOO_SAPLING_PLACE" { $matchingMaterial = "BAMBOO" }
        "BLOCK_BAMBOO_WOOD_BUTTON_CLICK_OFF" { $matchingMaterial = "BAMBOO_BUTTON" }
        "BLOCK_BAMBOO_WOOD_BUTTON_CLICK_ON" { $matchingMaterial = "BAMBOO_BUTTON" }
        "BLOCK_BAMBOO_WOOD_DOOR_CLOSE" { $matchingMaterial = "BAMBOO_DOOR" }
        "BLOCK_BAMBOO_WOOD_DOOR_OPEN" { $matchingMaterial = "BAMBOO_DOOR" }
        "BLOCK_BAMBOO_WOOD_FENCE_GATE_CLOSE" { $matchingMaterial = "BAMBOO_FENCE_GATE" }
        "BLOCK_BAMBOO_WOOD_FENCE_GATE_OPEN" { $matchingMaterial = "BAMBOO_FENCE_GATE" }
        "BLOCK_BAMBOO_WOOD_HANGING_SIGN_BREAK" { $matchingMaterial = "BAMBOO_WOOD_HANGING_SIGN" }
        "BLOCK_BAMBOO_WOOD_HANGING_SIGN_FALL" { $matchingMaterial = "BAMBOO_WOOD_HANGING_SIGN" }
        "BLOCK_BAMBOO_WOOD_HANGING_SIGN_HIT" { $matchingMaterial = "BAMBOO_WOOD_HANGING_SIGN" }
        "BLOCK_BAMBOO_WOOD_HANGING_SIGN_PLACE" { $matchingMaterial = "BAMBOO_WOOD_HANGING_SIGN" }
        "BLOCK_BAMBOO_WOOD_HANGING_SIGN_STEP" { $matchingMaterial = "BAMBOO_WOOD_HANGING_SIGN" }
        "BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "BAMBOO_PRESSURE_PLATE" }
        "BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "BAMBOO_PRESSURE_PLATE" }
        "BLOCK_BAMBOO_WOOD_TRAPDOOR_CLOSE" { $matchingMaterial = "BAMBOO_TRAPDOOR" }
        "BLOCK_BAMBOO_WOOD_TRAPDOOR_OPEN" { $matchingMaterial = "BAMBOO_TRAPDOOR" }
        "BLOCK_BARREL_CLOSE" { $matchingMaterial = "BARREL" }
        "BLOCK_BARREL_OPEN" { $matchingMaterial = "BARREL" }
        "BLOCK_BEEHIVE_DRIP" { $matchingMaterial = "BEEHIVE" }
        "BLOCK_BEEHIVE_SHEAR" { $matchingMaterial = "BEEHIVE" }
        "BLOCK_BEEHIVE_WORK" { $matchingMaterial = "BEEHIVE" }
        "BLOCK_BLASTFURNACE_FIRE_CRACKLE" { $matchingMaterial = "BLAST_FURNACE" }
        "BLOCK_CAKE_ADD_CANDLE" { $matchingMaterial = "CANDLE" }
        "BLOCK_CAVE_VINES_BREAK" { $matchingMaterial = "VINE" }
        "BLOCK_CAVE_VINES_FALL" { $matchingMaterial = "VINE" }
        "BLOCK_CAVE_VINES_HIT" { $matchingMaterial = "VINE" }
        "BLOCK_CAVE_VINES_PICK_BERRIES" { $matchingMaterial = "VINE" }
        "BLOCK_CAVE_VINES_PLACE" { $matchingMaterial = "VINE" }
        "BLOCK_CAVE_VINES_STEP" { $matchingMaterial = "VINE" }
        "BLOCK_CANDLE_HIT" { $matchingMaterial = "CANDLE" }
        "BLOCK_CANDLE_STEP" { $matchingMaterial = "CANDLE" }
        "BLOCK_CHAIN_HIT" { $matchingMaterial = "CHAIN" }
        "BLOCK_CHERRY_WOOD_BREAK" { $matchingMaterial = "CHERRY_WOOD" }
        "BLOCK_CHERRY_WOOD_BUTTON_CLICK_OFF" { $matchingMaterial = "CHERRY_BUTTON" }
        "BLOCK_CHERRY_WOOD_BUTTON_CLICK_ON" { $matchingMaterial = "CHERRY_BUTTON" }
        "BLOCK_CHERRY_WOOD_DOOR_CLOSE" { $matchingMaterial = "CHERRY_DOOR" }
        "BLOCK_CHERRY_WOOD_DOOR_OPEN" { $matchingMaterial = "CHERRY_DOOR" }
        "BLOCK_CHERRY_WOOD_FENCE_GATE_CLOSE" { $matchingMaterial = "CHERRY_FENCE_GATE" }
        "BLOCK_CHERRY_WOOD_FENCE_GATE_OPEN" { $matchingMaterial = "CHERRY_FENCE_GATE" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_BREAK" { $matchingMaterial = "CHERRY_HANGING_SIGN" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_FALL" { $matchingMaterial = "CHERRY_HANGING_SIGN" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_HIT" { $matchingMaterial = "CHERRY_HANGING_SIGN" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_PLACE" { $matchingMaterial = "CHERRY_HANGING_SIGN" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_STEP" { $matchingMaterial = "CHERRY_HANGING_SIGN" }
        "BLOCK_CHERRY_WOOD_HIT" { $matchingMaterial = "CHERRY_WOOD" }
        "BLOCK_CHERRY_WOOD_PLACE" { $matchingMaterial = "CHERRY_WOOD" }
        "BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "CHERRY_PRESSURE_PLATE" }
        "BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "CHERRY_PRESSURE_PLATE" }
        "BLOCK_CHERRY_WOOD_STEP" { $matchingMaterial = "CHERRY_WOOD" }
        "BLOCK_CHERRY_WOOD_TRAPDOOR_CLOSE" { $matchingMaterial = "CHERRY_TRAPDOOR" }
        "BLOCK_CHERRY_WOOD_TRAPDOOR_OPEN" { $matchingMaterial = "CHERRY_TRAPDOOR" }
        "BLOCK_CHEST_CLOSE" { $matchingMaterial = "CHEST" }
        "BLOCK_CHEST_OPEN" { $matchingMaterial = "CHEST" }
        "BLOCK_COMPOSTER_FILL" { $matchingMaterial = "COMPOSTER" }
        "BLOCK_COMPOSTER_FILL_SUCCESS" { $matchingMaterial = "COMPOSTER" }
        "BLOCK_COPPER_BREAK" { $matchingMaterial = "COPPER_BLOCK" }
        "BLOCK_COPPER_FALL" { $matchingMaterial = "COPPER_BLOCK" }
        "BLOCK_CORAL_BLOCK_BREAK" { $matchingMaterial = "FIRE_CORAL_BLOCK" }
        "BLOCK_CORAL_BLOCK_FALL" { $matchingMaterial = "FIRE_CORAL_BLOCK" }
        "BLOCK_CORAL_BLOCK_HIT" { $matchingMaterial = "FIRE_CORAL_BLOCK" }
        "BLOCK_CORAL_BLOCK_PLACE" { $matchingMaterial = "FIRE_CORAL_BLOCK" }
        "BLOCK_CORAL_BLOCK_STEP" { $matchingMaterial = "FIRE_CORAL_BLOCK" }
        "BLOCK_CROP_BREAK" { $matchingMaterial = "CARROT" }
        "BLOCK_ENCHANTMENT_TABLE_USE" { $matchingMaterial = "ENCHANTING_TABLE" }
        "BLOCK_END_GATEWAY_SPAWN" { $matchingMaterial = "END_GATEWAY" }
        "BLOCK_FIRE_AMBIENT" { $matchingMaterial = "FLINT_AND_STEEL" }
        "BLOCK_FIRE_EXTINGUISH" { $matchingMaterial = "WATER_BUCKET" }
        "BLOCK_FROGLIGHT_HIT" { $matchingMaterial = "OCHRE_FROGLIGHT" }
        "BLOCK_FROGLIGHT_STEP" { $matchingMaterial = "OCHRE_FROGLIGHT" }
        "BLOCK_FROGSPAWN_HIT" { $matchingMaterial = "FROGSPAWN" }
        "BLOCK_FROGSPAWN_STEP" { $matchingMaterial = "FROGSPAWN" }
        "BLOCK_FUNGUS_HIT" { $matchingMaterial = "CRIMSON_FUNGUS" }
        "BLOCK_GLASS_HIT" { $matchingMaterial = "GLASS" }
        "BLOCK_GRAVEL_BREAK" { $matchingMaterial = "GRAVEL" }
        "BLOCK_GRAVEL_FALL" { $matchingMaterial = "GRAVEL" }
        "BLOCK_GRAVEL_HIT" { $matchingMaterial = "GRAVEL" }
        "BLOCK_GRAVEL_PLACE" { $matchingMaterial = "GRAVEL" }
        "BLOCK_GRAVEL_STEP" { $matchingMaterial = "GRAVEL" }
        "BLOCK_GROWING_PLANT_CROP" { $matchingMaterial = "CARROT" }
        "BLOCK_HANGING_SIGN_WAXED_INTERACT_FAIL" { $matchingMaterial = "HONEYCOMB" }
        "BLOCK_LADDER_HIT" { $matchingMaterial = "LADDER" }
        "BLOCK_LANTERN_BREAK" { $matchingMaterial = "LANTERN" }
        "BLOCK_LANTERN_FALL" { $matchingMaterial = "LANTERN" }
        "BLOCK_LANTERN_HIT" { $matchingMaterial = "LANTERN" }
        "BLOCK_LANTERN_PLACE" { $matchingMaterial = "LANTERN" }
        "BLOCK_LANTERN_STEP" { $matchingMaterial = "LANTERN" }
        "BLOCK_LAVA_POP" { $matchingMaterial = "LAVA_BUCKET" }
        "BLOCK_LODESTONE_HIT" { $matchingMaterial = "LODESTONE" }
        "BLOCK_LODESTONE_STEP" { $matchingMaterial = "LODESTONE" }
        "BLOCK_METAL_BREAK" { $matchingMaterial = "IRON_BLOCK" }
        "BLOCK_METAL_FALL" { $matchingMaterial = "IRON_BLOCK" }
        "BLOCK_METAL_HIT" { $matchingMaterial = "IRON_BLOCK" }
        "BLOCK_METAL_PLACE" { $matchingMaterial = "IRON_BLOCK" }
        "BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "HEAVY_WEIGHTED_PRESSURE_PLATE" }
        "BLOCK_METAL_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "HEAVY_WEIGHTED_PRESSURE_PLATE" }
        "BLOCK_METAL_STEP" { $matchingMaterial = "IRON_BLOCK" }
        "BLOCK_MOSS_BREAK" { $matchingMaterial = "MOSS_BLOCK" }
        "BLOCK_MOSS_FALL" { $matchingMaterial = "MOSS_BLOCK" }
        "BLOCK_MOSS_HIT" { $matchingMaterial = "MOSS_BLOCK" }
        "BLOCK_MOSS_PLACE" { $matchingMaterial = "MOSS_BLOCK" }
        "BLOCK_MOSS_STEP" { $matchingMaterial = "MOSS_BLOCK" }
        "BLOCK_NETHERRACK_HIT" { $matchingMaterial = "NETHERRACK" }
        "BLOCK_NETHER_ORE_BREAK" { $matchingMaterial = "NETHER_QUARTZ_ORE" }
        "BLOCK_NETHER_ORE_FALL" { $matchingMaterial = "NETHER_QUARTZ_ORE" }
        "BLOCK_NETHER_ORE_HIT" { $matchingMaterial = "NETHER_QUARTZ_ORE" }
        "BLOCK_NETHER_ORE_PLACE" { $matchingMaterial = "NETHER_QUARTZ_ORE" }
        "BLOCK_NETHER_ORE_STEP" { $matchingMaterial = "NETHER_QUARTZ_ORE" }
        "BLOCK_NETHER_WART_BREAK" { $matchingMaterial = "NETHER_WART" }
        "BLOCK_NETHER_WOOD_BREAK" { $matchingMaterial = "CRIMSON_HYPHAE" }
        "BLOCK_NETHER_WOOD_BUTTON_CLICK_OFF" { $matchingMaterial = "CRIMSON_BUTTON" }
        "BLOCK_NETHER_WOOD_BUTTON_CLICK_ON" { $matchingMaterial = "CRIMSON_BUTTON" }
        "BLOCK_NETHER_WOOD_DOOR_CLOSE" { $matchingMaterial = "CRIMSON_DOOR" }
        "BLOCK_NETHER_WOOD_DOOR_OPEN" { $matchingMaterial = "CRIMSON_DOOR" }
        "BLOCK_NETHER_WOOD_FALL" { $matchingMaterial = "CRIMSON_HYPHAE" }
        "BLOCK_NETHER_WOOD_FENCE_GATE_CLOSE" { $matchingMaterial = "CRIMSON_FENCE_GATE" }
        "BLOCK_NETHER_WOOD_FENCE_GATE_OPEN" { $matchingMaterial = "CRIMSON_FENCE_GATE" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_BREAK" { $matchingMaterial = "CRIMSON_HANGING_SIGN" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_FALL" { $matchingMaterial = "CRIMSON_HANGING_SIGN" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_HIT" { $matchingMaterial = "CRIMSON_HANGING_SIGN" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_PLACE" { $matchingMaterial = "CRIMSON_HANGING_SIGN" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_STEP" { $matchingMaterial = "CRIMSON_HANGING_SIGN" }
        "BLOCK_NETHER_WOOD_HIT" { $matchingMaterial = "CRIMSON_HYPHAE" }
        "BLOCK_NETHER_WOOD_PLACE" { $matchingMaterial = "CRIMSON_HYPHAE" }
        "BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "CRIMSON_PRESSURE_PLATE" }
        "BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "CRIMSON_PRESSURE_PLATE" }
        "BLOCK_NETHER_WOOD_STEP" { $matchingMaterial = "CRIMSON_HYPHAE" }
        "BLOCK_NETHER_WOOD_TRAPDOOR_CLOSE" { $matchingMaterial = "CRIMSON_TRAPDOOR" }
        "BLOCK_NETHER_WOOD_TRAPDOOR_OPEN" { $matchingMaterial = "CRIMSON_TRAPDOOR" }
        "BLOCK_SCAFFOLDING_HIT" { $matchingMaterial = "SCAFFOLDING" }
        "BLOCK_SCULK_HIT" { $matchingMaterial = "SCULK" }
        "BLOCK_SHROOMLIGHT_HIT" { $matchingMaterial = "SHROOMLIGHT" }
        "BLOCK_SHROOMLIGHT_STEP" { $matchingMaterial = "SHROOMLIGHT" }
        "BLOCK_SIGN_WAXED_INTERACT_FAIL" { $matchingMaterial = "HONEYCOMB" }
        "BLOCK_SNOW_BREAK" { $matchingMaterial = "SNOW_BLOCK" }
        "BLOCK_SNOW_FALL" { $matchingMaterial = "SNOW_BLOCK" }
        "BLOCK_SNOW_HIT" { $matchingMaterial = "SNOW_BLOCK" }
        "BLOCK_SNOW_PLACE" { $matchingMaterial = "SNOW_BLOCK" }
        "BLOCK_SNOW_STEP" { $matchingMaterial = "SNOW_BLOCK" }
        "BLOCK_SWEET_BERRY_BUSH_BREAK" { $matchingMaterial = "SWEET_BERRIES" }
        "BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES" { $matchingMaterial = "SWEET_BERRIES" }
        "BLOCK_SWEET_BERRY_BUSH_PLACE" { $matchingMaterial = "SWEET_BERRIES" }
        "BLOCK_VAULT_BREAK" { $matchingMaterial = "VAULT" }
        "BLOCK_VAULT_CLOSE_SHUTTER" { $matchingMaterial = "VAULT" }
        "BLOCK_VAULT_HIT" { $matchingMaterial = "VAULT" }
        "BLOCK_VAULT_OPEN_SHUTTER" { $matchingMaterial = "VAULT" }
        "BLOCK_VAULT_STEP" { $matchingMaterial = "VAULT" }
        "BLOCK_VINE_BREAK" { $matchingMaterial = "VINE" }
        "BLOCK_VINE_FALL" { $matchingMaterial = "VINE" }
        "BLOCK_VINE_HIT" { $matchingMaterial = "VINE" }
        "BLOCK_VINE_PLACE" { $matchingMaterial = "VINE" }
        "BLOCK_VINE_STEP" { $matchingMaterial = "VINE" }
        "BLOCK_WOODEN_BUTTON_CLICK_OFF" { $matchingMaterial = "OAK_BUTTON" }
        "BLOCK_WOODEN_BUTTON_CLICK_ON" { $matchingMaterial = "OAK_BUTTON" }
        "BLOCK_WOODEN_DOOR_CLOSE" { $matchingMaterial = "OAK_DOOR" }
        "BLOCK_WOODEN_DOOR_OPEN" { $matchingMaterial = "OAK_DOOR" }
        "BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "OAK_PRESSURE_PLATE" }
        "BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "OAK_PRESSURE_PLATE" }
        "BLOCK_WOODEN_TRAPDOOR_CLOSE" { $matchingMaterial = "OAK_TRAPDOOR" }
        "BLOCK_WOODEN_TRAPDOOR_OPEN" { $matchingMaterial = "OAK_TRAPDOOR" }
        "BLOCK_WOOD_BREAK" { $matchingMaterial = "OAK_WOOD" }
        "BLOCK_WOOD_FALL" { $matchingMaterial = "OAK_WOOD" }
        "BLOCK_WOOD_HIT" { $matchingMaterial = "OAK_WOOD" }
        "BLOCK_WOOD_PLACE" { $matchingMaterial = "OAK_WOOD" }
        "BLOCK_WOOD_STEP" { $matchingMaterial = "OAK_WOOD" }
        "ENCHANT_THORNS_HIT" { $matchingMaterial = "DEAD_BUSH" }
        "ENTITY_ARROW_HIT" { $matchingMaterial = "ARROW" }
        "ENTITY_ARROW_HIT_PLAYER" { $matchingMaterial = "ARROW" }
        "EVENT_MOB_EFFECT_BAD_OMEN" { $matchingMaterial = "OMINOUS_BOTTLE" }
        "EVENT_MOB_EFFECT_RAID_OMEN" { $matchingMaterial = "OMINOUS_BOTTLE" }
        "EVENT_MOB_EFFECT_TRIAL_OMEN" { $matchingMaterial = "TRIAL_KEY" }
        "EVENT_RAID_HORN" { $matchingMaterial = "GOAT_HORN" }
        "INTENTIONALLY_EMPTY" { $matchingMaterial = "BARRIER" }
        "ITEM_ARMOR_EQUIP_CHAIN" { $matchingMaterial = "CHAINMAIL_HELMET" }
        "ITEM_ARMOR_EQUIP_DIAMOND" { $matchingMaterial = "DIAMOND_HELMET" }
        "ITEM_ARMOR_EQUIP_ELYTRA" { $matchingMaterial = "ELYTRA" }
        "ITEM_ARMOR_EQUIP_GENERIC" { $matchingMaterial = "CARVED_PUMPKIN" }
        "ITEM_ARMOR_EQUIP_GOLD" { $matchingMaterial = "GOLDEN_HELMET" }
        "ITEM_ARMOR_EQUIP_IRON" { $matchingMaterial = "IRON_HELMET" }
        "ITEM_ARMOR_EQUIP_LEATHER" { $matchingMaterial = "LEATHER_HELMET" }
        "ITEM_ARMOR_EQUIP_NETHERITE" { $matchingMaterial = "NETHERITE_HELMET" }
        "ITEM_ARMOR_EQUIP_TURTLE" { $matchingMaterial = "TURTLE_HELMET" }
        "ITEM_AXE_SCRAPE" { $matchingMaterial = "DIAMOND_AXE" }
        "ITEM_AXE_STRIP" { $matchingMaterial = "STRIPPED_OAK_WOOD" }
        "ITEM_AXE_WAX_OFF" { $matchingMaterial = "DIAMOND_AXE" }
        "ITEM_AXE_STRIP" { $matchingMaterial = "STRIPPED_OAK_WOOD" }
        "ITEM_BOOK_PAGE_TURN" { $matchingMaterial = "BOOK" }
        "ITEM_BOOK_PUT" { $matchingMaterial = "BOOK" }
        "ITEM_BOTTLE_FILL" { $matchingMaterial = "BOTTLE" }
        "ITEM_BOTTLE_FILL_DRAGONBREATH" { $matchingMaterial = "DRAGON_BREATH" }
        "ITEM_BUNDLE_DROP_CONTENTS" { $matchingMaterial = "BUNDLE" }
        "ITEM_CROP_PLANT" { $matchingMaterial = "CARROT" }
        "ITEM_DYE_USE" { $matchingMaterial = "WHITE_DYE" }
        "ITEM_FIRECHARGE_USE" { $matchingMaterial = "FIRE_CHARGE" }
        "ITEM_FLINTANDSTEEL_USE" { $matchingMaterial = "FLINT_AND_STEEL" }
        "ITEM_HONEYCOMB_WAX_ON" { $matchingMaterial = "HONEYCOMB" }
        "ITEM_LODESTONE_COMPASS_LOCK" { $matchingMaterial = "LODESTONE" }
        "ITEM_NETHER_WART_PLANT" { $matchingMaterial = "NETHER_WART" }
        "ITEM_SHIELD_BLOCK" { $matchingMaterial = "SHIELD" }
        "MUSIC_CREATIVE" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_CREDITS" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_END" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_GAME" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_MENU" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_NETHER_BASALT_DELTAS" { $matchingMaterial = "MUSIC_DISC_PIGSTEP" }
        "MUSIC_NETHER_NETHER_WASTES" { $matchingMaterial = "MUSIC_DISC_PIGSTEP" }
        "MUSIC_NETHER_SOUL_SAND_VALLEY" { $matchingMaterial = "MUSIC_DISC_PIGSTEP" }
        "MUSIC_OVERWORLD_BADLANDS" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_BAMBOO_JUNGLE" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_CHERRY_GROVE" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_DEEP_DARK" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_DESERT" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_FLOWER_FOREST" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_FOREST" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_FROZEN_PEAKS" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_GROVE" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_JAGGED_PEAKS" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_JUNGLE" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_LUSH_CAVES" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_MEADOW" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_OLD_GROWTH_TAIGA" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_SNOWY_SLOPES" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_SPARSE_JUNGLE" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_STONY_PEAKS" { $matchingMaterial = "MUSIC_DISC_5" }
        "MUSIC_OVERWORLD_SWAMP" { $matchingMaterial = "MUSIC_DISC_5" }
        "UI_TOAST_CHALLENGE_COMPLETE" { $matchingMaterial = "OAK_HANGING_SIGN" }
        "UI_TOAST_IN" { $matchingMaterial = "OAK_HANGING_SIGN" }
        "UI_TOAST_OUT" { $matchingMaterial = "OAK_HANGING_SIGN" }
        "WEATHER_RAIN" { $matchingMaterial = "OAK_HANGING_SIGN" }
        "WEATHER_RAIN_ABOVE" { $matchingMaterial = "LIGHTNING_ROD" }
    }

    if (-not $matchingMaterial) {
        if($sound -match "UNDERWATER" -or $sound -match "BUBBLE_COLUMN") {
            $matchingMaterial = "WATER_BUCKET"
        }
    }

    if (-not $matchingMaterial) {
        if ($category -eq "ENTITY")
        {
            $matchingMaterial = $materialSet | Where-Object {
                ($_ -match $sound.Split('_')[1] -or $_ -match $sound.Split('_')[2]) -and $_ -match "BUCKET"
            } | Select-Object -First 1

            if (-not $matchingMaterial) {
                $matchingMaterial = $materialSet | Where-Object {
                    ($_ -match $sound.Split('_')[1] -or $_ -match $sound.Split('_')[2]) -and  $_ -match "SPAWN_EGG"
                } | Select-Object -First 1
            }
        }
    }

    # Try to match materials based on sound parts
    if (-not $matchingMaterial) {
        $matchingMaterial = $materialSet | Where-Object {
            $_ -match $sound.Split('_')[1] -and $_ -match $sound.Split('_')[2]
        } | Select-Object -First 1
    }

    if (-not $matchingMaterial) {
        $matchingMaterial = $materialSet | Where-Object {
            $_ -match $sound.Split('_')[1] -or $_ -match $sound.Split('_')[2]
        } | Select-Object -First 1
    }

    # Default to BARRIER if no match is found
    if (-not $matchingMaterial) {
        $matchingMaterial = "BARRIER"
    }

    # Add the enum entry
    $output += "    $sound(Material.$matchingMaterial, `"$category`"),`n"
}

# Finish the Enum class
$output += @"
    ;

    private final Material material;
    private final String category;

    SoundEnum(Material material, String category) {
        this.material = material;
        this.category = category;
    }

    public static List<SoundEnum> getSoundsByCategory(String category) {
        return Arrays.stream(SoundEnum.values())
                .filter(sound -> sound.category.equals(category))
                .collect(Collectors.toList());
    }

    public Material getMaterial() {
        return material;
    }

    public String getCategory() {
        return category;
    }

    public Sound getSound() {
        try {
            return Sound.valueOf(name());
        } catch (IllegalArgumentException e) {
            System.out.println("No matching Sound for: " + name());
            return null;
        }
    }
}
"@

# Write the output to a Java file
$output | Set-Content "SoundEnum.java"

Write-Host "Enum class generated as SoundEnum.java"

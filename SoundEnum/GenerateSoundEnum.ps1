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
        "AMBIENT_NETHER_WASTES_ADDITIONS" { $matchingMaterial = "" }
        "AMBIENT_NETHER_WASTES_LOOP" { $matchingMaterial = "" }
        "AMBIENT_NETHER_WASTES_MOOD" { $matchingMaterial = "" }
        "BLOCK_ANVIL_HIT" { $matchingMaterial = "WHITE_WOOL" }
        "BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "" }
        "BLOCK_BAMBOO_WOOD_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "" }
        "BLOCK_BAMBOO_WOOD_TRAPDOOR_CLOSE" { $matchingMaterial = "" }
        "BLOCK_BAMBOO_WOOD_TRAPDOOR_OPEN" { $matchingMaterial = "" }
        "BLOCK_BARREL_CLOSE" { $matchingMaterial = "" }
        "BLOCK_BARREL_OPEN" { $matchingMaterial = "" }
        "BLOCK_BEEHIVE_DRIP" { $matchingMaterial = "" }
        "BLOCK_BEEHIVE_SHEAR" { $matchingMaterial = "" }
        "BLOCK_BEEHIVE_WORK" { $matchingMaterial = "" }
        "BLOCK_BLASTFURNACE_FIRE_CRACKLE" { $matchingMaterial = "" }
        "BLOCK_CAKE_ADD_CANDLE" { $matchingMaterial = "" }
        "BLOCK_CANDLE_HIT" { $matchingMaterial = "" }
        "BLOCK_CANDLE_STEP" { $matchingMaterial = "" }
        "BLOCK_CHAIN_HIT" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_BREAK" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_BUTTON_CLICK_OFF" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_BUTTON_CLICK_ON" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_DOOR_CLOSE" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_DOOR_OPEN" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_FENCE_GATE_CLOSE" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_FENCE_GATE_OPEN" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_BREAK" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_FALL" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_HIT" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_PLACE" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_HANGING_SIGN_STEP" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_HIT" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_PLACE" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_STEP" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_TRAPDOOR_CLOSE" { $matchingMaterial = "" }
        "BLOCK_CHERRY_WOOD_TRAPDOOR_OPEN" { $matchingMaterial = "" }
        "BLOCK_CHEST_CLOSE" { $matchingMaterial = "" }
        "BLOCK_CHEST_OPEN" { $matchingMaterial = "" }
        "BLOCK_COMPOSTER_FILL" { $matchingMaterial = "" }
        "BLOCK_COMPOSTER_FILL_SUCCESS" { $matchingMaterial = "" }
        "BLOCK_COPPER_BREAK" { $matchingMaterial = "" }
        "BLOCK_COPPER_FALL" { $matchingMaterial = "" }
        "BLOCK_CORAL_BLOCK_BREAK" { $matchingMaterial = "" }
        "BLOCK_CORAL_BLOCK_FALL" { $matchingMaterial = "" }
        "BLOCK_CORAL_BLOCK_HIT" { $matchingMaterial = "" }
        "BLOCK_CORAL_BLOCK_PLACE" { $matchingMaterial = "" }
        "BLOCK_CORAL_BLOCK_STEP" { $matchingMaterial = "" }
        "BLOCK_CROP_BREAK" { $matchingMaterial = "" }
        "BLOCK_ENCHANTMENT_TABLE_USE" { $matchingMaterial = "" }
        "BLOCK_FIRE_AMBIENT" { $matchingMaterial = "" }
        "BLOCK_FIRE_EXTINGUISH" { $matchingMaterial = "" }
        "BLOCK_FROGLIGHT_HIT" { $matchingMaterial = "" }
        "BLOCK_FROGLIGHT_STEP" { $matchingMaterial = "" }
        "BLOCK_FROGSPAWN_HIT" { $matchingMaterial = "" }
        "BLOCK_FROGSPAWN_STEP" { $matchingMaterial = "" }
        "BLOCK_FUNGUS_HIT" { $matchingMaterial = "CRIMSON_FUNGUS" }
        "BLOCK_GLASS_HIT" { $matchingMaterial = "" }
        "BLOCK_GRAVEL_BREAK" { $matchingMaterial = "" }
        "BLOCK_GRAVEL_FALL" { $matchingMaterial = "" }
        "BLOCK_GRAVEL_HIT" { $matchingMaterial = "" }
        "BLOCK_GRAVEL_PLACE" { $matchingMaterial = "" }
        "BLOCK_GRAVEL_STEP" { $matchingMaterial = "" }
        "BLOCK_GROWING_PLANT_CROP" { $matchingMaterial = "" }
        "BLOCK_HANGING_SIGN_WAXED_INTERACT_FAIL" { $matchingMaterial = "HONEYCOMB" }
        "BLOCK_LADDER_HIT" { $matchingMaterial = "" }
        "BLOCK_LANTERN_BREAK" { $matchingMaterial = "" }
        "BLOCK_LANTERN_FALL" { $matchingMaterial = "" }
        "BLOCK_LANTERN_HIT" { $matchingMaterial = "" }
        "BLOCK_LANTERN_PLACE" { $matchingMaterial = "" }
        "BLOCK_LANTERN_STEP" { $matchingMaterial = "" }
        "BLOCK_LAVA_POP" { $matchingMaterial = "" }
        "BLOCK_LODESTONE_HIT" { $matchingMaterial = "" }
        "BLOCK_LODESTONE_STEP" { $matchingMaterial = "" }
        "BLOCK_METAL_BREAK" { $matchingMaterial = "" }
        "BLOCK_METAL_FALL" { $matchingMaterial = "" }
        "BLOCK_METAL_HIT" { $matchingMaterial = "" }
        "BLOCK_METAL_PLACE" { $matchingMaterial = "" }
        "BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "" }
        "BLOCK_METAL_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "" }
        "BLOCK_METAL_STEP" { $matchingMaterial = "" }
        "BLOCK_MOSS_BREAK" { $matchingMaterial = "" }
        "BLOCK_MOSS_FALL" { $matchingMaterial = "" }
        "BLOCK_MOSS_HIT" { $matchingMaterial = "" }
        "BLOCK_MOSS_PLACE" { $matchingMaterial = "" }
        "BLOCK_MOSS_STEP" { $matchingMaterial = "" }
        "BLOCK_NETHERRACK_HIT" { $matchingMaterial = "" }
        "BLOCK_NETHER_ORE_BREAK" { $matchingMaterial = "" }
        "BLOCK_NETHER_ORE_FALL" { $matchingMaterial = "" }
        "BLOCK_NETHER_ORE_HIT" { $matchingMaterial = "" }
        "BLOCK_NETHER_ORE_PLACE" { $matchingMaterial = "" }
        "BLOCK_NETHER_ORE_STEP" { $matchingMaterial = "" }
        "BLOCK_NETHER_WART_BREAK" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_BREAK" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_BUTTON_CLICK_OFF" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_BUTTON_CLICK_ON" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_DOOR_CLOSE" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_DOOR_OPEN" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_FALL" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_FENCE_GATE_CLOSE" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_FENCE_GATE_OPEN" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_BREAK" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_FALL" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_HIT" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_PLACE" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_HANGING_SIGN_STEP" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_HIT" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_PLACE" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_STEP" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_TRAPDOOR_CLOSE" { $matchingMaterial = "" }
        "BLOCK_NETHER_WOOD_TRAPDOOR_OPEN" { $matchingMaterial = "" }
        "BLOCK_SCAFFOLDING_HIT" { $matchingMaterial = "" }
        "BLOCK_SCULK_HIT" { $matchingMaterial = "" }
        "BLOCK_SHROOMLIGHT_HIT" { $matchingMaterial = "" }
        "BLOCK_SHROOMLIGHT_STEP" { $matchingMaterial = "" }
        "BLOCK_SIGN_WAXED_INTERACT_FAIL" { $matchingMaterial = "" }
        "BLOCK_SNOW_HIT" { $matchingMaterial = "" }
        "BLOCK_VAULT_BREAK" { $matchingMaterial = "" }
        "BLOCK_VAULT_CLOSE_SHUTTER" { $matchingMaterial = "" }
        "BLOCK_VAULT_HIT" { $matchingMaterial = "" }
        "BLOCK_VAULT_OPEN_SHUTTER" { $matchingMaterial = "" }
        "BLOCK_VAULT_STEP" { $matchingMaterial = "" }
        "BLOCK_VINE_BREAK" { $matchingMaterial = "" }
        "BLOCK_VINE_FALL" { $matchingMaterial = "" }
        "BLOCK_VINE_HIT" { $matchingMaterial = "" }
        "BLOCK_VINE_PLACE" { $matchingMaterial = "" }
        "BLOCK_VINE_STEP" { $matchingMaterial = "" }
        "BLOCK_WOODEN_BUTTON_CLICK_OFF" { $matchingMaterial = "" }
        "BLOCK_WOODEN_BUTTON_CLICK_ON" { $matchingMaterial = "" }
        "BLOCK_WOODEN_DOOR_CLOSE" { $matchingMaterial = "" }
        "BLOCK_WOODEN_DOOR_OPEN" { $matchingMaterial = "" }
        "BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF" { $matchingMaterial = "" }
        "BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON" { $matchingMaterial = "" }
        "BLOCK_WOODEN_TRAPDOOR_CLOSE" { $matchingMaterial = "" }
        "BLOCK_WOODEN_TRAPDOOR_OPEN" { $matchingMaterial = "" }
        "BLOCK_WOOD_BREAK" { $matchingMaterial = "" }
        "BLOCK_WOOD_FALL" { $matchingMaterial = "" }
        "BLOCK_WOOD_HIT" { $matchingMaterial = "" }
        "BLOCK_WOOD_PLACE" { $matchingMaterial = "" }
        "BLOCK_WOOD_STEP" { $matchingMaterial = "" }
        "ENCHANT_THORNS_HIT" { $matchingMaterial = "DEAD_BUSH" }
        <#"ENTITY_ARMADILLO_EAT" { $matchingMaterial = "" }
        "ENTITY_ARMADILLO_LAND" { $matchingMaterial = "" }
        "ENTITY_ARROW_HIT" { $matchingMaterial = "" }
        "ENTITY_ARROW_HIT_PLAYER" { $matchingMaterial = "" }
        "ENTITY_BEE_DEATH" { $matchingMaterial = "" }
        "ENTITY_BEE_HURT" { $matchingMaterial = "" }
        "ENTITY_BEE_LOOP" { $matchingMaterial = "" }
        "ENTITY_BEE_LOOP_AGGRESSIVE" { $matchingMaterial = "" }
        "ENTITY_BEE_POLLINATE" { $matchingMaterial = "" }
        "ENTITY_BEE_STING" { $matchingMaterial = "" }
        "ENTITY_BLAZE_AMBIENT" { $matchingMaterial = "" }
        "ENTITY_BLAZE_BURN" { $matchingMaterial = "" }
        "ENTITY_BLAZE_DEATH" { $matchingMaterial = "" }
        "ENTITY_BLAZE_HURT" { $matchingMaterial = "" }
        "ENTITY_BLAZE_SHOOT" { $matchingMaterial = "" }
        "ENTITY_BOGGED_SHEAR" { $matchingMaterial = "" }
        "ENTITY_BREEZE_LAND" { $matchingMaterial = "" }
        "ENTITY_CAMEL_EAT" { $matchingMaterial = "" }
        "ENTITY_CAMEL_SIT" { $matchingMaterial = "" }
        "ENTITY_CAMEL_STAND" { $matchingMaterial = "" }
        "ENTITY_CAT_AMBIENT" { $matchingMaterial = "" }
        "ENTITY_CAT_BEG_FOR_FOOD" { $matchingMaterial = "" }
        "ENTITY_CAT_DEATH" { $matchingMaterial = "" }
        "ENTITY_CAT_EAT" { $matchingMaterial = "" }
        "ENTITY_CAT_HISS" { $matchingMaterial = "" }
        "ENTITY_CAT_HURT" { $matchingMaterial = "" }
        "ENTITY_CAT_PURR" { $matchingMaterial = "" }
        "ENTITY_CAT_PURREOW" { $matchingMaterial = "" }
        "ENTITY_CAT_STRAY_AMBIENT" { $matchingMaterial = "" }
        "ENTITY_CHICKEN_AMBIENT" { $matchingMaterial = "" }
        "ENTITY_CHICKEN_DEATH" { $matchingMaterial = "" }
        "ENTITY_CHICKEN_HURT" { $matchingMaterial = "" }
        "ENTITY_CHICKEN_STEP" { $matchingMaterial = "" }
        "ENTITY_DONKEY_EAT" { $matchingMaterial = "" }
        "ENTITY_EGG_THROW" { $matchingMaterial = "" }
        "ENTITY_FISH_SWIM" { $matchingMaterial = "" }
        "ENTITY_FOX_EAT" { $matchingMaterial = "" }
        "ENTITY_FOX_SNIFF" { $matchingMaterial = "" }
        "ENTITY_FROG_EAT" { $matchingMaterial = "" }
        "ENTITY_FROG_LAY_SPAWN" { $matchingMaterial = "" }
        "ENTITY_GENERIC_BIG_FALL" { $matchingMaterial = "" }
        "ENTITY_GENERIC_BURN" { $matchingMaterial = "" }
        "ENTITY_GENERIC_DEATH" { $matchingMaterial = "" }
        "ENTITY_GENERIC_DRINK" { $matchingMaterial = "" }
        "ENTITY_GENERIC_EAT" { $matchingMaterial = "" }
        "ENTITY_GENERIC_EXPLODE" { $matchingMaterial = "" }
        "ENTITY_GENERIC_EXTINGUISH_FIRE" { $matchingMaterial = "" }
        "ENTITY_GENERIC_HURT" { $matchingMaterial = "" }
        "ENTITY_GENERIC_SMALL_FALL" { $matchingMaterial = "" }
        "ENTITY_GENERIC_SPLASH" { $matchingMaterial = "" }
        "ENTITY_GENERIC_SWIM" { $matchingMaterial = "" }
        "ENTITY_GHAST_AMBIENT" { $matchingMaterial = "" }
        "ENTITY_GHAST_DEATH" { $matchingMaterial = "" }
        "ENTITY_GHAST_HURT" { $matchingMaterial = "" }
        "ENTITY_GHAST_SCREAM" { $matchingMaterial = "" }
        "ENTITY_GHAST_SHOOT" { $matchingMaterial = "" }
        "ENTITY_GHAST_WARN" { $matchingMaterial = "" }
        "ENTITY_GOAT_EAT" { $matchingMaterial = "" }
        "ENTITY_GOAT_RAM_IMPACT" { $matchingMaterial = "" }
        "ENTITY_GUARDIAN_AMBIENT" { $matchingMaterial = "" }
        "ENTITY_GUARDIAN_AMBIENT_LAND" { $matchingMaterial = "" }
        "ENTITY_GUARDIAN_ATTACK" { $matchingMaterial = "" }
        "ENTITY_GUARDIAN_DEATH" { $matchingMaterial = "" }
        "ENTITY_GUARDIAN_DEATH_LAND" { $matchingMaterial = "" }
        "ENTITY_GUARDIAN_FLOP" { $matchingMaterial = "" }
        "ENTITY_GUARDIAN_HURT" { $matchingMaterial = "" }
        "ENTITY_GUARDIAN_HURT_LAND" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }
        "" { $matchingMaterial = "" }#>
        "EVENT_MOB_EFFECT_BAD_OMEN" { $matchingMaterial = "" }
        "EVENT_MOB_EFFECT_RAID_OMEN" { $matchingMaterial = "" }
        "EVENT_MOB_EFFECT_TRIAL_OMEN" { $matchingMaterial = "" }
        "EVENT_RAID_HORN" { $matchingMaterial = "" }
        "INTENTIONALLY_EMPTY" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_CHAIN" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_DIAMOND" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_ELYTRA" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_GENERIC" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_GOLD" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_IRON" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_LEATHER" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_NETHERITE" { $matchingMaterial = "" }
        "ITEM_ARMOR_EQUIP_TURTLE" { $matchingMaterial = "" }
        "ITEM_AXE_SCRAPE" { $matchingMaterial = "" }
        "ITEM_AXE_STRIP" { $matchingMaterial = "" }
        "ITEM_AXE_WAX_OFF" { $matchingMaterial = "" }
        "ITEM_AXE_STRIP" { $matchingMaterial = "" }
        "ITEM_AXE_SCRAPE " { $matchingMaterial = "" }
        "ITEM_BOOK_PAGE_TURN" { $matchingMaterial = "" }
        "ITEM_BOOK_PUT" { $matchingMaterial = "" }
        "ITEM_BOTTLE_FILL" { $matchingMaterial = "" }
        "ITEM_BOTTLE_FILL_DRAGONBREATH" { $matchingMaterial = "" }
        "ITEM_BUNDLE_DROP_CONTENTS" { $matchingMaterial = "" }
        "ITEM_CROP_PLANT" { $matchingMaterial = "" }
        "ITEM_DYE_USE" { $matchingMaterial = "" }
        "ITEM_FIRECHARGE_USE" { $matchingMaterial = "" }
        "ITEM_FLINTANDSTEEL_USE" { $matchingMaterial = "" }
        "ITEM_HONEYCOMB_WAX_ON" { $matchingMaterial = "" }
        "ITEM_LODESTONE_COMPASS_LOCK" { $matchingMaterial = "" }
        "ITEM_NETHER_WART_PLANT" { $matchingMaterial = "" }
        "ITEM_SHIELD_BLOCK" { $matchingMaterial = "" }
        "MUSIC_CREATIVE" { $matchingMaterial = "" }
        "MUSIC_CREDITS" { $matchingMaterial = "" }
        "MUSIC_END" { $matchingMaterial = "" }
        "MUSIC_GAME" { $matchingMaterial = "" }
        "MUSIC_MENU" { $matchingMaterial = "" }
        "MUSIC_NETHER_BASALT_DELTAS" { $matchingMaterial = "" }
        "MUSIC_NETHER_NETHER_WASTES" { $matchingMaterial = "" }
        "MUSIC_NETHER_SOUL_SAND_VALLEY" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_BADLANDS" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_BAMBOO_JUNGLE" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_CHERRY_GROVE" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_DEEP_DARK" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_DESERT" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_FLOWER_FOREST" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_FOREST" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_FROZEN_PEAKS" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_GROVE" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_JAGGED_PEAKS" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_JUNGLE" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_LUSH_CAVES" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_MEADOW" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_OLD_GROWTH_TAIGA" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_SNOWY_SLOPES" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_SPARSE_JUNGLE" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_STONY_PEAKS" { $matchingMaterial = "" }
        "MUSIC_OVERWORLD_SWAMP" { $matchingMaterial = "" }
        "UI_TOAST_CHALLENGE_COMPLETE" { $matchingMaterial = "" }
        "UI_TOAST_IN" { $matchingMaterial = "" }
        "UI_TOAST_OUT" { $matchingMaterial = "" }
        "WEATHER_RAIN" { $matchingMaterial = "" }
        "WEATHER_RAIN_ABOVE" { $matchingMaterial = "" }
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

    public Material getMaterial() {
        return material;
    }

    public String getCategory() {
        return category;
    }
}
"@

# Write the output to a Java file
$output | Set-Content "SoundEnum.java"

Write-Host "Enum class generated as SoundEnum.java"

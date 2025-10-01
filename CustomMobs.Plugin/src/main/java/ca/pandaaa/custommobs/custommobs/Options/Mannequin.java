package ca.pandaaa.custommobs.custommobs.Options;

import ca.pandaaa.custommobs.CustomMobs;
import ca.pandaaa.custommobs.configurations.CustomMobConfiguration;
import ca.pandaaa.custommobs.custommobs.CustomMob;
import ca.pandaaa.custommobs.utils.CustomMobsItem;
import ca.pandaaa.custommobs.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Pose;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Mannequin extends CustomMobOption {
    private String editing = "";

    /**
     * Represents the texture of the mannequin, which will change it's skin.
     */
    private static final String TEXTURE = "mob.mannequin-texture";
    private String texture;

    /**
     * Represents the mannequin's cape's texture.
     */
    private static final String CAPE = "mob.mannequin-cape";
    private String cape;

    /**
     * Represents the mannequin's skin size. Typically, Alex uses the slim size and Steve uses the wide size.
     */
    private static final String WIDE = "mob.mannequin-wide";
    private boolean wide;

    /**
     * Determines which hand the mannequin uses as it's main.
     */
    private static final String RIGHT_HANDED = "mob.mannequin-right-handed";
    private boolean rightHanded;

    /**
     * Determines which pose the mannequin is in.
     */
    private static final String POSE = "mob.mannequin-pose";
    private Pose pose;

    /**
     * When disabled, the mannequin cannot be pushed by a player (such as an NPC).
     */
    private static final String MOVABLE = "mob.mannequin-movable";
    private boolean movable;

    /**
     * Adds a description under the name of the mannequin.
     */
    private static final String DESCRIPTION = "mob.mannequin-description";
    private String description;

    // The API does not allow for Mannequin to have armor.
    public Mannequin(CustomMobConfiguration mobConfiguration) {
        super(mobConfiguration);
        this.texture = getOption(TEXTURE, String.class, "");
        this.cape = getOption(CAPE, String.class, "");
        this.wide = getOption(WIDE, Boolean.class, false);
        this.rightHanded = getOption(RIGHT_HANDED, Boolean.class, true);
        this.pose = getOption(POSE, Pose.class);
        this.movable = getOption(MOVABLE, Boolean.class, false);
        this.description = getOption(DESCRIPTION, String.class, "");
    }

    public void applyOptions(Entity customMob) {
        if(!(customMob instanceof org.bukkit.entity.Mannequin mannequin))
            return;

        try {
            PlayerProfile profile = Bukkit.createPlayerProfile(UUID.randomUUID(), "Mannequin");
            PlayerTextures textures = profile.getTextures();

            if (!texture.isEmpty())
                textures.setSkin(new URL(texture), wide
                        ? PlayerTextures.SkinModel.SLIM
                        : PlayerTextures.SkinModel.CLASSIC);

            if (!cape.isEmpty())
                textures.setCape(new URL(cape));

            profile.setTextures(textures);
            mannequin.setPlayerProfile(profile);
        } catch (MalformedURLException e) {
            CustomMobs.getPlugin().getServer().getConsoleSender()
                    .sendMessage(ChatColor.translateAlternateColorCodes('&', "&c[!] An error occured while applying the mannequin's texture."));
        }

        mannequin.setMainHand(rightHanded ? MainHand.RIGHT : MainHand.LEFT);
        if(pose != null)
            mannequin.setPose(pose);
        mannequin.setImmovable(!movable);
        if(description != null && !description.isEmpty())
            mannequin.setDescription(Utils.applyFormat(description));
    }

    @Override
    public void resetOptions() {
        setOption(TEXTURE, null);
        setOption(CAPE, null);
        setOption(WIDE, null);
        setOption(RIGHT_HANDED, null);
        setOption(POSE, null);
        setOption(MOVABLE, null);
        setOption(DESCRIPTION, null);
    }

    public List<ItemStack> getOptionItems() {
        List<ItemStack> items = new ArrayList<>();

        items.add(getOptionItemStack(getTextureItem(), true, false));
        items.add(getOptionItemStack(getCapeItem(), true, false));
        items.add(getOptionItemStack(getWideItem(), false, false));
        items.add(getOptionItemStack(getHandItem(), false, false));
        items.add(getOptionItemStack(getPoseItem(), true, true));
        items.add(getOptionItemStack(getMovableItem(), false, false));
        items.add(getOptionItemStack(getDescriptionItem(), true, false));

        return items;
    }

    public ItemStack modifyOption(Player clicker, CustomMob customMob, String option, ClickType clickType) {
        switch (option.toLowerCase()) {
            case "texture": {
                if (clickType.isRightClick()) {
                    texture = "";
                    setOption(TEXTURE, texture);
                } else {
                    this.editing = "Texture";
                    sendChangeTextureMessage(clicker, "skin's texture");
                }
                break;
            }

            case "cape": {
                if (clickType.isRightClick()) {
                    cape = "";
                    setOption(CAPE, cape);
                } else {
                    this.editing = "Cape";
                    sendChangeTextureMessage(clicker, "cape's texture");
                }
                break;
            }

            case "wide": {
                this.wide = !wide;
                setOption(WIDE, wide);
                return getOptionItemStack(getWideItem(), false, false);
            }

            case "hand": {
                this.rightHanded = !rightHanded;
                setOption(RIGHT_HANDED, rightHanded);
                return getOptionItemStack(getHandItem(), false, false);
            }

            case "pose": {
                if (clickType.isRightClick()) {
                    this.pose = null;
                } else {
                    List<Pose> poses = Arrays.asList(org.bukkit.entity.Pose.values()).stream()
                            .filter(x -> x != Pose.SPIN_ATTACK)
                            .filter(x -> x != Pose.LONG_JUMPING)
                            .filter(x -> x != Pose.DYING)
                            .filter(x -> x != Pose.CROAKING)
                            .filter(x -> x != Pose.USING_TONGUE)
                            .filter(x -> x != Pose.SITTING)
                            .filter(x -> x != Pose.ROARING)
                            .filter(x -> x != Pose.SNIFFING)
                            .filter(x -> x != Pose.EMERGING)
                            .filter(x -> x != Pose.DIGGING)
                            .filter(x -> x != Pose.SLIDING)
                            .filter(x -> x != Pose.SHOOTING)
                            .filter(x -> x != Pose.INHALING)
                            .toList();

                    if (poses.indexOf(pose) == poses.size() - 1)
                        this.pose = poses.get(0);
                    else
                        this.pose = poses.get(poses.indexOf(pose) + 1);
                }
                setOption(POSE, pose != null ? pose.name() : null);
                return getOptionItemStack(getPoseItem(), true, true);
            }

            case "movable": {
                this.movable = !movable;
                setOption(MOVABLE, movable);
                return getOptionItemStack(getMovableItem(), false, false);
            }

            case "description": {
                if (clickType.isRightClick()) {
                    description = "";
                    setOption(DESCRIPTION, description);
                } else {
                    this.editing = "Description";
                    sendChangeDescriptionMessage(clicker);
                }
                break;
            }
        }
        return null;
    }

    public static boolean isApplicable(EntityType entityType) {
        return Utils.isVersionAtLeast("1.21.9") && org.bukkit.entity.Mannequin.class.isAssignableFrom(entityType.getEntityClass());
    }

    public CustomMobsItem getTextureItem() {
        CustomMobsItem item = new CustomMobsItem(Material.MAP);
        item.setName("&e&lSkin texture");
        item.addLore("&eSkin's texture: &f" + (!texture.isEmpty() ? texture : "None"));
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Texture");
        return item;
    }

    public CustomMobsItem getCapeItem() {
        CustomMobsItem item = new CustomMobsItem(Material.PHANTOM_MEMBRANE);
        item.setName("&3&lCape texture");
        item.addLore("&eCape's texture: &f" + (!cape.isEmpty() ? cape : "None"));
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Cape");
        return item;
    }

    public CustomMobsItem getWideItem() {
        CustomMobsItem item = new CustomMobsItem(Material.HOPPER);
        String wide = this.wide ? "&a&lOn" : "&c&lOff";
        item.setName("&2&lWide skin");
        item.addLore("&eWide skin: &f" + wide);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Wide");
        return item;
    }

    public CustomMobsItem getHandItem() {
        CustomMobsItem item = new CustomMobsItem(Material.TORCH);
        item.setName("&9&lMain hand");
        item.addLore("&eMain hand: &f" + (rightHanded ? "Right" : "Left"));
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Hand");
        return item;
    }

    public CustomMobsItem getPoseItem() {
        CustomMobsItem item = new CustomMobsItem(Material.COPPER_GOLEM_STATUE);
        item.setName("&d&lPose");
        String pose = this.pose == null ? "&fDefault" : "&f" + Utils.getSentenceCase(this.pose.name());
        item.addLore("&eMannequin's pose: &f" + pose);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Pose");
        return item;
    }

    public CustomMobsItem getMovableItem() {
        CustomMobsItem item = new CustomMobsItem(Material.ANVIL);
        String movable = this.movable ? "&a&lOn" : "&c&lOff";
        item.setName("&5&lMovable");
        item.addLore("&eCan be moved: &f" + movable);
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Movable");
        return item;
    }

    public CustomMobsItem getDescriptionItem() {
        CustomMobsItem item = new CustomMobsItem(Material.SPRUCE_HANGING_SIGN);
        item.setName("&6&lDescription");
        item.addLore("&eDescription: &f" + (!description.isEmpty() ? description : "None"));
        item.setOptionPersistentDataContainer(this.getClass().getSimpleName(), "Description");
        return item;
    }

    public void sendChangeTextureMessage(Player player, String texture) {
        player.closeInventory();
        player.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eChanging " + texture));
        player.sendMessage(Utils.applyFormat(" &6&l- &fEnter the texture value in the chat."));
        player.sendMessage(Utils.applyFormat(" &6&l- &fType &ccancel &fin the chat to cancel."));
    }

    public void sendChangeDescriptionMessage(Player player) {
        player.closeInventory();
        player.sendMessage(Utils.applyFormat("&6&lCus&e&ltom&8&lMo&7&lbs &7&l>> &eChanging description"));
        player.sendMessage(Utils.applyFormat(" &6&l- &fEnter the description value in the chat."));
        player.sendMessage(Utils.applyFormat(" &6&l- &fThe desc. supports &4c&co&6l&eo&ar&bs&f ( &* (&ebasic&f) and &#* (&#cbff0fhex&f) )"));
        player.sendMessage(Utils.applyFormat(" &6&l- &fType &ccancel &fin the chat to cancel."));
    }


    public String getEditing() {
        return editing;
    }

    public void changeTextEditingOptionValue(String value) {
        switch (editing) {
            case "Texture":
                texture = value;
                setOption(TEXTURE, texture);
                break;
            case "Cape":
                cape = value;
                setOption(CAPE, cape);
                break;
            case "Description":
                description = value;
                setOption(DESCRIPTION, description);
                break;
            default:
                break;
        }
        editing = "";
    }
}

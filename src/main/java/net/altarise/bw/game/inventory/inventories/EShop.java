package net.altarise.bw.game.inventory.inventories;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.ItemFactory;
import net.altarise.bw.game.inventory.inventories.shop.InventoryBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public enum EShop {



    WOOL(InventoryBuilder.InventoryType.BLOCK, 20, "Laine", new ItemStack(Material.WOOL, 16), Material.IRON_INGOT, 4),
    HARDENED_CLAY(InventoryBuilder.InventoryType.BLOCK, 29, "Terre cuite", new ItemStack(Material.HARD_CLAY, 16), Material.IRON_INGOT, 12),
    WOOD(InventoryBuilder.InventoryType.BLOCK, 24, "Bois", new ItemStack(Material.WOOD, 16), Material.GOLD_INGOT, 4),
    GLASS(InventoryBuilder.InventoryType.BLOCK, 38, "Verre", new ItemStack(Material.GLASS, 4), Material.IRON_INGOT, 12),
    ENDSTONE(InventoryBuilder.InventoryType.BLOCK, 33, "Endstone", new ItemStack(Material.ENDER_STONE, 12), Material.IRON_INGOT, 24),
    LADDER(InventoryBuilder.InventoryType.BLOCK, 31, "Echelles", new ItemStack(Material.LADDER, 8), Material.IRON_INGOT, 4),
    OBSIDIAN(InventoryBuilder.InventoryType.BLOCK, 42, "Obsidienne", new ItemStack(Material.OBSIDIAN, 4), Material.EMERALD, 4),

    STONE_SWORD(InventoryBuilder.InventoryType.WEAPON, 20, "Epée en pierre", new ItemStack(Material.STONE_SWORD), Material.IRON_INGOT, 10),
    IRON_SWORD(InventoryBuilder.InventoryType.WEAPON, 29, "Epée en fer", new ItemStack(Material.IRON_SWORD), Material.GOLD_INGOT, 7),
    DIAMOND_SWORD(InventoryBuilder.InventoryType.WEAPON, 38, "Epée en diamant", new ItemStack(Material.DIAMOND_SWORD), Material.EMERALD, Integer.parseInt(Bedwars.INSTANCE().getGameProperties().getModeProperties().getSection("shop").getValue("DIAMOND_SWORD"))),
    KNOCKBACK_STICK(InventoryBuilder.InventoryType.WEAPON, 22, "Baton de knockback", new ItemFactory(new ItemBuilder(Material.STICK).enchant(Enchantment.KNOCKBACK, 2).build()).unSafeEnchant(Enchantment.KNOCKBACK, 2).build(), Material.GOLD_INGOT, 5),
    BOW(InventoryBuilder.InventoryType.WEAPON, 24, "Arc", new ItemStack(Material.BOW), Material.GOLD_INGOT, 12),
    POWER_BOW(InventoryBuilder.InventoryType.WEAPON, 33, "Arc (Power I)", new ItemFactory(new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 1).build()).unSafeEnchant(Enchantment.ARROW_DAMAGE, 1).build(), Material.GOLD_INGOT, 20),
    PUNCH_BOW(InventoryBuilder.InventoryType.WEAPON, 42, "Arc (Power I, Punch I)", new ItemFactory(new ItemBuilder(Material.BOW).enchant(Enchantment.ARROW_DAMAGE, 1).enchant(Enchantment.ARROW_KNOCKBACK, 1).build()).unSafeEnchant(Enchantment.ARROW_DAMAGE, 1).unSafeEnchant(Enchantment.ARROW_KNOCKBACK, 1).build(), Material.EMERALD, 6),
    ARROW(InventoryBuilder.InventoryType.WEAPON, 31, "Flèches", new ItemStack(Material.ARROW, 6), Material.GOLD_INGOT, 2),

    CHAINMAIL_CHESTPLATE(InventoryBuilder.InventoryType.ARMOR, 20, "Armure en maille", new ItemStack(Material.CHAINMAIL_CHESTPLATE), Material.IRON_INGOT, 30),
    CHAINMAIL_ICON(InventoryBuilder.InventoryType.ARMOR, 29, "Armure en maille", new ItemStack(Material.PRISMARINE_CRYSTALS), Material.IRON_INGOT, 30),
    CHAINMAIL_BOOTS(InventoryBuilder.InventoryType.ARMOR, 38, "Armure en maille", new ItemStack(Material.CHAINMAIL_BOOTS), Material.IRON_INGOT, 30),
    IRON_CHESTPLATE(InventoryBuilder.InventoryType.ARMOR, 22, "Armure en fer", new ItemStack(Material.IRON_CHESTPLATE), Material.GOLD_INGOT, 12),
    IRON_ICON(InventoryBuilder.InventoryType.ARMOR, 31, "Armure en fer", new ItemStack(Material.IRON_INGOT), Material.GOLD_INGOT, 12),
    IRON_BOOTS(InventoryBuilder.InventoryType.ARMOR, 40, "Armure en fer", new ItemStack(Material.IRON_BOOTS), Material.GOLD_INGOT, 12),
    DIAMOND_CHESTPLATE(InventoryBuilder.InventoryType.ARMOR, 24, "Armure en diamant", new ItemStack(Material.DIAMOND_CHESTPLATE), Material.EMERALD, 6),
    DIAMOND_ICON(InventoryBuilder.InventoryType.ARMOR, 33, "Armure en diamant", new ItemStack(Material.DIAMOND), Material.EMERALD, 6),
    DIAMOND_BOOTS(InventoryBuilder.InventoryType.ARMOR, 42, "Armure en diamant", new ItemStack(Material.DIAMOND_BOOTS), Material.EMERALD, 6),

    SHEARS(InventoryBuilder.InventoryType.TOOL, 31, "Ciseaux", new ItemStack(Material.SHEARS), Material.IRON_INGOT, 20),
    WOODEN_PICKAXE(InventoryBuilder.InventoryType.TOOL, 29, "Pioche en bois", new ItemBuilder(Material.WOOD_PICKAXE).enchant(Enchantment.DIG_SPEED, 1).build(), Material.IRON_INGOT, 10),
    STONE_PICKAXE(InventoryBuilder.InventoryType.TOOL, 29, "Pioche en pierre", new ItemBuilder(Material.STONE_PICKAXE).enchant(Enchantment.DIG_SPEED, 2).build(), Material.IRON_INGOT, 20),
    IRON_PICKAXE(InventoryBuilder.InventoryType.TOOL, 29, "Pioche en fer", new ItemBuilder(Material.IRON_PICKAXE).enchant(Enchantment.DIG_SPEED, 3).build(), Material.GOLD_INGOT, 6),
    DIAMOND_PICKAXE(InventoryBuilder.InventoryType.TOOL, 29, "Pioche en diamant", new ItemBuilder(Material.DIAMOND_PICKAXE).enchant(Enchantment.DIG_SPEED, 3).build(), Material.GOLD_INGOT, 12),
    WOODEN_AXE(InventoryBuilder.InventoryType.TOOL, 33, "Hache en bois", new ItemBuilder(Material.WOOD_AXE).enchant(Enchantment.DIG_SPEED, 1).build(), Material.IRON_INGOT, 10),
    STONE_AXE(InventoryBuilder.InventoryType.TOOL, 33, "Hache en pierre", new ItemBuilder(Material.STONE_AXE).enchant(Enchantment.DIG_SPEED, 2).build(), Material.IRON_INGOT, 20),
    IRON_AXE(InventoryBuilder.InventoryType.TOOL, 33, "Hache en fer", new ItemBuilder(Material.IRON_AXE).enchant(Enchantment.DIG_SPEED, 3).build(), Material.GOLD_INGOT, 6),
    DIAMOND_AXE(InventoryBuilder.InventoryType.TOOL, 33, "Hache en diamant", new ItemBuilder(Material.DIAMOND_AXE).enchant(Enchantment.DIG_SPEED, 3).build(), Material.GOLD_INGOT, 12),

    SWIFTNESS_POTION(InventoryBuilder.InventoryType.POTIONS, 29, "Potion de rapidité", new ItemBuilder(Material.POTION).displayname("§fPotion de rapidité").mainPotionEffect(PotionEffectType.SPEED, 2, 45*20).build(), Material.EMERALD, 1),
    JUMP_POTION(InventoryBuilder.InventoryType.POTIONS, 31, "Potion de saut", new ItemBuilder(Material.POTION).displayname("§fPotion de saut").mainPotionEffect(PotionEffectType.JUMP, 5, 45*20).build(), Material.EMERALD, 1),
    INVISIBILITY_POTION(InventoryBuilder.InventoryType.POTIONS, 33, "Potion d'invisibilité", new ItemBuilder(Material.POTION).displayname("§fPotion d'invisibilité").mainPotionEffect(PotionEffectType.INVISIBILITY, 1, 30*20).build(), Material.EMERALD, 2),

    GOLDEN_APPLE(InventoryBuilder.InventoryType.UTILITY, 22, "Pomme en or", new ItemStack(Material.GOLDEN_APPLE), Material.GOLD_INGOT, 3),
    FIREBALL(InventoryBuilder.InventoryType.UTILITY, 29, "Boule de feu", new ItemFactory(new ItemStack(Material.FIREBALL)).handle(event -> {event.getPlayer().getInventory().remove(new ItemStack(Material.FIREWORK_CHARGE, 1)); event.getPlayer().launchProjectile(Fireball.class);}).build(), Material.IRON_INGOT, 40),
    TNT(InventoryBuilder.InventoryType.UTILITY, 38, "TNT", new ItemStack(Material.TNT), Material.GOLD_INGOT, Integer.parseInt(Bedwars.INSTANCE().getGameProperties().getModeProperties().getSection("shop").getValue("TNT"))),
    ENDER_PEARL(InventoryBuilder.InventoryType.UTILITY, 31, "Ender Pearl", new ItemStack(Material.ENDER_PEARL), Material.EMERALD, 4),
    SPONGE(InventoryBuilder.InventoryType.UTILITY, 40, "Éponge", new ItemStack(Material.SPONGE, 4), Material.GOLD_INGOT, Integer.parseInt(Bedwars.INSTANCE().getGameProperties().getModeProperties().getSection("shop").getValue("SPONGE"))),
    WATER_BUCKET(InventoryBuilder.InventoryType.UTILITY, 33, "Seau d'eau", new ItemStack(Material.WATER_BUCKET), Material.GOLD_INGOT, Integer.parseInt(Bedwars.INSTANCE().getGameProperties().getModeProperties().getSection("shop").getValue("WATER_BUCKET"))),
    MILK_BUCKET(InventoryBuilder.InventoryType.UTILITY, 42, "Seau de lait", new ItemStack(Material.MILK_BUCKET), Material.GOLD_INGOT, 4);




    private final InventoryBuilder.InventoryType inventoryType;
    private final int slot;
    private final String displayName;
    private final ItemStack reward;
    private final Material cost;
    private final int price;





    EShop(InventoryBuilder.InventoryType type, int slot, String displayName, ItemStack reward, Material cost, int price) {
        this.inventoryType = type;
        this.slot = slot;
        this.displayName = displayName;
        this.reward = reward;
        this.cost = cost;
        this.price = price;

    }




    public InventoryBuilder.InventoryType getInventoryType() {
        return inventoryType;
    }


    public int getSlot() {
        return slot;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ItemStack getReward() {
        return reward;
    }

    public Material getCost() {
        return cost;
    }

    public int getPrice() {
        return price;
    }





}

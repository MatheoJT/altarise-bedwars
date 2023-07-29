package net.altarise.bw.game.inventory;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.game.inventory.inventories.EShop;
import net.altarise.bw.game.inventory.inventories.shop.*;
import net.altarise.bw.game.inventory.inventories.upgrade.Enchants;
import net.altarise.bw.game.inventory.inventories.upgrade.Forge;
import net.altarise.bw.game.inventory.inventories.upgrade.Traps;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class AbstractInventory implements InventoryHolder, Listener {


    private final String name;
    private  int size = 9*6;
    private final Inventory inventory;
    private final Map<Integer, Consumer<InventoryClickEvent>> itemHandlers  = new HashMap<>();
    private final List<Consumer<InventoryCloseEvent>> closeHandlers = new ArrayList<>();

    private final List<Integer> silentSlots = new ArrayList<>();





    public AbstractInventory(String name) {
        this.name = name;
        this.inventory = Bukkit.createInventory(this, size, name);

    }

    public AbstractInventory(String name, int size) {
        this.name = name;
        this.size = size;
        this.inventory = Bukkit.createInventory(this, size, name);

    }
    public AbstractInventory() {
        this.name = "default";
        this.inventory = Bukkit.createInventory(this, size, name);
    }


    public void setDefaultUpgradeMenu() {

        ItemStack grayPanel = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 8)).displayname(" ").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack purplePanel = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 2)).displayname(" ").flag(ItemFlag.HIDE_ATTRIBUTES).build();

        ItemStack enchants = new ItemBuilder(Material.ENCHANTMENT_TABLE).displayname("§f> §eEnchantements").lore("", "§7Echantez l'armure et les épées de votre équipe").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack forge = new ItemBuilder(Material.ANVIL).displayname("§f> §eForge").lore("", "§7Améliorez la forge de votre équipe").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack traps = new ItemBuilder(Material.TRIPWIRE_HOOK).displayname("§f> §ePièges").lore("", "§7Achetez des pièges").flag(ItemFlag.HIDE_ATTRIBUTES).build();


        setItems(0,8, grayPanel);
        setItems(3,5, purplePanel);

        for(int i = 9; i < size; i+=9) {
            setItem(i, grayPanel);
        }

        setItem(0, enchants, event -> new Enchants((Player) event.getWhoClicked()).open((Player) event.getWhoClicked()));
        setItem(18, forge, event -> new Forge((Player) event.getWhoClicked()).open((Player) event.getWhoClicked()));
        setItem(36, traps, event -> new Traps((Player) event.getWhoClicked()).open((Player) event.getWhoClicked()));


    }

    public void setDefaultBuyableMenu() {
        ItemStack grayPanel = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 8)).displayname(" ").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack purplePanel = new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 2)).displayname(" ").flag(ItemFlag.HIDE_ATTRIBUTES).build();

        ItemStack blockCategory = new ItemBuilder(Material.STAINED_CLAY).displayname("§f> §eBlocks").lore("", "§7Accéder à la boutique des blocks").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack weaponCategory = new ItemBuilder(Material.IRON_SWORD).displayname("§f> §eArmes").lore("", "§7Accéder à la boutique des armes").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack toolCategory = new ItemBuilder(Material.IRON_PICKAXE).displayname("§f> §eOutils").lore("", "§7Accéder à la boutique des outils").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack armorCategory = new ItemBuilder(Material.IRON_CHESTPLATE).displayname("§f> §eArmures").lore("", "§7Accéder à la boutique des armures").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack potionCategory = new ItemBuilder(Material.POTION).displayname("§f> §ePotions").lore("", "§7Accéder à la boutique des potions").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack utilityCategory = new ItemBuilder(Material.TNT).displayname("§f> §eUtilitaires").lore("", "§7Accéder à la boutique des utilitaires").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        ItemStack quickBuyCategory = new ItemBuilder(Material.NETHER_STAR).displayname("§f> §eAchats Rapides").lore("", "§7Accéder à la boutique des achats rapides").flag(ItemFlag.HIDE_ATTRIBUTES).build();

        setItems(1, 7, grayPanel);
        setItem(3, purplePanel);
        setItem(5, purplePanel);

        setItem(0, blockCategory, event -> {
            new Blocks((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        });

        setItem(9, weaponCategory, event -> {
            new Weapons((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        });

        setItem(18, toolCategory, event -> {
            new Tools((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        });

        setItem(27, armorCategory, event -> {
            new Armors((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        });

        setItem(36, potionCategory, event -> {
            new Potions((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        });

        setItem(45, utilityCategory, event -> {
            new Utilities((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        });


        setItem(8, quickBuyCategory, event -> {
            new QuickBuy((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        });


    }

    public void updateItem(int slot, ItemStack itemStack) {
        inventory.setItem(slot, itemStack);
    }


    public String transcriptGetter(boolean var) {
        return var ? "§aPOSSEDE " : "";
    }


    public void setBuyableItem(int slot, String displayName, ItemStack reward, Material cost, int price) {
        setBuyableItem(slot, displayName, reward, cost, price, null);
    }

    public void setBuyableItem(int slot, String displayName, ItemStack reward, Material cost, int price, boolean isSilent) {
        setBuyableItem(slot, displayName, reward, cost, price);
        if(isSilent) {
            silentSlots.add(slot);
        }
    }


    public void setBuyableItem(int slot, String displayName, ItemStack reward, Material cost, int price, Consumer<InventoryClickEvent> consumer) {
        setItem(slot, new ItemBuilder(new ItemStack(reward.getType(), reward.getAmount(), reward.getDurability())).displayname("§f" + reward.getAmount() + " §e" + displayName).lore("§7Prix: " + transcript(cost, price)).build());
        itemHandlers.put(slot, event -> {
            if(transaction((Player) event.getWhoClicked(), cost, price)) {
              if(consumer != null) {
                  consumer.accept(event);
              }
                event.getWhoClicked().getInventory().addItem(new ItemBuilder(reward).unbreakable(true).build());
            }
        });
    }





    public boolean transaction(Player player, Material cost, int price) {
        ItemStack item = new ItemStack(cost);
        item.setAmount(price);
        if(player.getInventory().containsAtLeast(item, price)) {
            player.getInventory().removeItem(item);
            return true;
        }
        return false;
    }


    public void setUpgradableItem(Player player, int slot, ItemStack icon, Material cost, int price, Consumer<InventoryClickEvent> reward, boolean isSilent) {
        setUpgradableItem(player, slot, icon, cost, price, reward);
        if(isSilent) {
            silentSlots.add(slot);
        }
    }


    public void setUpgradableItem(Player player, int slot, ItemStack icon, Material cost, int price, Consumer<InventoryClickEvent> reward) {
        setItem(slot, new ItemBuilder(new ItemStack(icon.getType(), icon.getAmount(), icon.getDurability())).displayname("§e" + icon.getItemMeta().getDisplayName()).lore("§7Prix: " + transcript(cost, price)).build());
        itemHandlers.put(slot, event -> {

                reward.accept(event);

        });
    }


    public void useRandom(Player player) {


        ItemStack random = new ItemBuilder(Material.REDSTONE).displayname("§cAléatoire").lore("", "§7Utilisez §f5 Nether Stars ","§7pour obtenir un item aléatoire").flag(ItemFlag.HIDE_ATTRIBUTES).build();
        setItem(35, random, event -> {
            AtomicInteger netherStars = new AtomicInteger();
            Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).filter(itemStack -> itemStack.getType() == Material.NETHER_STAR).forEach(itemStack -> {
                netherStars.set(itemStack.getAmount());
            });
            if(netherStars.intValue() >= 5) {
                player.getInventory().removeItem(new ItemStack(Material.NETHER_STAR, 5));
                player.getInventory().addItem(new ItemBuilder(getRandomEmeraldItem().getReward()).unbreakable(true).build());
                player.updateInventory();
                open(player);
            }
        });
    }






    public String transcript(Material cost, int price) {
        switch (cost) {
            case EMERALD: return  "§2" + price + " émeraude(s)";
            case DIAMOND: return  "§b" + price + " diamant(s)";
            case GOLD_INGOT: return  "§6" + price + " or(s)";
            case IRON_INGOT: return "§f" + price + " fer(s)";
            default: return "null";
        }
    }


    private EShop getRandomEmeraldItem() {
        final List<EShop> emeradItems = new ArrayList<>();

        for(EShop eShop : EShop.values()) {
            if (eShop.getCost().equals(Material.EMERALD)
            && !Objects.equals(eShop, EShop.DIAMOND_AXE)
            && !Objects.equals(eShop, EShop.DIAMOND_PICKAXE)
            && !Objects.equals(eShop, EShop.DIAMOND_BOOTS)
            && !Objects.equals(eShop, EShop.DIAMOND_CHESTPLATE)
            && !Objects.equals(eShop, EShop.DIAMOND_ICON)) {
                emeradItems.add(eShop);
            }
        }

        return emeradItems.get(new Random().nextInt(emeradItems.size()));
    }



    public void open(Player player) {
        player.openInventory(inventory);
    }


    public void addCloseHandler(Consumer<InventoryCloseEvent> handler) {
        closeHandlers.add(handler);
    }





    public void setItem(int slot, ItemStack it, boolean isSilent) {
        setItem(slot, it);
        if(isSilent) {
            silentSlots.add(slot);
        }
    }


    public void setItem(int slot, ItemStack it) {
        setItem(slot, it, null);
    }


    public void setItem(int slot, ItemStack it, Consumer<InventoryClickEvent> handler, boolean isSilent) {
        setItem(slot, it, handler);
        if(isSilent) {
            silentSlots.add(slot);
        }
    }
    public void setItem(int slot, ItemStack it, Consumer<InventoryClickEvent> eventConsumer) {
        this.inventory.setItem(slot, it);
        if(eventConsumer != null) {
            itemHandlers.put(slot, eventConsumer);
        }
    }

    public void setItems(int slot1, int slot2, ItemStack it) {
        setItems(slot1, slot2, it, null);
    }

    public void setItems(int slot1, int slot2, ItemStack it, Consumer<InventoryClickEvent> eventConsumer) {
        for(int i = slot1; i <= slot2; i++) {
            setItem(i, it, eventConsumer);
        }
    }

    public void setSilent(int slot) {
        silentSlots.add(slot);
    }



    void handle(InventoryClickEvent event) {
        Consumer<InventoryClickEvent> consumer = itemHandlers.get(event.getRawSlot());

        if(consumer != null) {
            consumer.accept(event);
        }
    }




    void handleClose(InventoryCloseEvent event) {
        this.closeHandlers.forEach(handler -> handler.accept(event));
    }



    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }


        @EventHandler
        public void inventoryHandle(InventoryClickEvent event) {
            if(event.getClickedInventory() == null) return;
            if(!(event.getClickedInventory().getHolder() instanceof AbstractInventory)) return;
            if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                event.setCancelled(true);
            }
            Player player = (Player) event.getWhoClicked();

            event.setCancelled(true);
            AbstractInventory inventory = (AbstractInventory) event.getInventory().getHolder();
            inventory.handle(event);
            if(!inventory.silentSlots.contains(event.getRawSlot()) && event.getCurrentItem().getType() != null && event.getCurrentItem().getType() != Material.AIR) {
                player.playSound(player.getLocation(), Sound.CLICK, 1, 1);
            }
        }

        @EventHandler
        public void inventoryClose(InventoryCloseEvent event) {
            if(event.getInventory() == null) return;
            if(!(event.getInventory().getHolder() instanceof AbstractInventory)) return;
            Player player = (Player) event.getPlayer();
            AbstractInventory inventory = (AbstractInventory) event.getInventory().getHolder();
            inventory.handleClose(event);
        }



}

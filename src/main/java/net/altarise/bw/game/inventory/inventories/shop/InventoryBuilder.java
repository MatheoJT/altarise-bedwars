package net.altarise.bw.game.inventory.inventories.shop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InventoryBuilder {


    public enum InventoryType {BLOCK, WEAPON, ARMOR, TOOL, UTILITY, POTIONS}

    @SuppressWarnings("unused")
    public static String slotsToString(HashMap<Integer, String> map) {
        ArrayList<String> list = new ArrayList<>();
        for(Integer i : map.keySet()) {
            list.add(i + ":" + map.get(i));
        }
        return String.join(",", list);
    }

    public static HashMap<Integer, String> stringToSlots(String string) {
        return Arrays.stream(string.split(","))
                .map(s -> s.split(":"))
                .collect(HashMap::new, (m, a) -> m.put(Integer.parseInt(a[0]), a[1]), HashMap::putAll);

    }






}

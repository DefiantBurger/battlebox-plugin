package tech.defiantburger.battlebox;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

public class BattleBoxItem {

    private int count;
    private String material;

    public BattleBoxItem(String material, int count) {
        this.material = material;
        this.count = count;
    }

    public ItemStack getTeamItem(String color) {

        String edit = material.replace("{team_color}", color);
        Material mat = Material.matchMaterial(edit);
        if (Objects.isNull(mat)) return null;

        ItemStack item = new ItemStack(mat, count);
        ItemMeta meta = item.getItemMeta();
        if (Objects.isNull(meta)) return null;
        meta.setUnbreakable(true);
        item.setItemMeta(meta);

        return item;
    }

}

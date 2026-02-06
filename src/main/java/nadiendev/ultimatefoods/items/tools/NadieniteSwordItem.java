package nadiendev.ultimatefoods.items.tools;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * Espada de Nadienite
 * - 105 de daño total
 * - Sin cooldown al atacar
 * - Irrompible
 */
public class NadieniteSwordItem extends SwordItem {
    
    public NadieniteSwordItem(Tier tier, Properties properties) {
        // attackDamage: 104 (se suma +1 del juego base = 105 total)
        // attackSpeed: 100.0F (sin cooldown, ataque instantáneo)
        super(tier, properties.attributes(SwordItem.createAttributes(tier, 104, 100.0F)));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        // Hace que el item sea irrompible
        return false;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        // Sin daño = irrompible
        return 0;
    }
}
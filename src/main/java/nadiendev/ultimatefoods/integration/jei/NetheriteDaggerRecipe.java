package nadiendev.ultimatefoods.integration.jei;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import nadiendev.ultimatefoods.items.ItemsAdds;

/**
 * Representa la "receta" de la Netherite Dagger para mostrar en JEI.
 * Input:  Espada de Netherite + Bloque de Netherite (clic derecho)
 * Output: Netherite Dagger.
 */
public class NetheriteDaggerRecipe {

    public static final NetheriteDaggerRecipe INSTANCE = new NetheriteDaggerRecipe();

    public ItemStack getSword() {
        return new ItemStack(Items.NETHERITE_SWORD);
    }

    public ItemStack getBlock() {
        return new ItemStack(Items.NETHERITE_BLOCK);
    }

    public ItemStack getOutput() {
        return new ItemStack(ItemsAdds.NETHERITE_DAGGER.get());
    }
}
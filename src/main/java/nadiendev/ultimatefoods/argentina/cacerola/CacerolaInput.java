package nadiendev.ultimatefoods.argentina.cacerola;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

/** Lo que hay adentro de la cacerola (solo las ranuras ocupadas), para buscar receta. */
public record CacerolaInput(List<ItemStack> items) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    @Override
    public int size() {
        return items.size();
    }
}

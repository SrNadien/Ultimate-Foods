package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.registry.ItemsAdds;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {

    public ModModelProvider(PackOutput output) {
        super(output, UltimateFoodsCore.MOD_ID);
    }

    private static List<Item> generatedItems() {
        return List.of(
                ItemsAdds.RAW_HAMBURGUER_MEAT.get(),
                ItemsAdds.COOKED_HAMBURGUER_MEAT.get(),
                ItemsAdds.BURGER.get()
        );
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(ItemsAdds.RAW_HAMBURGUER_MEAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsAdds.COOKED_HAMBURGUER_MEAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemsAdds.BURGER.get(), ModelTemplates.FLAT_ITEM);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return generatedItems().stream().map(Item::builtInRegistryHolder);
    }
}

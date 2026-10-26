package nadiendev.ultimatefoods.fisura.homenaje;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Homenaje a Lionel Andres Messi: La Copa, un trofeo decorativo, y el easter egg de
 * ponerle su nombre a un aldeano con una etiqueta (ver {@link MessiEasterEgg}).
 * El modelo, el blockstate, la receta y el loot de La Copa van como JSON en los recursos.
 */
public final class HomenajeModule {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(UltimateFoodsCore.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);

    public static final DeferredBlock<LaCopaBlock> LA_COPA = BLOCKS.registerBlock("la_copa", LaCopaBlock::new,
            properties -> properties
                    .mapColor(MapColor.GOLD)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .lightLevel(state -> 3));

    // el nombre sale de la clave del bloque (block.ultimatefoods.la_copa), como el BlockItem de antes
    public static final DeferredItem<Item> LA_COPA_ITEM = ITEMS.registerItem("la_copa",
            properties -> new LaCopaItem(LA_COPA.get(), properties),
            properties -> properties.stacksTo(1).rarity(Rarity.EPIC).useBlockDescriptionPrefix());

    private HomenajeModule() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }

    public static void displayItems(CreativeModeTab.Output output) {
        output.accept(LA_COPA_ITEM.get());
    }
}

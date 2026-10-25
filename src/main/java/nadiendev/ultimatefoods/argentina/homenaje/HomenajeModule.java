package nadiendev.ultimatefoods.argentina.homenaje;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.datagen.providers.NombreProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.concurrent.CompletableFuture;

/**
 * Homenaje a Lionel Andres Messi: La Copa, un trofeo decorativo, y el easter egg de
 * ponerle su nombre a un aldeano con una etiqueta (ver {@link MessiEasterEgg}).
 */
public final class HomenajeModule {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(UltimateFoodsCore.MOD_ID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);

    public static final DeferredBlock<LaCopaBlock> LA_COPA = BLOCKS.registerBlock("la_copa", LaCopaBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.GOLD)
                    .strength(2.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .noOcclusion()
                    .lightLevel(state -> 3));

    public static final DeferredItem<Item> LA_COPA_ITEM = ITEMS.register("la_copa",
            () -> new LaCopaItem(LA_COPA.get(), new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    private HomenajeModule() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
    }

    public static void displayItems(CreativeModeTab.Output output) {
        output.accept(LA_COPA_ITEM.get());
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(),
                new NombreProvider("Homenaje: estados de bloque", new HomenajeDatos.Estados(output, helper)));
        generator.addProvider(event.includeClient(),
                new NombreProvider("Homenaje: modelos de item", new HomenajeDatos.ModelosItem(output, helper)));
        generator.addProvider(event.includeServer(),
                new NombreProvider("Homenaje: loot tables", HomenajeDatos.loot(output, registries)));
        generator.addProvider(event.includeServer(),
                new NombreProvider("Homenaje: recetas", new HomenajeDatos.Recetas(output, registries)));
    }
}

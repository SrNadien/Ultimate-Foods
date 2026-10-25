package nadiendev.ultimatefoods.argentina.mate;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.aldeano.MateVillagers;
import nadiendev.ultimatefoods.argentina.mate.datagen.MateBlockLoot;
import nadiendev.ultimatefoods.argentina.mate.datagen.MateBlockStates;
import nadiendev.ultimatefoods.argentina.mate.datagen.MateItemModels;
import nadiendev.ultimatefoods.argentina.mate.datagen.MateRecipes;
import nadiendev.ultimatefoods.argentina.mate.datagen.MateTags;
import nadiendev.ultimatefoods.argentina.mate.datagen.NamedProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Modulo del mate: arbol de yerba, yerbera, pava, mate, bombilla, el ritual de
 * cebar, los cuatro efectos y el aldeano. Dueño: agente A (paquetes
 * argentina.mate y argentina.aldeano).
 */
public final class MateModule {

    private MateModule() {
    }

    public static void register(IEventBus modEventBus) {
        MateComponents.COMPONENTS.register(modEventBus);
        MateBlocks.BLOCKS.register(modEventBus);
        MateItems.ITEMS.register(modEventBus);
        MateBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        MateEffects.EFFECTS.register(modEventBus);
        MateVillagers.register(modEventBus);

        modEventBus.addListener(MateModule::commonSetup);
    }

    private static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ComposterBlock.COMPOSTABLES.put(MateItems.YERBA_MATE_LEAVES.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(MateItems.YERBA_MATE_SAPLING.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(MateItems.YERBA_MATE_LEAF.get(), 0.3F);
            ComposterBlock.COMPOSTABLES.put(MateItems.YERBA_MATE_LEAF_DRIED.get(), 0.2F);
            ComposterBlock.COMPOSTABLES.put(MateItems.YUYOS.get(), 0.3F);
        });
    }

    /** Orden de la pestaña: yerba y arbol, yerbera, mate y pava en sus estados, bombilla y yuyos. */
    public static void displayItems(CreativeModeTab.Output output) {
        output.accept(MateItems.YERBA_MATE_LOG.get());
        output.accept(MateItems.YERBA_MATE_LEAVES.get());
        output.accept(MateItems.YERBA_MATE_SAPLING.get());
        output.accept(MateItems.YERBA_MATE_LEAF.get());
        output.accept(MateItems.YERBA_MATE_LEAF_DRIED.get());
        output.accept(MateItems.YERBA_MATE.get());
        output.accept(MateItems.YERBA_VERDEFLOR.get());

        output.accept(MateItems.YERBERA.get());

        output.accept(MateItems.MATE.get());
        output.accept(MateItems.MATE_CON_YERBA.get());
        output.accept(MateItems.MATE_AMARGO.get());
        output.accept(MateItems.MATE_DULCE.get());
        output.accept(MateItems.MATE_DE_LECHE.get());
        output.accept(MateItems.MATE_CON_YUYOS.get());
        output.accept(MateItems.MATE_LAVADO.get());

        output.accept(MateItems.PAVA.get());
        output.accept(MateItems.PAVA_CON_AGUA.get());
        output.accept(MateItems.PAVA_CALIENTE.get());

        output.accept(MateItems.BOMBILLA.get());
        output.accept(MateItems.YUYOS.get());
    }

    public static ItemStack tabIcon() {
        return new ItemStack(MateItems.MATE.get());
    }

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // Cada provider va envuelto con un nombre propio ("Mate: ...") para no chocar con
        // los providers de nombre fijo de otros modulos.
        generator.addProvider(event.includeServer(), new NamedProvider("Mate: Recetas", new MateRecipes(output, lookup)));
        generator.addProvider(event.includeServer(), new NamedProvider("Mate: Loot de bloques",
                new LootTableProvider(output, Set.of(), List.of(
                        new LootTableProvider.SubProviderEntry(MateBlockLoot::new, LootContextParamSets.BLOCK)), lookup)));

        MateTags.Bloques bloques = new MateTags.Bloques(output, lookup, helper);
        generator.addProvider(event.includeServer(), new NamedProvider("Mate: Tags de bloques", bloques));
        generator.addProvider(event.includeServer(), new NamedProvider("Mate: Tags de items",
                new MateTags.Items(output, lookup, bloques.contentsGetter(), helper)));

        generator.addProvider(event.includeServer(), new NamedProvider("Mate: Mundo (arbol de yerba)",
                new DatapackBuiltinEntriesProvider(output, lookup, new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, MateWorldgen::bootstrapConfigured)
                        .add(Registries.PLACED_FEATURE, MateWorldgen::bootstrapPlaced)
                        .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, MateWorldgen::bootstrapModifiers),
                        Set.of(UltimateFoodsCore.MOD_ID))));

        generator.addProvider(event.includeClient(), new NamedProvider("Mate: Estados de bloque", new MateBlockStates(output, helper)));
        generator.addProvider(event.includeClient(), new NamedProvider("Mate: Modelos de item", new MateItemModels(output, helper)));
    }
}

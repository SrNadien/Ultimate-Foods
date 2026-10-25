package nadiendev.ultimatefoods.argentina.comida.colocada;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Datagen del bloque {@code comida_colocada}: blockstate (una variante por comida x orientacion) y
 * loot table (suelta el item de su comida). Los modelos del bloque ({@code models/block/argentina/comida/*})
 * los entrega el agente de modelos, asi que aca solo se apunta a ellos sin validarlos.
 */
public final class ColocadaData {

    private ColocadaData() {
    }

    public static class Loot extends LootTableProvider {

        public Loot(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, Set.of(), List.of(new SubProviderEntry(BlockLoot::new, LootContextParamSets.BLOCK)), registries);
        }
    }

    private static class BlockLoot extends BlockLootSubProvider {

        BlockLoot(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            Block block = ColocadaRegistry.COMIDA_COLOCADA.get();
            LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F));
            for (Comida comida : Comida.values()) {
                pool.add(LootItem.lootTableItem(comida.item()).when(
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(ComidaColocadaBlock.COMIDA, comida)
                                        // Una comida empezada no suelta el item entero.
                                        .hasProperty(ComidaColocadaBlock.BOCADOS, 0))));
            }
            add(block, LootTable.lootTable().withPool(pool));
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return List.of(ColocadaRegistry.COMIDA_COLOCADA.get());
        }
    }

    public static class States extends BlockStateProvider {

        public States(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, UltimateFoodsCore.MOD_ID, existingFileHelper);
        }

        @Override
        public String getName() {
            return "Comida colocada: Block States";
        }

        @Override
        protected void registerStatesAndModels() {
            getVariantBuilder(ColocadaRegistry.COMIDA_COLOCADA.get()).forAllStates(state -> {
                Comida comida = state.getValue(ComidaColocadaBlock.COMIDA);
                return ConfiguredModel.builder()
                        .modelFile(new ModelFile.UncheckedModelFile(comida.blockModel(state.getValue(ComidaColocadaBlock.BOCADOS))))
                        .rotationY(Comida.rotationY(state.getValue(ComidaColocadaBlock.FACING)))
                        .build();
            });
        }
    }
}

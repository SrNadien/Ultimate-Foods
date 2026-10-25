package nadiendev.ultimatefoods.argentina.mate.datagen;

import nadiendev.ultimatefoods.argentina.mate.MateBlocks;
import nadiendev.ultimatefoods.argentina.mate.MateComponents;
import nadiendev.ultimatefoods.argentina.mate.MateItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.Set;

/** Loot de los bloques del mate. */
public class MateBlockLoot extends BlockLootSubProvider {

    /** Probabilidad de que una hoja suelte una hoja de yerba (sin y con Fortuna I, II, III). */
    private static final float[] CHANCE_HOJA = {0.25F, 0.30F, 0.40F, 0.50F};
    /** Probabilidad de que una hoja suelte el brote. */
    private static final float[] CHANCE_BROTE = {0.04F, 0.05F, 0.067F, 0.08F};

    public MateBlockLoot(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return MateBlocks.todos();
    }

    @Override
    protected void generate() {
        dropSelf(MateBlocks.YERBA_MATE_LOG.get());
        dropSelf(MateBlocks.YERBA_MATE_SAPLING.get());

        // Hojas: con tijeras o toque de seda, el bloque; sino, a veces el brote, palitos
        // y (lo que importa) la hoja de yerba.
        Block hojas = MateBlocks.YERBA_MATE_LEAVES.get();
        var fortuna = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        add(hojas, createLeavesDrops(hojas, MateBlocks.YERBA_MATE_SAPLING.get(), CHANCE_BROTE)
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0F))
                        .when(HAS_SHEARS.or(hasSilkTouch()).invert())
                        .add(applyExplosionCondition(hojas, LootItem.lootTableItem(MateItems.YERBA_MATE_LEAF.get()))
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(fortuna, CHANCE_HOJA)))));

        // La yerbera y la azucarera conservan lo que tengan adentro.
        for (Block bolsa : new Block[]{MateBlocks.YERBERA.get(), MateBlocks.AZUCARERA.get()}) {
            add(bolsa, LootTable.lootTable().withPool(applyExplosionCondition(bolsa, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1.0F))
                    .add(LootItem.lootTableItem(bolsa)
                            .apply(CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                    .include(MateComponents.PORCIONES.get()))))));
        }
    }
}

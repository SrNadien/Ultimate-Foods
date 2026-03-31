package nadiendev.ultimatefoods.datagen.loot;

import nadiendev.ultimatefoods.items.ItemsAdds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

/**
 * Datagen de loot tables para cofres.
 */
public class UltimateFoodsChestLootTables implements LootTableSubProvider {

    private final HolderLookup.Provider registries;

    public UltimateFoodsChestLootTables(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {

        // Tabla de bonus de nadienite para mazmorras/dungeons
        // Esta tabla es invocada desde el GLM (nadienite_chest_loot)
        // o puede ser referenciada con LootTableReference desde otra tabla.
        output.accept(
            ResourceKey.create(
                net.minecraft.core.registries.Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath("ultimatefoods", "chests/nadienite_dungeon_bonus")
            ),
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    // 15% de chance de aparecer
                    .when(LootItemRandomChanceCondition.randomChance(0.15f))
                    .add(LootItem.lootTableItem(ItemsAdds.NADIENITE_INGOT.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                    )
                )
        );

        // Tabla de bonus de nadienite para bastiones/nether
        output.accept(
            ResourceKey.create(
                net.minecraft.core.registries.Registries.LOOT_TABLE,
                ResourceLocation.fromNamespaceAndPath("ultimatefoods", "chests/nadienite_nether_bonus")
            ),
            LootTable.lootTable()
                .withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .when(LootItemRandomChanceCondition.randomChance(0.15f))
                    .add(LootItem.lootTableItem(ItemsAdds.NADIENITE_INGOT.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                    )
                )
        );
    }
}
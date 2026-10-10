package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.registry.ItemsAdds;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

public class UltimateFoodsMobLootTables extends EntityLootSubProvider {

    public UltimateFoodsMobLootTables(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        add(EntityType.SNIFFER, burgerTable(0.01f));
        add(EntityType.ARMADILLO, burgerTable(0.02f));
        add(EntityType.VILLAGER, burgerTable(0.20f));

        add(EntityType.WARDEN, guaranteedTable(ItemsAdds.BURGER.get(), 6));
    }

    private LootTable.Builder burgerTable(float chance) {
        return LootTable.lootTable().withPool(chancePool(ItemsAdds.BURGER.get(), 6, chance));
    }

    private LootTable.Builder guaranteedTable(ItemLike item, int count) {
        return LootTable.lootTable().withPool(guaranteedPool(item, count));
    }

    private LootPool.Builder chancePool(ItemLike item, int count, float chance) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                registries, UniformGenerator.between(0, 1))))
                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                        registries, chance, 0.01f));
    }

    private LootPool.Builder guaranteedPool(ItemLike item, int count) {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(count)))
                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                registries, UniformGenerator.between(0, 1))));
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return Stream.of(
                EntityType.SNIFFER,
                EntityType.ARMADILLO,
                EntityType.VILLAGER,
                EntityType.WARDEN
        );
    }
}

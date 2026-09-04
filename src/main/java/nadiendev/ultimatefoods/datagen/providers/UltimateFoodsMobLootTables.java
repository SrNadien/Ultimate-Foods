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

        add(EntityType.ZOMBIE, heartTable(ItemsAdds.CORAZON_DE_LOS_CAIDOS.get(), 0.03f));
        add(EntityType.SKELETON, heartTable(ItemsAdds.CORAZON_DE_LOS_CAIDOS.get(), 0.03f));
        add(EntityType.WITHER_SKELETON, heartTable(ItemsAdds.CORAZON_DE_LOS_CAIDOS.get(), 0.25f));

        add(EntityType.PHANTOM, heartTable(ItemsAdds.CORAZON_DE_LOS_FANTASMAS.get(), 0.15f));
        add(EntityType.GHAST, heartTable(ItemsAdds.CORAZON_DE_LOS_FANTASMAS.get(), 0.25f));
        add(EntityType.VEX, heartTable(ItemsAdds.CORAZON_DE_LOS_FANTASMAS.get(), 0.20f));

        add(EntityType.ELDER_GUARDIAN, heartTable(ItemsAdds.CORAZON_DE_LA_ELITE.get(), 0.50f));
        add(EntityType.WITHER, guaranteedTable(ItemsAdds.CORAZON_DE_LA_ELITE.get(), 1));
        add(EntityType.ENDER_DRAGON, guaranteedTable(ItemsAdds.CORAZON_DE_LA_ELITE.get(), 2));

        add(EntityType.WARDEN, LootTable.lootTable()
                .withPool(guaranteedPool(ItemsAdds.BURGER.get(), 6))
                .withPool(guaranteedPool(ItemsAdds.CORAZON_DE_LA_ELITE.get(), 1))
        );
    }

    private LootTable.Builder burgerTable(float chance) {
        return LootTable.lootTable().withPool(chancePool(ItemsAdds.BURGER.get(), 6, chance));
    }

    private LootTable.Builder heartTable(ItemLike heart, float chance) {
        return LootTable.lootTable().withPool(chancePool(heart, 1, chance));
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
                EntityType.WARDEN,
                EntityType.ZOMBIE,
                EntityType.SKELETON,
                EntityType.WITHER_SKELETON,
                EntityType.PHANTOM,
                EntityType.GHAST,
                EntityType.VEX,
                EntityType.ELDER_GUARDIAN,
                EntityType.WITHER,
                EntityType.ENDER_DRAGON
        );
    }
}

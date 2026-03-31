package nadiendev.ultimatefoods.datagen.loot;

import nadiendev.ultimatefoods.items.ItemsAdds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

/**
 * Datagen de loot tables para mobs que dropean Hamburguesa.
 *
 * Chances:
 *   Sniffer   → 1%  (+ looting)
 *   Armadillo → 2%  (+ looting)
 *   Villager  → 20% (+ looting)
 *   Warden    → 100% garantizado, 6 unidades (+ looting)
 */
public class UltimateFoodsMobLootTables extends EntityLootSubProvider {

    public UltimateFoodsMobLootTables(HolderLookup.Provider registries) {
        super(FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {

        // ── SNIFFER — 1% + looting ──
        add(EntityType.SNIFFER, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemsAdds.BURGER.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                        registries, UniformGenerator.between(0, 1))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                                registries, 0.01f, 0.01f))
                )
        );

        // ── ARMADILLO — 2% + looting ──
        add(EntityType.ARMADILLO, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemsAdds.BURGER.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                        registries, UniformGenerator.between(0, 1))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                                registries, 0.02f, 0.01f))
                )
        );

        // ── VILLAGER — 20% + looting ──
        add(EntityType.VILLAGER, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemsAdds.BURGER.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                        registries, UniformGenerator.between(0, 1))))
                        .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(
                                registries, 0.20f, 0.01f))
                )
        );

        // ── WARDEN — 100% garantizado, siempre 6 ──
        add(EntityType.WARDEN, LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemsAdds.BURGER.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(6)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(
                                        registries, UniformGenerator.between(0, 1))))
                )
        );
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
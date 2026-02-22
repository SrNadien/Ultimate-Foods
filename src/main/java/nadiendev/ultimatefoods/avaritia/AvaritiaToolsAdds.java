package nadiendev.ultimatefoods.avaritia;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.avaritia.InfinityTier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.Unbreakable;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registro de herramientas de Avaritia (Infinity tier)
 * By NadienDev
 */
public class AvaritiaToolsAdds {
    public static final DeferredRegister<Item> AVARITIA_ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, UltimateFoodsCore.MOD_ID);

    // ========== INFINITY-1 TIER TOOLS ==========

    /**
     * INFINITY-1 SWORD
     * - 60 de daño
     * - Durabilidad infinita
     */
    public static final DeferredHolder<Item, Item> INFINITY_1_SWORD = AVARITIA_ITEMS.register(
            "infinity_1_sword",
            () -> new Infinity1SwordItem(
                    InfinityTier.INFINITY,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    /**
     * INFINITY-1 PICKAXE
     * - Multitool (pico, hacha, pala)
     * - Cambio entre Fortune III y Silk Touch
     * - Durabilidad infinita
     */
    public static final DeferredHolder<Item, Item> INFINITY_1_PICKAXE = AVARITIA_ITEMS.register(
            "infinity_1_pickaxe",
            () -> new Infinity1PickaxeItem(
                    InfinityTier.INFINITY,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    /**
     * INFINITY-1 HOE
     * - Compatible con Farming Stations
     * - Durabilidad infinita
     */
    public static final DeferredHolder<Item, Item> INFINITY_1_HOE = AVARITIA_ITEMS.register(
            "infinity_1_hoe",
            () -> new Infinity1HoeItem(
                    InfinityTier.INFINITY,
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    // ========== INFINITY TIER TOOLS (BALANCED) ==========

    /**
     * INFINITY SWORD (BALANCED)
     * - 400 de daño
     * - Durabilidad infinita
     * - No hace /kill instant kill
     */
    public static final DeferredHolder<Item, Item> INFINITY_SWORD_BALANCED = AVARITIA_ITEMS.register(
            "infinity_sword_balanced",
            () -> new InfinitySwordBalancedItem(
                    InfinityTier.INFINITY_BALANCED,  // Usa el tier con 400 de daño
                    new Item.Properties()
                            .stacksTo(1)
                            .fireResistant()
                            .rarity(Rarity.EPIC)
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
            )
    );

    /**
     * Registra todos los items de Avaritia en el event bus
     */
    public static void register(IEventBus eventBus) {
        AVARITIA_ITEMS.register(eventBus);
    }
}
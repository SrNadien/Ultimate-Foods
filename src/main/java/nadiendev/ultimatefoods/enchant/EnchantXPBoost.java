package nadiendev.ultimatefoods.enchant;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * XPBoost enchantment ported to NeoForge 1.21.1.
 *
 * IMPORTANTE: En 1.21.1 los enchantments son data-driven (JSON).
 * Ver EnchantXPBoost_data.json para el JSON correspondiente.
 */
public class EnchantXPBoost {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(Registries.ENCHANTMENT, UltimateFoodsCore.MOD_ID);

    /**
     * ResourceKey para referencias en código (efectos, predicados, loot tables, etc.)
     */
    public static final ResourceKey<Enchantment> XPBOOST =
            ResourceKey.create(Registries.ENCHANTMENT,
                    ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "xpboost"));

    public static void register(IEventBus modEventBus) {
        ENCHANTMENTS.register(modEventBus);
    }
}
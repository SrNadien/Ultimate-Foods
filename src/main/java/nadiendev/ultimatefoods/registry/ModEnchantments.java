package nadiendev.ultimatefoods.registry;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> AUTOSMELT = key("autosmelt");
    public static final ResourceKey<Enchantment> XPBOOST   = key("xpboost");

    private static ResourceKey<Enchantment> key(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT,
                ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, name));
    }
}

package nadiendev.ultimatefoods.items.armor;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.items.ItemsAdds;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

/**
 * Material de armadura Nadienite - Extremadamente poderoso
 * Mejor que Netherite en todos los aspectos
 */
public class NadieniteArmorMaterial {
    
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = 
        DeferredRegister.create(Registries.ARMOR_MATERIAL, UltimateFoodsCore.MOD_ID);

    public static final Holder<ArmorMaterial> NADIENITE = ARMOR_MATERIALS.register("nadienite", () -> new ArmorMaterial(
            // Defensa por pieza (Helmet, Chestplate, Leggings, Boots)
            // Para comparación: Netherite = {3, 8, 6, 3} = 20 total
            // Nadienite = {5, 10, 8, 5} = 28 total (mucho más fuerte)
            Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                map.put(ArmorItem.Type.HELMET, 5);      // Netherite = 3
                map.put(ArmorItem.Type.CHESTPLATE, 10);  // Netherite = 8
                map.put(ArmorItem.Type.LEGGINGS, 8);     // Netherite = 6
                map.put(ArmorItem.Type.BOOTS, 5);        // Netherite = 3
            }),
            // Encantabilidad (mayor = mejores encantamientos)
            // Netherite = 15, Nadienite = 25 (mucho mejor para encantar)
            25,
            // Sonido al equipar
            SoundEvents.ARMOR_EQUIP_NETHERITE,
            // Ingrediente de reparación
            () -> Ingredient.of(ItemsAdds.NADIENITE_INGOT.get()),
            // Capas de armadura (para texturas)
            // Las texturas deben estar en:
            // assets/ultimatefoods/textures/models/armor/nadienite_layer_1.png (casco, pechera, botas)
            // assets/ultimatefoods/textures/models/armor/nadienite_layer_2.png (pantalones)
            List.of(
                new ArmorMaterial.Layer(
                    ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "nadienite")
                )
            ),
            // Dureza (resistencia al knockback)
            // Netherite = 4.0F, Nadienite = 6.0F (más resistencia)
            6.0F,
            // Resistencia al knockback
            // Netherite = 0.1F, Nadienite = 0.15F (15% de resistencia)
            0.15F
    ));
}
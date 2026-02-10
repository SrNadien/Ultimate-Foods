package nadiendev.ultimatefoods.items.tools;

import nadiendev.ultimatefoods.items.ItemsAdds;
import nadiendev.ultimatefoods.tags.ModBlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * Tool Tiers para Ultimate Foods
 */
public class NadieniteToolMaterial {
    
    /**
     * Tier de Nadienite
     * - Nivel de minado: Puede minar bloques que requieren netherite tool
     * - Durabilidad: 9999 (prácticamente infinita con Unbreaking en el item)
     * - Velocidad: 20.0F
     * - Daño: 10.0F
     * - Encantabilidad: 25
     */
    public static final Tier NADIENITE = new SimpleTier(
            ModBlockTags.Blocks.INCORRECT_FOR_NADIENITE_TOOL,       // Tag custom para bloques que necesitan nadienite
            9999,                                            // Durabilidad alta
            20.0F,                                           // Velocidad de minado
            10.0F,                                           // Bonus de daño
            25,                                              // Encantabilidad
            () -> Ingredient.of(ItemsAdds.NADIENITE_INGOT.get())
    );
}
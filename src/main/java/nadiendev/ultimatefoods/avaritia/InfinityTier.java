package nadiendev.ultimatefoods.avaritia;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

/**
 * Material de herramientas Infinity
 * By NadienDev
 */
public class InfinityTier {
    
    /**
     * Tier para Infinity-1 (espada, pico, azada)
     * - Daño: 60
     * - Velocidad: 100
     */
    public static final Tier INFINITY = new SimpleTier(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,  // Puede minar todo
        10000,                                     // Usos (irrompible de todas formas)
        100.0F,                                    // Velocidad extremadamente rápida
        60.0F,                                     // Daño base
        30,                                        // Encantabilidad alta
        () -> Ingredient.EMPTY                     // No se puede reparar
    );
    
    /**
     * Tier para Infinity Sword Balanced
     * - Daño: 400
     * - Velocidad: 100
     */
    public static final Tier INFINITY_BALANCED = new SimpleTier(
        BlockTags.INCORRECT_FOR_NETHERITE_TOOL,
        10000,
        100.0F,
        400.0F,                                    // Daño alto para versión balanceada
        30,
        () -> Ingredient.EMPTY
    );
}
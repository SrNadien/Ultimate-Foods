package nadiendev.ultimatefoods.items.armor;

import nadiendev.ultimatefoods.effects.EffectsAdds;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Clase base para las piezas de armadura Nadienite
 * Cada pieza otorga efectos específicos cuando se lleva puesta
 * El set completo otorga efectos adicionales de bonificación
 */
public class NadieniteArmorItem extends ArmorItem {
    
    public NadieniteArmorItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide() && entity instanceof Player player) {
            // Aplicar efectos según la pieza equipada
            applyArmorEffects(player);
            
            // Verificar si tiene el set completo
            if (hasFullSet(player)) {
                applyFullSetBonus(player);
            }
        }
        
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }

    /**
     * Aplica los efectos específicos de cada pieza de armadura
     */
    private void applyArmorEffects(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack boots = player.getInventory().getArmor(0);

        // CASCO - Visión Nocturna y Respiración Acuática 4
        if (helmet.getItem() instanceof NadieniteArmorItem) {
            player.addEffect(new MobEffectInstance(
                MobEffects.NIGHT_VISION,
                220, // ~11 segundos
                0,   // Nivel 1 (la visión nocturna no tiene niveles útiles)
                false,
                false,
                true
            ));
            player.addEffect(new MobEffectInstance(
                MobEffects.WATER_BREATHING,
                220,
                3,   // Nivel 4 (índice 3)
                false,
                false,
                true
            ));
        }

        // PECHERA - Vuelo y Regeneración 6
        if (chestplate.getItem() instanceof NadieniteArmorItem) {
            player.addEffect(new MobEffectInstance(
                EffectsAdds.FLYING, 
                60,  // 3 segundos 
                0,   // Nivel 1
                false,
                false,
                true
            ));
            player.addEffect(new MobEffectInstance(
                MobEffects.REGENERATION,
                220,
                5,   // Nivel 6 (índice 5)
                false,
                false,
                true
            ));
        }

        // PANTALONES - Invisibilidad y Health Boost 6
        if (leggings.getItem() instanceof NadieniteArmorItem) {
            player.addEffect(new MobEffectInstance(
                MobEffects.INVISIBILITY,
                220,
                0,   // La invisibilidad no tiene niveles
                false,
                false,
                true
            ));
            player.addEffect(new MobEffectInstance(
                MobEffects.HEALTH_BOOST,
                220,
                5,   // Nivel 6 (índice 5) = +12 corazones
                false,
                false,
                true
            ));
        }

        // BOTAS - Velocidad y Super Salto 6
        if (boots.getItem() instanceof NadieniteArmorItem) {
            player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SPEED,
                220,
                5,   // Nivel 6 (índice 5)
                false,
                false,
                true
            ));
            player.addEffect(new MobEffectInstance(
                MobEffects.JUMP,
                220,
                5,   // Nivel 6 (índice 5) = saltos enormes
                false,
                false,
                true
            ));
        }
    }

    /**
     * Aplica los efectos de bonificación cuando se tiene el set completo
     */
    private void applyFullSetBonus(Player player) {
        // PRISA MINERA 6
        player.addEffect(new MobEffectInstance(
            MobEffects.DIG_SPEED,
            220,
            5,   // Nivel 6 (índice 5)
            false,
            false,
            true
        ));

        // FUERZA 6 (Strength)
        player.addEffect(new MobEffectInstance(
            MobEffects.DAMAGE_BOOST,
            220,
            5,   // Nivel 6 (índice 5) = +3 daño por golpe
            false,
            false,
            true
        ));

        // RESISTENCIA 6 (Resistance)
        player.addEffect(new MobEffectInstance(
            MobEffects.DAMAGE_RESISTANCE,
            220,
            5,   // Nivel 6 (índice 5) = -24% daño recibido
            false,
            false,
            true
        ));

        // HEALTH BOOST 11 ADICIONAL (se suma al del pantalón)
        player.addEffect(new MobEffectInstance(
            MobEffects.HEALTH_BOOST,
            220,
            10,  // Nivel 11 (índice 10) = +22 corazones adicionales
            false,
            false,
            true
        ));
    }

    /**
     * Verifica si el jugador tiene todas las piezas de armadura Nadienite equipadas
     */
    private boolean hasFullSet(Player player) {
        ItemStack helmet = player.getInventory().getArmor(3);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack boots = player.getInventory().getArmor(0);

        return helmet.getItem() instanceof NadieniteArmorItem &&
               chestplate.getItem() instanceof NadieniteArmorItem &&
               leggings.getItem() instanceof NadieniteArmorItem &&
               boots.getItem() instanceof NadieniteArmorItem;
    }
}
package nadiendev.ultimatefoods.items;

import com.mojang.logging.LogUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * CustomFoodItem
 * By NadienDev
 * Clase base para alimentos con sonido personalizado al consumir
 */
public class CustomFoodItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final Supplier<SoundEvent> customSound;

    public CustomFoodItem(FoodProperties foodProperties, Supplier<SoundEvent> sound) {
        super(new Item.Properties().food(foodProperties));
        this.customSound = sound;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // Reproducir sonido ANTES de llamar a super (porque super consume el item)
        if (customSound != null) {
            try {
                SoundEvent sound = customSound.get();
                
                // Reproducir en AMBOS lados (cliente y servidor)
                if (!level.isClientSide()) {
                    // Servidor: envía a todos los jugadores cercanos
                    level.playSound(
                        null, 
                        entity.getX(), 
                        entity.getY(), 
                        entity.getZ(), 
                        sound,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F
                    );
                    LOGGER.info("Sonido reproducido en servidor: {}", sound.getLocation());
                } else {
                    // Cliente: reproduce localmente
                    level.playLocalSound(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        sound,
                        SoundSource.PLAYERS,
                        1.0F,
                        1.0F,
                        false
                    );
                    LOGGER.info("Sonido reproducido en cliente: {}", sound.getLocation());
                }
            } catch (Exception e) {
                LOGGER.error("Error al reproducir sonido personalizado", e);
            }
        }
        
        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32; // Duración estándar de comer (1.6 segundos)
    }
}
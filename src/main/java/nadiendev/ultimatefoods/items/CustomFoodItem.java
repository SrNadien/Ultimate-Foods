package nadiendev.ultimatefoods.items;

import com.mojang.logging.LogUtils;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class CustomFoodItem extends Item {
    private static final Logger LOGGER = LogUtils.getLogger();
    protected final Supplier<SoundEvent> customSound;

    public CustomFoodItem(Item.Properties properties, ModFood food, Supplier<SoundEvent> sound) {
        super(properties.food(food.properties(), food.consumable()));
        this.customSound = sound;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {

        if (customSound != null) {
            try {
                SoundEvent sound = customSound.get();

                if (!level.isClientSide()) {

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
                    LOGGER.info("Sonido reproducido en servidor: {}", sound.location());
                } else {

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
                    LOGGER.info("Sonido reproducido en cliente: {}", sound.location());
                }
            } catch (Exception e) {
                LOGGER.error("Error al reproducir sonido personalizado", e);
            }
        }

        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }
}

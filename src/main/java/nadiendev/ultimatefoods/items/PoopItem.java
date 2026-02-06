package nadiendev.ultimatefoods.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

/**
 * PoopItem
 * By NadienDev
 */
public class PoopItem extends Item {
    private final DeferredHolder<SoundEvent, SoundEvent> customSound;
    private final int effectDuration;

    public PoopItem(FoodProperties foodProperties, DeferredHolder<SoundEvent, SoundEvent> sound, int duration) {
        super(new Item.Properties().food(foodProperties));
        this.customSound = sound;
        this.effectDuration = duration;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        // Reproducir el sonido personalizado
        if (!level.isClientSide() && customSound != null) {
            level.playSound(
                null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                customSound.get(),
                SoundSource.PLAYERS,
                1.0F,
                1.0F
            );
        }

        return super.finishUsingItem(stack, level, entity);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    public int getEffectDuration() {
        return effectDuration;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        
        tooltipComponents.add(Component.translatable("item.ultimatefoods.cacotas.description_0"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.cacotas.description_1"));
    }
}
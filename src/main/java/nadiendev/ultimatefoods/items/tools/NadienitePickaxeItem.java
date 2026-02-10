package nadiendev.ultimatefoods.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Pico de Nadienite
 * - Velocidad extremadamente rápida (Eficiencia 10 simulada)
 * - Fortuna 10 (aplicada en loot tables)
 * - Irrompible (no consume durabilidad)
 */
public class NadienitePickaxeItem extends PickaxeItem {
    
    public NadienitePickaxeItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide) {
            var registry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
            
            var efficiency = registry.getHolderOrThrow(Enchantments.EFFICIENCY);
            if (stack.getEnchantmentLevel(efficiency) < 10) {
                stack.enchant(efficiency, 10);
            }
            
            var fortune = registry.getHolderOrThrow(Enchantments.FORTUNE);
            if (stack.getEnchantmentLevel(fortune) < 10) {
                stack.enchant(fortune, 10);
            }
        }
    }

      @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull TooltipContext context, 
                                @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
        tooltipComponents.add(Component.translatable("tooltip.ultimatefoods.unbreakable").withStyle(style -> style.withColor(0xFFAA00)));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getEnchantmentValue(@NotNull ItemStack stack) {
        return 25;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        float baseSpeed = super.getDestroySpeed(stack, state);
        if (baseSpeed > 1.0F) {
            return baseSpeed + 100.0F;
        }
        return baseSpeed;
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        return true;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull ItemStack stack, @NotNull BlockState state) {
        return super.isCorrectToolForDrops(stack, state);
    }
}
package nadiendev.ultimatefoods.items.tools;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Azada de Nadienite
 * - Eficiencia 10 permanente
 * - Fortuna 10 permanente
 * - Irrompible
 * - TODAS las funciones vanilla (heredadas de HoeItem)
 */
public class NadieniteHoeItem extends HoeItem {
    
    public NadieniteHoeItem(Tier tier, Properties properties) {
        super(tier, properties.attributes(HoeItem.createAttributes(tier, 0, -3.0F)).durability(0));
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
            
            // Resetear el daño si de alguna forma se dañó
            if (stack.getDamageValue() > 0) {
                stack.setDamageValue(0);
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
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, @NotNull net.minecraft.world.level.block.state.BlockState state, 
                            @NotNull net.minecraft.core.BlockPos pos, @NotNull LivingEntity entity) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.copy();
    }
}
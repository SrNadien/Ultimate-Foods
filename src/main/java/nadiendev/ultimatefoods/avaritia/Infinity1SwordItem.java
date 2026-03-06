package nadiendev.ultimatefoods.avaritia;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * Infinity-1 Sword
 * - 60 de daño de ataque
 * - Durabilidad infinita
 */
public class Infinity1SwordItem extends SwordItem {

    public Infinity1SwordItem(Tier tier, Properties properties) {
        // 59 + 1 base = 60 total
        super(tier, properties.attributes(SwordItem.createAttributes(tier, -1, -2.4F)));
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
       tooltip.add(Component.translatable("tooltip.ultimatefoods.infinity1sword.damage").withStyle(ChatFormatting.RED));
tooltip.add(Component.translatable("tooltip.ultimatefoods.infinity1sword.durability").withStyle(ChatFormatting.GREEN));
        
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
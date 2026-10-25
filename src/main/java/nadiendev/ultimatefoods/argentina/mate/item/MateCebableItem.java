package nadiendev.ultimatefoods.argentina.mate.item;

import nadiendev.ultimatefoods.argentina.mate.MateComponents;
import nadiendev.ultimatefoods.argentina.mate.MateRitual;
import nadiendev.ultimatefoods.argentina.mate.MateSabor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.ChatFormatting;

import java.util.List;

/**
 * Mate al que se le puede echar agua caliente: el que tiene yerba ("mate con yerba") y el
 * lavado. Con este en una mano y la pava caliente en la otra, click derecho para cebar.
 */
public class MateCebableItem extends Item {

    public MateCebableItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (MateRitual.cebar(level, player, hand)) {
            return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        MateSabor sabor = stack.getOrDefault(MateComponents.SABOR.get(), MateSabor.AMARGO);
        tooltip.add(Component.translatable("tooltip.ultimatefoods.mate.sabor", Component.translatable(sabor.translationKey()))
                .withStyle(ChatFormatting.GRAY));
        int cebadas = stack.getOrDefault(MateComponents.CEBADAS.get(), 0);
        if (cebadas > 0) {
            tooltip.add(Component.translatable("tooltip.ultimatefoods.mate.cebadas", cebadas, MateRitual.MAX_CEBADAS)
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        tooltip.add(Component.translatable("tooltip.ultimatefoods.mate.cebar").withStyle(ChatFormatting.DARK_GRAY));
    }
}

package nadiendev.ultimatefoods.fisura.mate.item;

import nadiendev.ultimatefoods.fisura.mate.MateComponents;
import nadiendev.ultimatefoods.fisura.mate.MateRitual;
import nadiendev.ultimatefoods.fisura.mate.MateSabor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.ChatFormatting;

import java.util.function.Consumer;

/**
 * Mate al que se le puede echar agua caliente: el que tiene yerba ("mate con yerba") y el
 * lavado. Con este en una mano y la pava caliente en la otra, click derecho para cebar.
 */
public class MateCebableItem extends Item {

    public MateCebableItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (MateRitual.cebar(level, player, hand)) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        MateSabor sabor = stack.getOrDefault(MateComponents.SABOR.get(), MateSabor.AMARGO);
        tooltip.accept(Component.translatable("tooltip.ultimatefoods.mate.sabor", Component.translatable(sabor.translationKey()))
                .withStyle(ChatFormatting.GRAY));
        int cebadas = stack.getOrDefault(MateComponents.CEBADAS.get(), 0);
        if (cebadas > 0) {
            tooltip.accept(Component.translatable("tooltip.ultimatefoods.mate.cebadas", cebadas, MateRitual.MAX_CEBADAS)
                    .withStyle(ChatFormatting.DARK_GRAY));
        }
        tooltip.accept(Component.translatable("tooltip.ultimatefoods.mate.cebar").withStyle(ChatFormatting.DARK_GRAY));
    }
}

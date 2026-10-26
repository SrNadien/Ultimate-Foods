package nadiendev.ultimatefoods.fisura.mate.item;

import nadiendev.ultimatefoods.fisura.mate.MateComponents;
import nadiendev.ultimatefoods.fisura.mate.MateRitual;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * Pava con agua caliente (no hirviendo: justo para el mate). Tiene un numero limitado de
 * cebadas; al agotarse vuelve a ser una pava vacia. Con un mate en la otra mano, click
 * derecho para cebar.
 */
public class PavaCalienteItem extends Item {

    public PavaCalienteItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (MateRitual.cebar(level, player, MateRitual.otraMano(hand))) {
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.ultimatefoods.pava.cebadas",
                stack.getOrDefault(MateComponents.CEBADAS_RESTANTES.get(), MateRitual.CEBADAS_POR_PAVA),
                MateRitual.CEBADAS_POR_PAVA).withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("tooltip.ultimatefoods.pava.cebar").withStyle(ChatFormatting.DARK_GRAY));
    }
}

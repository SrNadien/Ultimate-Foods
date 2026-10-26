package nadiendev.ultimatefoods.fisura.mate.item;

import nadiendev.ultimatefoods.fisura.mate.MateComponents;
import nadiendev.ultimatefoods.fisura.mate.block.YerberaBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

/** El item de la yerbera: si se rompio con yerba adentro, muestra cuantas porciones lleva. */
public class YerberaItem extends BlockItem {

    public YerberaItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        int porciones = stack.getOrDefault(MateComponents.PORCIONES.get(), 0);
        if (porciones > 0) {
            tooltip.accept(Component.translatable("message.ultimatefoods.yerbera.porciones", porciones, YerberaBlock.MAX_PORCIONES)
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}

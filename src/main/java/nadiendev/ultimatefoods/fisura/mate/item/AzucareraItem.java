package nadiendev.ultimatefoods.fisura.mate.item;

import nadiendev.ultimatefoods.fisura.mate.MateComponents;
import nadiendev.ultimatefoods.fisura.mate.block.AzucareraBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

/** El item de la azucarera: si se rompio con azucar adentro, muestra cuantas porciones lleva. */
public class AzucareraItem extends BlockItem {

    public AzucareraItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        int porciones = stack.getOrDefault(MateComponents.PORCIONES.get(), 0);
        if (porciones > 0) {
            tooltip.add(Component.translatable("message.ultimatefoods.azucarera.porciones", porciones, AzucareraBlock.MAX_PORCIONES)
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}

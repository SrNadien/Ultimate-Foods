package nadiendev.ultimatefoods.fisura.homenaje;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

/** El item de La Copa, con su dedicatoria en el tooltip. */
public class LaCopaItem extends BlockItem {

    public LaCopaItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.ultimatefoods.la_copa")
                .withStyle(ChatFormatting.AQUA, ChatFormatting.ITALIC));
    }
}

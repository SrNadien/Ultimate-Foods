package nadiendev.ultimatefoods.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class SuperChileItem extends Item {

    public SuperChileItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_0"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_1"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_2"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_3"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_4"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_5"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_6"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_7"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_8"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_9"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_10"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_11"));
        tooltipComponents.add(Component.translatable("item.ultimatefoods.super_chile.description_12"));
    }
}

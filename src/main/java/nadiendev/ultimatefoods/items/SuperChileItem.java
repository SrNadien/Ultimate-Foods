package nadiendev.ultimatefoods.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class SuperChileItem extends Item {

    public SuperChileItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);

        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_0"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_1"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_2"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_3"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_4"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_5"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_6"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_7"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_8"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_9"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_10"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_11"));
        tooltipComponents.accept(Component.translatable("item.ultimatefoods.super_chile.description_12"));
    }
}

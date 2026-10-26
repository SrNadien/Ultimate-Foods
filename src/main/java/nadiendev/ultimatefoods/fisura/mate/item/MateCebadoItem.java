package nadiendev.ultimatefoods.fisura.mate.item;

import nadiendev.ultimatefoods.fisura.mate.MateComponents;
import nadiendev.ultimatefoods.fisura.mate.MateRitual;
import nadiendev.ultimatefoods.fisura.mate.MateSabor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

/**
 * Mate ya cebado (amargo, dulce, de leche o con yuyos). Se toma de a sorbos; con click
 * derecho sobre otro jugador o un aldeano se lo pasas (eso lo resuelve {@code MateEvents}).
 *
 * <p>Lleva un componente {@code consumable} solo para que suene al tomar (ver {@code MateItems});
 * el final del sorbo lo maneja este item y no el consumible, asi que no come el stack.</p>
 */
public class MateCebadoItem extends Item {

    /** Lo que dura un sorbo, en ticks. */
    public static final int TICKS_SORBO = 32;

    private final MateSabor sabor;

    public MateCebadoItem(MateSabor sabor, Properties properties) {
        super(properties);
        this.sabor = sabor;
    }

    public MateSabor sabor() {
        return sabor;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return TICKS_SORBO;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide()) {
            MateRitual.aplicarEfecto(entity, sabor, stack.getOrDefault(MateComponents.CEBADAS.get(), 1));
        }
        if (entity instanceof Player player && player.hasInfiniteMaterials()) {
            return stack;
        }
        return MateRitual.consumirSorbo(stack, entity.getRandom());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.ultimatefoods.mate.sorbos",
                stack.getOrDefault(MateComponents.SORBOS.get(), MateRitual.SORBOS_POR_CEBADA))
                .withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("tooltip.ultimatefoods.mate.pasar").withStyle(ChatFormatting.DARK_GRAY));
    }
}

package nadiendev.ultimatefoods.argentina.mate.item;

import nadiendev.ultimatefoods.argentina.mate.MateComponents;
import nadiendev.ultimatefoods.argentina.mate.MateRitual;
import nadiendev.ultimatefoods.argentina.mate.MateSabor;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * Mate ya cebado (amargo, dulce, de leche o con yuyos). Se toma de a sorbos; con click
 * derecho sobre otro jugador o un aldeano se lo pasas (eso lo resuelve {@code MateEvents}).
 */
public class MateCebadoItem extends Item {

    private final MateSabor sabor;

    public MateCebadoItem(MateSabor sabor, Properties properties) {
        super(properties);
        this.sabor = sabor;
    }

    public MateSabor sabor() {
        return sabor;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            MateRitual.aplicarEfecto(entity, sabor, stack.getOrDefault(MateComponents.CEBADAS.get(), 1));
        }
        if (entity instanceof Player player && player.hasInfiniteMaterials()) {
            return stack;
        }
        return MateRitual.consumirSorbo(stack, entity.getRandom());
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.ultimatefoods.mate.sorbos",
                stack.getOrDefault(MateComponents.SORBOS.get(), MateRitual.SORBOS_POR_CEBADA))
                .withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("tooltip.ultimatefoods.mate.pasar").withStyle(ChatFormatting.DARK_GRAY));
    }
}

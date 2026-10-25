package nadiendev.ultimatefoods.fisura.mate.item;

import nadiendev.ultimatefoods.fisura.mate.MateComponents;
import nadiendev.ultimatefoods.fisura.mate.MateItems;
import nadiendev.ultimatefoods.fisura.mate.MateRitual;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;

/** Pava con agua fria: click derecho a una fuente de calor y queda lista para cebar. */
public class PavaConAguaItem extends Item {

    public PavaConAguaItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (player == null || !MateRitual.esFuenteDeCalor(level.getBlockState(pos))) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            ItemStack caliente = context.getItemInHand().transmuteCopy(MateItems.PAVA_CALIENTE.get(), 1);
            caliente.set(MateComponents.CEBADAS_RESTANTES.get(), MateRitual.CEBADAS_POR_PAVA);
            player.setItemInHand(context.getHand(), caliente);
            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 1.6F);
            if (level instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        5, 0.2, 0.1, 0.2, 0.02);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.ultimatefoods.pava.calentar").withStyle(ChatFormatting.DARK_GRAY));
    }
}

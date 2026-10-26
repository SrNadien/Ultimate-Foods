package nadiendev.ultimatefoods.fisura.mate;

import nadiendev.ultimatefoods.fisura.mate.item.MateCebableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.gossip.GossipType;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Las reglas del ritual: cebar, tomar, pasar el mate y calentar el agua. */
public final class MateRitual {

    /** Sorbos que da cada cebada. */
    public static final int SORBOS_POR_CEBADA = 4;
    /** Cebadas que aguanta la misma yerba antes de quedar lavada del todo. */
    public static final int MAX_CEBADAS = 4;
    /** Cebadas que rinde una pava caliente. */
    public static final int CEBADAS_POR_PAVA = 8;
    /** Probabilidad de que al terminar el mate quede con yerba usada en vez de vacio. */
    public static final float PROBABILIDAD_YERBA_USADA = 0.2F;

    private MateRitual() {
    }

    // ---------------------------------------------------------------- calor

    /** Fogata encendida (comun o de las almas), horno/ahumador/alto horno encendido, fuego o magma. */
    public static boolean esFuenteDeCalor(BlockState state) {
        if (state.getBlock() instanceof CampfireBlock) {
            return state.getValue(CampfireBlock.LIT);
        }
        if (state.getBlock() instanceof AbstractFurnaceBlock) {
            return state.getValue(AbstractFurnaceBlock.LIT);
        }
        return state.is(Blocks.FIRE) || state.is(Blocks.LAVA_CAULDRON) || state.is(Blocks.MAGMA_BLOCK);
    }

    // ---------------------------------------------------------------- cebar

    public static boolean tieneBombilla(Player player) {
        if (player.hasInfiniteMaterials()) {
            return true;
        }
        for (ItemStack stack : player.getInventory().getNonEquipmentItems()) {
            if (stack.is(MateItems.BOMBILLA.get())) return true;
        }
        return player.getOffhandItem().is(MateItems.BOMBILLA.get());
    }

    public static InteractionHand otraMano(InteractionHand mano) {
        return mano == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    /** True si en {@code manoMate} hay un mate cebable y en la otra mano una pava caliente. */
    public static boolean puedeCebar(Player player, InteractionHand manoMate) {
        return player.getItemInHand(manoMate).getItem() instanceof MateCebableItem
                && player.getItemInHand(otraMano(manoMate)).is(MateItems.PAVA_CALIENTE.get());
    }

    /**
     * Ceba el mate de {@code manoMate} con la pava de la otra mano. Cambia los dos stacks
     * en el servidor. Devuelve true si el gesto se resolvio (incluso si fallo por falta de
     * bombilla, para no seguir con otras acciones del click).
     */
    public static boolean cebar(Level level, Player player, InteractionHand manoMate) {
        if (!puedeCebar(player, manoMate)) {
            return false;
        }
        if (!tieneBombilla(player)) {
            if (!level.isClientSide()) {
                player.sendOverlayMessage(Component.translatable("message.ultimatefoods.mate.sin_bombilla"));
            }
            return true;
        }
        if (level.isClientSide()) {
            return true;
        }

        InteractionHand manoPava = otraMano(manoMate);
        ItemStack mate = player.getItemInHand(manoMate);
        ItemStack pava = player.getItemInHand(manoPava);

        MateSabor sabor = mate.getOrDefault(MateComponents.SABOR.get(), MateSabor.AMARGO);
        int cebadas = mate.getOrDefault(MateComponents.CEBADAS.get(), 0) + 1;

        ItemStack cebado = mate.transmuteCopy(sabor.itemCebado(), 1);
        cebado.set(MateComponents.SABOR.get(), sabor);
        cebado.set(MateComponents.CEBADAS.get(), cebadas);
        cebado.set(MateComponents.SORBOS.get(), SORBOS_POR_CEBADA);
        player.setItemInHand(manoMate, cebado);

        int restantes = pava.getOrDefault(MateComponents.CEBADAS_RESTANTES.get(), 1) - 1;
        if (restantes <= 0 && !player.hasInfiniteMaterials()) {
            ItemStack vacia = pava.transmuteCopy(MateItems.PAVA.get(), 1);
            vacia.remove(MateComponents.CEBADAS_RESTANTES.get());
            player.setItemInHand(manoPava, vacia);
        } else if (!player.hasInfiniteMaterials()) {
            pava.set(MateComponents.CEBADAS_RESTANTES.get(), restantes);
        }

        level.playSound(null, player.blockPosition(), SoundEvents.BREWING_STAND_BREW, SoundSource.PLAYERS, 0.6F, 1.4F);
        level.playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 0.8F, 0.9F);
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.CLOUD, player.getX(), player.getEyeY() - 0.4, player.getZ(), 3, 0.2, 0.1, 0.2, 0.01);
        }
        return true;
    }

    // ---------------------------------------------------------------- tomar

    /** Aplica el efecto del sabor, mas flojo cuanto mas lavada esta la yerba. */
    public static void aplicarEfecto(LivingEntity quien, MateSabor sabor, int cebadas) {
        double factor = Math.max(0.4, 1.0 - 0.15 * (Math.max(cebadas, 1) - 1));
        int duracion = (int) (sabor.duracionBase() * factor);
        quien.addEffect(new MobEffectInstance(sabor.efecto(), duracion, 0, false, true, true));
    }

    /**
     * Un sorbo menos. Devuelve el stack que queda: el mismo mate con un sorbo menos, un mate
     * lavado (quedo sin sorbos pero la yerba da para otra cebada) o, si la yerba ya dio todo,
     * un mate vacio (o, el 20 % de las veces, con yerba usada).
     */
    public static ItemStack consumirSorbo(ItemStack mate, RandomSource random) {
        int sorbos = mate.getOrDefault(MateComponents.SORBOS.get(), 1) - 1;
        if (sorbos > 0) {
            ItemStack resto = mate.copy();
            resto.set(MateComponents.SORBOS.get(), sorbos);
            return resto;
        }

        int cebadas = mate.getOrDefault(MateComponents.CEBADAS.get(), 1);
        if (cebadas >= MAX_CEBADAS) {
            // Yerba lavada del todo: vuelve el recipiente.
            return random.nextFloat() < PROBABILIDAD_YERBA_USADA
                    ? new ItemStack(MateItems.MATE_CON_YERBA.get())
                    : new ItemStack(MateItems.MATE.get());
        }

        ItemStack lavado = mate.transmuteCopy(MateItems.MATE_LAVADO.get(), 1);
        lavado.remove(MateComponents.SORBOS.get());
        return lavado;
    }

    // ---------------------------------------------------------------- pasar el mate

    /**
     * Le pasa el mate cebado de {@code mano} a otro jugador o a un aldeano. Solo servidor.
     * Devuelve true si se pudo.
     */
    public static boolean pasarMate(Player quien, InteractionHand mano, LivingEntity destino) {
        ItemStack mate = quien.getItemInHand(mano);
        if (!(mate.getItem() instanceof nadiendev.ultimatefoods.fisura.mate.item.MateCebadoItem cebado)) {
            return false;
        }
        MateSabor sabor = cebado.sabor();
        int cebadas = mate.getOrDefault(MateComponents.CEBADAS.get(), 1);

        aplicarEfecto(destino, sabor, cebadas);

        if (destino instanceof Villager aldeano) {
            aldeano.getGossips().add(quien.getUUID(), GossipType.MINOR_POSITIVE, 4);
        }

        Level level = quien.level();
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.HEART, destino.getX(), destino.getY(1.0) + 0.5, destino.getZ(),
                    5, 0.3, 0.3, 0.3, 0.02);
            BlockPos pos = destino.blockPosition();
            level.playSound(null, pos, SoundEvents.GENERIC_DRINK.value(), SoundSource.PLAYERS, 0.5F, 1.2F);
        }

        if (!quien.hasInfiniteMaterials()) {
            quien.setItemInHand(mano, consumirSorbo(mate, quien.getRandom()));
        }
        quien.getCooldowns().addCooldown(mate, 20);
        return true;
    }
}

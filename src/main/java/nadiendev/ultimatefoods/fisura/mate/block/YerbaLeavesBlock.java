package nadiendev.ultimatefoods.fisura.mate.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Hojas del arbol de yerba mate (inflamables como las demas hojas). En 26.1 {@link LeavesBlock}
 * es abstracto y suma hojas cayendo; estas no sueltan hojas (como en 1.21.1), solo el goteo de lluvia.
 */
public class YerbaLeavesBlock extends LeavesBlock {

    public static final MapCodec<YerbaLeavesBlock> CODEC = simpleCodec(YerbaLeavesBlock::new);

    public YerbaLeavesBlock(Properties properties) {
        super(0.0F, properties);
    }

    @Override
    public MapCodec<? extends LeavesBlock> codec() {
        return CODEC;
    }

    @Override
    protected void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 30;
    }
}

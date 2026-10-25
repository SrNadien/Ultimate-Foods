package nadiendev.ultimatefoods.argentina.mate.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

/**
 * Hitbox de la yerbera y la azucarera (bolsitas de tela), con el modelo mirando al NORTE.
 * Medidas en pixeles de 1/16 de bloque: {@code box(minX, minY, minZ, maxX, maxY, maxZ)}.
 * Medidas finales del modelo de C: x 2-13.6, y 0-15.7, z 5-11 (azucarera z 4.9-11).
 * Para ajustarlas basta cambiar estas dos constantes; la rotacion segun {@code facing}
 * se calcula sola.
 */
public final class MateShapes {

    public static final VoxelShape YERBERA = Block.box(2, 0, 5, 14, 16, 11);
    public static final VoxelShape AZUCARERA = Block.box(2, 0, 5, 14, 16, 11);

    private MateShapes() {
    }

    /** Las cuatro orientaciones de una forma que esta definida mirando al norte. */
    public static Map<Direction, VoxelShape> orientaciones(VoxelShape norte) {
        Map<Direction, VoxelShape> mapa = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            mapa.put(dir, girar(norte, dir));
        }
        return mapa;
    }

    private static VoxelShape girar(VoxelShape norte, Direction destino) {
        VoxelShape resultado = Shapes.empty();
        for (AABB b : norte.toAabbs()) {
            // Giros horizontales de bloque entero, alrededor del centro (0.5, 0.5).
            AABB g = switch (destino) {
                case SOUTH -> new AABB(1 - b.maxX, b.minY, 1 - b.maxZ, 1 - b.minX, b.maxY, 1 - b.minZ);
                case EAST -> new AABB(1 - b.maxZ, b.minY, b.minX, 1 - b.minZ, b.maxY, b.maxX);
                case WEST -> new AABB(b.minZ, b.minY, 1 - b.maxX, b.maxZ, b.maxY, 1 - b.minX);
                default -> b;
            };
            resultado = Shapes.or(resultado, Shapes.create(g));
        }
        return resultado;
    }
}

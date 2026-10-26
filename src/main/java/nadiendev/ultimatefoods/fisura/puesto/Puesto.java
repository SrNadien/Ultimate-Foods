package nadiendev.ultimatefoods.fisura.puesto;

import nadiendev.ultimatefoods.fisura.mate.MateItems;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Los items del mate que se pueden dejar apoyados como bloque (Shift + click derecho): el mate en
 * todos sus estados y la pava. Cada uno usa su modelo 3D {@code item/fisura/<id>} y una hitbox
 * en pixeles (modelo mirando al norte), recortada a 0-16 aunque el modelo asome (el mango de la
 * pava, la bombilla).
 */
public enum Puesto implements StringRepresentable {

    MATE("mate", () -> MateItems.MATE.get(), 3.5, 0, 3.5, 12.5, 11.5, 12.5),
    MATE_CON_YERBA("mate_con_yerba", () -> MateItems.MATE_CON_YERBA.get(), 3.5, 0, 3.5, 12.9, 16, 12.5),
    MATE_AMARGO("mate_amargo", () -> MateItems.MATE_AMARGO.get(), 3.5, 0, 3.5, 12.9, 16, 12.5),
    MATE_DULCE("mate_dulce", () -> MateItems.MATE_DULCE.get(), 3.5, 0, 3.5, 12.9, 16, 12.5),
    MATE_DE_LECHE("mate_de_leche", () -> MateItems.MATE_DE_LECHE.get(), 3.5, 0, 3.5, 12.9, 16, 12.5),
    MATE_CON_YUYOS("mate_con_yuyos", () -> MateItems.MATE_CON_YUYOS.get(), 3.5, 0, 3.5, 12.9, 16, 12.5),
    MATE_LAVADO("mate_lavado", () -> MateItems.MATE_LAVADO.get(), 3.5, 0, 3.5, 12.9, 16, 12.5),
    PAVA("pava", () -> MateItems.PAVA.get(), 0, 0, 2, 16, 14.6, 14),
    PAVA_CON_AGUA("pava_con_agua", () -> MateItems.PAVA_CON_AGUA.get(), 0, 0, 2, 16, 14.6, 14),
    PAVA_CALIENTE("pava_caliente", () -> MateItems.PAVA_CALIENTE.get(), 0, 0, 2, 16, 16, 14),
    YERBA_VERDEFLOR("yerba_verdeflor", () -> MateItems.YERBA_VERDEFLOR.get(), 3.5, 0, 5.5, 12.5, 14.8, 10.5),
    BOMBILLA("bombilla", () -> MateItems.BOMBILLA.get(), 6.75, 0, 7.5, 9.25, 16, 8.5);

    private final String id;
    private final Supplier<? extends Item> item;
    private final Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);

    Puesto(String id, Supplier<? extends Item> item,
           double x1, double y1, double z1, double x2, double y2, double z2) {
        this.id = id;
        this.item = item;
        VoxelShape norte = Block.box(x1, y1, z1, x2, y2, z2);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            shapes.put(dir, girar(norte, dir));
        }
    }

    /** Los mates con yerba (con o sin cebar): los unicos a los que se les clava la bombilla. */
    public boolean esMateConYerba() {
        return switch (this) {
            case MATE_CON_YERBA, MATE_LAVADO, MATE_AMARGO, MATE_DULCE, MATE_DE_LECHE, MATE_CON_YUYOS -> true;
            default -> false;
        };
    }

    /** Los que se apoyan inclinados hacia donde mira quien los pone: los mates (con su bombilla) y la bombilla. */
    public boolean seInclina() {
        return this == MATE || esMateConYerba() || this == BOMBILLA;
    }

    public String id() {
        return id;
    }

    public Item item() {
        return item.get();
    }

    public VoxelShape shape(Direction facing) {
        return shapes.get(facing);
    }

    @Override
    public String getSerializedName() {
        return id;
    }

    /** El puesto de un item, o {@code null} si no se puede colocar. */
    public static Puesto de(ItemStack stack) {
        for (Puesto puesto : values()) {
            if (stack.is(puesto.item())) {
                return puesto;
            }
        }
        return null;
    }

    private static VoxelShape girar(VoxelShape norte, Direction destino) {
        VoxelShape resultado = Shapes.empty();
        for (AABB b : norte.toAabbs()) {
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

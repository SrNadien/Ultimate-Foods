package nadiendev.ultimatefoods.argentina.comida.colocada;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.argentina.comida.ComidaItems;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Las comidas que se pueden colocar como bloque ({@link ComidaColocadaBlock}). Cada valor esta
 * ligado a su item de {@link ComidaItems} (se resuelve en forma perezosa, por eso el
 * {@link Supplier}) y trae la caja de colision/seleccion por defecto, en pixeles (0-16), para el
 * modelo mirando al norte (-Z). El modelo de cada una vive en
 * {@code models/block/argentina/comida/<id>.json}.
 */
public enum Comida implements StringRepresentable {

    ASADO("asado", () -> ComidaItems.ASADO.get(), 1, 0, 2, 15, 7, 14),
    CHORIPAN("choripan", () -> ComidaItems.CHORIPAN.get(), 1, 0, 4, 15, 5, 12),
    CHORIZO_ASADO("chorizo_asado", () -> ComidaItems.CHORIZO_ASADO.get(), 2, 0, 5, 14, 4, 11),
    PROVOLETA("provoleta", () -> ComidaItems.PROVOLETA.get(), 3, 0, 3, 13, 3, 13),
    MILANESA_DE_CARNE("milanesa_de_carne", () -> ComidaItems.MILANESA_DE_CARNE.get(), 2, 0, 2, 14, 3, 14),
    MILANESA_DE_POLLO("milanesa_de_pollo", () -> ComidaItems.MILANESA_DE_POLLO.get(), 2, 0, 2, 14, 3, 14),
    SANGUCHE_DE_MILANESA("sanguche_de_milanesa", () -> ComidaItems.SANGUCHE_DE_MILANESA.get(), 3, 0, 3, 13, 5, 13),
    GUISO("guiso", () -> ComidaItems.GUISO.get(), 3, 0, 3, 13, 5, 13),
    LOCRO("locro", () -> ComidaItems.LOCRO.get(), 3, 0, 3, 13, 5, 13),
    NOQUIS("noquis", () -> ComidaItems.NOQUIS.get(), 3, 0, 3, 13, 4, 13),
    ALFAJOR("alfajor", () -> ComidaItems.ALFAJOR.get(), 5, 0, 5, 11, 3, 11),
    MEDIALUNA("medialuna", () -> ComidaItems.MEDIALUNA.get(), 3, 0, 4, 13, 4, 12),
    PANQUEQUE_CON_DULCE_DE_LECHE("panqueque_con_dulce_de_leche",
            () -> ComidaItems.PANQUEQUE_CON_DULCE_DE_LECHE.get(), 3, 0, 3, 13, 4, 13),
    FLAN_CON_DULCE_DE_LECHE("flan_con_dulce_de_leche",
            () -> ComidaItems.FLAN_CON_DULCE_DE_LECHE.get(), 4, 0, 4, 12, 5, 12),
    TORTAFRITA("tortafrita", () -> ComidaItems.TORTAFRITA.get(), 3, 0, 3, 13, 3, 13),
    EMPANADA("empanada", () -> ComidaItems.EMPANADA.get(), 3, 0, 5, 13, 4, 11),
    PITUSAS("pitusas", () -> ComidaItems.PITUSAS.get(), 4, 0, 4, 12, 3, 12),
    PEPAS("pepas", () -> ComidaItems.PEPAS.get(), 4, 0, 4, 12, 3, 12),
    FUGAZZETA("fugazzeta", () -> ComidaItems.FUGAZZETA.get(), 2, 0, 2, 14, 3, 14),
    MEGA_DIBU("mega_dibu", () -> ComidaItems.MEGA_DIBU.get(), 3, 0, 3, 13, 8, 13),
    FERNET_BRANCA("fernet_branca", () -> ComidaItems.FERNET_BRANCA.get(), 5, 0, 5, 11, 11, 11),
    QUILMES("quilmes", () -> ComidaItems.QUILMES.get(), 5, 0, 5, 11, 12, 11),
    STELLA_ARTOIS("stella_artois", () -> ComidaItems.STELLA_ARTOIS.get(), 5, 0, 5, 11, 12, 11),
    ANDES("andes", () -> ComidaItems.ANDES.get(), 5, 0, 5, 11, 12, 11);

    private static final Map<String, Comida> BY_ID = new HashMap<>();

    static {
        for (Comida comida : values()) {
            BY_ID.put(comida.id, comida);
        }
    }

    private final String id;
    private final Supplier<? extends Item> item;
    /** Una forma por cuarto de giro en y (el indice es {@link #quarterTurns(Direction)}). */
    private final VoxelShape[] shapes = new VoxelShape[4];

    Comida(String id, Supplier<? extends Item> item,
           double x1, double y1, double z1, double x2, double y2, double z2) {
        this.id = id;
        this.item = item;
        VoxelShape shape = Block.box(x1, y1, z1, x2, y2, z2);
        for (int turn = 0; turn < 4; turn++) {
            shapes[turn] = shape;
            shape = rotateQuarter(shape);
        }
    }

    public String id() {
        return id;
    }

    @Override
    public String getSerializedName() {
        return id;
    }

    /** El item de esta comida. */
    public Item item() {
        return item.get();
    }

    /** Forma (colision y seleccion) con el bloque mirando hacia {@code facing}. */
    public VoxelShape shape(Direction facing) {
        return shapes[quarterTurns(facing)];
    }

    /** Modelo 3D del bloque, hecho a mano: {@code ultimatefoods:block/argentina/comida/<id>}. */
    public ResourceLocation blockModel() {
        return ResourceLocation.fromNamespaceAndPath(UltimateFoodsCore.MOD_ID, "block/argentina/comida/" + id);
    }

    @Nullable
    public static Comida byId(String id) {
        return BY_ID.get(id);
    }

    /**
     * Cuartos de giro (en sentido horario visto desde arriba) que hay que darle al modelo, que mira al
     * norte, para que mire hacia {@code facing}. Es el mismo giro que el {@code y} del blockstate.
     */
    public static int quarterTurns(Direction facing) {
        return (((int) facing.toYRot() + 180) % 360) / 90;
    }

    /** Giro en grados del blockstate para {@code facing}. */
    public static int rotationY(Direction facing) {
        return quarterTurns(facing) * 90;
    }

    private static VoxelShape rotateQuarter(VoxelShape shape) {
        VoxelShape[] result = {Shapes.empty()};
        // (x, z) -> (1 - z, x): giro horario visto desde arriba, igual que "y: 90" en un blockstate.
        shape.forAllBoxes((x1, y1, z1, x2, y2, z2) ->
                result[0] = Shapes.or(result[0], Shapes.box(1 - z2, y1, x1, 1 - z1, y2, x2)));
        return result[0];
    }
}

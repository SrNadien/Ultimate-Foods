package nadiendev.ultimatefoods.datagen.providers;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;

/** Atajo para escribir cada cubo de un modelo 3D en una sola linea. */
final class ModelosCubos {

    private ModelosCubos() {
    }

    static <T extends ModelBuilder<T>> Cubo<T> cubo(T modelo, float x1, float y1, float z1,
                                                   float x2, float y2, float z2) {
        return new Cubo<>(modelo.element().from(x1, y1, z1).to(x2, y2, z2));
    }

    static final class Cubo<T extends ModelBuilder<T>> {

        private final ModelBuilder<T>.ElementBuilder cubo;

        private Cubo(ModelBuilder<T>.ElementBuilder cubo) {
            this.cubo = cubo;
        }

        Cubo<T> rot(float angulo, Direction.Axis eje, float ox, float oy, float oz) {
            cubo.rotation().origin(ox, oy, oz).axis(eje).angle(angulo).end();
            return this;
        }

        Cubo<T> cara(Direction cara, String textura, float u1, float v1, float u2, float v2) {
            cubo.face(cara).texture(textura).uvs(u1, v1, u2, v2).end();
            return this;
        }

        /** Cara que brilla: las brasas de la parrilla. */
        Cubo<T> caraLuz(Direction cara, String textura, float u1, float v1, float u2, float v2,
                        int luzBloque, int luzCielo) {
            cubo.face(cara).texture(textura).uvs(u1, v1, u2, v2).emissivity(luzBloque, luzCielo).end();
            return this;
        }

        void fin() {
            cubo.end();
        }
    }
}

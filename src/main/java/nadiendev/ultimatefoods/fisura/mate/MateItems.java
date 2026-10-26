package nadiendev.ultimatefoods.fisura.mate;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import nadiendev.ultimatefoods.fisura.mate.item.AzucareraItem;
import nadiendev.ultimatefoods.fisura.mate.item.MateCebableItem;
import nadiendev.ultimatefoods.fisura.mate.item.MateCebadoItem;
import nadiendev.ultimatefoods.fisura.mate.item.PavaCalienteItem;
import nadiendev.ultimatefoods.fisura.mate.item.PavaConAguaItem;
import nadiendev.ultimatefoods.fisura.mate.item.PavaItem;
import nadiendev.ultimatefoods.fisura.mate.item.YerberaItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Items del modulo del mate. Los que son 3D (C) apuntan a modelos hechos a mano. */
public final class MateItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(UltimateFoodsCore.MOD_ID);

    // ---- arbol y yerba ----
    public static final DeferredItem<BlockItem> YERBA_MATE_LOG = ITEMS.registerSimpleBlockItem(MateBlocks.YERBA_MATE_LOG);
    public static final DeferredItem<BlockItem> YERBA_MATE_PLANKS = ITEMS.registerSimpleBlockItem(MateBlocks.YERBA_MATE_PLANKS);
    public static final DeferredItem<BlockItem> YERBA_MATE_LEAVES = ITEMS.registerSimpleBlockItem(MateBlocks.YERBA_MATE_LEAVES);
    public static final DeferredItem<BlockItem> YERBA_MATE_SAPLING = ITEMS.registerSimpleBlockItem(MateBlocks.YERBA_MATE_SAPLING);

    public static final DeferredItem<Item> YERBA_MATE_LEAF = ITEMS.registerSimpleItem("yerba_mate_leaf");
    /** Hoja ya seca (paso intermedio entre la hoja y la yerba molida). */
    public static final DeferredItem<Item> YERBA_MATE_LEAF_DRIED = ITEMS.registerSimpleItem("yerba_mate_leaf_dried");
    public static final DeferredItem<Item> YERBA_MATE = ITEMS.registerSimpleItem("yerba_mate");
    public static final DeferredItem<Item> YERBA_VERDEFLOR = ITEMS.registerItem("yerba_verdeflor", Item::new,
            props -> props.stacksTo(16));

    // ---- yerbera ----
    // En 26.1 el BlockItem tiene que usar la clave de descripcion del bloque (si no, el nombre sale como item).
    public static final DeferredItem<YerberaItem> YERBERA = ITEMS.registerItem("yerbera",
            props -> new YerberaItem(MateBlocks.YERBERA.get(), props.useBlockDescriptionPrefix()));

    public static final DeferredItem<AzucareraItem> AZUCARERA = ITEMS.registerItem("azucarera",
            props -> new AzucareraItem(MateBlocks.AZUCARERA.get(), props.useBlockDescriptionPrefix()));

    // ---- el mate, en todos sus estados ----
    public static final DeferredItem<Item> MATE = ITEMS.registerItem("mate", Item::new,
            props -> props.stacksTo(1));

    public static final DeferredItem<MateCebableItem> MATE_CON_YERBA = ITEMS.registerItem("mate_con_yerba",
            props -> new MateCebableItem(props.stacksTo(1)
                    .component(MateComponents.SABOR.get(), MateSabor.AMARGO)));

    public static final DeferredItem<MateCebadoItem> MATE_AMARGO = cebado("mate_amargo", MateSabor.AMARGO);
    public static final DeferredItem<MateCebadoItem> MATE_DULCE = cebado("mate_dulce", MateSabor.DULCE);
    public static final DeferredItem<MateCebadoItem> MATE_DE_LECHE = cebado("mate_de_leche", MateSabor.LECHE);
    public static final DeferredItem<MateCebadoItem> MATE_CON_YUYOS = cebado("mate_con_yuyos", MateSabor.YUYOS);

    public static final DeferredItem<MateCebableItem> MATE_LAVADO = ITEMS.registerItem("mate_lavado",
            props -> new MateCebableItem(props.stacksTo(1)
                    .component(MateComponents.SABOR.get(), MateSabor.AMARGO)
                    .component(MateComponents.CEBADAS.get(), 1)));

    // ---- la pava ----
    public static final DeferredItem<PavaItem> PAVA = ITEMS.registerItem("pava",
            props -> new PavaItem(props.stacksTo(1)));
    public static final DeferredItem<PavaConAguaItem> PAVA_CON_AGUA = ITEMS.registerItem("pava_con_agua",
            props -> new PavaConAguaItem(props.stacksTo(1)));
    public static final DeferredItem<PavaCalienteItem> PAVA_CALIENTE = ITEMS.registerItem("pava_caliente",
            props -> new PavaCalienteItem(props.stacksTo(1)
                    .component(MateComponents.CEBADAS_RESTANTES.get(), MateRitual.CEBADAS_POR_PAVA)));

    // ---- accesorios ----
    public static final DeferredItem<Item> BOMBILLA = ITEMS.registerSimpleItem("bombilla");
    public static final DeferredItem<Item> YUYOS = ITEMS.registerSimpleItem("yuyos");

    private MateItems() {
    }

    /**
     * Mate cebado. En 26.1 los sonidos de tomar mientras se usa salen del componente
     * {@code consumable}; se lo pone solo para eso (animacion de tomar, 32 ticks, sin
     * particulas). El efecto y el sorbo los sigue resolviendo {@link MateCebadoItem}.
     */
    private static DeferredItem<MateCebadoItem> cebado(String id, MateSabor sabor) {
        return ITEMS.registerItem(id, props -> new MateCebadoItem(sabor, props.stacksTo(1)
                .component(MateComponents.SABOR.get(), sabor)
                .component(MateComponents.SORBOS.get(), MateRitual.SORBOS_POR_CEBADA)
                .component(MateComponents.CEBADAS.get(), 1)
                .component(DataComponents.CONSUMABLE, Consumable.builder()
                        .consumeSeconds(MateCebadoItem.TICKS_SORBO / 20.0F)
                        .animation(ItemUseAnimation.DRINK)
                        .sound(SoundEvents.GENERIC_DRINK)
                        .hasConsumeParticles(false)
                        .build())));
    }
}

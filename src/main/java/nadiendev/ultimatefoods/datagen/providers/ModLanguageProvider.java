package nadiendev.ultimatefoods.datagen.providers;

import nadiendev.ultimatefoods.UltimateFoodsCore;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import java.util.Map;
import java.util.TreeMap;

/** Un archivo de idioma por instancia: en_us, es_es, es_ar y es_mx. */
public class ModLanguageProvider extends LanguageProvider {

    private final Map<String, String> textos;

    public ModLanguageProvider(PackOutput output, String locale, Map<String, String> textos) {
        super(output, UltimateFoodsCore.MOD_ID, locale);
        this.textos = textos;
    }

    @Override
    protected void addTranslations() {
        new TreeMap<>(textos).forEach(this::add);
    }
}

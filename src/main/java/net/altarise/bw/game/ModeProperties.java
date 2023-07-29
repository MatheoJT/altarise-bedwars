package net.altarise.bw.game;

import net.altarise.bw.Bedwars;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class ModeProperties {

    private final Map<String, Section> sections = new HashMap<>();
    private final String modeId;

    public ModeProperties(String modeId) {
        this.modeId = modeId;
        String mode = getModeFromID();
        File file = new File(Bedwars.INSTANCE().getDataFolder() + "/gameconfig", mode + ".config.yml");
        if(!file.exists()) Bedwars.INSTANCE().getLogger().log(Level.SEVERE, "File does not exist ! ->" + modeId);
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        yamlConfiguration.getKeys(false).forEach(section -> {
            ConfigurationSection sec = yamlConfiguration.getConfigurationSection(section);
            Map<String, String> values = sec.getKeys(false).stream().collect(Collectors.toMap(key -> key, sec::getString));
            sections.put(section, new Section(values));
        });
    }

    public int getMaxPlayers() {
        return Objects.equals(modeId, "bedw-quatuor") ? 4*8 : Objects.equals(modeId, "bedw-trio") ? 3*8 : Objects.equals(modeId, "bedw-duo") ? 2*8 : 8;
    }

    private String getModeFromID() {
        return Objects.equals(modeId, "bedw-quatuor") ? "quatuor" : Objects.equals(modeId, "bedw-trio") ? "trio" : Objects.equals(modeId, "bedw-duo") ? "duo" : "solo";
    }
    public Section getSection(String section) {
        return sections.get(section);
    }

    public static class Section {

        private final Map<String, String> values;

        public Section(Map<String, String> values) {
            this.values = values;
        }

        public String getValue(String valueKey) {
            return values.get(valueKey);
        }

    }

}

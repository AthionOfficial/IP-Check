package net.risenphoenix.commons.configuration;

import java.io.Reader;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
    private int comments;
    private ConfigurationManager manager;

    private FileConfiguration config;

    public Configuration(ConfigurationManager manager,
                         Reader reader, int comments) {
        this.comments = comments;
        this.manager = manager;
        this.config = YamlConfiguration.loadConfiguration(reader);
    }

    public final FileConfiguration getConfig() {
        return this.config;
    }

    public void createSection(String path) {
        this.config.createSection(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return this.config.getConfigurationSection(path);
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }

    public void removeKey(String path) {
        this.config.set(path, null);
    }

    public void set(String path, Object value, String comment) {
        if(!this.config.contains(path)) {
            this.config.set(manager.getPluginName() + "_COMMENT_" + comments,
                    " " + comment);
            comments++;
        }

        this.config.set(path, value);
    }

    public void set(String path, Object value, String[] comment) {
        for(String comm : comment) {

            if(!this.config.contains(path)) {
                this.config.set(manager.getPluginName() + "_COMMENT_" +
                        comments, " " + comm);
                comments++;
            }
        }

        this.config.set(path, value);
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(manager
                .getConfigContent());
    }

    public void saveConfig() {
        String config = this.config.saveToString();
        manager.saveConfig();
    }

    public Set<String> getKeys() {
        return this.config.getKeys(false);
    }

}

package net.cybercake.cyberapi.spigot.server;

import net.cybercake.cyberapi.spigot.basic.BetterStackTraces;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * Represents the server.properties file in your root server directory
 */
public class ServerProperties {

    private final Properties properties;

    /**
     * Instantiate the {@link ServerProperties}
     */
    public ServerProperties() {
        this.properties = new Properties();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("server.properties"));
            properties.load(reader);
            reader.close();
        } catch (Exception exception) {
            BetterStackTraces.print(exception);
        }
    }

    public Properties getPropertiesManager() { return properties; }
    public HashMap<String, Object> getAllProperties() {
        HashMap<String, Object> serverProperties = new HashMap<>();
        for(String key : getPropertiesManager().stringPropertyNames()) {
            serverProperties.put(key, getPropertiesManager().get(key));
        }
        return serverProperties;
    }
    public Object getProperty(String key) { return getAllProperties().get(key); }

    public void setProperty(String key, String value) { getPropertiesManager().setProperty(key, value); }
    public void clear() { getPropertiesManager().clear(); }

    public boolean contains(Object value) { return getPropertiesManager().contains(value); }
    public boolean containsKey(String key) { return getPropertiesManager().containsKey(key); }
    public boolean containsValue(Object value) { return getPropertiesManager().containsValue(value); }
    public boolean isEmpty() { return getPropertiesManager().isEmpty(); }

    public List<String> keys() { return new ArrayList<>(getAllProperties().keySet()); }
    public List<Object> values() { return new ArrayList<>(getAllProperties().values()); }

    public int getSize() { return getAllProperties().size(); }

}

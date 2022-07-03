package net.cybercake.cyberapi.settings;

import net.cybercake.cyberapi.CyberAPI;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;

/**
 * The FinalizedSettings, basically {@link Settings} but finalized and that cannot be changed. Used in the initialization of CyberAPI on server start in {@link CyberAPI}.
 */
public final class FinalizedSettings {

    private final String data;
    private final HashMap<String, Object> values;

    private FinalizedSettings() { this.data = null; this.values = null; }
    public FinalizedSettings(String data) {
        this.data = data;
        this.values = fromData(data);
    }


    /**
     * Get a value from a key
     * @param key the key, will return an object
     * @return the value associated with the key provided
     */
    @Nullable
    public Object get(String key) {
        validateKey(key);
        if(values.get(key).equals("${ISNULL}")) return null;
        return values.get(key);
    }

    /**
     * Get a value, in {@link String} form, from a key
     * @param key the key, will return a {@link String}
     * @return the value associated with the key provided, in {@link String} form
     */
    @Nullable
    public String getString(String key) {
        validateKey(key);
        return String.valueOf(get(key));
    }

    /**
     * Get a value, in {@link Boolean} form, from a key
     * @param key the key, will return a {@link Boolean}
     * @return the value associated with the key provided, in {@link Boolean} form
     */
    public @NotNull Boolean getBoolean(String key) {
        return Boolean.valueOf(getString(key));
    }

    /**
     * Get the {@link Settings.FeatureSupport} from the feature name
     * @param feature the feature to get, usually looks like 'adventureSupport'
     * @return the value associated with the key provided, including the feature name, in {@link Settings.FeatureSupport} form
     */
    public Settings.FeatureSupport getFeatureSupportStatus(String feature) {
        try {
            String value = getString(feature);
           // CyberAPI.getInstance().getAPILogger().verbose("SETTINGS", "Found value " + value + " for " + this.getClass().getMethod("getFeatureSupportStatus", String.class).getName() + "!");
           // I know it's suppose to be verbose, but I feel it's **too** verbose
            Settings.FeatureSupport featureSupport = Settings.FeatureSupport.valueOf(value.split("-")[0]);
            featureSupport.setFeature(value.split("-")[1]);
            return featureSupport;
        } catch (Exception exception) {
            CyberAPI.getInstance().getAPILogger().verbose("SETTINGS", "Exception occurred whilst getting FeatureSupport status in " + this.getClass().getCanonicalName() + ": " + exception);
            CyberAPI.getInstance().getAPILogger().verboseException("SETTINGS", exception);
            return Settings.FeatureSupport.AUTO;
        }
    }

    /**
     * Gets the "stringified" version of the data
     * @return the data in {@link String} form
     */
    public String getData() { return data; }

    /**
     * Gets the values from the {@link HashMap} directly
     * @return the data in {@link HashMap} form
     */
    public HashMap<String, Object> getValues() { return values; }

    /**
     * Check if a key exists in the data
     * @param key the key
     * @return a boolean on whether the key exists, 'true' if yes, 'false' if no
     */
    public boolean keyExists(String key) { return values.containsKey(key); }
    private void validateKey(String key) { if(!keyExists(key)) throw new IllegalArgumentException("There is no key '" + key + "' in " + this.getClass().getSimpleName() + "!"); }


    /**
     * Convert the "stringified" version of the data into a {@link HashMap}
     * @param data the {@link String} version of the data
     * @return a {@link HashMap} with the data
     */
    public HashMap<String, Object> fromData(String data) {
        HashMap<String, Object> values = new HashMap<>();
        data = data.substring(9, data.length()-1);
        for(String value : data.split(", ")) {
            if(value.split("=").length != 2) throw new IllegalStateException("Values must be key-value pairs!");

            values.put(value.split("=")[0], value.split("=")[1].replace("${EQUALS}", "=").replace("${COMMA}", ","));
        }
        return values;
    }

}

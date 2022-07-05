package net.cybercake.cyberapi.common.builders.settings;

/**
 * Allows plugin to use 'SUPPORTED', 'UNSUPPORTED', or 'AUTO' for determining a certain feature's support
 */
public enum FeatureSupport {
    /**
     * Represents a feature that is supported
     */
    SUPPORTED,

    /**
     * Represents a feature that is unsupported
     */
    UNSUPPORTED,

    /**
     * Allows CyberAPI to determine if a feature is {@link FeatureSupport#SUPPORTED} or {@link FeatureSupport#UNSUPPORTED}
     */
    AUTO;

    private String feature;
    FeatureSupport() {
        this.feature = "__UNKNOWN__";
    }

    public String getFeature() { return feature; }
    public void setFeature(String feature) { this.feature = feature; }
}
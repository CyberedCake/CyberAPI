package net.cybercake.cyberapi.config;

/**
 * Errors relating to the {@link Config} class for CyberAPI!
 * @since 3.3
 */
public class ConfigurationException extends IllegalStateException {

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }

}

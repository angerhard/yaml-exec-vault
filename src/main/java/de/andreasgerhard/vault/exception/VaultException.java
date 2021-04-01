package de.andreasgerhard.vault.exception;

public class VaultException extends RuntimeException {
    public VaultException(String message) {
        super(message);
    }

    public VaultException(String message, Throwable cause) {
        super(message, cause);
    }
}

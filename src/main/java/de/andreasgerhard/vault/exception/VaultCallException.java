package de.andreasgerhard.vault.exception;

import lombok.Getter;

@Getter
public class VaultCallException extends RuntimeException {

    private final String url;
    private final String message;
    private final String body;
    private final int httpCode;

    public VaultCallException(String url, String message, String body, int httpCode) {
        this.url = url;
        this.message = message;
        this.body = body;
        this.httpCode = httpCode;
    }


}

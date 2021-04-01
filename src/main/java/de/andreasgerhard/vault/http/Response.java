package de.andreasgerhard.vault.http;

import lombok.Data;
import lombok.Value;

@Value
public class Response {
  private int code;
  private String body;
  private String responseMessage;
  private Exception exception;
}

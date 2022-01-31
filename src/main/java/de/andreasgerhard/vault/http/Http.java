package de.andreasgerhard.vault.http;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import de.andreasgerhard.vault.exception.VaultCallException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.commons.io.IOUtils;

@Log
public abstract class Http {

  @Getter(AccessLevel.PROTECTED)
  private final String host;
  @Getter(AccessLevel.PROTECTED)
  private final String path;
  @Getter(AccessLevel.PROTECTED)
  private String token;
  private PasswordAuthentication passwordAuthentication;
  private Consumer<Response> errorConsumer;


  public Http(String host, String path) {
    this.host = host;
    this.path = path;
  }

  public Http token(String token) {
    this.token = token;
    return this;
  }

  public Http auth(String user, String password) {
    this.passwordAuthentication = new PasswordAuthentication(user, password.toCharArray());
    return this;
  }

  protected String executeAndReturnResult(HttpURLConnection httpCon) throws IOException {
    log.fine("HTTP Request: " + httpCon.getURL());

    ObjectMapper objectMapper = new ObjectMapper();
    TypeFactory typeFactory = TypeFactory.defaultInstance();
    try {
      return IOUtils.toString(httpCon.getInputStream(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw handleException(httpCon, e);
    }
  }

  protected JsonNode executeAndReturnJsonResult(HttpURLConnection httpCon) throws IOException {
    log.fine("HTTP Request: " + httpCon.getURL());

    printOutCurl(httpCon);

      ObjectMapper objectMapper = new ObjectMapper();
    TypeFactory typeFactory = TypeFactory.defaultInstance();
    try {
      return objectMapper.readTree(httpCon.getInputStream());
    } catch (IOException e) {
      throw handleException(httpCon, e);
    }
  }

  private VaultCallException handleException(HttpURLConnection httpCon, IOException e)
      throws IOException {
    String body = IOUtils.toString(httpCon.getErrorStream(), StandardCharsets.UTF_8);
    Response response = new Response(httpCon.getResponseCode(), body,
        httpCon.getResponseMessage(), e);

    if (errorConsumer != null) {
      errorConsumer.accept(response);
    } else {
      log.warning("Code:          " + response.getCode());
      log.warning("ResponseMsg:   " + response.getResponseMessage());
      log.warning("Body:          " + response.getBody());
    }
    return new VaultCallException(
        httpCon.getURL().toString(),
        httpCon.getResponseMessage(),
        body,
        httpCon.getResponseCode());
  }

  protected void setToken(HttpURLConnection httpCon) {
    if (token != null) {
      httpCon.addRequestProperty("X-Vault-Token", token);
    }
    if (passwordAuthentication != null) {
      httpCon.addRequestProperty("Authorization",
          "Basic YW5nZXJoYXJkOmE2YjliMGIzZGNmN2EzODZiMTQ1Y2JlYmU5MTVjZmNiOTgzOGFmZWM");
    }
  }


  public void printOutCurl(HttpURLConnection httpCon) {
    if (this instanceof Put) {
      String payload = ((Put) this).getPayload();
      System.out.println("curl \\\n");
      System.out.println("\t--header \"X-Vault-Token: $VAULT_TOKEN\" \\\n");
      System.out.println("\t--request PUT \\\n");
      System.out.println("\t--data '"+payload+"' \\\n");
      System.out.println("\t" + httpCon.getURL().toString());
    }
    if (this instanceof Get) {
      System.out.println("curl \\\n");
      System.out.println("\t--header \"X-Vault-Token: $VAULT_TOKEN\" \\\n");
      System.out.println("\t--request GET \\\n");
      System.out.println("\t" + httpCon.getURL().toString());
    }
  }

}




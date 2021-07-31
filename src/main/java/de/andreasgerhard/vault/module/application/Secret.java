package de.andreasgerhard.vault.module.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.andreasgerhard.vault.exception.VaultCallException;
import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.vault.http.Get;
import de.andreasgerhard.vault.http.Put;
import lombok.Getter;

public class Secret {

  @Getter
  private Application application;
  private String key;
  private String value;

  public Secret(Application application) {
    this.application = application;
  }

  public Secret key(@Tag("key") String key) {
    this.key = key;
    return this;
  }

  public Secret value(@Tag("value") String value) {
    this.value = value;
    return this;
  }

  public Secret policy(@Tag("policy") String policy) {
    this.value = secretByPasswordPolicy(policy);
    return this;
  }

  public Application create() {
    String applicationPath = getApplicationPath().toString();

    try {
      JsonNode jsonNode = new Get(application.getVault().getHost(), applicationPath)
          .token(application.getVault().getVaultToken()).runResultJson();

      if (!jsonNode.get("data").isNull()) {
        ObjectNode secrets = ((ObjectNode) jsonNode.get("data"));
        if (secrets.get(key) == null) {
          secrets.put(key, value);
        }
      } else {
        ((ObjectNode) jsonNode).putObject("data").put(key, value);

      }
      new Put(application.getVault().getHost(), applicationPath)
          .token(application.getVault().getVaultToken())
          .payload(jsonNode.get("data").toString())
          .run();
    } catch (VaultCallException e) {
      if (e.getHttpCode() == 404) {
        new Put(application.getVault().getHost(), applicationPath)
            .token(application.getVault().getVaultToken())
            .payload("{\"" + key + "\": \"" + value + "\"}")
            .run();
      } else {
        throw e;
      }
    }
    return application;
  }

  public Application update() {
    new Put(application.getVault().getHost(), getApplicationPath())
        .token(application.getVault().getVaultToken())
        .payload("{\"" + key + "\": \"" + value + "\"}")
        .run();
    return application;
  }

  private String getApplicationPath() {
    StringBuilder applicationPath = new StringBuilder("/secret/");
    applicationPath.append(application.getName());
    if (application.getProfile() != null) {
      applicationPath.append("/").append(application.getProfile());
    }
    return applicationPath.toString();
  }

  private String secretByPasswordPolicy(String nameOfPolicy) {
    JsonNode jsonNode = new Get(getApplication().getVault().getHost(),
        "/sys/policies/password/" + getApplication().getName() + "-" + nameOfPolicy + "/generate")
        .token(getApplication().getVault().getVaultToken()).runResultJson();
    return jsonNode.get("data").get("password").asText();
  }

}

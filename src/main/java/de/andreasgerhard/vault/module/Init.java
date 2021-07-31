package de.andreasgerhard.vault.module;

import com.fasterxml.jackson.databind.JsonNode;
import de.andreasgerhard.vault.exception.VaultException;
import de.andreasgerhard.vault.http.Get;
import de.andreasgerhard.vault.http.Put;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

@Log
public class Init {

  private static final String INIT_PARAMETER = "{\n" +
      "  \"secret_shares\": %d,\n" +
      "  \"secret_threshold\": %d\n" +
      "}";

  @Getter
  private boolean initialized;
  @Getter
  private String initLetter;

  @Getter
  private final String rootToken;
  @Getter
  private final Set<String> unsealToken = new HashSet<>();

  @SneakyThrows
  public Init(String host, int shares, int threshold) {

    JsonNode isVaultInit = new Get(host, "/sys/init").runResultJson();
    if (isVaultInit.get("initialized").booleanValue()) {
      log.info("Vault is already initialized");
      if (System.getenv("VAULT_TOKEN") == null) {
        throw new VaultException(
            "VAULT_TOKEN environment has to be set, VAULT already initialized!");
      } else {
        this.rootToken = System.getenv("VAULT_TOKEN");
      }
    } else {
      JsonNode result = new Put(host, "/sys/init")
          .payload(String.format(INIT_PARAMETER, shares, threshold))
          .run();
      result.get("keys").elements().forEachRemaining(jsonNode -> {
        unsealToken.add(jsonNode.textValue());
      });
      rootToken = result.get("root_token").textValue();
      log.info("Vault has been initialized");
      initialized = true;
      initLetter = result.toString();
    }
  }

  @SneakyThrows
  public Init(String host) {

    JsonNode isVaultInit = new Get(host, "/sys/init").runResultJson();
    if (isVaultInit.get("initialized").booleanValue()) {
      if (System.getenv("VAULT_TOKEN") == null) {
        throw new VaultException(
            "VAULT_TOKEN environment has to be set, VAULT already initialized!");
      } else {
        this.rootToken = System.getenv("VAULT_TOKEN");
        log.info("Using root token from environment.");
      }
    } else {
      throw new VaultException("Vault not initialized! Please use init(host, shares, threshold)");
    }
  }

  @SneakyThrows
  public Init(String host, String rootToken) {

    JsonNode isVaultInit = new Get(host, "/sys/init").runResultJson();
    if (isVaultInit.get("initialized").booleanValue()) {
      log.info("Vault is already initialized");
      this.rootToken = rootToken;
    } else {
      throw new VaultException("Vault not initialized! Please use init(host, shares, threshold)");
    }
  }

}

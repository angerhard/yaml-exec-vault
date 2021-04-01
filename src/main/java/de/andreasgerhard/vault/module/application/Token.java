package de.andreasgerhard.vault.module.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.andreasgerhard.vault.exception.VaultException;
import de.andreasgerhard.vault.http.Put;
import java.util.Set;

public class Token {

  private Application application;
  private Set<AclPolicy> acls;
  private final ObjectMapper mapper = new ObjectMapper();

  public Token(Application application, Set<AclPolicy> acls) {
    this.application = application;
    this.acls = acls;
  }

  protected Application getApplication() {
    return application;
  }

  public Application generate() {

    ObjectNode node = getConfiguration();
    ArrayNode arrayNode = mapper.createArrayNode();
    acls.forEach(acl -> arrayNode.add(acl.toString()));
    node.set("policies", arrayNode);

    System.out.println(node.toString());

    try {
      JsonNode tokenResult = new Put(application.getVault().getHost(),
          getRestPath())
          .token(application.getVault().getVaultToken())
          .payload(mapper.writeValueAsString(node))
          .run();
      String clientToken = tokenResult.get("auth").get("client_token").asText();
      System.out.println("client-token: " + clientToken);

      System.out.println("result:       " + tokenResult.toString());

      return application;
    } catch (JsonProcessingException e) {
      throw new VaultException("Could not create token request", e);
    }
  }

  protected String getRestPath() {
    return "/auth/token/create";
  }

  protected ObjectNode getConfiguration() {
    ObjectNode node = mapper.createObjectNode();
    node.put("display_name", application.getName() + "_token");
    node.put("ttl", "1h");
    node.put("renewable", true);
    return node;
  }

}

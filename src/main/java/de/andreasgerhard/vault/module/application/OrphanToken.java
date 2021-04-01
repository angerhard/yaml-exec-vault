package de.andreasgerhard.vault.module.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.Set;

public class OrphanToken extends Token {

  private final ObjectMapper mapper = new ObjectMapper();

  public OrphanToken(Application application,
      Set<AclPolicy> acls) {
    super(application, acls);
  }

  @Override
  protected String getRestPath() {
    return "/auth/token/create-orphan";
  }

  @Override
  protected ObjectNode getConfiguration() {
    ObjectNode node = mapper.createObjectNode();
    node.put("display_name", getApplication().getName() + "_token");
    node.put("period", "46080m");
    node.put("renewable", true);
    return node;
  }

}

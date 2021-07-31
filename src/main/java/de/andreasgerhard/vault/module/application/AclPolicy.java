package de.andreasgerhard.vault.module.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.vault.http.Put;
import de.andreasgerhard.vault.policy.Permission;
import java.util.Arrays;

public class AclPolicy {

  private final static String POLICY_JSON = "{\n" +
      "  \"policy\": \"%s\"\n" +
      "}";

  private final Application application;
  private String name;
  private String policyName;

  private StringBuilder policy = new StringBuilder();

  public AclPolicy(Application application) {
    this.application = application;
  }

  public AclPolicy applicationPermission(@Tag("permissions") Permission... permissions) {
    final ObjectMapper mapper = new ObjectMapper();
    ArrayNode arrayNode = mapper.createArrayNode();
    Arrays.stream(permissions).forEach(permission -> arrayNode.add(permission.toString()));

    createPolicyString("secret/" + this.application.getName(), arrayNode);
    return this;
  }

  public AclPolicy securityPermission(@Tag("path") String path,
      @Tag("permissions") Permission... permissions) {

    final ObjectMapper mapper = new ObjectMapper();
    ArrayNode arrayNode = mapper.createArrayNode();
    Arrays.stream(permissions).forEach(permission -> arrayNode.add(permission.toString()));

    createPolicyString(path, arrayNode);
    return this;
  }

  public AclPolicy suffix(@Tag("suffix") String suffix) {
    this.name = suffix;
    this.policyName = application.getName() + "_" + name;
    return this;
  }

  private void createPolicyString(String path, ArrayNode arrayNode) {
    policy.append("path \"")
        .append(path)
        .append("\" {\n")
        .append("  capabilities = ")
        .append(arrayNode.toString())
        .append("\n")
        .append("}\n")
        .append("\n");
  }

  public AclPolicy databasePermissions() {
    policy.append("path \"secret/")
        .append(this.application.getName())
        .append("\" {\n")
        .append("  capabilities = [\"read\"]\n")
        .append("}\n")
        .append("\n");
    return this;
  }

  public Application publish() {

    String jsonPolicy = String.format(POLICY_JSON, policy.toString()
        .replace("\n", "\\n")
        .replace("\"", "\\\""));

    new Put(application.getVault().getHost(), "/sys/policy/" + policyName)
        .payload(jsonPolicy).token(application.getVault().getVaultToken()).run();
    return application;
  }

  @Override
  public String toString() {
    return policyName;
  }
}

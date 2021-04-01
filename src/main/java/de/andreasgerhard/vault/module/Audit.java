package de.andreasgerhard.vault.module;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.andreasgerhard.vault.http.Delete;
import de.andreasgerhard.vault.http.Put;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Audit {

  private final Vault vault;
  private final String name;
  private final String file;


  public Vault activate() {
    ObjectMapper mapper = new ObjectMapper();
    ObjectNode objectNode = mapper.createObjectNode();
    objectNode.put("type", "file");
    objectNode.set("options", mapper
        .createObjectNode()
        .put("file_path", file));

    new Delete(vault.getHost(),
        "/sys/audit/"+name)
        .token(vault.getVaultToken())
        .runResultJson();

    new Put(vault.getHost(),
        "/sys/audit/"+name)
        .token(vault.getVaultToken())
        .payload(objectNode.toString())
        .run();

    return vault;
  }
}

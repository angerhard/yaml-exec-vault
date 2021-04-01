package de.andreasgerhard.vault.module;

import com.fasterxml.jackson.databind.JsonNode;
import de.andreasgerhard.vault.http.Get;

public class Mount {

  public Mount(Vault vault) {

    JsonNode nodeMounts = new Get(vault.getHost(), "/sys/mounts")
        .token(vault.getVaultToken())
        .runResultJson();
    System.out.println(nodeMounts);

    JsonNode nodeAuth = new Get(vault.getHost(), "/sys/auth")
        .token(vault.getVaultToken())
        .runResultJson();
    System.out.println(nodeAuth);

  }


}

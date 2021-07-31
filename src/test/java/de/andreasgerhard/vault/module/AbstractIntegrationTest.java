package de.andreasgerhard.vault.module;

import org.testcontainers.utility.DockerImageName;
import org.testcontainers.vault.VaultContainer;

public class AbstractIntegrationTest {


  public static final String ROOT_TOKEN = "root-token";
  public static VaultContainer<?> vaultContainer = new VaultContainer<>(
      DockerImageName.parse("vault:1.6.5")).withVaultToken(ROOT_TOKEN);

  static {
    vaultContainer.start();
  }

}

package de.andreasgerhard.vault.module;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.vault.VaultContainer;

@Testcontainers
class VaultTest {

  @Container
  public VaultContainer vaultContainer = new VaultContainer<>(DockerImageName.parse("vault:1.6.3"));

  @Test
  void shouldInitVault() {
    String host = vaultContainer.getHost();
    int port = vaultContainer.getFirstMappedPort();
    Vault vault = new Vault().host("http://" + host + ":" + port).init(5, 3);
    assertThat(vault.getVaultToken()).isNotNull();
  }
}

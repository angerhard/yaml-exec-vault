package de.andreasgerhard.vault.module;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;


class VaultTest extends AbstractIntegrationTest {

  @Test
  void shouldInitVault() {
    String host = vaultContainer.getHost();
    int port = vaultContainer.getFirstMappedPort();
    Vault vault = new Vault().host(String.format("http://%s:%d", host, port)).init(ROOT_TOKEN);
    assertThat(vault.getVaultToken()).isNotNull();
  }
}

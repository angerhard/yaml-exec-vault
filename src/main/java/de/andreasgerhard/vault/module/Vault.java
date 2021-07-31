package de.andreasgerhard.vault.module;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.interpreter.YamlCommanderRoot;
import de.andreasgerhard.vault.exception.VaultException;
import de.andreasgerhard.vault.module.application.Application;
import lombok.Getter;

@YamlCommanderRoot("vault")
public class Vault {

  @Getter
  private String host;
  private Init init;
  private String vaultToken;


  public Vault host(@Tag("host") String host) {
    this.host = host;
    return this;
  }

  public String getVaultToken() {
    if (vaultToken == null) {
      throw new VaultException("Token is empty. Please run init first!");
    }
    return vaultToken;
  }

  public Vault init(@Tag("shares") int shares, @Tag("threshold") int threshold) {
    init = new Init(this.host, 5, 2);
    vaultToken = init.getRootToken();
    return this;
  }

  public Vault init(@Tag("rootToken") String rootToken) {
    init = new Init(this.host, rootToken);
    vaultToken = init.getRootToken();
    return this;
  }

  public Vault init() {
    init = new Init(this.host);
    vaultToken = init.getRootToken();
    return this;
  }

  public Audit audit(@Tag("name") String name, @Tag("file") String file) {
    return new Audit(this, name, file);
  }

  public Vault unseal() {
    new Unseal(host, init.getUnsealToken());
    return this;
  }

  public Vault printCredentials() {
    if (init.isInitialized()) {
      System.out.println(init.getInitLetter());
    }
    return this;
  }

  public Vault engine(@Tag("type") String type, @Tag("path") String path) {
    new Engine(getVaultToken(), host, path, type);
    return this;
  }

  public Vault auth(@Tag("type") String type, @Tag("path") String path) {
    new Auth(getVaultToken(), host, path, type);
    return this;
  }

  public Vault mounts() {
    new Mount(this);
    return this;
  }

  public Application application() {
    return new Application(this);
  }

}

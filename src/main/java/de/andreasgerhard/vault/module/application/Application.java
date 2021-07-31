package de.andreasgerhard.vault.module.application;

import de.andreasgerhard.interpreter.Tag;
import de.andreasgerhard.vault.module.Vault;
import de.andreasgerhard.vault.policy.Password;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;

public class Application {

  @Getter
  private final Vault vault;
  @Getter
  private String name;
  @Getter
  private String profile;
  private Set<AclPolicy> acls = new HashSet<>();

  public Application(Vault vault) {
    this.vault = vault;
  }

  public Application name(@Tag("name") String name) {
    this.name = name;
    return this;
  }

  public Application profile(@Tag("profile") String profile) {
    this.profile = profile;
    return this;
  }

  public Application passwordPolicy(@Tag("name") String name, @Tag("policy") Password policy) {
    new PasswordPolicy(vault, this.name + "-" + name, policy.getPolicyAsString());
    return this;
  }

  public Secret secret() {
    return new Secret(this);
  }

  public Token token() {
    return new Token(this, this.acls);
  }

  public OrphanToken orphanToken() {
    return new OrphanToken(this, this.acls);
  }

  public AclPolicy acl() {
    AclPolicy acl = new AclPolicy(this);
    acls.add(acl);
    return acl;
  }

  public AppRole appRole() {
    return new AppRole(this, this.acls);
  }

  public Vault publish() {
    return getVault();
  }

}

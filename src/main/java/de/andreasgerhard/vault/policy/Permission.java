package de.andreasgerhard.vault.policy;

public enum Permission {

  CREATE, UPDATE, READ, DELETE, LIST, SUDO;

  public String toString() {
    return super.name().toLowerCase();
  }
}

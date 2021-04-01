package de.andreasgerhard.vault.module.application;

import de.andreasgerhard.vault.module.Vault;
import de.andreasgerhard.vault.http.Put;

public class PasswordPolicy {

    private Vault vault;
    private String name;
    private String policy;

    public PasswordPolicy(Vault vault, String name, String policy) {

        String payload = "{\"policy\": \"" +
                policy.replace("\"", "\\\"").replace("\n", "\\n")
                + "\"}";
        new Put(vault.getHost(),
                "/sys/policies/password/" + name)
                .token(vault.getVaultToken())
                .payload(payload)
                .run();
    }


}

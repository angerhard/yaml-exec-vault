package de.andreasgerhard.vault.module;

import com.fasterxml.jackson.databind.JsonNode;
import de.andreasgerhard.vault.exception.VaultException;
import de.andreasgerhard.vault.http.Get;
import de.andreasgerhard.vault.http.Put;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class Unseal {
    private final String host;
    private final Set<String> unsealKeys = new HashSet<>();

    private static final String KEY_PAYLOAD = "{\n" +
            "  \"key\": \"%s\"\n" +
            "}";

    public Unseal(String host) {
        this(host, Collections.emptySet());
    }

    public Unseal(String host, Set<String> unsealKeys) {
        this.host = host;
        this.unsealKeys.addAll(unsealKeys);

        boolean sealed = isSealed(host);

        if (sealed && this.unsealKeys.isEmpty()) {
            throw new VaultException("Vault is sealed. When not init, you have to care to unseal it.");
        }

        if (sealed) {
            unseal(host);
        }
    }

    private void unseal(String host) {
        int threshold = getThreshold(this.host);
        String[] unsealKeysArr = this.unsealKeys.toArray(new String[0]);

        IntStream.range(0, threshold).forEach(key -> {
            new Put(host, "/sys/unseal").payload(String.format(KEY_PAYLOAD, unsealKeysArr[key])).run();
        });

        boolean sealed = isSealed(host);
        if (sealed) {
            throw new VaultException("Vault is sealed. Unsealing has no effect!");
        }
    }

    private boolean isSealed(String host) {
        Get get = new Get(host, "/sys/seal-status");
        JsonNode result = get.runResultJson();
        return result.get("sealed").booleanValue();
    }

    private int getThreshold(String host) {
        Get get = new Get(host, "/sys/seal-status");
        JsonNode result = get.runResultJson();
        return result.get("t").intValue();
    }

}

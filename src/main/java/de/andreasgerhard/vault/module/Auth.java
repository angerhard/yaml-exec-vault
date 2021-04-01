package de.andreasgerhard.vault.module;

import com.fasterxml.jackson.databind.JsonNode;
import de.andreasgerhard.vault.exception.VaultCallException;
import de.andreasgerhard.vault.http.Get;
import de.andreasgerhard.vault.http.Put;
import lombok.extern.java.Log;

@Log
public class Auth {

    public static final int HTTP_BAD_REQUEST = 400;

    public Auth(String token, String host, String path, String type) {
        try {
            JsonNode run = new Get(host,  "/sys/auth"+path+"/tune")
                    .token(token)
                    .runResultJson();
            log.info(String.format("Auth %s as %s already initialized", type, path));
        } catch (VaultCallException e) {
            if (e.getHttpCode() == HTTP_BAD_REQUEST && e.getBody().contains("cannot fetch sysview for path \\\"auth/"+path.replace("/", "")+"/\\\"")) {
                new Put(host, "/sys/auth" + path)
                        .token(token)
                        .payload(String.format("{\"type\": \"%s\"}", type))
                        .run();
            } else {
                throw e;
            }
        }
    }

}

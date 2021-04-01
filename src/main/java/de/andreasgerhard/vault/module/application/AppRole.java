package de.andreasgerhard.vault.module.application;

import com.fasterxml.jackson.databind.JsonNode;
import de.andreasgerhard.vault.exception.VaultCallException;
import de.andreasgerhard.vault.http.Get;
import de.andreasgerhard.vault.http.Put;
import java.util.Set;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;

@Log
public class AppRole {

    public static final int HTTP_BAD_REQUEST = 400;

    private Application application;
    private Set<AclPolicy> acls;

    private String roleId;
    private String secretId;
    private String secretIdAccessor;

    public AppRole(Application application, Set<AclPolicy> acls) {

        this.application = application;
        this.acls = acls;
    }

    public AppRole enable() {

        try {
            JsonNode run = new Get(application.getVault().getHost(), "/sys/auth/approle/tune")
                    .token(application.getVault().getVaultToken())
                    .runResultJson();
            log.info("Engine /auth/approle already initialized");
        } catch (VaultCallException e) {
            if (e.getHttpCode() == 400 && e.getBody().contains("cannot fetch sysview for path \\\"auth/approle/\\\"")) {
                new Put(application.getVault().getHost(), "/sys/auth/approle").token(application.getVault().getVaultToken())
                        .payload("{\"type\": \"approle\"}").run();
            } else {
                throw e;
            }
        }
        return this;
    }

    public Application create() {
        String aclList = StringUtils.join(acls, ",");
        String policyList = String.format("{\"policies\": \"%s\"}", aclList);
        JsonNode result = new Put(application.getVault().getHost(), String.format("/auth/approle/role/%s", application.getName()))
                .token(application.getVault().getVaultToken())
                .payload(policyList)
                .run();

        JsonNode jsonNodeRoleId = new Get(application.getVault().getHost(), String.format("/auth/approle/role/%s/role-id", application.getName()))
                .token(application.getVault().getVaultToken())
                .runResultJson();
        roleId = jsonNodeRoleId.get("data").get("role_id").asText();
        JsonNode jsonNodeSecretId = new Put(application.getVault().getHost(), String.format("/auth/approle/role/%s/secret-id", application.getName()))
                .token(application.getVault().getVaultToken())
                .run();
        secretIdAccessor = jsonNodeSecretId.get("data").get("secret_id_accessor").asText();
        secretId = jsonNodeSecretId.get("data").get("secret_id").asText();

        System.out.println("role_id: " + roleId);
        System.out.println("secret_id_accessor: " + secretIdAccessor);
        System.out.println("secret_id: " + secretId);

        return application;
    }

}

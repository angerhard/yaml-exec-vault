package de.andreasgerhard.vault.policy;

public enum Password {

    STANDARD("length = 85\n" +
            "\n" +
            "rule \"charset\" {\n" +
            "  charset = \"abcdefghijklmnopqrstuvwxyz\"\n" +
            "  min-chars = 1\n" +
            "}\n" +
            "\n" +
            "rule \"charset\" {\n" +
            "  charset = \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\"\n" +
            "  min-chars = 1\n" +
            "}\n" +
            "\n" +
            "rule \"charset\" {\n" +
            "  charset = \"0123456789\"\n" +
            "  min-chars = 1\n" +
            "}");

    private String policy;

    Password(String policy) {
        this.policy = policy;
    }

    public String getPolicyAsString() {
        return policy;
    }
}

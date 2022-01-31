package de.andreasgerhard.vault.http;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.SneakyThrows;

public class Put extends Http {

    public Put(String host, String path) {
        super(host, path);
    }

    private String payload;

    public Put payload(String payload) {
        this.payload = payload;
        return this;
    }

    @Override
    public Put token(String token) {
        super.token(token);
        return this;
    }

    public String getPayload() {
        return payload;
    }

    @SneakyThrows
    public JsonNode run() {
        URL url = new URL(String.format("%s/v1%s", getHost(), getPath()));
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        setToken(httpCon);
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        if (payload != null) {
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(payload);
            out.close();
        }
        return executeAndReturnJsonResult(httpCon);
    }

}

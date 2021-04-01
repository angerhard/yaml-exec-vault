package de.andreasgerhard.vault.http;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.SneakyThrows;

public class Get extends Http {


    public Get(String host, String path) {
        super(host, path);
    }

    @Override
    public Get token(String token) {
        super.token(token);
        return this;
    }

    @Override
    public Get auth(String user, String password) {
        super.auth(user, password);
        return this;
    }

    @SneakyThrows
    public String runResultString() {
        URL url = new URL(String.format("%s/v1%s", getHost(), getPath()));
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        setToken(httpCon);
        httpCon.setRequestMethod("GET");
        httpCon.setDoOutput(true);
        return executeAndReturnResult(httpCon);
    }

    @SneakyThrows
    public JsonNode runResultJson() {
        URL url = new URL(String.format("%s/v1%s", getHost(), getPath()));
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        setToken(httpCon);

        httpCon.setRequestMethod("GET");
        httpCon.setDoOutput(true);
        return executeAndReturnJsonResult(httpCon);
    }



}

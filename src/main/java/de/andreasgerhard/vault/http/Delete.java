package de.andreasgerhard.vault.http;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.SneakyThrows;

public class Delete extends Http {


    public Delete(String host, String path) {
        super(host, path);
    }

    @Override
    public Delete token(String token) {
        super.token(token);
        return this;
    }

    @SneakyThrows
    public String runResultString() {
        URL url = new URL(String.format("%s/v1%s", getHost(), getPath()));
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        setToken(httpCon);
        httpCon.setRequestMethod("DELETE");
        httpCon.setDoOutput(true);
        return executeAndReturnResult(httpCon);
    }

    @SneakyThrows
    public JsonNode runResultJson() {
        URL url = new URL(String.format("%s/v1%s", getHost(), getPath()));
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        setToken(httpCon);
        httpCon.setRequestMethod("DELETE");
        httpCon.setDoOutput(true);
        return executeAndReturnJsonResult(httpCon);
    }



}

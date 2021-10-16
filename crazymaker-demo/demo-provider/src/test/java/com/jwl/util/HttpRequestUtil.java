package com.jwl.util;

import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
public class HttpRequestUtil {

    public static Optional<String> simpleGet(String link) {
        try {
            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            ArrayList<Byte> buffer = new ArrayList<>();
            byte[] bytes = new byte[1024];
            int length;
            while((length = inputStream.read(bytes)) != -1) {
                for (int i = 0; i < length; i++) {
                    buffer.add(bytes[i]);
                }
            }
            bytes = new byte[buffer.size()];
            for (int i = 0; i < buffer.size(); i++) {
                bytes[i] = buffer.get(i);
            }
            inputStream.close();
            urlConnection.disconnect();
            return Optional.of(new String(bytes, StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.empty();
    }
}

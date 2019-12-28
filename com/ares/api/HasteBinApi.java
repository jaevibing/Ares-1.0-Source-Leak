package com.ares.api;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HasteBinApi
{
    public static String uploadImpl(final String v-8, final String v-7, final String v-6) throws IOException {
        final byte[] bytes = /*EL:23*/v-6.getBytes(StandardCharsets.UTF_8);
        final URL url = /*EL:24*/new URL(v-8 + "/documents");
        final HttpURLConnection httpURLConnection = /*EL:26*/(HttpURLConnection)url.openConnection();
        /*SL:27*/httpURLConnection.setRequestMethod("POST");
        /*SL:28*/httpURLConnection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
        /*SL:29*/httpURLConnection.setFixedLengthStreamingMode(bytes.length);
        /*SL:30*/httpURLConnection.setDoInput(true);
        /*SL:31*/httpURLConnection.setDoOutput(true);
        /*SL:32*/httpURLConnection.connect();
        try {
            final OutputStream a1 = /*EL:36*/httpURLConnection.getOutputStream();
            /*SL:37*/a1.write(bytes);
            final InputStreamReader a2 = /*EL:39*/new InputStreamReader(httpURLConnection.getInputStream());
            final JsonObject a3 = /*EL:41*/(JsonObject)new Gson().fromJson((Reader)a2, (Class)JsonObject.class);
            String v1 = /*EL:43*/"";
            /*SL:45*/if (v-7 != null && !v-7.isEmpty()) {
                /*SL:47*/v1 = "." + v-7;
            }
            /*SL:50*/httpURLConnection.disconnect();
            /*SL:51*/return v-8 + "/" + a3.get("key").getAsString() + "." + v-7;
        }
        finally {
            /*SL:55*/httpURLConnection.disconnect();
        }
    }
}

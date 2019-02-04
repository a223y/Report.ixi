package com.ictreport.ixi.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class IctRestCaller {

    public static JSONObject getConfig(final String ictRestPassword) {

        String json = call("getConfig", ictRestPassword);
        if (json != null) {
            return new JSONObject(json);
        }
        return null;
    }

    public static JSONArray getNeighbors(final String ictRestPassword) {

        String json = call("getNeighbors", ictRestPassword);
        if (json != null) {
            return new JSONArray(json);
        }
        return null;
    }

    private static String call(final String route, final String ictRestPassword) {
        try {
            final CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://localhost:2187/" + route);

            // Request parameters and other properties.
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("password", ictRestPassword));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            //Execute and get the response.
            final CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                try (InputStream instream = entity.getContent()) {

                    StringWriter writer = new StringWriter();
                    IOUtils.copy(instream, writer, "UTF-8");
                    final String json = writer.toString();
                    return json;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
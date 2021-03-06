package com.soopercode.pingapp.utils;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.IDN;
import java.net.URL;

/**
 * Pings a given host by attempting to establish a {@link java.net.HttpURLConnection}
 * and retrieving the server's response code.
 *
 * @author Ria
 */
public class HttpPinger {

    private static final String TAG = HttpPinger.class.getSimpleName();

    /**
     * Attempts to obtain a Http Response Code from the specified host
     * via {@link java.net.HttpURLConnection}.
     * Used by {@link com.soopercode.pingapp.listview.AsyncListPing}.
     *
     * @param hostname String of the URL to connect to
     * @return The HTTP Response Code received from the server,
     * or 0 if no response has been received
     */
    public int getResponseCode(String hostname) {
        int response = 0;
        URL url;
        HttpURLConnection urlc;

        /* TODO ******** FOR DEMO ********* */
        if (hostname.equals("www.unavailable.com") ||
                hostname.equals("thepiratebay.org")) {
            return response;
        } else if (hostname.equals("www.somewebsite.com")) {
            return 503;
        } else if (hostname.equals("www.serverdown.nl")) {
            return 401;
        }
        /* ****************************** */

        try {
            String idn = IDN.toASCII(hostname);
            url = new URL("http", idn, "/");

            urlc = (HttpURLConnection) url.openConnection();
            urlc.setConnectTimeout(7000);
            response = urlc.getResponseCode();
        } catch (NullPointerException npe) {
            Log.d(TAG, "HttpPinger: NPE: " + npe.toString());
            /* ANDROID-BUG:
            java.lang.NullPointerException
            at libcore.net.http.HttpConnection$Address.hashCode(HttpConnection.java:343)  */
        } catch (IOException ioe) {
            Log.d(TAG, "HttpPinger: IOE:", ioe);
        }

        return response;
    }

    /**
     * Attempts to obtain a Http Response Code and Http Response Message
     * from the specified host via {@link java.net.HttpURLConnection}.
     * Used by {@link com.soopercode.pingapp.AddAndPingActivity.QuickPing}.
     *
     * @param hostname String of the URL to connect to
     * @return A Bundle containing Response Code and Response Message,
     * or 0 and "nix" if no response has been received.
     */
    public Bundle getResponse(String hostname) {
        int responseCode = 0;
        String responseMsg = "nix";
        URL url;
        HttpURLConnection urlc;

        /* TODO ******** FOR DEMO ********* */
        if (hostname.equals("www.somewebsite.com")) {
            Bundle responseBundle = new Bundle();
            responseBundle.putInt("responseCode", 401);
            responseBundle.putString("responseMsg", "Unauthorized");
            return responseBundle;
        } else if (hostname.equals("www.unavailable.com") ||
                hostname.equals("www.serverdown.nl")) {
            Bundle responseBundle = new Bundle();
            responseBundle.putInt("responseCode", responseCode);
            responseBundle.putString("responseMsg", responseMsg);
            return responseBundle;
        } /* ****************************** */

        try {
            String idn = IDN.toASCII(hostname);
            url = new URL("http", idn, "/");

            urlc = (HttpURLConnection) url.openConnection();
            urlc.setConnectTimeout(7000);
            responseCode = urlc.getResponseCode();
            responseMsg = urlc.getResponseMessage();
        } catch (NullPointerException npe) {
            Log.d(TAG, "HttpPinger: NPE: " + npe.toString());
            /* ANDROID-BUG:
            java.lang.NullPointerException
            at libcore.net.http.HttpConnection$Address.hashCode(HttpConnection.java:343)  */
        } catch (IOException ioe) {
            Log.d(TAG, "HttpPinger: IOE:", ioe);
        }
        Bundle responseBundle = new Bundle();
        responseBundle.putInt("responseCode", responseCode);
        responseBundle.putString("responseMsg", responseMsg);
        return responseBundle;
    }
}

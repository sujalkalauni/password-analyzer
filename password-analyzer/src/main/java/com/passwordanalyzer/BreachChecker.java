package com.passwordanalyzer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Checks passwords against the HaveIBeenPwned API using k-anonymity.
 *
 * How k-anonymity works:
 * 1. SHA-1 hash the password locally
 * 2. Send only the first 5 characters of the hash to HIBP API
 * 3. HIBP returns all hash suffixes matching that prefix
 * 4. We check locally if our full hash is in the returned list
 * => The actual password (or full hash) NEVER leaves your machine.
 */
public class BreachChecker {

    private static final String HIBP_API_URL = "https://api.pwnedpasswords.com/range/";
    private static final String USER_AGENT   = "PasswordAnalyzer-Java/1.0";

    /**
     * @return number of times this password has appeared in breaches (0 = safe)
     */
    public int checkBreach(String password) throws Exception {
        String sha1Hash  = sha1(password).toUpperCase();
        String prefix    = sha1Hash.substring(0, 5);
        String suffix    = sha1Hash.substring(5);

        String response  = fetchHashRange(prefix);
        return parseBreachCount(response, suffix);
    }

    private String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String fetchHashRange(String prefix) throws Exception {
        URL url = new URL(HIBP_API_URL + prefix);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("User-Agent", USER_AGENT);
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("HIBP API returned HTTP " + responseCode);
        }

        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line).append("\n");
            }
        }

        return response.toString();
    }

    /**
     * Each line in the response is: HASH_SUFFIX:COUNT
     * We look for our suffix and return the count.
     */
    private int parseBreachCount(String response, String targetSuffix) {
        for (String line : response.split("\n")) {
            String[] parts = line.trim().split(":");
            if (parts.length == 2 && parts[0].equalsIgnoreCase(targetSuffix)) {
                try {
                    return Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0; // not found = not breached
    }
}

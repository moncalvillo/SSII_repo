import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class HttpsClient {
    public static HttpsURLConnection getConnection(String username, String password, String message)
            throws KeyManagementException, CertificateException, IOException {

        try {
            URL url = new URL("https://localhost:8443/server/verification");
            String postData = String.format("{\"username\": \"%s\",\"password\": \"%s\",\"message\": \"%s\"}", username,
                    password, message);
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(null, new TrustManager[] { new CertificateTrustManager("./certificate.cer") }, null);

            SSLContext.setDefault(ctx);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.setHostnameVerifier(new CertificateHostVerifier());
            conn.getOutputStream().write(postDataBytes);

            return conn;
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
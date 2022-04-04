import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.X509TrustManager;

public class CertificateTrustManager implements X509TrustManager {

    byte[] certificate;

    public CertificateTrustManager(String certPath) throws IOException {
        this.certificate = Files.readAllBytes(Path.of(certPath));
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
            throws CertificateException {
        for (X509Certificate serverCert : paramArrayOfX509Certificate) {
            Boolean isTrustedCertificate = Arrays.equals(this.certificate, serverCert.getEncoded());
            if (!isTrustedCertificate) {
                throw new CertificateException("Certificate is not trusted");
            }

        }

    }

    @Override
    public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
            throws CertificateException {

    }
}
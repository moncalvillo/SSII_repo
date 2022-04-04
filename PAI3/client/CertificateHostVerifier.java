import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class CertificateHostVerifier implements HostnameVerifier{
    @Override
    public boolean verify(String paramString, SSLSession paramSSLSession) {
        return true;
    }
}
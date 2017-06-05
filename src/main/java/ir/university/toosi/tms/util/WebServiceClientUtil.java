package ir.university.toosi.tms.util;

import javax.net.ssl.*;
import javax.xml.ws.Service;
import java.net.URL;

/* @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi, Mostafa Rastgar
 * @version : 0.8
 */


public class WebServiceClientUtil {

    private static String nameSpace = "http://service.model.ACH.Aria.isc.ir/";
    private static String serviceName = "AgentServiceImplService";


    public void sendNewFiles(String wsdlUrl, String businessDate, String deleteQuery, String bankName, String serverUrl) {
        try {

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            Service service = Service.create(new URL(wsdlUrl), new javax.xml.namespace.QName(nameSpace, serviceName));
//            AgentService agentService = service.getPort(AgentService.class);
//            Client client = ClientProxy.getClient(agentService);
            /*if (client != null) {
                HTTPConduit conduit = (HTTPConduit) client.getConduit();
                TLSClientParameters tlsClientParameters = new TLSClientParameters();
                tlsClientParameters.setTrustManagers(trustAllCerts);
                tlsClientParameters.setDisableCNCheck(true);
                conduit.setTlsClientParameters(tlsClientParameters);
                HTTPClientPolicy policy = new HTTPClientPolicy();
                policy.setConnectionTimeout(0);
                policy.setReceiveTimeout(0);
                policy.setAllowChunking(false);
                conduit.setClient(policy);
            }*/
//            agentService.sendNewFiles(businessDate, deleteQuery, bankName, serverUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}


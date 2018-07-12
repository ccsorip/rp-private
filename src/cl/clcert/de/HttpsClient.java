package cl.clcert.de;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.io.*;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Class used to wrap the connection to the beacon services. With or without certificate verification for
 * development purposes.
 * 
 * @author constanzacsori
 *
 */
public class HttpsClient{
	
  private TrustManager[ ] get_trust_mgr() {
     TrustManager[ ] certs = new TrustManager[ ] {
        new X509TrustManager() {
           public X509Certificate[ ] getAcceptedIssuers() { return null; }
           public void checkClientTrusted(X509Certificate[ ] certs, String t) { }
           public void checkServerTrusted(X509Certificate[ ] certs, String t) { }
         }
      };
      return certs;
  }
  
  /**
   * Function that wraps the connection to the udechile beacon, takes the content and parses it into a 
   * json object ready for use in the different scenarios.
   * 
   * @param timestamp timestamp in unix time to ask for data from the becon
   * @return parsed content of the beacon response for the timestamp parameter in json format
   * @throws MalformedURLException
   * @throws IOException
   */
  protected JsonObject getContent(String timestamp) throws MalformedURLException, IOException{
	  String https_url = "https://beacon.clcert.cl/beacon/1.0/pulse/next/" + timestamp;
	  URL url = new URL(https_url);
	  HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
	  JsonObject obj = null;
	  
	  if (con.getResponseCode() == 200) {
      	//dump all the content
	        JsonParser jp = new JsonParser();
	        JsonElement root = jp.parse(new InputStreamReader(con.getInputStream())); //Convert the input stream to a json element
	        obj = root.getAsJsonObject();
	        return obj;
	        
      }else {
      	return null;
      }
  }
  
  /**
   * Function that wraps the connection to the udechile beacon, takes the content and parses it into a 
   * json object ready for use in the different scenarios. It ignores the certificate for the site,
   * only for development purposes.
   * 
   * @param timestamp timestamp in unix time to ask for data from the becon
   * @return parsed content of the beacon response for the timestamp parameter in json format
   * @throws FileNotFoundException
   */
  protected JsonObject getNoCertificateContent(String timestamp) throws FileNotFoundException{
	  String https_url = "https://beacon.clcert.cl/beacon/1.0/pulse/next/" + timestamp;
	  URL url;
	  JsonObject obj = null;
	     try {
				
		    // Create a context that doesn't check certificates.
	            SSLContext ssl_ctx = SSLContext.getInstance("TLS");
	            TrustManager[ ] trust_mgr = get_trust_mgr();
	            ssl_ctx.init(null,                // key manager
	                         trust_mgr,           // trust manager
	                         new SecureRandom()); // random number generator
	            HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());

		    url = new URL(https_url);
		    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
				
		    // Guard against "bad hostname" errors during handshake.
	            con.setHostnameVerifier(new HostnameVerifier() {
	                public boolean verify(String host, SSLSession sess) {
	                    if (host.equals("beacon.clcert.cl")) return true;
	                    else return false;
	                }
	            });

	           
            if (con.getResponseCode() == 200) {
            	//dump all the content
    	        JsonParser jp = new JsonParser();
    	        //Convert the input stream to a json element
    	        JsonElement root = jp.parse(new InputStreamReader(con.getInputStream())); 
    	        obj = root.getAsJsonObject();
    	        return obj;
            }else {
            	return null;
            }
		    
				
		 } catch (MalformedURLException e) {
			e.printStackTrace();
		 } catch (IOException e) {
			e.printStackTrace();
		 }catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		 }catch (KeyManagementException e) {
			e.printStackTrace();
	      }
	  return obj;
  }
  
/**
 * Function that exposes the parameters of the page that has the connection.
 * @param con connection generated
 */
@SuppressWarnings("unused")
private void print_https_cert(HttpsURLConnection con){
     if(con!=null){
			
     try {
				
	System.out.println("Response Code : " + con.getResponseCode());
	System.out.println("Cipher Suite : " + con.getCipherSuite());
	System.out.println("\n");
				
	Certificate[] certs = con.getServerCertificates();
	for(Certificate cert : certs){
	  System.out.println("Cert Type : " + cert.getType());
	  System.out.println("Cert Hash Code : " + cert.hashCode());
	  System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
	  System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
	  System.out.println("\n");
	}
				
				
     } catch (SSLPeerUnverifiedException e) {
	  e.printStackTrace();
     } catch (IOException e){
	  e.printStackTrace();
     }	   
   }		
  }
	

/**
 * Function that exposes the contents of the url in the connection.
 * @param con connection generated
 */
@SuppressWarnings("unused")
private void print_content(HttpsURLConnection con){
    if(con!=null){
			
    try {
		
	System.out.println("****** Content of the URL ********");
				
	BufferedReader br = 
		new BufferedReader(
			new InputStreamReader(con.getInputStream()));
				
	String input;
				
	while ((input = br.readLine()) != null){
	   System.out.println(input);
	}
	br.close();
				
     } catch (IOException e) {
	e.printStackTrace();
     }		
   }
  }
}
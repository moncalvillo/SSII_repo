import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.*;
import java.util.UUID;
import java.io.*;

import javax.swing.JOptionPane;

public class Client {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 */

	private static String challenge = "challenge";

	public static String createMAC(String hashFile, String token, String challenge) throws NoSuchAlgorithmException {
		String str = hashFile + token + challenge;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return encodedhash.toString();
    }

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public static void verificationFunction(String mac, Map<String, String> respuesta) {
		String b = "";
		System.out.println("MAC: " + mac + " Respuesta recibida: " + respuesta);
		System.out.println(respuesta.containsKey("error"));
		if(respuesta.containsKey("error")) {
			b="INTEGRITY_FILE_FAIL";
		} else {
			if(respuesta.get("mac").equals(mac)) {
				b = "INTEGRITY_FILE_OK";
			} else {
				b="INTEGRITY_FILE_FAIL";
			}
		}
		JOptionPane.showMessageDialog(null, b);
	}

	public static Map<String, String> llamar(String token, String hashFile) throws IOException {

		URL url = new URL("http://localhost:8080/server/verification");
        String postData = "id=00001&token="+token+"&hashFile="+hashFile;

		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
		
		//Respuesta
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (int c = in.read(); c != -1; c = in.read()) {
			sb.append((char) c);
		}
		System.out.println("Respuesta del servidor: " + sb);

		//Tratamiento de datos
		Map<String, String> result = new HashMap<String, String>();
		sb.deleteCharAt(0).deleteCharAt(sb.length()-1);
		String response = sb.toString();
		String[] values = response.split(",");
		for (int i = 0; i<values.length; i++) {
			String[] value = values[i].split(":");
			result.put(value[0], value[1]);
		}

		return result;
        
	} 
	
	public static void main(String[] args) throws NoSuchAlgorithmException {

		try {

			String token = generateUUID();
			String hashfile = "8aca9664752dbae36135fd0956c956fc4a370feeac67485b49bcd4b99608ae41";

			String mac = createMAC(hashfile, token, challenge);
			System.out.println("MAC generada: " + mac);

			// Llamada a la API
			Map<String, String> respuesta = llamar(token, hashfile);

			// Recibe una respuesta
			verificationFunction(mac, respuesta);


		} // end try

		// handle exception communicating with server
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}
}

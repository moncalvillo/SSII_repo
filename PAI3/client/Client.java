package ssi.pai1;

import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.io.*;

public class Client {

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 */

	// private static String challenge = "challenge";


	// public static String generateUUID() {
	// 	return UUID.randomUUID().toString();
	// }

	// public static String createMAC(String mensaje, String challenge) throws NoSuchAlgorithmException {
	// 	String str = mensaje + challenge;
	// 	MessageDigest digest = MessageDigest.getInstance("SHA-256");
	// 	byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
	// 	return toHex(encodedhash);
	// }

	// public static String toHex(byte[] bytes) {
    //     BigInteger bi = new BigInteger(1, bytes);
    //     return String.format("%0" + (bytes.length << 1) + "X", bi);
    // }

	// public static Boolean verificationFunction(String nonce, Map<String, String> respuesta) {
	// 	String mensaje = respuesta.get("mensaje");
	// 	String mac = "";
	// 	try {
	// 		mac = createMAC(mensaje+nonce, challenge);
			
	// 	} catch (NoSuchAlgorithmException e) {
	// 		e.printStackTrace();
	// 	}
	// 	String responseMAC = respuesta.get("mac");
	// 	return responseMAC.equals(mac) && mensaje.equals("OK");
	// }

	public static Map<String, String> llamar(String username, String password, String message) throws IOException {

		URL url = new URL("https://localhost:8443/server/verification");
		String postData = String.format("{\"username\": \"%s\",\"password\": \"%s\",\"message\": \"%s\"}", username, password, message);

		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);

		// Respuesta
		Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (int c = in.read(); c != -1; c = in.read()) {
			sb.append((char) c);
		}
		// System.out.println("Respuesta del servidor: " + sb);

		// Tratamiento de datos
		Map<String, String> result = new HashMap<String, String>();
		sb.deleteCharAt(0).deleteCharAt(sb.length() - 1);
		String response = sb.toString();
		String[] values = response.split(",");
		for (int i = 0; i < values.length; i++) {
			String[] value = values[i].split(":");
			result.put(value[0].replace("\"", ""), value[1].replace("\"", ""));
		}

		return result;

	}

	public static void sendData() throws IOException {
		try {
			
			Map<String, String> respuesta = new HashMap<>();
			JTextField username = new JTextField();
			JPasswordField password = new JPasswordField();
			JTextField message = new JTextField();
			Object[] dialog = {
				"Username:", username,
				"Password:", password,
				"Message:", message,
			};

			int option = JOptionPane.showConfirmDialog(null, dialog, "Dialog", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				respuesta = llamar(username.getText(), password.getSelectedText(), message.getText());
			}
			

			if(respuesta.get("200")!=null){
				JOptionPane.showMessageDialog(null, respuesta.get("200"));
			}else if (respuesta.get("403")!=null){
				JOptionPane.showMessageDialog(null, respuesta.get("403"));
			}
			
		} 
		catch (IOException ioe) {
		 	ioe.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		sendData();
	}
}

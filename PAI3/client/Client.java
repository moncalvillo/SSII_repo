
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.*;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.io.*;

public class Client {

	public static Map<String, String> llamar(String username, String password, String message)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, CertificateException {

		var conn = HttpsClient.getConnection(username, password, message);
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

	public static void sendData() throws IOException, KeyManagementException, NoSuchAlgorithmException {
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
				respuesta = llamar(username.getText(), password.getText(), message.getText());
			}
			JOptionPane.showMessageDialog(null, respuesta.get("data"));

		} catch (Exception ioe) {
			JOptionPane.showMessageDialog(null, "Se ha detectado un certificado no valido");
		}
	}

	public static void main(String[] args) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		sendData();

	}
}

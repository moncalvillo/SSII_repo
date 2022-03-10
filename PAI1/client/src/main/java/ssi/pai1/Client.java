package ssi.pai1;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.io.*;

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
		return Node.toHex(encodedhash);
	}

	public static String generateUUID() {
		return UUID.randomUUID().toString();
	}

	public static Boolean verificationFunction(String mac, Map<String, String> respuesta) {
		return !respuesta.containsKey("error") && respuesta.get("mac").equals(mac);
	}

	public static Map<String, String> llamar(String id, String token, String hashFile, Integer numbFiles) throws IOException {

		URL url = new URL("http://localhost:8080/server/verification");
		String postData = "path=" + id + "&token=" + token + "&hashFile=" + hashFile + "&numbFiles=" + numbFiles;

		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
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

	public static Boolean verifyFile(Node node) {
		try {

			String token = generateUUID();

			String hashfile = node.getHashFile();
			String id = node.getId();
			String mac = createMAC(hashfile, token, challenge);
			Integer numbFiles = node.getNumberOfChildren();
			// Llamada a la API
			Map<String, String> respuesta = llamar(id, token, hashfile, numbFiles);

			// Recibe una respuesta
			Boolean isOk = verificationFunction(mac, respuesta);
			return isOk;

		} // end try

		// handle exception communicating with server
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return false;

	}

	public static String[] getFilePaths() {
		URL url;
		try {
			url = new URL("http://localhost:8080/server/files");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");

			// Respuesta
			Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			for (int c = in.read(); c != -1; c = in.read()) {
				sb.append((char) c);
			}

			String arr = sb.toString().replaceAll("\"", "");
			return arr.substring(1, arr.length() - 1).split(",");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new String[0];
	}

	public static void verifyIntegrity() {
		System.out.println("Iniciando análisis de integridad de ficheros\n");
		String[] paths = getFilePaths();

		Node root = TreeBuilder.buildTree(paths);
		//System.out.println(root);

		Integer totalFiles = root.getNumberOfChildren();
		IntegrityProgress integrityProgress = new IntegrityProgress(totalFiles, 5.0);

		IntegrityChecker.bfsIterate(root, (node) -> {
			Boolean isOk = verifyFile(node);
			IntegrityChecker.processProgress(node, isOk, integrityProgress);
			return isOk;
		});

		integrityProgress.showResults();

	}

	public static void main(String[] args) throws IOException {
		Config config = new Config(".config");
		Long interval = config.getTime();

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(Client::verifyIntegrity, 0, interval, TimeUnit.MILLISECONDS);
	}
}

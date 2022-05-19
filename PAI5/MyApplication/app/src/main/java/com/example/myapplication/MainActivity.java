package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.net.*;
import java.security.*;

import java.io.*;


public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String url = "http://localhost:8080/server/verification";
    protected static int port = 7070;

    private static EditText sillas;
    private static EditText sillones;
    private static EditText mesas;
    private static EditText camas;
    private static EditText num;
    private static String numSillas;
    private static String numSillones;
    private static String numCamas;
    private static String numEmpleado;
    private static String numMesas;
    private static HttpURLConnection conn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });


    }

    // Creación de un cuadro de dialogo para confirmar pedido
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDialog() throws Resources.NotFoundException {

        sillas = (EditText) findViewById(R.id.num_sillas);
        camas = (EditText) findViewById(R.id.num_camas);
        sillones = (EditText) findViewById(R.id.num_sillones);
        mesas = (EditText) findViewById(R.id.num_mesas);
        num = (EditText) findViewById(R.id.text_number);
        numSillas = sillas.getText().toString();
        numSillones = sillones.getText().toString();
        numMesas = mesas.getText().toString();
        numCamas = camas.getText().toString();
        numEmpleado = num.getText().toString();



        System.out.println(numCamas  + numSillas + numSillones + numMesas  + numEmpleado );
        if (numSillas.isEmpty() && numSillones.isEmpty() && numMesas.isEmpty() && numCamas.isEmpty()) {
            // Mostramos un mensaje emergente;
            Toast.makeText(getApplicationContext(), "Solicita algún material", Toast.LENGTH_SHORT).show();
        }else if(numEmpleado.isEmpty()){
            Toast.makeText(getApplicationContext(), "El numero de empleado no debe estar vacío", Toast.LENGTH_SHORT).show();
        }else if(validateNumbers()){
            Toast.makeText(getApplicationContext(), "El numero de productos debe estar comprendido entre 0 y 300.", Toast.LENGTH_LONG).show();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Enviar")
                    .setMessage("Se va a proceder al envio")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                // Catch ok button and send information
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    SecureRandom nonce = new SecureRandom();

                                    byte[] firma = null;
                                    try{
                                        // 1. Extraer los datos de la vista

                                        String message = numCamas + numMesas + numSillas +  numSillones +
                                                numEmpleado + nonce.toString();
                                        // 2. Firmar los datos

                                        Signature sg = Signature.getInstance("SHA256withRSA");
                                        sg.initSign((PrivateKey) getPrivateKey(numEmpleado));
                                        sg.update(message.getBytes());

                                        firma = sg.sign();
                                    } catch (NoSuchAlgorithmException e) {
                                        e.printStackTrace();
                                    } catch (SignatureException e) {
                                        e.printStackTrace();
                                    } catch (InvalidKeyException e) {
                                        e.printStackTrace();
                                    } catch (CertificateException e) {
                                        e.printStackTrace();
                                    } catch (KeyStoreException e) {
                                        e.printStackTrace();
                                    }


                                    // 3. Enviar los datos
                                    try {
                                        URL peticion = new URL(url);
                                        String postData = String.format("{\"camas\": \"%s\",\"sillas\": \"%s\",\"sillones\": \"%s\",\"mesas\": \"%s\",\"empleado\": \"%s\",\"nonce\": \"%s\",\"firma\": \"%s\"}",
                                                numCamas, numSillas, numSillones, numMesas, numEmpleado, nonce, Base64.getEncoder().encode(firma));

                                        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                                        conn = (HttpURLConnection) peticion.openConnection();
                                        conn.setRequestMethod("POST");
                                        conn.setRequestProperty("Content-Type", "application/json");
                                        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                                        conn.setDoOutput(true);
                                        conn.getOutputStream().write(postDataBytes);
                                        Map<String, String> result = new HashMap<String, String>();
                                        // Respuesta
                                        if(conn != null) {
                                            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                            StringBuilder sb = new StringBuilder();
                                            for (int c = in.read(); c != -1; c = in.read()) {
                                                sb.append((char) c);
                                            }
                                            // System.out.println("Respuesta del servidor: " + sb);

                                            // Tratamiento de datos
                                            sb.deleteCharAt(0).deleteCharAt(sb.length() - 1);
                                            String response = sb.toString();
                                            String[] values = response.split(",");
                                            for (int i = 0; i < values.length; i++) {
                                                String[] value = values[i].split(":");
                                                result.put(value[0].replace("\"", ""), value[1].replace("\"", ""));
                                            }
                                        }
                                        if(result.containsKey("error")){
                                            Toast.makeText(MainActivity.this, "Ha habido un error", Toast.LENGTH_SHORT).show();
                                            Intent i = getBaseContext().getPackageManager()
                                                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                        }

                                    }catch (IOException e){

                                    }



                                    Toast.makeText(MainActivity.this, "Petición enviada correctamente", Toast.LENGTH_SHORT).show();
                                }
                            }

                    )
                    .setNegativeButton(android.R.string.no, null)
                    .show();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Boolean validateNumbers(){
        List<String> list = new ArrayList<String>();
        list.add(numSillas);
        list.add(numSillones);
        list.add(numMesas);
        list.add(numCamas);
        return list.stream().filter(x -> !x.equals("")).map(x -> Integer.valueOf(x)).filter(x -> x < 0 || x > 300).count() > 0;

    }

    public Key getPrivateKey(String id) throws CertificateException, KeyStoreException {
        KeyStore keystore;
        try {
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(readFile(), "password".toCharArray());

            // Get public key
            Key key = keystore.getKey(id, "password".toCharArray());
            return key;
        } catch (KeyStoreException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        return null;

    }

    public InputStream readFile(){
        BufferedReader reader = null;
        try {
            return  getAssets().open("springboot.p12");

            // do reading, usually loop until end of file reading

        } catch (IOException e) {
            //log the

        }
        return null;
    }


}

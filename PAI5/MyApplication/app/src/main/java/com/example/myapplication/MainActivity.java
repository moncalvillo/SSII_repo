package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.net.*;
import java.security.*;

import java.io.*;
import java.util.UUID;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    // Setup Server information
    protected static String url = "http://10.0.2.2:8081/server/verification";

    protected final byte[] sk1 = hexStringToByteArray("308204BF020100300D06092A864886F70D0101010500048204A9308204A50201000282010100D27A071DB78ABF9E307D8240049BE63202066889EEA766BCAADCFC5AF5EBBCF13A934E67162701639049177FE04AAAC287F7F13790E38CF5ABC34E798C38227F63626ECDA32C354818B20B2632EC29BC6430B486BBCC6CC08AA9756C96FE5CB721108A9E158B27A22C6CBE92F646F86B03ABBB87C2BA139FCECB2B297914E517A69A50626CADF139439E90E10CE99B7B2A7028B8140169C614DCFC5BEEF7E4F0D8E4BCBC65EC0E4CFE47B0C24D537DB466665FEC4E4C70667C0007002059ECDFE729C11BA2AF14F33A4071E585C059CD003FE5EED960D6AB4FEDB3F096367CC24A87DC86B10F61665860846B9F99EF66EE77B5E48AF1DB9088F6C1B0F093F03F0203010001028201010081907AC6F150690AA100F1E824AE2F734349DC9A641E0AA143710BAB96CD0A1F59168AE1EC821059D0DD1DA92B51EB721AAF277F3422CF87FFB7179CBD13653113E5E91D8F3FCC80D82AE9B01712C7D25FBE97B284F29F539B481BAFBFF74EEDB05DF566C10E4B548A925A722F469AEF8D6FEEDDF0A209288BED0761DD565E7B25CAD59389FCAD0A08F6F25AFE74D2E322F715D7A82F73BA717F5AE04366B1FE5D1A58698780359ACF90F8E17A5FD6F4A4CEF5191EF5716DEE9485E981432FFE16DABE67CFC6305D25DF5C916151E3CD78928F19C3C21A09D30644BBBBF647BEC4758D153E1B2FD810EA023456B1FB21313E9E0857AB682085DBB86499AA886902818100ED2874BD0FEBE043F53AFE773E13F525B45EBB6161FB641E7B26B37757370610F278CB13D1E9946357DAF52215A71E0845A5708F7D2A68D6A704BDA15B537DE45B4B8D876B77639666D839176321C2E92E023111050BFEDC3070E85DF6B8AF4263CF7F47BD373484BD5915D408071E3506EB3067CD1602A7BB797F21EE8FC13D02818100E332E6ADB5A54B9161D9DA046CFAFDC859571715B0E7F02E903388140FF6E698C8D3E3447BC22CFC2A59D1FA7E988F8A58752E96E9109D62967E63BFA587103402A6EE057EF7937DE5C2FC82D6D6BA60E5902B63284DAC7CFC3E564E68EE95AEFBF4F2CE6CCEC09FBBCC131B0AA2D0C4C88D4B38EC0166DAB0B104F4217A172B0281800901C5553CDA8F7CE53E6555B004E0059B91117AC456E0D98B81CFC51389E27018556019139AD468E5784A610E0377CF869D9EE5C4322D2321DABB3CFA93F42F0D0C9486751D66A9DAA4119F02C3D07C3CA416AAF7CF19D1D10128C210D1B8CE43AE28BE57C055FD4897AE8D8BF48140305014598CDF6E2062AD5D97B350C05D028181008C844DC18007D47DFD2BF4B82A02EDF4FCB46D759FED4352392375F51A8E94BE47EF2CAF8D6F61CD30104F5B02F32E4BDD3BD46DC785DC213E7CBB0AABC0A617D4D0138458AB9C90100B918B067ED5D4DA06599F412D1112581BE1DB2AF0ECA8C5F2103DF573C614C4DC89B07EDA860E3F0F8C478F47AAAA74FF76D5DAE29CF302818100EB0F509A3668B05C7DCA7BC2A6A943DA0A71A4A0353A1B945650EB0A508A39D01B8B785993B465FC7B4666FF84934E7BC7219AB15C3E1CEDCC75DD46EE5C5D7E58806B8EF915B74A01D0BF3CCD2E9955D5743C8554B5C298512129B5FE6F53F5DB1BC58CD77CADA98060187A639185889803AF606AA7DD6D7ECB97BCDE5876E4");
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
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
        // Capturamos el boton de Enviar
        View button = findViewById(R.id.button_send);

        // Llama al listener del boton Enviar
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                try {
                    showDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    // Creación de un cuadro de dialogo para confirmar pedido
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDialog() throws Resources.NotFoundException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, CertificateException, IOException, KeyManagementException {

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

                                    String nonce = UUID.randomUUID().toString();

                                    byte[] firma = null;
                                    try{
                                        // 1. Extraer los datos de la vista

                                        String message = numCamas + numMesas + numSillas +  numSillones +
                                                numEmpleado + nonce.toString();
                                        // 2. Firmar los datos

                                        Signature sg = Signature.getInstance("SHA256withRSA");
                                        sg.initSign(getPrivateKeyUser());

                                        sg.update(message.getBytes());

                                        firma = sg.sign();

                                        String hex = toHex(firma);
                                        byte[] byt = hexStringToByteArray(hex);
                                        for(int i= 0; i<byt.length; i++){
                                            if(byt[i] != firma[i]){
                                                System.out.println("FALLA");
                                                break;
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                    // 3. Enviar los datos
                                    try {

                                        URL peticion = new URL(url);
                                        String postData = String.format("{\"camas\": \"%s\",\"sillas\": \"%s\",\"sillones\": \"%s\",\"mesas\": \"%s\",\"idEmpleado\": \"%s\",\"nonce\": \"%s\",\"firma\": \"%s\"}",
                                                numCamas, numSillas, numSillones, numMesas, numEmpleado, nonce, toHex(firma));


                                        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                                        conn = (HttpURLConnection) peticion.openConnection();
                                        conn.setRequestMethod("POST");
                                        conn.setRequestProperty("Content-Type", "application/json");
                                        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                                        conn.setDoOutput(true);
                                        System.out.println(new String(postDataBytes, Charset.defaultCharset()));
                                        //conn.getOutputStream().write(postDataBytes);

                                        DataOutputStream d = new DataOutputStream(conn.getOutputStream());
                                        d.write(postDataBytes);
                                        d.flush();
                                        d.close();

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
                                        if(!result.get("errors").equals("[]")){
                                            Toast.makeText(MainActivity.this, result.get("errors"), Toast.LENGTH_SHORT).show();
                                            Intent i = getBaseContext().getPackageManager()
                                                    .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                        }

                                    }catch (IOException e){
                                        e.printStackTrace();
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
    public static Boolean validateNumbers() {
        List<String> list = new ArrayList<String>();
        list.add(numSillas);
        list.add(numSillones);
        list.add(numMesas);
        list.add(numCamas);
        return list.stream().filter(x -> !x.equals("")).map(x -> Integer.valueOf(x)).filter(x -> x < 0 || x > 300).count() > 0;

    }

    public PrivateKey getPrivateKeyUser() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance("RSA"); // or "EC" or whatever
        if(numEmpleado.equals("1")){
            return kf.generatePrivate(new PKCS8EncodedKeySpec(sk1));
        }
        // If added more employers, add its keyfactories
        return kf.generatePrivate(new PKCS8EncodedKeySpec(sk1));
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }


}

package ssi.pai2.server.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import ssi.pai2.server.utils.Config;
import ssi.pai2.server.utils.SendEmail;

@Service
public class ReportFile {

    private static final String logPath = "./src/main/resources/logs/logs.log";
    private static final String reportPath = "./src/main/resources/reports/report";
    private static final Integer interval = 120;

    private Integer transactionsOK = 0;
    private Integer errors = 0;
    private Integer total = 0;
    private Double ratioOK = 0.;
    private Double ratioError = 0.;
    private Map<String, String> ficheros = new HashMap<String, String>();

    private void readLogFile() throws IOException, FileNotFoundException, ParseException {
        FileReader logFile = new FileReader(logPath);
        BufferedReader reader = new BufferedReader(logFile);
        String log = reader.readLine();
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(interval);
        while (log != null) {
            log = log.replace("[", "").replace("]", "");
            String[] arr = log.split("~");
            LocalDateTime dateTime = LocalDateTime.parse(arr[1]);
            if (dateTime.isAfter(deadline)) {
                if (arr[0].equals("MESSAGE_OK")) {
                    this.transactionsOK += 1;
                } else {
                    this.addFiles(arr[3]);
                    this.errors += 1;
                }

            }
            log = reader.readLine();
        }
        total = transactionsOK + errors;
        if (total != 0) {
            this.ratioError = (errors / (double) total) * 100.;
            this.ratioOK = (transactionsOK / (double) total) * 100.;
        } else {
            this.ratioError = 0.;
            this.ratioOK = 0.;
        }
        reader.close();
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void report() throws FileNotFoundException, IOException, ParseException {
        readLogFile();

        File file = new File(reportPath + LocalDateTime.now().getMonth().name() + ".txt");
        file = createFile(file, 0);

        FileWriter writer = new FileWriter(file);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");


        String content = "Informe de analisis de integridad " + LocalDateTime.now().format(formatter) + "\n";

        content += "\n Transacciones analizadas: " + total;
        content += "\n Transacciones verificadas con exito: " + transactionsOK + " ( "
                + String.format("%.2f%%", ratioOK) + " )";
        content += "\n Transacciones erroneas: " + errors + " ( " + String.format("%.2f%%", ratioError) + " )";

        writer.write(content);
        writer.close();

        // Los correos deben ser gmail. Poner el correo destino
        // En caso de que falle, se debe a la caducidad de la contraseña de aplicacion.
        // Cambiar correo origen y crear contraseña de aplicacion y activar verificacion
        // en dos pasos.
        // Para crear una contrase�a de aplicacion entre en
        // https://myaccount.google.com/security?hl=es
        // La contrase�a generada se copia en el parametro password
        String emailReceiver = Config.configs.get("EMAIL_RECEIVER");
        SendEmail.sendEmail(emailReceiver, "team16ssii@gmail.com", content.replace("\n", "<br>"),
                "INFORMES PERIODICOS INTEGRIDAD", "nuazlkzwhfvfouhy");

    }

    private File createFile(File file, Integer i) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
            return file;
        } else {
            ++i;
            file = new File(reportPath + LocalDateTime.now().getMonth().name() + i + ".txt");
            return createFile(file, i);
        }
    }

    private void addFiles(String data) {
        Integer indexPathEnd = data.indexOf(",");
        String path = data.substring(data.indexOf("=") + 2, indexPathEnd).trim();
        Integer index2 = data.indexOf("=", indexPathEnd) + 2;
        String hash = data.substring(index2, data.indexOf(",", index2)).trim();
        this.ficheros.putIfAbsent(path, hash);
    }
}

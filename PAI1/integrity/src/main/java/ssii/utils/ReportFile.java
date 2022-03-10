package ssii.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ReportFile {

    private static final String logPath = "./src/main/resources/logs/logs.log";
    private static final String reportPath = "./src/main/resources/reports/report";
    private static final Integer interval = 120;

    private Integer filesOK = 0;
    private Integer errors = 0;
    private Integer total = 0;
    private Double ratioOK = 0.;
    private Double ratioError = 0.;
    private Integer requests = 0;
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
                this.requests += 1;
                if (arr[0].equals("HASH_OK")) {
                    this.filesOK += this.getNumberFiles(log);
                } else {
                    this.addFiles(arr[3]);
                    this.errors += 1;
                }

            }
            log = reader.readLine();
        }

        this.total = filesOK + errors;
        this.ratioOK = (this.filesOK / (double) this.total) * 100.;
        this.ratioError = (this.errors / (double) this.total) * 100.;
        reader.close();
    }

    public void report() throws FileNotFoundException, IOException, ParseException {
        readLogFile();

        File file = new File(reportPath + LocalDateTime.now().getMonth().name() + ".txt");
        file = createFile(file, 0);

        FileWriter writer = new FileWriter(file);

        String content = "Informe de análisis de integridad del mes " + LocalDateTime.now().getMonth().name() + "\n";

        content += "\n Logs analizados: " + requests;
        content += "\n Ficheros analizados: " + total;
        content += "\n Ficheros verificados con exito: " + filesOK + " ( " + String.format("%.0f%%", ratioOK) + " )";
        content += "\n Ficheros erroneos: " + errors + " ( " + String.format("%.0f%%", ratioError) + " )";
        content += "\n Lista de ficheros con errores: ";

        for (Entry<String, String> entry : ficheros.entrySet()) {
            content += "\n" + entry.getKey() + "con hash " + entry.getValue();
        }

        writer.write(content);
        writer.close();

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

    private Integer getNumberFiles(String log) {
        Integer poshash = log.indexOf("#");
        Integer posdot = log.indexOf(".", poshash);
        return Integer.valueOf(log.substring(poshash + 1, posdot));
    }

    private void addFiles(String data) {
        Integer indexPathEnd = data.indexOf(",");
        String path = data.substring(data.indexOf("=") + 2, indexPathEnd).trim();
        Integer index2 = data.indexOf("=", indexPathEnd) + 2;
        String hash = data.substring(index2, data.indexOf(",", index2)).trim();
        this.ficheros.putIfAbsent(path, hash);
    }
}

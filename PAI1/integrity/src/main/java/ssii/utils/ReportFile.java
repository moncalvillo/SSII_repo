package ssii.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportFile {
    
    private static final String path = "./src/main/resources/logs/logs.log";
    private static final Integer interval  = 120;

    private Integer filesOK = 0;
    private Integer errors = 0;
    private Integer total = 0;
    private Double ratioOK = 0.;
    private Double ratioError = 0.;
    private Integer requests = 0;
    private Map<String,String> ficheros = new HashMap<String,String>();

    

    private void readLogFile() throws IOException, FileNotFoundException, ParseException{
        FileReader logFile = new FileReader(path);
        BufferedReader reader = new BufferedReader(logFile);
        String log = reader.readLine();
        LocalDateTime deadline = LocalDateTime.now().minusMinutes(interval);
        while(log != null){
            log = log.replace("[", "").replace("]", "");
            String[] arr = log.split("~");
            LocalDateTime dateTime = LocalDateTime.parse(arr[1]);
            if(dateTime.isAfter(deadline)){
                this.requests += 1;
                if(arr[0].equals("HASH_OK")){
                    this.filesOK += this.getNumberFiles(log);
                }else{
                    this.addFiles(arr[3]);
                    this.errors += 1;
                }

            }
            log = reader.readLine();
        }

        this.total = filesOK+errors;

        reader.close();
        System.out.println(ficheros.entrySet());
    }

    public void report() throws FileNotFoundException, IOException, ParseException {
        readLogFile();

        
    }

    private Integer getNumberFiles(String log){
        Integer poshash = log.indexOf("#");
        Integer posdot = log.indexOf(".", poshash) ;
        return Integer.valueOf(log.substring(poshash+1, posdot));
    }

    private void addFiles(String data){
        Integer indexPathEnd = data.indexOf(",");
        String path = data.substring(data.indexOf("=")+2, indexPathEnd).trim();
        Integer index2 = data.indexOf("=", indexPathEnd)+2;
        String hash = data.substring(index2, data.indexOf(",",index2)).trim();
        this.ficheros.putIfAbsent(path, hash);
    }
}

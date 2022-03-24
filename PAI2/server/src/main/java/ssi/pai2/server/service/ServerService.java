package ssi.pai2.server.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssi.pai2.server.model.Message;
import ssi.pai2.server.repository.ServerRepository;
import ssi.pai2.server.utils.Config;



@Service
public class ServerService {


    private ServerRepository serverRepo;
    private ReportFile reportFile;

    @Autowired
    public ServerService(ServerRepository serverRepository, ReportFile reportFile) throws IOException{
        this.serverRepo = serverRepository;
        this.reportFile = reportFile;
        
        Config config = new Config(".config");
		Long interval = config.getTime();
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(() -> {
			try {
				this.report();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}, 0, interval, TimeUnit.MILLISECONDS);
        
    }

    public boolean verify(Message entity, String challenge) throws NoSuchAlgorithmException {
        boolean b = serverRepo.findNonce(entity.getNonce()) == 0;
        System.out.println(entity.getOrigen().toString() + entity.getDestino().toString() + entity.getCantidad().toString() + entity.getNonce().toString());
        String str = entity.getOrigen().toString() + entity.getDestino().toString() + entity.getCantidad().toString() + entity.getNonce().toString() + challenge;
        boolean mac = createMAC(str, challenge).equals(entity.getMac());
        return b && mac;
    }


    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }


    public void report() throws FileNotFoundException, IOException, ParseException{

        
        
        reportFile.report();
    }

    public String createMAC(String cosas, String challenge) throws NoSuchAlgorithmException {
        String str = cosas + challenge;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return toHex(encodedhash);
    }
}

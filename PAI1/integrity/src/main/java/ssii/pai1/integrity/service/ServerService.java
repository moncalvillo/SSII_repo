package ssii.pai1.integrity.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssii.pai1.integrity.model.IntegrityFile;
import ssii.pai1.integrity.model.Item;
import ssii.pai1.integrity.model.Node;
import ssii.pai1.integrity.repository.FileRepository;
import ssii.pai1.integrity.repository.ServerRepository;
import ssii.utils.Config;

@Service
public class ServerService {


    private ServerRepository serverRepo;
    private FileRepository fileRepository;
    private ReportFile reportFile;

    @Autowired
    public ServerService(ServerRepository serverRepository, FileRepository fileRepo, ReportFile reportFile) throws IOException{
        System.out.println(serverRepository + " "+ fileRepo + " " + reportFile);
        this.serverRepo = serverRepository;
        this.fileRepository = fileRepo;
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

    public boolean verify(Item entity) {
        Optional<Item> foundEntity = serverRepo.findItemByPath(entity.getPath());
        if (foundEntity.isPresent() && foundEntity.get().getHashFile().equals(entity.getHashFile())) {
            return true;
        } else {
            return false;
        }
    }

    public String createMAC(String hashFile, String token, String challenge) throws NoSuchAlgorithmException {
        String str = hashFile + token + challenge;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
        return toHex(encodedhash);
    }

    public static String toHex(byte[] bytes) {
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
    }

    public List<String> getFiles() {
        return fileRepository.findPaths();
    }

    public void populateDatabase(String directory) throws IOException {
        String[] files = TreeBuilder.getAllFilesFromDirectory(directory);

        fileRepository.deleteAll();
        serverRepo.deleteAll();

        List<IntegrityFile> integrityFiles = Stream.of(files).map(IntegrityFile::new).collect(Collectors.toList());
        fileRepository.saveAll(integrityFiles);
        files = getFiles().toArray(new String[0]);
        Node root = TreeBuilder.buildTree(files);

        IntegrityChecker.bfsIterate(root, (node) -> {
            Item item = new Item(node.getId(), node.getHashFile());
            serverRepo.save(item);
            return false;
        });
    }

    public void report() throws FileNotFoundException, IOException, ParseException{

        
        reportFile.setTotal((int) fileRepository.count());
        reportFile.report();
    }
}

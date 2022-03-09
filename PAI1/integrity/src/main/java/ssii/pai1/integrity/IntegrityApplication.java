package ssii.pai1.integrity;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ssii.pai1.integrity.service.ServerService;
import ssii.utils.Config;

@SpringBootApplication
public class IntegrityApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(IntegrityApplication.class, args);


		Config config = new Config(".config");
		Long interval = config.getTime();
		
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleWithFixedDelay(ServerService::report, 0, 3, TimeUnit.MINUTES);
	}

}

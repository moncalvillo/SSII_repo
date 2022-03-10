package src.main.java.pai1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    Double DAYS_INTERVAL = 0.;
    Double HOURS_INTERVAL = 0.;
    Double MINUTES_INTERVAL = 0.;

    Map<String, String> configs;

    public Config(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(fileName));
        this.configs = new HashMap<>();

        for (String line : lines) {
            String[] config = line.split("=");
            String key = config[0].trim();
            String value = config[1].trim();

            this.configs.put(key, value);
        }

        this.DAYS_INTERVAL = Double.valueOf(this.configs.get("DAYS_INTERVAL"));
        this.HOURS_INTERVAL = Double.valueOf(this.configs.get("HOURS_INTERVAL"));
        this.MINUTES_INTERVAL = Double.valueOf(this.configs.get("MINUTES_INTERVAL"));

    }

    public Long getTime() {
        return Math.round(this.DAYS_INTERVAL * 24 * 3600 * 1000 + this.HOURS_INTERVAL * 3600 * 1000
                + this.MINUTES_INTERVAL * 60 * 1000);
    }
}

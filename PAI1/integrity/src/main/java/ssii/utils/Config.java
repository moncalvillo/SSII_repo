package ssii.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public static Double DAYS_INTERVAL = 0.;
    public static Double HOURS_INTERVAL = 0.;
    public static Double MINUTES_INTERVAL = 0.;

    public static Map<String, String> configs;

    public Config(String fileName) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(fileName));
        configs = new HashMap<>();

        for (String line : lines) {
            String[] config = line.split("=");
            String key = config[0].trim();
            String value = config[1].trim();

            configs.put(key, value);
        }

        DAYS_INTERVAL = Double.valueOf(configs.get("DAYS_INTERVAL"));
        HOURS_INTERVAL = Double.valueOf(configs.get("HOURS_INTERVAL"));
        MINUTES_INTERVAL = Double.valueOf(configs.get("MINUTES_INTERVAL"));

    }

    public static Long getTime() {
        return Math.round(DAYS_INTERVAL * 24 * 3600 * 1000 + HOURS_INTERVAL * 3600 * 1000
                + MINUTES_INTERVAL * 60 * 1000);
    }
}

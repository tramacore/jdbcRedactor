package ribsjdbc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ribsjdbc.utilities.ConfigManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {

    private static final String CONFIG_FILE_PATH = "app/src/main/resources/assets/config.json";

    public static Map<String, String> parsing() {

        ConfigManager.checkAndCreateConfig();

        try {
            File file = new File(CONFIG_FILE_PATH);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
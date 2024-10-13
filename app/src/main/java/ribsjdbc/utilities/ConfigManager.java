package ribsjdbc.utilities;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static final String CONFIG_FILE_PATH = "app/src/main/resources/assets/config.json";

    public static void checkAndCreateConfig() {
        File configFile = new File(CONFIG_FILE_PATH);

        if (!configFile.exists()) {
            promptUserForConfig(configFile);
        }
    }

    private static void promptUserForConfig(File configFile) {
        JTextField urlField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] message = {
                "URL базы данных:", urlField,
                "Имя пользователя:", usernameField,
                "Пароль:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Введите конфигурацию базы данных", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String url = urlField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Сохранение в config.json
            Map<String, String> config = new HashMap<>();
            config.put("url", url);
            config.put("username", username);
            config.put("password", password);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                objectMapper.writeValue(configFile, config);
                JOptionPane.showMessageDialog(null, "Конфигурация успешно сохранена!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Ошибка при сохранении конфигурации: " + e.getMessage());
            }
        } else {
            System.exit(0); // Выход, если пользователь отменил
        }
    }
}
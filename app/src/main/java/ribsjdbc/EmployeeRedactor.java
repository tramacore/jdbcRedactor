package ribsjdbc;

import ribsjdbc.utilities.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class EmployeeRedactor extends JFrame {
    private final JTextField nameField;
    private final JTextField ageField;
    private final JButton submitButton;
    private final JTable userTable;
    private final DefaultTableModel tableModel;
    private final JTextField idField;
    private final JButton deleteButton;


    public EmployeeRedactor() {
        setTitle("Employee Redactor");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Создание компонентов
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        nameField = createInputField(inputPanel, "Имя:");
        ageField = createInputField(inputPanel, "Возраст:");
        idField = createInputField(inputPanel, "ID:");

        submitButton = new JButton("Отправить");
        deleteButton = new JButton("Удалить");

        // Добавление кнопок на панель ввода
        inputPanel.add(submitButton);
        inputPanel.add(deleteButton);

        add(inputPanel, BorderLayout.NORTH);

        // Создание таблицы
        tableModel = new DefaultTableModel(new String[]{"ID", "Имя", "Возраст"}, 0);
        userTable = new JTable(tableModel);
        add(new JScrollPane(userTable), BorderLayout.CENTER);

        // Обработчики событий
        submitButton.addActionListener(this::handleSubmit);
        deleteButton.addActionListener(this::handleDelete);

        loadData(); // Загружаем данные при старте приложения
    }

    private JTextField createInputField(JPanel panel, String label) {
        panel.add(new JLabel(label));
        JTextField textField = new JTextField();
        panel.add(textField);
        return textField;
    }

    public void start() {
        setVisible(true);
        setLocationRelativeTo(null); // Центрируем окно на экране
    }

    private void handleSubmit(ActionEvent e) {
        String name = nameField.getText();
        try {
            int age = Integer.parseInt(ageField.getText());
            saveToDatabase(name, age);
            loadData(); // Обновляем данные в таблице
            clearFields();
            showMessage("Данные успешно сохранены!");
        } catch (NumberFormatException ex) {
            showMessage("Пожалуйста, введите корректный возраст.");
        } catch (SQLException ex) {
            showMessage("Ошибка при сохранении данных: " + ex.getMessage());
        }
    }

    private void handleDelete(ActionEvent e) {
        try {
            int id = Integer.parseInt(idField.getText());
            deleteFromDatabase(id);
            loadData(); // Обновляем данные в таблице
            idField.setText("");
            showMessage("Данные успешно удалены!");
        } catch (NumberFormatException ex) {
            showMessage("ID некорректен");
        } catch (SQLException ex) {
            showMessage("Ошибка при удалении данных: " + ex.getMessage());
        }
    }

    private void clearFields() {
        nameField.setText("");
        ageField.setText("");
        idField.setText("");
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    private void saveToDatabase(String name, int age) throws SQLException {
        String query = "INSERT INTO public.employee (name, age) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, capitalizeFirstLetter(name));
            pstmt.setInt(2, age);
            pstmt.executeUpdate();
        }
    }

    private void deleteFromDatabase(int id) throws SQLException {
        String query = "DELETE FROM public.employee WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private void loadData() {
        String query = "SELECT id, name, age FROM public.employee";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            tableModel.setRowCount(0); // Очищаем текущие данные в таблице

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                tableModel.addRow(new Object[]{id, name, age});
            }
        } catch (SQLException e) {
            showMessage("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

    private Connection getConnection() throws SQLException {
        String url = User.getUrl();
        String user = User.getUsername();
        String password = User.getPassword();

        return DriverManager.getConnection(url, user, password);
    }

    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
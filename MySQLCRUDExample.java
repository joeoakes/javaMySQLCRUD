import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLCRUDExample {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/your_database_name";
    private static final String USERNAME = "your_username";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Create
            insertEmployee(connection, "John", "Doe", "john@example.com");

            // Read
            List<Employee> employees = getAllEmployees(connection);
            for (Employee employee : employees) {
                System.out.println(employee);
            }

            // Update
            updateEmployee(connection, 1, "Updated First Name");

            // Read again
            employees = getAllEmployees(connection);
            for (Employee employee : employees) {
                System.out.println(employee);
            }

            // Delete
            deleteEmployee(connection, 1);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void insertEmployee(Connection connection, String firstName, String lastName, String email) throws SQLException {
        String sql = "INSERT INTO employees (first_name, last_name, email) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        }
    }

    private static List<Employee> getAllEmployees(Connection connection) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT id, first_name, last_name, email FROM employees";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                employees.add(new Employee(id, firstName, lastName, email));
            }
        }
        return employees;
    }

    private static void updateEmployee(Connection connection, int id, String newFirstName) throws SQLException {
        String sql = "UPDATE employees SET first_name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newFirstName);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        }
    }

    private static void deleteEmployee(Connection connection, int id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}

class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private String email;

    public Employee(int id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

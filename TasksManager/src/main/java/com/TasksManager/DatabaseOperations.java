package com.TasksManager;

import com.TasksManager.entity.Task;
import com.TasksManager.entity.User;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Component
public class DatabaseOperations {
    // SQL Server is running on a virtual machine with Ubuntu 22.04 running on VirtualBox.
    // The virtual machine is isolated from the network.
    String dbURL = "jdbc:mysql://100.100.100.100:3306/taskManagerDb";
    String username = "sqlTest";
    String userpassword = "ZAQ!2wsx";
    String sql;

    public boolean isUserExist(User user){
        sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try{
            Connection connection = DriverManager.getConnection(dbURL, username, userpassword);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean canUserLogin(String login, String password){
        sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try{
            Connection connection = DriverManager.getConnection(dbURL, username, userpassword);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void registerToDatabase(User user){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbURL, username, userpassword);

            sql = "INSERT INTO users (username, password, first_name, last_name, email) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFname());
            statement.setString(4, user.getLname());
            statement.setString(5, user.getEmail());
            int status1 = statement.executeUpdate();

            String createTableSQL = "CREATE TABLE " + user.getLogin() + " (id INT PRIMARY KEY AUTO_INCREMENT, task_title VARCHAR(255), task_description TEXT, task_priority INT, task_is_completed INT, task_start_date DATE, task_end_date DATE)";
            PreparedStatement createTableStatement = connection.prepareStatement(createTableSQL);
            int status2 = createTableStatement.executeUpdate();
            if(status1 > 0 && status2 > 0){
                System.out.println("new user added succefully");
            }

            connection.close();
            statement.close();
            createTableStatement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Task> getTasksForUserByID(String userLogin) {
        List<Task> tasks = new ArrayList<>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbURL, username, userpassword);
            String sql = "SELECT * FROM "+userLogin;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Task task = new Task();
                task.setId(resultSet.getLong("id"));
                task.setTitle(resultSet.getString("task_title"));
                task.setDescription(resultSet.getString("task_description"));
                task.setPriority(resultSet.getInt("task_priority"));
                task.setCompleted(resultSet.getInt("task_is_completed"));
                task.setStartDate(resultSet.getDate("task_start_date"));
                task.setEndDate(resultSet.getDate("task_end_date"));
                tasks.add(task);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("POBRANO LISTĘ ZADAŃ: "+tasks);
        return tasks;
    }

    public Long getUserIdByLogin(String login) {
        Long userId = null;
        String sql = "SELECT id FROM users WHERE username = ?";

        try (Connection connection = DriverManager.getConnection(dbURL, username, userpassword);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("POBRANO ID: "+ userId);
        return userId;
    }

    public void addTask(Task task, String userLogin){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbURL, username, userpassword);

            sql = "INSERT INTO "+userLogin+" (task_title, task_description, task_priority, task_is_completed, task_start_date, task_end_date) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setInt(3, task.getPriority());
            statement.setInt(4, 0);
            statement.setDate(5, task.getStartDate());
            statement.setDate(6, task.getEndDate());

            int status = statement.executeUpdate();
            if (status > 0) {
                System.out.println("A new task was inserted successfully!");
                task.toString();
            }

            connection.close();
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTask(Long taskId, String userLogin){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbURL, username, userpassword);

            sql = "DELETE FROM " + userLogin + " WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, taskId);

            int status = statement.executeUpdate();
            if(status > 0){
                System.out.println("Task with ID: " + taskId + " was deleted succesfully");
            }else {
                System.out.println("fail");
            }

            connection.close();
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void changeStatus(long taskId, String userLogin, int completed){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbURL, username, userpassword);

            sql = "UPDATE "+userLogin+" SET task_is_completed = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, completed);
            statement.setLong(2, taskId);

            int status = statement.executeUpdate();
            if(status > 0){
                System.out.println("Task with ID: " + taskId + " was edited succesfully");
            }else {
                System.out.println("fail editing");
            }

            connection.close();
            statement.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package be.sint_andries;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * if the program exits whit code 14 it means that there was a database error.(SQLException)
 */
public class Main extends Application {
    public static Connection connection;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/HomeScreenView.fxml"));
        primaryStage.setTitle("Restaurantdag");
        primaryStage.setScene(new Scene(root));
        //primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {

        //keep this!!!!!!!!!!!!!!!!!!!!!!!!!!
        try {
            getConnection();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error while establishing the database connection.", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(14);
        }
        launch(args);
    }

    /**
     * gets the connection and savas it statically to the database wit a sqlite jdbc driver
     * @throws SQLException thrown when the db file cannot be connected to
     */
    private static void getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        connection = DriverManager.getConnection("jdbc:sqlite::resource:DB/Restaurantdag.db");
        System.out.println("Connected");
    }
}

package be.sint_andries;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;


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




    public static void main(String[] args) throws SQLException {
        getConnection();
        launch(args);
    }

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

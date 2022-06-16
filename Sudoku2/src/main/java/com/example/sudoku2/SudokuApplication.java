package com.example.sudoku2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class SudokuApplication extends Application {

    public static DBManager dbcon;

    @Override
    public void start(Stage stage) throws IOException {

        dbcon = new DBManager();

        dbcon.prepareDb();

        try {

            Parent root = FXMLLoader.load(getClass().getResource("welcome-screen.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Sudoku");
            stage.show();

        } catch(Exception e) {

            e.printStackTrace();

        }

    }

    public static void main(String[] args) {
        launch();
    }

}
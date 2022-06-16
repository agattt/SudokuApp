package com.example.sudoku2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.TextFormatter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static java.lang.Integer.parseInt;

public class SudokuController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static String playing;

    private static HashMap<String, String> board;

    private static String currentLevel;

    @FXML
    TextField userName;
    @FXML
    TextArea notification;
    @FXML
    TextArea sudokuNotification;

    public void switchToHard(ActionEvent event) throws IOException {

        boolean userEntered = checkIfUserEntered();

        boolean exists = SudokuApplication.dbcon.verifyIfUserExists(userName.getText());

        playing = userName.getText();

        if (userEntered && exists) {

            if (!SudokuApplication.dbcon.verifyIfUserCompletedLevel("HARD", userName.getText())){

                SudokuController.currentLevel = "hard";

                GridPane generatedBoard = createSudokuBoard("hard");

                AnchorPane pane = FXMLLoader.load(getClass().getResource("board.fxml"));

                pane.getChildren().add(1, generatedBoard);

                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();

            } else {

                notification.setText("""
                        Dany użytkownik już uzupełnił sudoku na tym poziomie.
                        \s
                        Jeżeli chcesz ponownie je rozwiązać to podaj inną nazwę użytkownika.""");

            }

        } else if (userEntered){

            SudokuController.currentLevel = "hard";

            GridPane generatedBoard = createSudokuBoard("hard");

            AnchorPane pane = FXMLLoader.load(getClass().getResource("board.fxml"));

            pane.getChildren().add(1, generatedBoard);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();
        }

    }

    @FXML
    public void switchToEasy(ActionEvent event) throws IOException {

        boolean userEntered = checkIfUserEntered();

        boolean exists = SudokuApplication.dbcon.verifyIfUserExists(userName.getText());

        playing = userName.getText();

        if (userEntered && exists) {

            if (!SudokuApplication.dbcon.verifyIfUserCompletedLevel("EASY", userName.getText())){

                SudokuController.currentLevel = "easy";

                GridPane generatedBoard = createSudokuBoard("easy");

                AnchorPane pane = FXMLLoader.load(getClass().getResource("board.fxml"));

                pane.getChildren().add(1, generatedBoard);

                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();

            } else {

                notification.setText("""
                        Dany użytkownik już uzupełnił sudoku na tym poziomie.
                        \s
                        Jeżeli chcesz ponownie je rozwiązać to podaj inną nazwę użytkownika.""");

            }

        } else if (userEntered){

            SudokuController.currentLevel = "easy";

            GridPane generatedBoard = createSudokuBoard("easy");

            AnchorPane pane = FXMLLoader.load(getClass().getResource("board.fxml"));

            pane.getChildren().add(1, generatedBoard);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();

        }

    }

    @FXML
    public void switchToMedium(ActionEvent event) throws IOException {

        boolean userEntered = checkIfUserEntered();

        boolean exists = SudokuApplication.dbcon.verifyIfUserExists(userName.getText());

        playing = userName.getText();

        if (userEntered && exists) {

            if (!SudokuApplication.dbcon.verifyIfUserCompletedLevel("MEDIUM", userName.getText())){

                SudokuController.currentLevel = "medium";

                GridPane generatedBoard = createSudokuBoard("medium");

                AnchorPane pane = FXMLLoader.load(getClass().getResource("board.fxml"));

                pane.getChildren().add(1, generatedBoard);

                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(pane);
                stage.setScene(scene);
                stage.show();

            } else {

                notification.setText("""
                        Dany użytkownik już uzupełnił sudoku na tym poziomie.
                        \s
                        Jeżeli chcesz ponownie je rozwiązać to podaj inną nazwę użytkownika.""");

            }

        } else if (userEntered){

            SudokuController.currentLevel = "medium";

            GridPane generatedBoard = createSudokuBoard("medium");

            AnchorPane pane = FXMLLoader.load(getClass().getResource("board.fxml"));

            pane.getChildren().add(1, generatedBoard);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(pane);
            stage.setScene(scene);
            stage.show();

        }

    }

    @FXML
    public void switchToMain(ActionEvent event) throws IOException {

        playing = "";

        SudokuController.currentLevel=null;

        root = FXMLLoader.load(getClass().getResource("welcome-screen.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void clearSudoku(ActionEvent event) throws IOException {

        sudokuNotification.setText("Powodzenia!");

        GridPane generatedBoard = createSudokuBoard(currentLevel);

        AnchorPane pane = FXMLLoader.load(getClass().getResource("board.fxml"));

        pane.getChildren().add(1, generatedBoard);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void verifySudoku(ActionEvent event) throws IOException, SQLException {

        HashMap<String, Boolean> sudoku = new HashMap<>();

        if (verifySquares() && verifyColumns() && verifyRows()) {

            SudokuApplication.dbcon.setLevelCompleted(currentLevel, playing);

            sudokuNotification.setText("Udało Ci się rozwiązać sudoku! Naciśnij przycisk Powrót i wybierz inny poziom :)");

        }

    }

    private boolean verifyColumns() {

        ArrayList<Boolean> result = new ArrayList<>();

        for (int i=0; i<9; i++) {

            ArrayList<Integer> row = new ArrayList<>();

            for (int j = 0; j < 9; j++) {

                String position = Integer.toString(j) + Integer.toString(i);

                if (board.get(position).equals("")) {

                    sudokuNotification.setText("Sudoku nie jest w pełni wypełnione");

                    return false;

                } else if (row.contains(parseInt(board.get(position)))){

                    sudokuNotification.setText("W kolumnie numer " + Integer.toString(i+1) + " są powtarzające się wartośći");

                    return false;

                }

                row.add(parseInt(board.get(position)));

            }

            result.add(true);

        }

        for (Boolean elt : result) {

            if (!elt) {

                return false;

            }

        }

        return true;

    }

    private boolean verifySquares( ){

        ArrayList<Boolean> result = new ArrayList<>();

        for (int i=0; i<9; i+=3) {

            for (int j = 0; j < 9; j+=3) {

                ArrayList<Integer> square = new ArrayList<>();

                for (int k=0; k < 3; k++) {

                    for (int l=0; l<3; l++) {

                        String position = Integer.toString(i+k) + Integer.toString(j + l);

                        if (board.get(position).equals("")) {

                            sudokuNotification.setText("Sudoku nie jest w pełni wypełnione");

                            return false;

                        } else if (square.contains(parseInt(board.get(position)))) {

                            sudokuNotification.setText("W kwadracie, który zaczyna się w wierszu " + Integer.toString(i) +
                                    ", kolumnie  " + Integer.toString(j) +
                                    " są powtarzające się wartości");

                            return false;

                        }

                        square.add(parseInt(board.get(position)));

                    }

                }

                result.add(true);

            }

        }

        for (Boolean elt : result) {

            if (!elt) {

                return false;

            }

        }

        return true;

    }

    private boolean verifyRows () {

        ArrayList<Boolean> result = new ArrayList<>();

        for (int i=0; i<9; i++) {

            ArrayList<Integer> row = new ArrayList<>();

            for (int j = 0; j < 9; j++) {

                String position = Integer.toString(i) + Integer.toString(j);

                if (board.get(position).equals("")) {

                    sudokuNotification.setText("Sudoku nie jest w pełni wypełnione");

                    return false;

                } else if (row.contains(parseInt(board.get(position)))){

                    sudokuNotification.setText("W wierszu numer " + Integer.toString(i+1) + " są powtarzające się wartośći");

                    return false;

                }

                row.add(parseInt(board.get(position)));
                
            }

            result.add(true);

        }

        for (Boolean elt : result) {

            if (!elt) {

                return false;

            }

        }

        return true;

    }

    private boolean checkIfUserEntered(){

        if (userName.getText()!= ""){

            return true;

        } else {

            notification.setText("Przed wybraniem poziomu wpisz nazwę użytkownika!!!");

            return false;

        }

    }

    private GridPane createSudokuBoard(String level){

        if (level == "easy"){

            this.board = createEasyBoard();

        } else if (level == "medium") {

            this.board = createMediumBoard();

        } else {

            this.board = createHardBoard();

        }

        GridPane gridSudoku = new GridPane();

        gridSudoku.setMinSize(500, 500);

        gridSudoku.setGridLinesVisible(true);

        gridSudoku.setLayoutX(25.0);
        gridSudoku.setLayoutY(75.0);
        gridSudoku.setAlignment(Pos.CENTER);

        Font font = new Font("Arial", 24);

        for (int i=0; i<9; i++){

            for (int j=0; j<9; j++) {

                TextField tf = new TextField();

                tf.setId(String.valueOf(i) + String.valueOf(j));

                tf.setPrefSize(55, 55);

                tf.setAlignment(Pos.CENTER);

                tf.setFont(font);

                String position = Integer.toString(i) + Integer.toString(j);

                if (this.board.get(position) != null) {

                    tf.setStyle("-fx-background-color: #e6e6e6;");
                    tf.setText(this.board.get(position));
                    tf.setEditable(false);

                } else {

                    this.board.put(position, "");

                    tf.setEditable(true);

                    tf.setTextFormatter(
                            new TextFormatter<>(
                                    change -> {
                                        if (change.getControlNewText().matches("[1-9]?")) {
                                            change.setCaretPosition(0);
                                            return change;
                                        }
                                        return null;
                                    }));
                }

                tf.textProperty().addListener((observable, oldValue, newValue) -> {

                        insertIntoBoard(tf.getId(), tf.getText());

                    });

                gridSudoku.add(tf, j, i);

            }

        }

        return gridSudoku;

    }

    private static HashMap<String, String> createEasyBoard() {

        HashMap<String, String> easyBoard = new HashMap<String, String>();

        easyBoard.put("00", "5");
        easyBoard.put("02", "7");
        easyBoard.put("05", "1");
        easyBoard.put("14", "8");
        easyBoard.put("16", "4");
        easyBoard.put("25", "5");
        easyBoard.put("27", "6");
        easyBoard.put("30", "7");
        easyBoard.put("32", "4");
        easyBoard.put("33", "5");
        easyBoard.put("35", "9");
        easyBoard.put("36", "8");
        easyBoard.put("37", "2");
        easyBoard.put("41", "9");
        easyBoard.put("47", "4");
        easyBoard.put("51", "2");
        easyBoard.put("52", "6");
        easyBoard.put("53", "7");
        easyBoard.put("55", "4");
        easyBoard.put("56", "1");
        easyBoard.put("58", "9");
        easyBoard.put("61", "3");
        easyBoard.put("63", "1");
        easyBoard.put("72", "8");
        easyBoard.put("74", "9");
        easyBoard.put("83", "3");
        easyBoard.put("86", "7");
        easyBoard.put("88", "2");

        return easyBoard;

    }

    private static HashMap<String, String>  createMediumBoard() {

        HashMap<String, String> easyBoard = new HashMap<String, String>();

        easyBoard.put("02", "9");
        easyBoard.put("03", "7");
        easyBoard.put("06", "8");
        easyBoard.put("14", "1");
        easyBoard.put("15", "5");
        easyBoard.put("18", "2");
        easyBoard.put("24", "8");
        easyBoard.put("26", "5");
        easyBoard.put("28", "9");
        easyBoard.put("30", "2");
        easyBoard.put("32", "5");
        easyBoard.put("37", "9");
        easyBoard.put("40", "7");
        easyBoard.put("48", "4");
        easyBoard.put("51", "8");
        easyBoard.put("56", "2");
        easyBoard.put("58", "3");
        easyBoard.put("60", "4");
        easyBoard.put("62", "8");
        easyBoard.put("64", "6");
        easyBoard.put("70", "1");
        easyBoard.put("73", "2");
        easyBoard.put("74", "7");
        easyBoard.put("82", "2");
        easyBoard.put("85", "1");
        easyBoard.put("86", "7");

        return easyBoard;

    }

    private static HashMap<String, String>  createHardBoard() {

        HashMap<String, String> easyBoard = new HashMap<String, String>();

        easyBoard.put("12", "1");
        easyBoard.put("03", "2");
        easyBoard.put("04", "8");
        easyBoard.put("17", "3");
        easyBoard.put("23", "5");
        easyBoard.put("25", "9");
        easyBoard.put("26", "4");
        easyBoard.put("28", "8");
        easyBoard.put("34", "3");
        easyBoard.put("37", "4");
        easyBoard.put("41", "1");
        easyBoard.put("42", "8");
        easyBoard.put("46", "6");
        easyBoard.put("47", "9");
        easyBoard.put("51", "3");
        easyBoard.put("54", "5");
        easyBoard.put("60", "1");
        easyBoard.put("62", "6");
        easyBoard.put("63", "3");
        easyBoard.put("65", "7");
        easyBoard.put("71", "7");
        easyBoard.put("76", "1");
        easyBoard.put("84", "4");
        easyBoard.put("85", "2");

        return easyBoard;

    }

    private void insertIntoBoard(String id, String value) {

        this.board.replace(id, value);

    }

}

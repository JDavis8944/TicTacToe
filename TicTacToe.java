/* Joshua Davis
 Tic-Tac-Toe
 */

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Font;

public class TicTacToe extends Application {

    // Indicate which player has a turn, initially it is the player
    private char whoseTurn;

    // Create and initialize cell
    private Cell[][] cell = new Cell[3][9];
    private int gridIndex = 1;
    private Label[] gridLabel = new Label[27];
    private ColumnConstraints column1 = new ColumnConstraints();
    private int player = 0;
    private int aIPlayer = 0;
    private int[][] futureBoard = new int[3][9];
    private int[][] currentBoard = new int[3][9];
    private int[] label = {1, 2, 3, 10, 11, 12, 19, 20, 21, 4, 5, 6, 13, 14, 15, 22, 23, 24, 7, 8, 9, 16, 17, 18, 25, 26, 27};

    //Array of all winning lines: every 2 numbers represents a location on the gameboard
    int[][] lines = {{0, 0, 0, 1, 0, 2}, {1, 0, 1, 1, 1, 2}, {2, 0, 2, 1, 2, 2},
    {0, 0, 1, 0, 2, 0}, {0, 1, 1, 1, 2, 1}, {0, 2, 1, 2, 2, 2},
    {0, 0, 1, 1, 2, 2}, {0, 2, 1, 1, 2, 0}, {0, 3, 0, 4, 0, 5},
    {1, 3, 1, 4, 1, 5}, {2, 3, 2, 4, 2, 5}, {0, 3, 1, 3, 2, 3},
    {0, 4, 1, 4, 2, 4}, {0, 5, 1, 5, 2, 5}, {0, 3, 1, 4, 2, 5},
    {0, 5, 1, 4, 2, 3}, {0, 6, 0, 7, 0, 8}, {1, 6, 1, 7, 1, 8},
    {2, 6, 2, 7, 2, 8}, {0, 6, 1, 6, 2, 6}, {0, 7, 1, 7, 2, 7},
    {0, 8, 1, 8, 2, 8}, {0, 6, 1, 7, 2, 8}, {0, 8, 1, 7, 2, 6},
    {0, 0, 0, 3, 0, 6}, {0, 1, 0, 4, 0, 7}, {0, 2, 0, 5, 0, 8},
    {1, 0, 1, 3, 1, 6}, {1, 1, 1, 4, 1, 7}, {1, 2, 1, 5, 1, 8},
    {2, 0, 2, 3, 2, 6}, {2, 1, 2, 4, 2, 7}, {2, 2, 2, 5, 2, 8},
    {0, 0, 0, 4, 0, 8}, {0, 0, 1, 4, 2, 8}, {1, 0, 1, 4, 1, 8},
    {2, 0, 2, 4, 2, 8}, {0, 0, 1, 3, 2, 6}, {0, 1, 1, 4, 2, 7},
    {0, 2, 1, 5, 2, 8}, {2, 0, 1, 3, 0, 6}, {2, 1, 1, 4, 0, 7},
    {2, 2, 1, 5, 0, 8}, {0, 2, 0, 4, 0, 6}, {1, 2, 1, 4, 1, 6},
    {2, 2, 2, 4, 2, 6}, {2, 0, 1, 4, 0, 8}, {0, 2, 1, 4, 2, 6},
    {2, 2, 1, 4, 0, 6}};

    //A representation of the winning lines that was easier for me to visualize...this gets a little ridiculous at times
    int[][] linesLocation = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}, {1, 4, 7}, {2, 5, 8}, {3, 6, 9}, {1, 5, 9},
    {3, 5, 7}, {10, 11, 12}, {13, 14, 15}, {16, 17, 18}, {10, 13, 16}, {11, 14, 17}, {12, 15, 18},
    {10, 14, 18}, {12, 14, 16}, {19, 20, 21}, {22, 23, 24}, {25, 26, 27}, {19, 22, 25}, {20, 23, 26},
    {21, 24, 27}, {19, 23, 27}, {21, 23, 25}, {1, 10, 19}, {2, 11, 20}, {3, 12, 21}, {4, 13, 22},
    {5, 14, 23}, {6, 15, 24}, {7, 16, 25}, {8, 17, 26}, {9, 18, 27}, {1, 11, 21}, {1, 14, 27},
    {4, 14, 24}, {7, 17, 27}, {1, 13, 25}, {2, 14, 26}, {3, 15, 27}, {7, 13, 19}, {8, 14, 20},
    {9, 15, 21}, {3, 11, 19}, {6, 14, 22}, {9, 16, 25}, {7, 14, 21}, {3, 14, 25}, {9, 14, 19}};

    private Label lblStatus = new Label("X's turn to play");
    private Button pass = new Button("Pass");
    private char play, comp;
    private boolean alreadyMoved = false;

    @Override
    public void start(Stage primaryStage) {
        play = 'X';
        comp = 'O';
        GridPane pane = new GridPane();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                currentBoard[i][j] = 0;
                pane.add(cell[i][j] = new Cell(), j, i);
                gridLabel[gridIndex - 1] = new Label(label[gridIndex - 1] + "");
                gridLabel[gridIndex - 1].setFont(new Font("Times New Roman", 25));
                pane.add(gridLabel[gridIndex - 1], j, i);
                column1.setHalignment(HPos.CENTER);
                pane.getColumnConstraints().add(column1);
                gridIndex++;
            }
        }
        HBox hb = new HBox();
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pane);
        
        pass.setPadding(new Insets(0, 20, 0, 20));
        pass.setOnAction(e -> pass());

        hb.setPadding(new Insets(5, 20, 5, 20));
        hb.setSpacing(10);

        hb.getChildren().addAll(lblStatus, pass);
        borderPane.setBottom(hb);

        Scene scene = new Scene(borderPane, 1200, 400);
        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(scene);
        primaryStage.show();

        whoseTurn = play;
    }

    public void pass() {
        whoseTurn = comp;
        nextMove();
    }

    public void calculateScore() {
        for (int i = 0; i < 49; i++) {
            if (cell[lines[i][0]][lines[i][1]].getToken() == play
                    && cell[lines[i][2]][lines[i][3]].getToken() == play
                    && cell[lines[i][4]][lines[i][5]].getToken() == play) {
                System.out.println("Win at line: " + "(" + lines[i][0] + ", " + lines[i][1] + ") ("
                        + lines[i][2] + ", " + lines[i][3] + ") (" + lines[i][4] + ", " + lines[i][5] + ")");
                player = 1;
            }
            if (cell[lines[i][0]][lines[i][1]].getToken() == comp
                    && cell[lines[i][2]][lines[i][3]].getToken() == comp
                    && cell[lines[i][4]][lines[i][5]].getToken() == comp) {
                System.out.println("Win at line: " + "(" + lines[i][0] + ", " + lines[i][1] + ") ("
                        + lines[i][2] + ", " + lines[i][3] + ") (" + lines[i][4] + ", " + lines[i][5] + ")");
                aIPlayer = 1;
            }
        }
    }

    public int calculateTheoreticalScore(int[][] board) {
        int score = 0;
        for (int i = 0; i < 49; i++) {
            if (board[lines[i][0]][lines[i][1]] == -1
                    && board[lines[i][2]][lines[i][3]] == -1
                    && board[lines[i][4]][lines[i][5]] == -1) {
                score = -10;
                break;
            }
            if (board[lines[i][0]][lines[i][1]] == 1
                    && board[lines[i][2]][lines[i][3]] == 1
                    && board[lines[i][4]][lines[i][5]] == 1) {
                score = 10;
                break;
            }
        }
        return score;
    }

    public void nextMove() {
        int[] coords = miniMax();
        cell[coords[0]][coords[1]].setToken(comp);

        calculateScore();
        if (player < aIPlayer) {
            lblStatus.setText("The computer beat you! ");
            whoseTurn = ' '; // Game is over
        } else if (player > aIPlayer) {
            lblStatus.setText("You beat the computer! ");
            whoseTurn = ' '; // Game is over
        } else {
            whoseTurn = play;
        }

    }

    public int[] miniMax() {
        int[] maxCoords = new int[2];
        int current = 0;
        int[] block = new int[49];
        int[] win = new int[49];
        alreadyMoved = false;

        for (int i = 0; i < 49; i++) {
            block[i] = 0;
            win[i] = 0;
            if (cell[lines[i][0]][lines[i][1]].getToken() == 'O') {
                win[i]++;
            }
            if (cell[lines[i][2]][lines[i][3]].getToken() == 'O') {
                win[i]++;
            }
            if (cell[lines[i][4]][lines[i][5]].getToken() == 'O') {
                win[i]++;
            }
            if (cell[lines[i][0]][lines[i][1]].getToken() == 'X') {
                block[i]--;
            }
            if (cell[lines[i][2]][lines[i][3]].getToken() == 'X') {
                block[i]--;
            }
            if (cell[lines[i][4]][lines[i][5]].getToken() == 'X') {
                block[i]--;
            }
        }

        //Win the game if the computer has 2 in a row
        for (int j = 0; j < 49; j++) {
            if (win[j] == 2 && block[j] == 0) {
                alreadyMoved = true;
                if (cell[lines[j][0]][lines[j][1]].getToken() == ' ') {
                    maxCoords[0] = lines[j][0];
                    maxCoords[1] = lines[j][1];
                    whoseTurn = play;
                    System.out.println("I'm trying to win!\n");
                    break;
                } else if (cell[lines[j][2]][lines[j][3]].getToken() == ' ') {
                    maxCoords[0] = lines[j][2];
                    maxCoords[1] = lines[j][3];
                    whoseTurn = play;
                    System.out.println("I'm trying to win!\n");
                    break;
                } else if (cell[lines[j][4]][lines[j][5]].getToken() == ' ') {
                    maxCoords[0] = lines[j][4];
                    maxCoords[1] = lines[j][5];
                    whoseTurn = play;
                    System.out.println("I'm trying to win!\n");
                    break;
                }
            }
        }
        //Block line if opponent has 2 in a row
        if (!alreadyMoved) {
            for (int j = 0; j < 49; j++) {
                if (block[j] == -2 && win[j] == 0) {
                    alreadyMoved = true;
                    if (cell[lines[j][0]][lines[j][1]].getToken() == ' ') {
                        maxCoords[0] = lines[j][0];
                        maxCoords[1] = lines[j][1];
                        whoseTurn = play;
                        System.out.println("I'm trying to block!\n");
                        break;
                    } else if (cell[lines[j][2]][lines[j][3]].getToken() == ' ') {
                        maxCoords[0] = lines[j][2];
                        maxCoords[1] = lines[j][3];
                        whoseTurn = play;
                        System.out.println("I'm trying to block!\n");
                        break;
                    } else if (cell[lines[j][4]][lines[j][5]].getToken() == ' ') {
                        maxCoords[0] = lines[j][4];
                        maxCoords[1] = lines[j][5];
                        whoseTurn = play;
                        System.out.println("I'm trying to block!\n");
                        break;
                    }

                }
            }
        }
        //This should evaluate the optimal play for the computer's next turn
        if (!alreadyMoved) {
            alreadyMoved = true;
            for (int a = 0; a < 3; a++) {
                for (int b = 0; b < 9; b++) {
                    maxCoords[0] = a;
                    maxCoords[1] = b;
                    if (currentBoard[a][b] == 0) {
                        futureBoard[a][b] = 1;
                        current = calculateTheoreticalScore(futureBoard);
                        if (current > 0) {
                            System.out.println("\nOne ply to the rescue? Score: " + current + "\n");
                            return maxCoords;
                        }
                        for (int c = 0; c < 3; c++) {
                            for (int d = 0; d < 9; d++) {
                                if (futureBoard[c][d] == 0) {
                                    futureBoard[c][d] = -1;
                                    current = calculateTheoreticalScore(futureBoard);
                                    if (current < 0) {
                                        break;
                                    }

                                    for (int e = 0; e < 3; e++) {
                                        for (int f = 0; f < 9; f++) {
                                            if (futureBoard[e][f] == 0) {
                                                futureBoard[e][f] = 1;
                                                current = calculateTheoreticalScore(futureBoard);
                                                if (current > 0) {
                                                    System.out.println("\nThree ply? I don't even need to be here. Score: " + current + "\n");
                                                    return maxCoords;
                                                }
                                                for (int g = 0; g < 3; g++) {
                                                    for (int h = 0; h < 9; h++) {
                                                        if (futureBoard[g][h] == 0) {
                                                            futureBoard[g][h] = -1;
                                                            current = calculateTheoreticalScore(futureBoard);
                                                            if (current < 0) {
                                                                break;
                                                            }

                                                            for (int i = 0; i < 3; i++) {
                                                                for (int j = 0; j < 9; j++) {
                                                                    if (futureBoard[i][j] == 0) {
                                                                        futureBoard[i][j] = 1;
                                                                        current = calculateTheoreticalScore(futureBoard);
                                                                        if (current > 0) {
                                                                            System.out.println("\nFive ply to the rescue? Score: " + current + "\n");
                                                                            return maxCoords;
                                                                        }
                                                                        for (int k = 0; k < 3; k++) {
                                                                            for (int l = 0; l < 9; l++) {
                                                                                if (futureBoard[k][l] == 0) {
                                                                                    futureBoard[k][l] = -1;
                                                                                    current = calculateTheoreticalScore(futureBoard);
                                                                                    if (current < 0) {
                                                                                        break;
                                                                                    }

                                                                                    for (int m = 0; m < 3; m++) {
                                                                                        for (int n = 0; n < 9; n++) {
                                                                                            if (futureBoard[m][n] == 0) {
                                                                                                futureBoard[m][n] = 1;
                                                                                                current = calculateTheoreticalScore(futureBoard);
                                                                                                if (current > 0) {
                                                                                                    System.out.println("\nSeven ply is exhausting. Score: " + current + "\n");
                                                                                                    System.out.print(maxCoords[0] + " , " + maxCoords[1] + "\n");
                                                                                                    return maxCoords;
                                                                                                }
                                                                                                for (int o = 0; o < 3; o++) {
                                                                                                    for (int p = 0; p < 9; p++) {
                                                                                                        if (futureBoard[o][p] == 0) {
                                                                                                            futureBoard[o][p] = -1;
                                                                                                            current = calculateTheoreticalScore(futureBoard);
                                                                                                            if (current < 0) {
                                                                                                                break;
                                                                                                            }

                                                                                                            for (int q = 0; q < 3; q++) {
                                                                                                                for (int r = 0; r < 9; r++) {
                                                                                                                    if (futureBoard[q][r] == 0) {
                                                                                                                        futureBoard[q][r] = 1;
                                                                                                                        current = calculateTheoreticalScore(futureBoard);
                                                                                                                        if (current > 0) {
                                                                                                                            System.out.println("\nNine . . . is too much! I'm DYING! Score: " + current + "\n");
                                                                                                                            System.out.print(maxCoords[0] + " , " + maxCoords[1] + "\n");
                                                                                                                            return maxCoords;
                                                                                                                        }
                                                                                                                        futureBoard[q][r] = 0;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                            futureBoard[o][p] = 0;
                                                                                                        }}}
                                                                                                            futureBoard[m][n] = 0;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                                futureBoard[k][l] = 0;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    futureBoard[i][j] = 0;
                                                                                }
                                                                            }
                                                                        }
                                                                        futureBoard[g][h] = 0;
                                                                    }
                                                                }
                                                            }
                                                            futureBoard[e][f] = 0;
                                                        }
                                                    }
                                                }

                                                futureBoard[c][d] = 0;
                                            }
                                        }
                                    }
                                    futureBoard[a][b] = 0;
                                }
                            }

                        }
                    }

                    System.out.print(+maxCoords[0] + " , " + maxCoords[1] + "\n");

                    return maxCoords;
                }

    

    

    public class Cell extends Pane {

        // Token used for this cell
        private char token = ' ';

        public Cell() {
            setStyle("-fx-border-color: black");
            this.setPrefSize(2000, 2000);
            this.setOnMouseClicked(e -> handleMouseClick());
        }

        public char getToken() {
            return token;
        }

        public void setToken(char c) {
            token = c;

            if (token == play) {
                Line line1 = new Line(10, 10,
                        this.getWidth() - 10, this.getHeight() - 10);
                line1.endXProperty().bind(this.widthProperty().subtract(10));
                line1.endYProperty().bind(this.heightProperty().subtract(10));
                Line line2 = new Line(10, this.getHeight() - 10,
                        this.getWidth() - 10, 10);
                line2.startYProperty().bind(
                        this.heightProperty().subtract(10));
                line2.endXProperty().bind(this.widthProperty().subtract(10));
                // Add the X to the cell
                this.getChildren().addAll(line1, line2);
            } else if (token == comp) {
                Ellipse ellipse = new Ellipse(this.getWidth() / 2,
                        this.getHeight() / 2, this.getWidth() / 2 - 10,
                        this.getHeight() / 2 - 10);
                ellipse.centerXProperty().bind(
                        this.widthProperty().divide(2));
                ellipse.centerYProperty().bind(
                        this.heightProperty().divide(2));
                ellipse.radiusXProperty().bind(
                        this.widthProperty().divide(2).subtract(10));
                ellipse.radiusYProperty().bind(
                        this.heightProperty().divide(2).subtract(10));
                ellipse.setStroke(Color.BLACK);
                ellipse.setFill(Color.WHITE);
                getChildren().add(ellipse); // Add the O to the cell
            } else if (token == ' ') {
                getChildren().removeAll();
                getChildren().clear();
            }
        }

        private void buildBoard() {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 9; k++) {
                    if (cell[j][k].getToken() == play) {
                        currentBoard[j][k] = -1;
                    }
                    if (cell[j][k].getToken() == comp) {
                        currentBoard[j][k] = 1;
                    }
                    futureBoard[j][k] = currentBoard[j][k];
                }
            }
        }

        private void handleMouseClick() {
            // If cell is empty and game is not over
            if (token == ' ' && whoseTurn != ' ') {
                setToken(whoseTurn); // Set token in the cell

                calculateScore();
                if (player < aIPlayer) {
                    lblStatus.setText("The computer beat you! ");
                    whoseTurn = ' '; // Game is over
                } else if (player > aIPlayer) {
                    lblStatus.setText("You beat the computer! ");
                    whoseTurn = ' '; // Game is over
                } else {
                    buildBoard();
                    whoseTurn = comp;
                    nextMove();
                }
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

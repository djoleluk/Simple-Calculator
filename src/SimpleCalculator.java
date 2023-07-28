import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.math.BigInteger;
import java.util.Arrays;

public class SimpleCalculator extends Application {
    private final double WIDTH = 350;
    private final double HEIGHT = 450;
    private final String[] expressions = new String[3]; // an array to hold expressions, e.g. 3 + 2, 45 - 7, 5 * 3..
    private BigInteger result = new BigInteger("0"); // BigInteger is used for large numbers

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Text text = new Text("0");
        text.setFont(new Font("Serif", 70));

        ScrollPane textPane = new ScrollPane(text);
        textPane.setStyle("-fx-background-color: #f0f0f0;");

        HBox hBox = new HBox(textPane);
        hBox.setStyle("-fx-background-color: #f0f0f0;");
        hBox.setAlignment(Pos.TOP_RIGHT);
        HBox.setMargin(textPane, new Insets(5));

        StringBuilder textBuilder = new StringBuilder();

        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(5);
        buttonsGrid.setVgap(5);
        buttonsGrid.setAlignment(Pos.CENTER);

        String[] buttonLabels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", "=", "+"
        };

        int row = 0;
        int col = 0;
        for (String label : buttonLabels) { // loop that generates buttons and position them on the GridPane
            HBox button = createButton(label, text, textBuilder);
            buttonsGrid.add(button, col, row);

            col++;
            if (col == 4) {
                col = 0;
                row++;
            }
        }

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(hBox);
        borderPane.setCenter(buttonsGrid);

        BorderPane.setMargin(hBox, new Insets(10));

        primaryStage.setTitle("Simple Calculator");
        primaryStage.setScene(new Scene(borderPane, WIDTH, HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // a method to generate calculator buttons
    private HBox createButton(String txt, Text text, StringBuilder textBuilder) {
        HBox button = new HBox();
        button.setAlignment(Pos.CENTER);
        button.setPrefSize(70, 70);
        button.setStyle("-fx-background-color: #e0e0e0;");
        Label label = new Label(txt);
        label.setFont(new Font("Serif", 30));
        button.getChildren().add(label);

        // set color effects for the buttons on mouse hover
        button.setOnMouseEntered(event -> button.setStyle("-fx-background-color: #d0d0d0;"));
        button.setOnMouseExited(event -> button.setStyle("-fx-background-color: #e0e0e0;"));

        Arrays.fill(expressions, "");

        if ("C".equals(txt)) { // clear everything and set result to 0
            button.setOnMouseClicked(event -> {
                textBuilder.delete(0, textBuilder.length());
                result = new BigInteger("0");
                text.setText(result.toString());
                Arrays.fill(expressions, "");
            });
        } else if ("+".equals(txt) || "-".equals(txt) || "*".equals(txt) || "/".equals(txt)) { // if any operator is pressed, either chain operations or just set operator
            button.setOnMouseClicked(event -> {
                if (!expressions[0].isEmpty()) {
                    if (!expressions[2].isEmpty()) { // if second operand is assigned, its a chain operation..
                        calculate(text);
                        expressions[0] = result.toString();
                    }
                    textBuilder.delete(0, textBuilder.length());
                    text.setText(txt);
                    expressions[1] = text.getText();
                }
            });
        } else if ("=".equals(txt)) { // calculate and display result
            button.setOnMouseClicked(event -> calculate(text));
        } else {
            button.setOnMouseClicked(event -> {
                textBuilder.append(txt);
                text.setText(textBuilder.toString());
                if (expressions[1].isEmpty()) { // in case of number entered, distribute operands left and right to the operator
                    expressions[0] = textBuilder.toString();
                } else {
                    expressions[2] = textBuilder.toString();
                }
            });
        }
        return button;
    }

    // a method to calculate and display result(stores result each time in case chained operations occur later)
    private void calculate(Text text) {
            switch (expressions[1]) {
                case "+":
                    text.setText(String.valueOf(result = new BigInteger(expressions[0]).add(new BigInteger(expressions[2]))));
                    break;
                case "-":
                    text.setText(String.valueOf(result = new BigInteger(expressions[0]).subtract(new BigInteger(expressions[2]))));
                    break;
                case "*":
                    text.setText(String.valueOf(result = new BigInteger(expressions[0]).multiply(new BigInteger(expressions[2]))));
                    break;
                case "/":
                    text.setText(String.valueOf(result = new BigInteger(expressions[0]).divide(new BigInteger(expressions[2]))));
                    break;
            }
    }
}

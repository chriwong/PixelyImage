import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Runner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Get filepath
        String filepath = "yellow.file";
        PixelyImage image = null;

        // Read file for Image
        try {
            image = Service.getPixelyImageData(filepath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        Canvas canvas = Service.createCanvasForPixelyImage(image);

        GraphicsContext graphicsContext2D = canvas.getGraphicsContext2D();

        // Render Image on Canvas
        Service.drawImageInContext(image, graphicsContext2D);

        VBox vbox = new VBox(canvas);
        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void getTextBoxField() {
        TextField textField = new TextField();

        Button button = new Button("Enter");

        button.setOnAction(action -> {
            System.out.println(textField.getText());
        });

        HBox hbox = new HBox(textField, button);
    }

}

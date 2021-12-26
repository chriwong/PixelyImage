import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Runner extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // TODO - get filepath from TextBox
//        String filepath = "yellow.file";
        String rleFile = "1R1G2B1C1M2Y3W1K3K1W";
        PixelyImage image;

        // Read file for Image
        try {
//            image = Service.getPixelyImageData(filepath);
            byte[] rleBytes = Service.generatePixelyImageBytes(rleFile);
            image = Service.getPixelyImageData(rleBytes);
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

}

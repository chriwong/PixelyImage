import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileInputStream;

public class Service {
    private static final int SQUARE_SIZE = 16;

    public static Canvas createCanvasForPixelyImage(PixelyImage image) {
        Canvas canvas = new Canvas();
        canvas.setHeight((image.getRows()+2) * SQUARE_SIZE);
        canvas.setWidth((image.getColumns()+2) * SQUARE_SIZE);
        return canvas;
    }

    public static PixelyImage getPixelyImageData(String filepath) throws Exception {
        FileInputStream fs = new FileInputStream(filepath);

        byte[] bytes = fs.readAllBytes();

        return new PixelyImage(bytes);
    }

    public static void drawImageInContext(PixelyImage image, GraphicsContext context) {
        int x = SQUARE_SIZE, y = SQUARE_SIZE;

        for (int i=0; i<image.getPixels().length; i++) {
            PixelyImage.Pixel p = image.getPixel(i);

            context.setFill(Color.rgb(p.getRed(), p.getGreen(), p.getBlue(), p.getAlpha() / 255.0));
            context.fillRect(x, y, SQUARE_SIZE, SQUARE_SIZE);

            // fencepost (number of rows/columns does not start at zero (it's a count))
            if (i != 0 && (i+1) % image.getColumns() == 0) {
                x = SQUARE_SIZE;
                y += SQUARE_SIZE;
            } else {
                x += SQUARE_SIZE;
            }
        }
    }

}

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.FileInputStream;

public class Service {
    private static final int SQUARE_SIZE = 16;
    private static final char RED = 'R';
    private static final char GREEN = 'G';
    private static final char BLUE = 'B';
    private static final char CYAN = 'C';
    private static final char MAGENTA = 'M';
    private static final char YELLOW = 'Y';
    private static final char WHITE = 'W';
    private static final char BLACK = 'K';
    private static final char LIGHTRED = 'r';
    private static final char LIGHTGREEN = 'g';
    private static final char LIGHTBLUE = 'b';
    private static final char LIGHTCYAN = 'c';
    private static final char LIGHTMAGENTA = 'm';
    private static final char LIGHTYELLOW = 'y';
    private static final char GREY = 'k';


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

    public static PixelyImage getPixelyImageData(byte[] rleBytes) throws Exception {
        return new PixelyImage(rleBytes);
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




    // RUN-LENGTH ENCODING METHODS
    /**
     * Returns the total data length of a PixelyImage RLE-encoded string (e.g. 3r4c5x1y6z would return 12).
     */
    public static int getTotalPixels(String rleString) throws Exception {
        if (rleString.length() < 2) {
            throw new Exception("RLE input is malformed: too short.");
        }

        int totalLength = 0;
        int i = 0;
        StringBuilder sb = new StringBuilder();

        while (i < rleString.length()) {
            if (isNum(rleString.charAt(i))) {
                sb.append(rleString.charAt(i++));
            } else {
                if (i+1 < rleString.length() && !isNum(rleString.charAt(i+1))) {
                    throw new Exception("RLE input is malformed: detected two consecutive letters.");
                }
                totalLength += Integer.parseInt(sb.toString());
                sb.delete(0, sb.length());
                i++;
            }
        }
        return totalLength;
    }

    /**
     * Creates a byte array in the PixelyImage format, given a run-length encoding of the desired pixels.
     * @param rleString String representation of the desired pixels in RLE.
     *          8 colors are available for use: RGBCMYWK (black is 'K').
     *          Canvas size assumed to be 4x4.
     *          Alpha is 1. (TODO - implement custom alpha).
     * @return PixelyImage byte array
     */
    public static byte[] generatePixelyImageBytes(String rleString) throws Exception {
        if (rleString.length() < 2) {
            throw new Exception("RLE input is malformed.");
        }

        if (rleString.contains(" ")) {
            rleString = rleString.replaceAll(" ", "");
        }

        int totalCapacity = getTotalPixels(rleString);
        byte[] pixelyBytes = new byte[(totalCapacity*4) + 2];
        int byteIndex = 2;

        // Manually fill row & column bytes (TODO - implement custom canvas size)
        pixelyBytes[0] = 0x4;
        pixelyBytes[1] = 0x4;

        StringBuilder runString = new StringBuilder();

        for (int i=0; i < rleString.length(); i++) {

            while (rleString.charAt(i) > 47 && rleString.charAt(i) < 58) {
                runString.append(rleString.charAt(i++));
            }
            int run = Integer.parseInt(runString.toString());
            char color = rleString.charAt(i);

            insertPixelyBytes(pixelyBytes, byteIndex, run, color);
            byteIndex += run*4;
            runString.delete(0, runString.length());
        }
        return pixelyBytes;
    }

    private static void insertPixelyBytes(byte[] arr, int start, int run, char color) {
        switch (color) {
            case RED:
                insertPixelyBytes(arr, start, run, (byte)255, (byte)0, (byte)0);
                break;
            case GREEN:
                insertPixelyBytes(arr, start, run, (byte)0, (byte)255, (byte)0);
                break;
            case BLUE:
                insertPixelyBytes(arr, start, run, (byte)0, (byte)0, (byte)255);
                break;
            case CYAN:
                insertPixelyBytes(arr, start, run, (byte)0, (byte)255, (byte)255);
                break;
            case MAGENTA:
                insertPixelyBytes(arr, start, run, (byte)255, (byte)0, (byte)255);
                break;
            case YELLOW:
                insertPixelyBytes(arr, start, run, (byte)255, (byte)255, (byte)0);
                break;
            case WHITE:
                insertPixelyBytes(arr, start, run, (byte)255, (byte)255, (byte)255);
                break;
            case BLACK:
                insertPixelyBytes(arr, start, run, (byte)0, (byte)0, (byte)0);
                break;
            case LIGHTRED:
                insertPixelyBytes(arr, start, run, (byte)255, (byte)127, (byte)127);
                break;
            case LIGHTGREEN:
                insertPixelyBytes(arr, start, run, (byte)127, (byte)255, (byte)127);
                break;
            case LIGHTBLUE:
                insertPixelyBytes(arr, start, run, (byte)127, (byte)127, (byte)255);
                break;
            case LIGHTCYAN:
                insertPixelyBytes(arr, start, run, (byte)127, (byte)255, (byte)255);
                break;
            case LIGHTMAGENTA:
                insertPixelyBytes(arr, start, run, (byte)255, (byte)127, (byte)255);
                break;
            case LIGHTYELLOW:
                insertPixelyBytes(arr, start, run, (byte)255, (byte)255, (byte)127);
                break;
            case GREY:
                insertPixelyBytes(arr, start, run, (byte)127, (byte)127, (byte)127);
                break;
        }
    }
    private static void insertPixelyBytes(byte[] arr, int start, int run, byte r, byte g, byte b) throws IndexOutOfBoundsException {
        if (start + (run * 4) > arr.length) {
            throw new IndexOutOfBoundsException("Attempted to insert too many pixels.");
        }
        for (int i=0; i < run; i++) {
            insertPixelyByte(arr, start+(i*4), r, g, b);
        }
    }
    private static void insertPixelyByte(byte[] arr, int i, byte r, byte g, byte b) throws IndexOutOfBoundsException {
        if (i + 3 > arr.length) {
            throw new IndexOutOfBoundsException("Attempted to insert pixel out of bounds.");
        }
        arr[i] = r;
        arr[i+1] = g;
        arr[i+2] = b;
        arr[i+3] = -1;
    }

    private static boolean isNum(char c) {
        return c > 47 && c < 58;
    }
}

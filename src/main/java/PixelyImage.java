public class PixelyImage {

    static class Pixel {
        private final byte red;
        private final byte green;
        private final byte blue;
        private final byte alpha;

        public Pixel(byte r, byte g, byte b, byte a) {
            this.red = r;
            this.green = g;
            this.blue = b;
            this.alpha = a;
        }
        // Each byte is to be interpreted as unsigned.
        // AND-ing with 0xFF will flip the sign.
        public int getRed() {
            return red & 0xFF;
        }
        public int getGreen() {
            return green & 0xFF;
        }
        public int getBlue() {
            return blue & 0xFF;
        }
        public int getAlpha() {
            return alpha & 0xFF;
        }
    }

    private final int rows;
    private final int columns;
    private final Pixel[] pixels;

    public PixelyImage(byte[] bytes) throws Exception {
        if (bytes.length < 3) {
            throw new Exception("Byte array does not contain enough data!");
        } else if ((bytes.length-2) % 4 != 0) {
            throw new Exception("Malformed byte array - bad length.");
        } else if (bytes[0] * bytes[1] != (bytes.length-2) / 4) {
            throw new Exception("Malformed byte array - length not equal to size bytes.");
        }

        this.rows = bytes[0] & 0xFF;
        this.columns = bytes[1] & 0xFF;
        this.pixels = new Pixel[rows*columns];

        int pixelIndex = 0;
        for (int i=2; i<bytes.length; i+=4) {
            byte r = bytes[i],
                g = bytes[i+1],
                b = bytes[i+2],
                a = bytes[i+3];
            this.pixels[pixelIndex++] = new Pixel(r, g, b, a);;
        }
    }

    public int getRows() {
        return this.rows;
    }
    public int getColumns() {
        return this.columns;
    }
    public Pixel[] getPixels() {
        return this.pixels;
    }
    public Pixel getPixel(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index > this.pixels.length) {
            throw new IndexOutOfBoundsException();
        }
        return pixels[index];
    }
}

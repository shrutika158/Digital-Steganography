import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class SteganographyProcessor {

    public static BufferedImage encode(BufferedImage img, String message) {
        String fullMessage = message + "#"; // end marker
        byte[] bytes = fullMessage.getBytes();
        int msgIndex = 0, bitIndex = 0;

        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = result.getGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        outer:
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                if (msgIndex >= bytes.length) break outer;

                int rgb = result.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int gVal = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int bit = (bytes[msgIndex] >> (7 - bitIndex)) & 1;
                b = (b & 0xFE) | bit;

                int newRGB = (r << 16) | (gVal << 8) | b;
                result.setRGB(x, y, newRGB);

                bitIndex++;
                if (bitIndex == 8) {
                    bitIndex = 0;
                    msgIndex++;
                }
            }
        }

        return result;
    }

    public static String decode(BufferedImage img) {
        StringBuilder message = new StringBuilder();
        int currentByte = 0;
        int bitCount = 0;

        outer:
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                int b = rgb & 0xFF;

                currentByte = (currentByte << 1) | (b & 1);
                bitCount++;

                if (bitCount == 8) {
                    char ch = (char) currentByte;
                    if (ch == '#') break outer;
                    message.append(ch);
                    currentByte = 0;
                    bitCount = 0;
                }
            }
        }

        return message.toString();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class SteganographyApp extends JFrame {

    private JLabel imageLabel;
    private JTextField messageField;
    private BufferedImage image;

    public SteganographyApp() {
        setTitle("Java Steganography App");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton loadButton = new JButton("Load Image");
        JButton encodeButton = new JButton("Encode & Save");
        JButton decodeButton = new JButton("Decode Message");

        messageField = new JTextField(30);
        imageLabel = new JLabel();

        add(loadButton);
        add(new JLabel("Message:"));
        add(messageField);
        add(encodeButton);
        add(decodeButton);
        add(imageLabel);

        // Event Handlers
        loadButton.addActionListener(e -> loadImage());
        encodeButton.addActionListener(e -> encodeImage());
        decodeButton.addActionListener(e -> decodeImage());
    }

    private void loadImage() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                image = ImageIO.read(file);
                imageLabel.setIcon(new ImageIcon(image.getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading image.");
            }
        }
    }

    private void encodeImage() {
        if (image == null) {
            JOptionPane.showMessageDialog(this, "Please load an image first.");
            return;
        }

        String message = messageField.getText();
        if (message.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Message cannot be empty.");
            return;
        }

        BufferedImage stegoImage = SteganographyProcessor.encode(image, message);
        try {
            File output = new File("encoded_image.png");
            ImageIO.write(stegoImage, "png", output);
            JOptionPane.showMessageDialog(this, "Message encoded and saved as 'encoded_image.png'.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving image.");
        }
    }

    private void decodeImage() {
        if (image == null) {
            JOptionPane.showMessageDialog(this, "Please load an image first.");
            return;
        }

        String decodedMessage = SteganographyProcessor.decode(image);
        JOptionPane.showMessageDialog(this, "Decoded Message:\n" + decodedMessage);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SteganographyApp().setVisible(true));
    }
}

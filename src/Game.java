import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

// to work on next (as of June 14, 2024): work on a feature that allows you to upload audio
// make git history

public class Game {
    private final JFrame gameFrame = new JFrame("Zoom In - Zoom Out");

    JButton uploadButton = new JButton("Upload Image");

    //private JPanel gamePanel = new JPanel();

    private boolean isValidExtension(String extension) {
        String[] validExtensions = ImageIO.getReaderFileSuffixes();
        System.out.println("calling isValidExtension method:");
        for (String validExtension : validExtensions) {
            if (validExtension.equals(extension)) { // if the given extension is one of the valid extensions, then return true
                return true;
            }
        }
        return false; //valid extension not found
    }

    private String obtainFileExtension(File selectedFile) {
        String fileName = selectedFile.getName();
        int lastIndexOf = fileName.lastIndexOf('.');
        return (lastIndexOf > 0) ? fileName.substring(lastIndexOf + 1) : "";
    }

    public void renderAgain(JLabel label) {
        gameFrame.getContentPane().removeAll(); // Remove any existing components from the content pane
        gameFrame.getContentPane().add(label, BorderLayout.CENTER); // Add the new JLabel to the content pane
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.add(uploadButton);
        this.gameFrame.add(buttonContainer, BorderLayout.SOUTH);

        gameFrame.pack(); // Resize the frame to fit its contents

        gameFrame.revalidate(); // Revalidate the container to update the layout
        gameFrame.repaint(); // Repaint the container to reflect the changes
        System.out.println("render Again over");
    }

    Game() {

        uploadButton.addActionListener(_ -> {
            JFileChooser fileChooser = new JFileChooser();

            // Set file filter to accept only image files
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", ImageIO.getReaderFileSuffixes());
            fileChooser.setFileFilter(filter);
            int result = fileChooser.showOpenDialog(gameFrame);

            // Check if a file was selected
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String currentFileExtension = obtainFileExtension(selectedFile);
                System.out.println("currentFileExtension is " + currentFileExtension);
                if (!isValidExtension(currentFileExtension)) {
                    JOptionPane.showMessageDialog(gameFrame, "Error. Please choose an image-based file.", "Invalid File", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error. Please choose an image-based file.");
                    return;
                }
                System.out.println("Image-based file chosen");
                ImageIcon icon = new ImageIcon(selectedFile.getPath());
                JLabel imageRenderer = new JLabel(icon);
                renderAgain(imageRenderer);
            }
        });

        this.gameFrame.setSize(400,300);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setVisible(true);
        this.uploadButton.setPreferredSize(new Dimension(150, 50));
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.add(uploadButton);
        this.gameFrame.add(buttonContainer, BorderLayout.SOUTH);

    }
}

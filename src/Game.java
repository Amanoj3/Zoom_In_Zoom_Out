import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;
// to work on next (as of June 14, 2024): work on a feature that allows you to upload audio

public class Game {
    private final JFrame gameFrame = new JFrame("Zoom In - Zoom Out");

    private final JButton uploadButton = new JButton("Upload Image");

    private final JButton playPauseButton = new JButton("Play");

    private final JButton zoomInButton = new JButton("Zoom In");

    private final JButton zoomOutButton = new JButton("Zoom Out");

    private Clip audioClip;

    private boolean isPlaying = false;

    JLabel imageRenderer;

    ImageIcon icon;

    int iconWidth;

    int iconHeight;

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

    private void playAudio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File audioFile = new File("src/mixkit-light-rain-loop-2393.wav"); // Replace with your audio file path
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

        audioClip = AudioSystem.getClip();
        audioClip.open(audioStream);
        audioClip.start();
        System.out.println("playAudio invoked");
        playPauseButton.setText("Pause");
    }

    private void pauseAudio() {
        if (audioClip != null && audioClip.isRunning()) {
            audioClip.stop();
            System.out.println("pauseAudio invoked");
            playPauseButton.setText("Play");
        }
    }

    private void zoom(boolean zoomingIn) {
        // credits to https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon
        int zoomFactor = 50;
        Image image = this.icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(1, 1,  Image.SCALE_SMOOTH); // placeholder
        if (zoomingIn) {
            iconWidth = iconWidth + zoomFactor;
            iconHeight = iconHeight + zoomFactor;
            newimg = image.getScaledInstance(iconWidth, iconHeight,  Image.SCALE_SMOOTH); // scale it smoothly
        }
        else {
            iconWidth = iconWidth - zoomFactor;
            iconHeight = iconHeight - zoomFactor;
            newimg = image.getScaledInstance(iconWidth, iconHeight,  Image.SCALE_SMOOTH); // scale it smoothly
        }
        ImageIcon newImageIcon = new ImageIcon(newimg);
        imageRenderer.setIcon(newImageIcon);
    }


    private void renderAgain(JLabel label) {
        gameFrame.getContentPane().removeAll(); // Remove any existing components from the content pane
        gameFrame.getContentPane().add(label, BorderLayout.CENTER); // Add the new JLabel to the content pane
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.add(uploadButton);
        buttonContainer.add(playPauseButton);
        buttonContainer.add(zoomInButton);
        buttonContainer.add(zoomOutButton);
        this.gameFrame.add(buttonContainer, BorderLayout.SOUTH);

        gameFrame.pack(); // Resize the frame to fit its contents

        gameFrame.revalidate(); // Revalidate the container to update the layout
        gameFrame.repaint(); // Repaint the container to reflect the changes
        iconHeight = icon.getIconHeight();
        iconWidth = icon.getIconWidth();
        System.out.println("render Again over");
    }

    Game() {

        zoomInButton.addActionListener(_ ->{
            System.out.println("Zoom In Button clicked");
            zoom(true);
        });

        zoomOutButton.addActionListener(_ ->{
            System.out.println("Zoom Out Button clicked");
            zoom(false);
        });

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
                this.icon = new ImageIcon(selectedFile.getPath());
                // // // // // note to self: this way of resizing is the best solution at the moment
                /*Image image = this.icon.getImage(); // transform it
                Image newimg = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it smoothly
                ImageIcon newImageIcon = new ImageIcon(newimg);  // assign to a new ImageIcon instance*/
                // // // // //

                this.imageRenderer = new JLabel(icon); //original
                renderAgain(imageRenderer);
            }
        });

        playPauseButton.addActionListener(_ ->{
            System.out.println("playPauseButton clicked");
            isPlaying = !isPlaying;
                try {
                    if (isPlaying) {
                        playAudio();
                    }
                    else {
                        pauseAudio();
                    }
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    throw new RuntimeException(e);
                }
        });

        this.gameFrame.setSize(1000,300);
        this.gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.gameFrame.setVisible(true);
        this.uploadButton.setPreferredSize(new Dimension(150, 50));
        this.playPauseButton.setPreferredSize(new Dimension(150,50));
        this.zoomInButton.setPreferredSize(new Dimension(150, 50));
        this.zoomOutButton.setPreferredSize(new Dimension(150, 50));
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonContainer.add(uploadButton);
        buttonContainer.add(playPauseButton);
        buttonContainer.add(zoomInButton);
        buttonContainer.add(zoomOutButton);
        this.gameFrame.add(buttonContainer, BorderLayout.SOUTH);

    }
}

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class JPanelConFondo extends JPanel {
    private BufferedImage imagenFondo;

    public JPanelConFondo() {
        try {
            // Carga la imagen de fondo desde un archivo
            imagenFondo = ImageIO.read(getClass().getClassLoader().getResource("logoFondo.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Dibuja la imagen de fondo en el JPanel
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
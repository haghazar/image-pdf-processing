package am.picsartacademy.app;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageProcessor {

    private static final float TRANSPARENT_ALPHA = 0.5f;
    private static final Font SIGNATURE_FONT = new Font("Italiano", Font.ITALIC, 24);

    public BufferedImage overlaySignature(BufferedImage stampImage, String signature, int preferredWidth, int preferredHeight) throws IOException {
        // Resize the stamp image to the preferred size
        BufferedImage resizedStamp = resizeImage(stampImage, preferredWidth, preferredHeight);

        // Create a new BufferedImage with an alpha channel
        BufferedImage stampedImg = new BufferedImage(preferredWidth, preferredHeight, BufferedImage.TYPE_INT_ARGB);

        // Get the Graphics2D object from the stampedImg
        Graphics2D g2dSt = stampedImg.createGraphics();

        // Set alpha composite to make the stamp transparent
        g2dSt.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, TRANSPARENT_ALPHA));

        // Draw the resizedStamp onto the stampedImg
        g2dSt.drawImage(resizedStamp, 0, 0, null);

        // Set font and color for the signature
        //Font font = new Font("Arial", Font.BOLD, 24);
        g2dSt.setFont(SIGNATURE_FONT);
        g2dSt.setColor(Color.DARK_GRAY);

        // Calculate position for the signature in the center
        FontMetrics fm = g2dSt.getFontMetrics();
        int x = (preferredWidth - fm.stringWidth(signature)) / 2;
        int y = (preferredHeight + fm.getAscent()) / 2;

        // Draw the signature onto the stampedImg
        g2dSt.drawString(signature, x, y);

        // Dispose of the Graphics2D object
        g2dSt.dispose();

        return stampedImg;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g.dispose();
        return resizedImage;
    }
}

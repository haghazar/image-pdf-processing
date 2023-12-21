package am.picsartacademy.app;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PdfProcessor {
    public void stampPdf(String inputPdfPath, String outputPdfPath, BufferedImage stampImage) throws IOException {
        try (PDDocument document = PDDocument.load(new File(inputPdfPath))) {
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                PDPage page = document.getPage(i);
                PDRectangle pageSize = page.getMediaBox();

                // Calculate the position for the bottom left corner
                float x = 10; // X-coordinate for the bottom-left corner
                float y = pageSize.getLowerLeftY() + 10; // Y-coordinate for the bottom-left corner

                // Create a content stream to draw on the existing page
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    // Convert BufferedImage to PDImageXObject
                    PDImageXObject pdImage = LosslessFactory.createFromImage(document, stampImage);

                    // Draw the stamped image in the bottom left corner
                    contentStream.drawImage(pdImage, x, y, pdImage.getWidth(), pdImage.getHeight());
                }
            }

            // Create a new filename for the stamped PDF
            String stampedFilename = getStampedFilename(outputPdfPath);

            // Save the stamped PDF with the new filename
            document.save(stampedFilename);
        }
    }

    private String getStampedFilename(String originalFilename) {
        // Extract the file name without extension
        String baseName = originalFilename.replaceFirst("[.][^.]+$", "");

        // Add a suffix or version
        String stampedFilename = baseName + "_stamped.pdf";

        return stampedFilename;
    }
}

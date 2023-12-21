package am.picsartacademy.app;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class MainApp {

    // Final variables for preferred width and height
    private static final int PREFERRED_WIDTH = 100;
    private static final int PREFERRED_HEIGHT = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Allow users to input a short signature (2 or 3 characters)
        String signature = getValidSignature(scanner);

        // Allow users to choose a stamp image
        BufferedImage selectedStamp = chooseStampImage(scanner);

        ImageProcessor imageProcessor = new ImageProcessor();
        PdfProcessor pdfProcessor = new PdfProcessor();

        try {
            // Overlay signature on the selected stamp
            BufferedImage stampedSignature = imageProcessor.overlaySignature(selectedStamp, signature, PREFERRED_WIDTH, PREFERRED_HEIGHT);

            // Allow users to specify the location of a PDF file
            System.out.print("Enter the path of the PDF file: ");
            String inputPdfPath = scanner.next();

            // Specify output PDF file path
            System.out.print("Enter the output PDF file path (including filename and extension): ");
            String outputPdfPath = scanner.next();

            // Normalize and validate the paths
            Path inputPath = Paths.get(inputPdfPath).normalize();
            Path outputPath = Paths.get(outputPdfPath).normalize();

            // Check if the specified paths are valid files
            File inputFile = inputPath.toFile();
            File outputFile = outputPath.toFile();

            if (!inputFile.isFile() || outputFile.isDirectory()) {
                System.out.println("Invalid file paths. Please provide valid paths to input and output PDF files.");
                return;
            }

            // Stamp the PDF with the signature
            pdfProcessor.stampPdf(inputFile.getPath(), outputFile.getPath(), stampedSignature);

            System.out.println("PDF stamped successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static String getValidSignature(Scanner scanner) {
        String signature;
        do {
            System.out.print("Enter your short signature (2 or 3 characters): ");
            signature = scanner.next();
        } while (!isValidSignature(signature));
        return signature;
    }

    private static boolean isValidSignature(String signature) {
        return signature.length() >= 2 && signature.length() <= 3;
    }

    private static BufferedImage chooseStampImage(Scanner scanner) {
        int choice;
        do {
            try {
                // List available stamp options
                System.out.println("Choose a stamp image:");
                System.out.println("1. Black Stamp");
                System.out.println("2. Gold Stamp");
                System.out.println("3. Shabby Stamp");

            // Prompt user for choice
            System.out.print("Enter the number corresponding to the selected stamp: ");
            String input = scanner.next();
            choice = Integer.parseInt(input);

            // Check if the choice is within the valid range
            if (choice < 1 || choice > 3) {
                System.out.println("Invalid choice. Please enter a number between 1 and 3.");
            }
        } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                choice = 0; // Set choice to an invalid value to trigger re-prompt
            }
        } while (choice < 1 || choice > 3);

        // Load the selected stamp image
        try {
            return ImageIO.read(new File("src/main/resources/stamps/" + choice + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error loading stamp image.");
        }
    }
}

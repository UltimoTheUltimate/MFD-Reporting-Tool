import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.Desktop;
import java.net.URI;

public class conversions {

    // Method to open a URL in the default browser
    public static void openWebPage(String URL) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    URI uri = new URI(URL);
                    desktop.browse(uri);
                } else {
                    System.out.println("Browsing is not supported on this platform.");
                }
            } else {
                System.out.println("Desktop is not supported on this platform.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to clean URLs and create Erper objects
    public static List<Erper> cleanRobloxUrls(String inputFile) {
        List<Erper> erpers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String cleanedUrl = line.split(" ")[0];
                // Create an Erper object and add it to the list
                erpers.add(new Erper(cleanedUrl));
            }

            System.out.println("URLs cleaned and Erper objects created.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return erpers;
    }

    // Method to parse Erper URLs and reasons into a list
    public static List<Erper> parseRobloxUrlsWithReasons(String inputFile) {
        List<Erper> erpers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ", 2); // Split into URL and reason
                String url = parts[0];
                String reason = (parts.length > 1) ? parts[1] : ""; // Assign reason if available

                // Create an Erper object with URL and reason
                Erper erper = new Erper(url);
                erper.setReason(reason);
                erpers.add(erper);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return erpers;
    }

    // Method to export Erper URLs and reasons to a file, excluding empty reasons
    public static void exportRobloxUrlsWithReasons(List<Erper> erpers, String outputFile, boolean removeNulls) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {
            for (Erper erper : erpers) {
                String url = erper.getURL();
                String reason = erper.getReason();

                // Write URL and reason, optionally excluding empty reasons
                if (!removeNulls || (removeNulls && !reason.isEmpty())) {
                    bw.write(url + " " + reason);
                    bw.newLine();
                }
            }

            System.out.println("Erper URLs and reasons exported to " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

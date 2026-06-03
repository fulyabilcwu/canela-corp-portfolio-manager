/**
 * Canela Group
 * Data: 06/02/2026
 */
import javax.swing.SwingUtilities;

/**
 * Entry point for the Canela Corp Portfolio Manager application.
 * Launches the GUI on the Swing Event Dispatch Thread.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
} // end of main

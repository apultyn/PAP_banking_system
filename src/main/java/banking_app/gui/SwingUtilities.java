package banking_app.gui;

import javax.swing.*;
import java.awt.*;

public class SwingUtilities {
    public static JPanel findPanelByName(JPanel cardPanel, String panelName) {
        for (Component comp : cardPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(panelName)) {
                return (JPanel) comp;
            }
        }
        return null;
    }
}


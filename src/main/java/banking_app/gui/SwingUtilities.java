package banking_app.gui;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SwingUtilities {
    public static JPanel findPanelByName(JPanel cardPanel, String panelName) {
        for (Component comp : cardPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(panelName)) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    public static void resetComponents(JPanel panel) {
        for (Component component : panel.getComponents()) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else if (component instanceof JComboBox<?>) {
                ((JComboBox<?>) component).removeAllItems();
            }
        }
    }

    public static void resetComponents(List<Component> components) {
        for (Component component : components) {
            if (component instanceof JTextField) {
                ((JTextField) component).setText("");
            } else if (component instanceof JComboBox<?>) {
                ((JComboBox<?>) component).removeAllItems();
            } else if (component instanceof JPanel) {
                ( (JPanel) component).removeAll();
            }
        }
    }

    public static void addLabelAndComponent(JPanel panel, String labelText, Component component, GridBagConstraints gbc) {
        panel.add(new JLabel(labelText), gbc);
        gbc.gridx++;
        panel.add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

}


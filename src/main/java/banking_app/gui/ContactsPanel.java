package banking_app.gui;

import banking_app.classes.Contact;
import banking_app.classes.User;
import banking_exceptions.AccountNotFoundException;
import banking_exceptions.InvalidAccountNumberException;
import banking_exceptions.MissingInformationException;
import connections.ConnectionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ContactsPanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final ConnectionManager manager;
    private User user;
    private List<Contact> contactsList;
    private final JScrollPane scrollPane;
    private final JButton createNewContactButton;
    private final JButton backButton;
    private final JLabel headerLabel;
    private JList<Contact> contactsListDisplay;


    public ContactsPanel(ConnectionManager manager, CardLayout cardLayout, JPanel cardPanel, String panelName) {
        this.setName(panelName);
        this.manager = manager;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header Label
        headerLabel = new JLabel("Contacts");
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        headerLabel.setFont(headerLabel.getFont().deriveFont(24f));
        add(headerLabel, BorderLayout.NORTH);
        add(Box.createVerticalStrut(20));

        contactsListDisplay = new JList<>();
        contactsListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsListDisplay.setVisibleRowCount(-1); // Wymuszenie użycia JScrollPane
        scrollPane = new JScrollPane(contactsListDisplay);
        add(scrollPane, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        createNewContactButton = new JButton("Create new contact");
        backButton = new JButton("Back");
        bottomPanel.add(createNewContactButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e-> {
            cardLayout.show(cardPanel, "User");
        });
        createNewContactButton.addActionListener(e -> handleCreateContact());
    }

    private void uploadContacts() {
        DefaultListModel<Contact> listModel = new DefaultListModel<>();
        for (Contact contact : contactsList) {
            listModel.addElement(contact);
        }
        contactsListDisplay = new JList<>(listModel);
        contactsListDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsListDisplay.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component renderer = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Contact contact) {
                    setText(contact.getName() + " - " + contact.getAccountId());
                }
                return renderer;
            }
        });

        // Add selection listener to handle clicks on list items
        contactsListDisplay.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    Contact selectedContact = contactsListDisplay.getSelectedValue();
                    TransfersPanel transfersPanel = (TransfersPanel) SwingUtilities.findPanelByName(cardPanel, "Transfers");
                    if (transfersPanel != null) {
                        transfersPanel.setRecipientName(selectedContact.getName());
                        try {
                            transfersPanel.setUser(user);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        transfersPanel.setRecipientNumber(selectedContact.getAccountId());
                        cardLayout.show(cardPanel, "Transfers");
                    }
                }
            }
        });

        // Replace the previous scrollPane's viewport view with the new JList
        scrollPane.setViewportView(contactsListDisplay);
    }

    public void setUser(User user) {
        this.user = user;
        try {
            contactsList = manager.findUsersContacts(user.getId());
            uploadContacts();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleCreateContact() {
        Dialog dialog = new JDialog();
        dialog.setSize(350, 250);
        dialog.setLayout(new GridLayout(0, 2));
        ((JComponent) ((JDialog) dialog).getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));


        dialog.add(new JLabel("New contact name:"));
        JTextField newNameField = new JTextField();
        dialog.add(newNameField);

        dialog.add(new JLabel("Account number:"));
        JTextField accountNumberField = new JTextField();
        dialog.add(accountNumberField);

        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");
        dialog.add(confirmButton);
        dialog.add(cancelButton);

        confirmButton.addActionListener(e -> {
            try {
                user.createContact(manager, newNameField.getText(), accountNumberField.getText());
                JOptionPane.showMessageDialog(dialog, "Added new contact successful.");
                setUser(manager.findUser(user.getEmail()));
                dialog.dispose();
            } catch (MissingInformationException | InvalidAccountNumberException | AccountNotFoundException exception) {
                JOptionPane.showMessageDialog(dialog, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Database error!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(SwingUtilities.findPanelByName(cardPanel, "Contacts"));
        dialog.setVisible(true);
    }
}

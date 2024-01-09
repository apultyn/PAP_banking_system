package banking_app.gui;

import banking_app.classes.Contact;
import banking_app.classes.User;
import connections.ConnectionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class ContactsPanel extends JPanel {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private ConnectionManager manager;
    private User user;
    private List<Contact> contactsList;
    private JScrollPane scrollPane;
    private JButton createNewContactButton, backButton;
    private JLabel headerLabel;
    private JPanel contactsDisplayPanel;
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

        // Contacts display panel inside a scroll pane
//        contactsDisplayPanel = new JPanel();
//        contactsDisplayPanel.setLayout(new BoxLayout(contactsDisplayPanel, BoxLayout.Y_AXIS));
//        contactsDisplayPanel.setMaximumSize(new Dimension(400, 300));
//        scrollPane = new JScrollPane(contactsDisplayPanel);
//        scrollPane.setMaximumSize(new Dimension(400, 300));
//        add(scrollPane, BorderLayout.CENTER);
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
    }

    private void uploadContacts() {
//        for (Contact contact : contactsList) {
//            String contactInfo = contact.getName() + " - " + contact.getAccountId();
//            JLabel contactLabel = new JLabel(contactInfo);
//            contactsDisplayPanel.add(contactLabel);
//        }
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
                if (value instanceof Contact) {
                    Contact contact = (Contact) value;
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
                    // Handle selection of contact here, for example:
                    //JOptionPane.showMessageDialog(ContactsPanel.this, "Contact selected: " + selectedContact.getName());
                    TransactionsPanel transactionsPanel = (TransactionsPanel) SwingUtilities.findPanelByName(cardPanel, "Transaction");
                    //transactionsPanel.setRecipientName()
                    //transactionsPanel.setReciverId();
                    cardLayout.show(cardPanel, "Transaction");
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
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}

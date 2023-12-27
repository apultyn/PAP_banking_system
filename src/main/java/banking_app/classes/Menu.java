package banking_app.classes;

import connections.ConnectionManager;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu {
    ConnectionManager manager;

    public Menu(ConnectionManager manager) {
        this.manager = manager;
    }

    public long accountsMenu(User user) throws SQLException {
        int choosenAccountId;
        long accountId;
        Scanner sc;

        List<Account> usersAccounts = manager.findUsersAccounts(user.getId());
        List<Integer> accountsIds = new ArrayList<>();


        for(int i = 0; i < usersAccounts.size(); i++){
            System.out.println(i+1 + ") " + usersAccounts.get(i).getName());
            accountsIds.add(i);
        }
        System.out.println("Wybierz rachunek: ");

        do {
            sc = new Scanner(System.in);

            try {
                choosenAccountId = Integer.parseInt(sc.next()) - 1;

                if (!accountsIds.contains(choosenAccountId)) {
                    System.out.println("Błąd: Wybierz tylko rachunek z listy. Spróbuj ponownie.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wprowadź poprawną liczbę. Spróbuj ponownie.");
                choosenAccountId = 0;
            }
        } while (!accountsIds.contains(choosenAccountId));

        accountId = usersAccounts.get(choosenAccountId).getAccountId();
        return accountId;

    }

    public boolean userMenu(User user, boolean logged) throws SQLException {

        System.out.print("1) Wykonaj przelew \n2) Zobacz historię transakcji \n3) Sprawdź saldo konta " +
                "\n4) Utwórz nowy rachunek \n5) WYLOGUJ SIĘ\nWybierz cyfrę: ");
        user.loadAccounts(manager);
        Scanner sc;
        int choice;
        long account_id;

        do {
            sc = new Scanner(System.in);

            try {
                choice = Integer.parseInt(sc.next());

                if (choice != 1 && choice != 2 && choice != 3 && choice != 4 && choice != 5) {
                    System.out.println("Błąd: Wybierz tylko 1, 2, 3, 4 lub 5. Spróbuj ponownie.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wprowadź poprawną liczbę. Spróbuj ponownie.");
                choice = 0;
            }
        } while (choice != 1 && choice != 2 && choice != 3 & choice != 4 && choice != 5);

        switch (choice) {
            case 1 -> {
                //funkcja przelewu
                if (user.getAccounts().isEmpty()) {
                    System.out.println("Nie posiadasz jeszcze żadnego rachunku. ");
                } else {
                    user.makeTransaction(manager);
                    System.out.println("Przelew powiódł się.");
                }
            }
            case 2-> {
                //funkcja sprawdzania historii transakcji
                if (user.getAccounts().isEmpty()) {
                    System.out.println("Nie posiadasz jeszcze żadnego rachunku. ");
                } else {
                    TransactionHistory transactionHistory = new TransactionHistory(user, accountsMenu(user), this.manager);
                    transactionHistory.printTransactionHistory();
                }
            }
            case 3 -> {
                //funkcja sprawdzania salda
                if (user.getAccounts().isEmpty()) {
                    System.out.println("Nie posiadasz jeszcze żadnego rachunku. ");
                } else {
                    Account account = manager.findAccount(accountsMenu(user));
                    account.showBalance();
                }
            }
            case 4 -> {
                //funkcja tworzenia nowego rachunku
                user.createAccount(manager);
                System.out.println("Tworzenie rachunku.");
            }
            case 5 -> {
                System.out.println("Wylogowywanie...");
                logged = false;
            }
        }

        return logged;
    }

    public static int choiceLoop() {
        Scanner sc;
        int choice;
        do {
            sc = new Scanner(System.in);

            try {
                choice = Integer.parseInt(sc.next());

                if (choice != 1 && choice != 2) {
                    System.out.println("Błąd: Wybierz tylko 1 lub 2. Spróbuj ponownie.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wprowadź poprawną liczbę. Spróbuj ponownie.");
                choice = 0;
            }
        } while (choice != 1 && choice != 2);
        return choice;
    }

    public void menu() throws SQLException {
        System.out.println("Witamy w naszym banku.\nJeśli posiadasz konto - zaloguj się (1), jeśli nie zarejestruj się (2)");
        System.out.print("Wybierz liczbę: ");

        boolean logged = false;
        User user = null;

        JFrame mainWindow = new JFrame("Banking app");
        JPanel mainPanel = new JPanel();

        JButton loginButton = new JButton("Zaloguj sie");
        JButton registerButton = new JButton("Rejestruj");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean logged = false;
                User user = null;
                try {
                    user = User.login(manager);
                } catch (SQLException a) {
                    System.out.println("SQL threw");
                }
                logged = (user != null);
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean logged = false;
                User user = null;
                try {
                    User.register(manager);
                    System.out.println("Konto założone pomyślnie. Teraz ZALOGUJ SIĘ.");
                    user = User.login(manager);
                } catch (SQLException a) {
                    System.out.println("SQL threw");
                }

                logged=true;
            }
        });

        mainPanel.add(loginButton);
        mainPanel.add(registerButton);

        // Add the panel to the frame
        mainWindow.add(mainPanel);

        // Set the size and make the frame visible
        mainWindow.setSize(300, 200);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setVisible(true);

    }

    public void mainMenu() throws SQLException {
        while (true) {
            System.out.println("\n");
            menu();
        }
    }
    
}

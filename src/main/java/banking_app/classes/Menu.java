package banking_app.classes;

import connections.ConnectionManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    ConnectionManager manager;

    public Menu(ConnectionManager manager) {
        this.manager = manager;
    }

    public int accountsMenu(User user) throws SQLException {
        int choosenAccountId;
        Scanner sc;
        System.out.println("Wybierz rachunek: ");

        List<Account> usersAccounts = manager.findUsersAccounts(user.getId());
        List<Integer> accountsIds = new ArrayList<>();


        for(int i = 0; i < usersAccounts.size(); i++){
            System.out.println(i+1 + ") " + usersAccounts.get(i).getName());
            accountsIds.add(i);
        }
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

        return  choosenAccountId;

    }

    public void userMenu(User user) throws SQLException {
        String name = user.getName();
        System.out.println("Witaj, "+ name);
        System.out.print("1) Wykonaj przelew \n2) Zobacz historię transakcji \n3) Sprawdź saldo konta \nWybierz cyfrę: ");
        Scanner sc;
        int choice;
        int account_id;

        do {
            sc = new Scanner(System.in);

            try {
                choice = Integer.parseInt(sc.next());

                if (choice != 1 && choice != 2) {
                    System.out.println("Błąd: Wybierz tylko 1, 2 lub 3. Spróbuj ponownie.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Błąd: Wprowadź poprawną liczbę. Spróbuj ponownie.");
                choice = 0;
            }
        } while (choice != 1 && choice != 2 && choice != 3);

        switch (choice) {
            case 1 -> {
                //funkcja przelewu
                System.out.println("Przelew");
            }
            case 2-> {
                //funkcja sprawdzania historii transakcji
                account_id = accountsMenu(user);
                TransactionHistory transactionHistory = new TransactionHistory(user, account_id, this.manager);
                //zabezpieczyc to !!!!!!!!!!!!
                transactionHistory.printTransactionHistory();
            }
            case 3 -> {
                //funkcja sprawdzania salda
                System.out.println("Saldo");
            }
        }


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
        
        int choice = choiceLoop();

        switch (choice) {
            case 1 -> {
                //funkcja logowania
                user = User.login(this.manager);
                logged=true;
            }
            case 2 -> {
                //funkcja rejestracji
                User.register(this.manager);
                System.out.println("Konto założone pomyślnie. Teraz ZALOGUJ SIĘ.");
                user = User.login(this.manager);
                logged=true;
            }
        }

        if (logged){
            userMenu(user);
            //konto uzytkownika
        }
    }

    
}

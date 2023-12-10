package banking_app.classes;

import java.util.Scanner;

public class Menu {
    public static void userMenu() {
        String name = "Jan";
        System.out.println("Witaj, "+ name);
        System.out.print("1) Wykonaj przelew \n2) Zobacz historię transakcji \n3) Sprawdź saldo konta \nWybierz cyfrę: ");
        Scanner sc;
        int choice;

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
                System.out.println("Historia transakcji");
            }
            case 3 -> {
                //funkcja sprawdzania salda
                System.out.println("Saldo");
            }
        }


    }
    public static void main(String[] args) {
        System.out.println("Witamy w naszym banku.\nJeśli posiadasz konto - zaloguj się (1), jeśli nie zarejestruj się (2)");
        System.out.print("Wybierz liczbę: ");
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

        boolean logged = false;

        switch (choice) {
            case 1 -> {
                //funkcja logowania
                System.out.println("Logujemy się");
                logged=true;
            }
            case 2 -> {
                //funkcja rejestracji
                System.out.println("Rejestrujemy się");
            }
        }

        if (logged){
            userMenu();
            //konto uzytkownika
        }
    }
}

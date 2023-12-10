package banking_app.classes;

import java.util.Scanner;

public class Menu {
    public static void userMenu() {
        String name = "Jan";
        System.out.println("Witaj, "+ name);
    }
    public static void main(String[] args) {
        System.out.println("Witamy w naszym banku.\n Jeśli posiadasz konto - zaloguj się (1), jeśli nie zarejestruj się (2)");
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
        System.out.println("super");

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

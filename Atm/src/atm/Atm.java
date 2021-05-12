package atm;

import atm.Menu;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Atm {
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.println("ATM running...");
        Menu menu = new Menu();

        int pickedOption = -1;
        do {
            System.out.println("Introduceti optiunea dorita: ");
            menu.display();
            try {
                pickedOption = scanner.nextInt();
                System.out.println("PICKED " + pickedOption);
                if (pickedOption != 0 && pickedOption != 1 && pickedOption != 2 && pickedOption != 3) {
                    System.out.println("Ati introdus o optiune inexistenta. Tastati 0, 1, 2 sau 3.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Ati introdus o optiune inexistenta!");
                scanner.next(); // se curata scanner-ul de input gresit
            }
        }
        while (pickedOption != 0 && pickedOption != 1 && pickedOption != 2 && pickedOption != 3);

    }
}
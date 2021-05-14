package atm;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Atm {
    private final Scanner scanner = new Scanner(System.in);
    private final String INVALID_OPTION = "Ati introdus o optiune inexistenta.";
    private final String ENTER_OPTION = "Introduceti optiunea dorita: ";

    private void verifyOption(int pickedOption) {
        if (pickedOption != 0 && pickedOption != 1 && pickedOption != 2 && pickedOption != 3) {
            System.out.println("Ati introdus o optiune inexistenta. Tastati 0, 1, 2 sau 3.");
        }
    }

    private boolean isClientOfTheBank() {
        int answer = -1;
        do {
            try {
                answer = scanner.nextInt();
                if (answer != 0 && answer != 1) {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.err.println(INVALID_OPTION);
                scanner.next();
            }
        } while (answer != 0 && answer != 1);
        return answer == 1;
    }

    private void performActionsForExistingClient() {
        Menu menu = new Menu();

        int pickedOption = -1;
        do {
            System.out.println(ENTER_OPTION);
            menu.displayForClient();
            try {
                pickedOption = scanner.nextInt();
                verifyOption(pickedOption);
            } catch (InputMismatchException e) {
                System.err.println(INVALID_OPTION);
                scanner.next(); // se curata scanner-ul de input gresit
            }
        }
        while (pickedOption < 0 || pickedOption > 3);
    }

    private void performActionsForIndirectClient() {
        Menu menu = new Menu();

        int pickedOption = -1;
        do {
            System.out.println(ENTER_OPTION);
            menu.displayForIndirectClient();
            try {
                pickedOption = scanner.nextInt();
                verifyOption(pickedOption);
            } catch (InputMismatchException e) {
                System.err.println(INVALID_OPTION);
                scanner.next(); // se curata scanner-ul de input gresit
            }
        }
        while (pickedOption < 0 || pickedOption > 2);
    }

    public void run() {
        System.out.println("Sunteti client al bancii noastre? (0/1)");
        boolean answer = isClientOfTheBank();

        if (answer) {
            performActionsForExistingClient();
        } else {
            performActionsForIndirectClient();
        }
        scanner.close();
    }
}
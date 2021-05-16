package atm;

import bank.BankAccount;

import java.io.*;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Atm {
    private final Scanner scanner = new Scanner(System.in);
    private final String INVALID_OPTION = "Ati introdus o optiune inexistenta.";
    private final String ENTER_OPTION = "Introduceti optiunea dorita: ";
    private double defaultRonAmount = 1000.0d;
    private double defaultEuroAmount = 1000.0d;
    private double defaultPoundAmount = 1000.0d;
    private double defaultDollarAmount = 1000.0d;

    private void verifyOption(int pickedOption) {
        if (pickedOption < 0 || pickedOption > 3) {
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

    private int getActionsForExistingClient() {
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
        return pickedOption;
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

    private String[] getAccountDataById(String id, String pin) {
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(".").getCanonicalPath() + "/src/users.txt")));) {
            String line = bufferedReader.readLine();
            while (!line.equals("")) {
                String[] tokens = line.split(",");
                String currentId = tokens[0];
                String currentPin = tokens[1];
                if (currentId.equals(id) && currentPin.equals(pin)) {
                    return tokens;
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Acest cont nu exista.");
            System.err.flush();
            return null;
        }
        return null;
    }

    private BankAccount getAccountWithCredentials() {
        scanner.nextLine();
        System.out.println("ID: ");
        String searchedId = scanner.nextLine();
        System.out.println("PIN: ");
        String searchedPin = scanner.nextLine();
        System.out.println(searchedId + " " + searchedPin);
        String[] accountDataTokens = getAccountDataById(searchedId, searchedPin);
        if (accountDataTokens != null) {
            boolean blockedStatus = Boolean.parseBoolean(accountDataTokens[2]);
            double ronCurrencyValue = Double.parseDouble(accountDataTokens[3]);
            double euroCurrencyValue = Double.parseDouble(accountDataTokens[4]);
            double poundCurrencyValue = Double.parseDouble(accountDataTokens[5]);
            double dollarCurrencyValue = Double.parseDouble(accountDataTokens[6]);
            return new BankAccount(accountDataTokens[0], accountDataTokens[1], blockedStatus, ronCurrencyValue, euroCurrencyValue, poundCurrencyValue, dollarCurrencyValue);
        }
        return null;
    }

    private boolean isSufficientAmount(int amount, String currency) {
        switch (currency) {
            case "ron":
                if (this.defaultRonAmount < 10000 && amount > defaultRonAmount / 10) {
                    System.out.println("Nu se poate extrage aceasta suma.");
                    return false;
                }
                return true;
            case "£":
                if (this.defaultPoundAmount < 1000 && amount > defaultPoundAmount / 10) {
                    System.out.println("Nu se poate extrage aceasta suma.");
                    return false;
                }
                return true;
            case "$":
                if (this.defaultDollarAmount < 1000 && amount > defaultDollarAmount / 10) {
                    System.out.println("Nu se poate extrage aceasta suma.");
                    return false;
                }
                return true;
            case "€":
                if (this.defaultEuroAmount < 1000 && amount > defaultEuroAmount / 10) {
                    System.out.println("Nu se poate extrage aceasta suma.");
                    return false;
                }
                return true;
            default:
                System.err.println("Valuta inexistenta!");
                return false;
        }
    }

    public void run() {
        System.out.println("Sunteti client al bancii noastre? (0/1)");
        boolean answer = isClientOfTheBank();

        if (answer) {
            //introducere id si pin
            BankAccount bankAccount = getAccountWithCredentials();
            System.out.println("CONT BANCA :" + bankAccount);
            if (bankAccount != null) {
                int chosenOption = getActionsForExistingClient();
                switch (chosenOption) {
                    case 0:
                        System.out.println(bankAccount.checkBalance());
                        break;
                    case 1:
                        scanner.nextLine();
                        System.out.println("Introduceti suma si valuta: ");
                        String amountToBeWithdrawn = scanner.nextLine().toLowerCase(); //100 $
                        String[] currencyTokens = amountToBeWithdrawn.split(" ");
                        try {
                            int amount = Integer.parseInt(currencyTokens[0]);
                            String currency = currencyTokens[1];
                            if (isSufficientAmount(amount, currency)) {
                                if(bankAccount.withdrawAmount(amount, currency)){
                                    System.out.println("Va rugam ridicati banii. La revedere!");
                                }
                            }
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.err.println("Ati introdus datele intr-un format invalid.");
                            System.err.flush();
                        }
                }
            }
        } else {
            performActionsForIndirectClient();
        }
        scanner.close();
    }
}
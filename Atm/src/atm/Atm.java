package atm;

import bank.BankAccount;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Atm {
    private final Scanner scanner = new Scanner(System.in);
    private final String INVALID_OPTION = "Ati introdus o optiune inexistenta.";
    private final String ENTER_OPTION = "Introduceti optiunea dorita: ";
    private final String CANT_WITHDRAW_AMOUNT = "Nu se poate extrage aceasta suma.";
    private final String INVALID_CURRENCY = "Valuta inexistenta.";
    private double defaultRonAmount = 1000.0d;
    private double defaultEuroAmount = 1000.0d;
    private double defaultPoundAmount = 1000.0d;
    private double defaultDollarAmount = 1000.0d;
    private double euroExchangeRate;
    private double poundExchangeRate;
    private double dollarExchangeRate;

    private void verifyOption(int pickedOption) {
        if (pickedOption < 0 || pickedOption > 3) {
            System.out.println("Ati introdus o optiune inexistenta. Tastati 0, 1, 2 sau 3.");
        }
    }

    private void loadExchangeRatesFromFile() {
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(".").getCanonicalPath() + "/src/cursBNR.txt")))) {
            ArrayList<String> lines = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                lines.add(bufferedReader.readLine());
            }
            this.euroExchangeRate = Double.parseDouble(lines.get(0));
            this.dollarExchangeRate = Double.parseDouble(lines.get(1));
            this.poundExchangeRate = Double.parseDouble(lines.get(2));
        } catch (Exception e) {
            System.err.println("Nu s-a putut realiza citirea fisierului (curs valutar).");
        }
    }

    private void writeLogToFile(String text) {
        try {
            String f1 = new File(".").getCanonicalPath() + "/src/logs.txt";

            FileWriter fileWritter = new FileWriter(f1, true);
            BufferedWriter bw = new BufferedWriter(fileWritter);
            bw.write(text);
            bw.close();
            System.out.println("Done");
        } catch (IOException e) {
            System.err.println("Scrierea fisierului nu a putut fi efectuata.");
        }
    }

    private void addMoneyAdmin(String amount, String currency) {
        try {
            int money = Integer.parseInt(amount);
            switch (currency.toLowerCase()) {
                case "ron":
                    this.defaultRonAmount += money;
                    break;
                case "£":
                    this.defaultPoundAmount += money;
                    break;
                case "$":
                    this.defaultDollarAmount += money;
                    break;
                case "€":
                    this.defaultEuroAmount += money;
                    break;
                default:
                    System.err.println(INVALID_CURRENCY);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void withdrawMoneyAdmin(String amount, String currency) {
        try {
            double money = Double.parseDouble(amount);
            switch (currency.toLowerCase()) {
                case "ron":
                    if (money > this.defaultRonAmount) {
                        money = this.defaultRonAmount;
                    }
                    this.defaultRonAmount -= money;
                    break;
                case "£":
                    if (money > this.defaultPoundAmount) {
                        money = this.defaultPoundAmount;
                    }
                    this.defaultPoundAmount -= money;
                    break;
                case "$":
                    if (money > this.defaultDollarAmount) {
                        money = this.defaultDollarAmount;
                    }
                    this.defaultDollarAmount -= money;
                    break;
                case "€":
                    if (money > this.defaultEuroAmount) {
                        money = this.defaultEuroAmount;
                    }
                    this.defaultEuroAmount -= money;
                    break;
                default:
                    System.err.println(INVALID_CURRENCY);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void adminTrace(String id) {
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(".").getCanonicalPath() + "/src/logs.txt")))) {
            String line;
            do {
                line = bufferedReader.readLine();
                String[] tokens = line.split(",");
                if (tokens[0].equals(id)) {
                    System.out.println(line);
                }
            } while (line != null);
        }catch (IOException e){
            System.err.println("A aparut o problema la citirea fisierului.");
        }
        catch (NullPointerException e) {
            // sfarsit de fisier
        }
    }

    private void verifyAdminCommand(String command) {
        String[] tokens = command.split(" ");
        switch (tokens[0]) {
            case "ADMIN_TRACE":
                System.out.println("TRACE");
                try {
                    adminTrace(tokens[1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Numar invalid de argumente.");
                }
                break;
            case "ADMIN_UNLOCK":
                System.out.println("unlock");
                break;
            case "ADMIN_ADD_MONEY":
                try {
                    addMoneyAdmin(tokens[1], tokens[2]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Numar invalid de argumente.");
                }
                System.out.println(this.defaultRonAmount);
                break;
            case "ADMIN_WITHDRAW_MONEY":
                System.out.println("withdraw");
                try {
                    withdrawMoneyAdmin(tokens[1], tokens[2]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("Numar invalid de argumente.");
                }
                System.out.println(this.defaultRonAmount);
                break;
        }
    }

    private boolean isClientOfTheBank() {
        String answer = "";
        do {
            try {
                answer = scanner.nextLine();//verifyadmincommand
                verifyAdminCommand(answer);
                System.out.println("ANSWER " + answer);
                if (!answer.equals("0") && !answer.equals("1")) {
                    throw new InputMismatchException();
                }
            } catch (InputMismatchException e) {
                System.err.println(INVALID_OPTION);
            }
        } while (!answer.equals("0") && !answer.equals("1"));
        return answer.equals("1");
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

    private int performActionsForIndirectClient() {
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
        return pickedOption;
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
//        scanner.nextLine();
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
                if ((this.defaultRonAmount < 10000 && amount > defaultRonAmount / 10) || amount % 10 != 0) {
                    System.out.println(CANT_WITHDRAW_AMOUNT);
                    return false;
                }
                this.defaultRonAmount -= amount;
                return true;
            case "£":
                if ((this.defaultPoundAmount < 1000 && amount > defaultPoundAmount / 10) || amount % 10 != 0) {
                    System.out.println(CANT_WITHDRAW_AMOUNT);
                    return false;
                }
                this.defaultPoundAmount -= amount;
                return true;
            case "$":
                if ((this.defaultDollarAmount < 1000 && amount > defaultDollarAmount / 10) || amount % 10 != 0) {
                    System.out.println(CANT_WITHDRAW_AMOUNT);
                    return false;
                }
                this.defaultDollarAmount -= amount;
                return true;
            case "€":
                if ((this.defaultEuroAmount < 1000 && amount > defaultEuroAmount / 10) || amount % 10 != 0) {
                    System.out.println(CANT_WITHDRAW_AMOUNT);
                    return false;
                }
                this.defaultEuroAmount -= amount;
                return true;
            default:
                System.err.println(INVALID_CURRENCY);
                return false;
        }
    }

    private String getAmountFromUser() {
        scanner.nextLine();
        System.out.println("Introduceti suma si valuta: ");
        return scanner.nextLine().toLowerCase();
    }

    private void performWithdrawForIndirectClient(String[] currencyTokens) {
        try {
            int amount = Integer.parseInt(currencyTokens[0]);
            String currency = currencyTokens[1];
            if (isSufficientAmount(amount, currency)) {
                System.out.println("Va rugam ridicati banii. La revedere!");
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ati introdus datele intr-un format invalid.");
            System.err.flush();
        }
    }

    private void performDepositForIndirectClient(String[] currencyTokens) {
        try {
            int amount = Integer.parseInt(currencyTokens[0]);
            String currency = currencyTokens[1];
            if (canDepositInAtm(amount, currency)) {
                System.out.println("Banii au fost depozitati. La revedere!");
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ati introdus datele intr-un format invalid.");
            System.err.flush();
        }
    }

    private void performWithdraw(String[] currencyTokens, BankAccount bankAccount) {
        try {
            int amount = Integer.parseInt(currencyTokens[0]);
            String currency = currencyTokens[1];
            if (isSufficientAmount(amount, currency)) {
                if (bankAccount.withdrawAmount(amount, currency)) {
                    System.out.println("Va rugam ridicati banii. La revedere!");
                }
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ati introdus datele intr-un format invalid.");
            System.err.flush();
        }
    }

    private boolean canDepositInAtm(int amount, String currency) {
        switch (currency) {
            case "ron":
                this.defaultRonAmount += amount;
                return true;
            case "$":
                this.defaultDollarAmount += amount;
                return true;
            case "£":
                this.defaultPoundAmount += amount;
                return true;
            case "€":
                this.defaultEuroAmount += amount;
                return true;
            default:
                System.err.println(INVALID_CURRENCY);
                return false;
        }
    }

    private void performDeposit(String[] currencyTokens, BankAccount bankAccount) {
        try {
            int amount = Integer.parseInt(currencyTokens[0]);
            String currency = currencyTokens[1];
            if (canDepositInAtm(amount, currency)) {
                bankAccount.depositAmount(amount, currency);
                System.out.println("Banii au fost depozitati. La revedere!");
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ati introdus datele intr-un format invalid.");
            System.err.flush();
        }
    }

    private boolean isExchangePossible(int amount, String currency) {
        switch (currency) {
            case "$":
                if (this.defaultRonAmount >= amount * dollarExchangeRate) {
                    this.defaultDollarAmount += amount;
                    this.defaultRonAmount -= amount * dollarExchangeRate;
                    System.out.println("Ati primit " + amount * dollarExchangeRate + " lei.");
                    return true;
                }
                break;
            case "£":
                if (this.defaultRonAmount >= amount * poundExchangeRate) {
                    this.defaultPoundAmount += amount;
                    this.defaultRonAmount -= amount * poundExchangeRate;
                    System.out.println("Ati primit " + amount * poundExchangeRate + " lei.");
                    return true;
                }
                break;
            case "€":
                if (this.defaultRonAmount >= amount * euroExchangeRate) {
                    this.defaultEuroAmount += amount;
                    this.defaultRonAmount -= amount * euroExchangeRate;
                    System.out.println("Ati primit " + amount * euroExchangeRate + " lei.");
                    return true;
                }
                break;
            default:
                System.err.println(INVALID_CURRENCY);
                return false;
        }
        return false;
    }

    private void performExchange(String[] currencyTokens) {
        try {
            int amount = Integer.parseInt(currencyTokens[0]);
            String currency = currencyTokens[1];
            if (isExchangePossible(amount, currency)) {
                System.out.println("Va rugam ridicati banii. La revedere!");
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Ati introdus datele intr-un format invalid.");
        }
    }

    public void run() {
        loadExchangeRatesFromFile();
        System.out.println("Sunteti client al bancii noastre? (0/1)");
        boolean answer = isClientOfTheBank();

        if (answer) {
            //introducere id si pin
            BankAccount bankAccount = getAccountWithCredentials();
            System.out.println("CONT BANCA :" + bankAccount);
            int chosenOption;
            if (bankAccount != null) {
                chosenOption = getActionsForExistingClient();
                String[] currencyTokens = new String[0];
                switch (chosenOption) {
                    case 0:
                        System.out.println(bankAccount.checkBalance());
                        break;
                    case 1:
                        String amount = getAmountFromUser(); //100 $
                        currencyTokens = amount.split(" ");
                        performWithdraw(currencyTokens, bankAccount);
                        break;
                    case 2:
                        amount = getAmountFromUser();
                        currencyTokens = amount.split(" ");
                        performDeposit(currencyTokens, bankAccount);
                        break;
                    case 3:
                        amount = getAmountFromUser();
                        currencyTokens = amount.split(" ");
                        performExchange(currencyTokens);
                        break;
                }
                writeLogToFile(bankAccount.toString() + " | " + chosenOption + "," + Arrays.toString(currencyTokens) + "\n");
            }
        } else {
            int chosenOption = performActionsForIndirectClient(); //aici iau ce input da si scriu in logs
            String[] currencyTokens = new String[0];
            switch (chosenOption) {
                case 0:
                    String amount = getAmountFromUser();
                    currencyTokens = amount.split(" ");
                    performWithdrawForIndirectClient(currencyTokens);
                    break;
                case 1:
                    amount = getAmountFromUser();
                    currencyTokens = amount.split(" ");
                    performDepositForIndirectClient(currencyTokens);
                    break;
                case 2:
                    amount = getAmountFromUser();
                    currencyTokens = amount.split(" ");
                    performExchange(currencyTokens);
                    break;
            }
            writeLogToFile("unregistered user | " + chosenOption + "," + Arrays.toString(currencyTokens) + "\n");
        }
        scanner.close();
    }
}
package bank;

import java.io.*;

public class BankAccount {
    private String id;
    private String pin;
    private boolean blocked;
    private double ron;
    private double euro;
    private double pounds;
    private double dollar;

    public BankAccount(String id, String pin, boolean blocked, double ron, double euro, double pounds, double dollar) {
        this.id = id;
        this.pin = pin;
        this.blocked = blocked;
        this.ron = ron;
        this.euro = euro;
        this.pounds = pounds;
        this.dollar = dollar;
    }

    public void updateUserInConfigFile(String id, String toBeReplaced) {
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(".").getCanonicalPath() + "/src/users.txt")))) {
            StringBuffer inputBuffer = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] tokens = line.split(",");
                String currentId = tokens[0];
                if (currentId.equals(id)) {
                    inputBuffer.append(toBeReplaced);
                } else {
                    inputBuffer.append(line);
                }
                inputBuffer.append('\n');
                line = bufferedReader.readLine();
            }
            FileOutputStream fileOut = new FileOutputStream(new File(".").getCanonicalPath() + "/src/users.txt");
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
        } catch (Exception e) {
            System.err.println("Nu s-a putut realiza actualizarea fisierului.");
            System.err.flush();
        }
    }

    public String checkBalance() {
        return "Ron: " + ron + "\n" + "Euro: " + euro + "€\n" + "Lire: " + pounds + "£\n" + "Dolari: " + dollar + "$";
    }

    public boolean withdrawAmount(double quantity, String currency) {
        System.out.println("WITHDRAWING>>");//aici as putea sa updatez mereu userul dupa switch()
        if (quantity <= 0) {
            System.err.println("Ati introdus o valoare gresita pentru suma.");
            return false;
        }
        switch (currency) {
            case "ron":
                if (this.ron >= quantity) {
                    this.ron -= quantity;
                    String toBeReplaced = id + "," + pin + "," + blocked + "," + ron + "," + euro + "," + pounds + "," + dollar;
                    updateUserInConfigFile(this.id, toBeReplaced);
                    return true;
                } else {
                    System.err.println("Fonduri insuficiente.");
                }
                break;
            case "€":
                if (this.euro >= quantity) {
                    this.euro -= quantity;
                    String toBeReplaced = id + "," + pin + "," + blocked + "," + ron + "," + euro + "," + pounds + "," + dollar;
                    updateUserInConfigFile(this.id, toBeReplaced);
                    return true;
                } else {
                    System.err.println("Fonduri insuficiente.");
                }
                break;
            case "£":
                if (this.pounds >= quantity) {
                    this.pounds -= quantity;
                    String toBeReplaced = id + "," + pin + "," + blocked + "," + ron + "," + euro + "," + pounds + "," + dollar;
                    updateUserInConfigFile(this.id, toBeReplaced);
                    return true;
                } else {
                    System.err.println("Fonduri insuficiente.");
                }
                break;
            case "$":
                if (this.dollar >= quantity) {
                    this.dollar -= quantity;
                    String toBeReplaced = id + "," + pin + "," + blocked + "," + ron + "," + euro + "," + pounds + "," + dollar;
                    updateUserInConfigFile(this.id, toBeReplaced);
                    return true;
                } else {
                    System.err.println("Fonduri insuficiente.");
                }
                break;
            default:
                System.err.println("Valuta inexistenta!");
                System.err.flush();
                return false;
        }
        return false;
    }

    public void depositAmount(int amount, String currency) {
        System.out.println("DEPOSITING>>>");
        switch (currency) {
            case "ron":
                this.ron += amount;
                break;
            case "$":
                this.dollar += amount;
                break;
            case "£":
                this.pounds += amount;
                break;
            case "€":
                this.euro += amount;
                break;
        }
        String toBeReplaced = id + "," + pin + "," + blocked + "," + ron + "," + euro + "," + pounds + "," + dollar;
        updateUserInConfigFile(this.id, toBeReplaced);
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' +
                ", pin='" + pin + '\'' +
                ", blocked=" + blocked +
                ", ron=" + ron +
                ", euro=" + euro +
                ", lire=" + pounds +
                ", dollar=" + dollar;
    }
}

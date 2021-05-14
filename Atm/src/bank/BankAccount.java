package bank;

public class BankAccount {
    private int id;
    private int pin;
    private boolean blocked;
    private double ron;
    private double euro;
    private double lire;
    private double dolari;

    public BankAccount(int id, int pin,boolean blocked, double ron, double euro, double lire, double dolari) {
        this.id = id;
        this.pin = pin;
        this.blocked = blocked;
        this.ron = ron;
        this.euro = euro;
        this.lire = lire;
        this.dolari = dolari;
    }
}

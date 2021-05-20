package application;

import atm.Atm;

public class App {
    public static void main(String[] args) {
        Atm atm = new Atm();
        while (true) {
            atm.run();
        }
    }
}
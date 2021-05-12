package atm;

public class Menu {
    private final String[] options = {"0. Interogare sold", "1. Retragere", "2. Depunere", "3. Exchange"};

    public void display() {
        for (String str : options) {
            System.out.println(str);
        }
    }
}
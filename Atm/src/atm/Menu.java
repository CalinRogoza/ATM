package atm;

public class Menu {
    private final String[] OPTIONS_CLIENT = {"0. Interogare sold", "1. Retragere", "2. Depunere", "3. Exchange"};
    private final String[] OPTIONS_INDIRECT_CLIENT = {"0. Retragere", "1. Depunere", "2. Exchange"};

    public void displayForClient() {
        for (String str : OPTIONS_CLIENT) {
            System.out.println(str);
        }
    }

    public void displayForIndirectClient() {
        for (String str : OPTIONS_INDIRECT_CLIENT) {
            System.out.println(str);
        }
    }
}
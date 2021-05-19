package application;

import atm.Atm;
import bank.BankAccount;

import java.io.*;

public class App {
    public static void main(String[] args) throws IOException {
//        BankAccount bankAccount = new BankAccount("123","1234",false,1000d,1000d,100d,100d);
//        bankAccount.updateUserInConfigFile("123", "fafafa");

        Atm atm = new Atm();
        while (true) {
            atm.run();
        }


//        String filePath = new File(".").getCanonicalPath() + "/src/users.txt";
//
//        try (var br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            int lineNumber = 0;
//            while ((line = br.readLine()) != null) {
//                System.out.println(line);
//                String[] fields = line.split(",");
//                if (fields.length != 7) {
//                    System.out.println("This line does not have 7 fields: " + line);
//                    continue;
//                }
//                try {
//                    int id = Integer.parseInt(fields[0]);
//                    System.out.println("ID: " + id);
//                } catch (NumberFormatException e) {
//                    System.out.println("Number badly formatted: " + fields[0] + " at line:" + lineNumber);
//                }
//                lineNumber++;
//            }
//            if (lineNumber ==0){
//                System.out.println("The file is empty!");
//            }
//        } catch (FileNotFoundException e) {
//            System.out.println("Cannot open file.");
//        } catch (IOException e) {
//            System.out.println("Cannot read file.");
//        }


    }
}
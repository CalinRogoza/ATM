package application;

import atm.Atm;

import java.io.*;

public class App {
    public static void main(String[] args) throws IOException {
        Atm atm = new Atm();
        atm.run();

        String filePath = new File(".").getCanonicalPath() + "/src/users.txt";

        try (var br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                String[] fields = line.split(",");
                if (fields.length != 2) {
                    System.out.println("This line does not have 2 fields: " + line);
                    continue;
                }
                try {
                    int id = Integer.parseInt(fields[0]);
                    System.out.println("ID: " + id);
                } catch (NumberFormatException e) {
                    System.out.println("Number badly formatted: " + fields[0] + " at line:" + lineNumber);
                }
                lineNumber++;
            }
            if (lineNumber ==0){
                System.out.println("The file is empty!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot open file.");
        } catch (IOException e) {
            System.out.println("Cannot read file.");
        }
    }
}
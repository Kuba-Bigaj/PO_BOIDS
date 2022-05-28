package sim;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Scribe {
    private FileWriter writer;

    Scribe() {
        try {
            System.out.println(LocalDate.now());
            this.writer = new FileWriter("data_out\\" + LocalDate.now() + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to initialize a file!");
            System.exit(1);
        }
    }

    public void write(String s) {
        try {
            writer.write(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fin() {
        try {
            writer.close();
            System.out.println("Finalized successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
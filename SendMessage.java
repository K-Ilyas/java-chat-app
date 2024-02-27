import java.net.Socket;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendMessage implements Runnable {

    Socket soc = null;
    UserInformation user = null;
    DataOutputStream out = null;
    ObjectOutputStream bos = null;

    public SendMessage(DataOutputStream out, ObjectOutputStream bos, UserInformation user) {
        this.user = user;
        this.out = out;
        this.bos = bos;
    }

    public void run() {
        String pseudo = "", name = "";
        Scanner sc = new Scanner(System.in);
        try {
            do {
                pseudo = sc.nextLine();
                out.writeUTF(pseudo.toString());
                if (!pseudo.toUpperCase().equals("N")) {
                    System.out.println("Enter your message ===>> ");
                    name = sc.nextLine();
                    out.writeUTF(name.toString());
                }
                out.flush();
                bos.writeObject(user);
                bos.flush();
                bos.flush();

                API.printMessage("\n Enter the user pseudo or nothing to brodcast ===>> ");


            } while (!pseudo.toUpperCase().equals("N"));

            sc.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

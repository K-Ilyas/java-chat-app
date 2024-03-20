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
    UserInformation friend = null;

    Scanner scanf = null;

    public SendMessage(DataOutputStream out, ObjectOutputStream bos, UserInformation user, UserInformation friend, Scanner scanf) {
        this.user = user;
        this.out = out;
        this.bos = bos;
        this.friend = friend;
        this.scanf = scanf;
    }

    public void run() {
        String  message = "";
        try {
            do {
                
                System.out.println("Enter your message ===>> ");
                
                message = scanf.nextLine();
                out.writeUTF(message.toString());
                out.flush();
                bos.writeObject(friend);
                bos.flush();
                bos.writeObject(user);
                bos.flush();


            } while (!message.toUpperCase().equals("N"));

            scanf.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

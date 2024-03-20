import java.net.Socket;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ReciveMessage implements Runnable {

    Socket soc = null;
    UserInformation user = null;

    public ReciveMessage(Socket soc, UserInformation user) {
        this.soc = soc;
        this.user = user;
    }

    public void run() {
        String sender = "", time = "", data = "";
        int header = 1;
        try {
            InputStream in = this.soc.getInputStream();
            DataInputStream dis = new DataInputStream(in);
            do {

                API.printMessage("[" + user.getPseudo() + "] : Waiting....");
                header = dis.readInt();
                if (header == 1) {
                    sender = dis.readUTF();
                    time = dis.readUTF();
                    data = dis.readUTF();
                    API.printMessage("{ TIME : [" + time + "] FROM : [" + sender + "]  }" + "\n{ MESSAGE: [" + data
                            + "]  }\n\n");
                } else if (header == 2) {
                    API.printMessage(dis.readUTF());
                } else {
                    API.printMessage(dis.readUTF());
                }
            } while (header != 0);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 

    }

}
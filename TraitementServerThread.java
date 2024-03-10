import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.Map.Entry;

public class TraitementServerThread implements Runnable {

    private Socket client = null;
    private SocketServer socketServer = null;
    
    public TraitementServerThread(Socket client, SocketServer socketServer) {
        this.client = client;
        this.socketServer = socketServer;
    }

    public void run() {
        try (OutputStream out = this.client.getOutputStream();
                InputStream in = this.client.getInputStream();

                DataInputStream dis = new DataInputStream(in);
                DataOutputStream dos = new DataOutputStream(out);
                ObjectInputStream ois = new ObjectInputStream(in);) {
            int i = 0;
            boolean isTrue = true;

            UserInformation userInformation = null;

            while (isTrue) {

                i = dis.readInt();

                switch (i) {
                    case 1:
                        System.out.println("SERVER : YOU CAN LOG IN");

                        userInformation = null;
                        try {
                            userInformation = (UserInformation) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        if (userInformation != null) {

                            if (this.socketServer.isLogedInOrInit(userInformation, this.client)) {
                                dos.writeInt(1);
                                dos.writeUTF("SERVER : YOUR CONNEXION HAS ESTABLISHED SUCCESFULLY");
                                dos.flush();
                                isTrue = false;
                            } else {
                                dos.writeInt(0);
                                dos.writeUTF("SERVER :YOUR PSEUDO OR PASSWORD AR RONG PLEASE RETAIT");
                                dos.flush();
                            }
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR OBJECT SEND IS NULL !!!");
                            dos.flush();
                            System.out.println("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");

                        }

                        break;
                    case 2:

                        System.out.println("SERVER : YOU CAN SING IN");
                        System.out.println(dis.readUTF());
                        userInformation = null;
                        try {
                            userInformation = (UserInformation) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println(dis.readUTF());

                        if (userInformation != null) {

                            if (this.socketServer.addUser(userInformation, this.client)) {
                                dos.writeInt(1);
                                dos.writeUTF("SERVER : YOUR REGISTARTION HAS ESTABLISHED SUCCESFULY");
                                dos.flush();
                                isTrue = false;

                            } else {
                                dos.writeInt(0);
                                dos.writeUTF(
                                        "SERVER :YOUR PSEUDO IS NOT SUPORTABLE OR PASSWORD NOT FORT PLEASE RETAIT");
                                dos.flush();
                            }
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR OBJECT SEND IS NULL !!!");
                            dos.flush();
                            System.out.println("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");

                        }

                        break;

                    default:
                        System.out.println("You have an error in your cmmande please retrait");
                        break;

                }

            }

            isTrue = true;
            String header = "", data = "";

            while (isTrue) {

                header = dis.readUTF();


                if (header.toUpperCase().equals("N")) {
                    isTrue = false;
                    userInformation = null;
                    try {
                        userInformation = (UserInformation) ois.readObject();
                    } catch (

                    ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (userInformation != null) {

                        if (this.socketServer.logOut(userInformation)) {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER : YOUR LOG OUT SUCCESFLY");
                            dos.flush();
                            System.out.println("SERVER : bye bye" + userInformation + " !!!!");
                            isTrue = false;
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR PSEUDO IS NOT SUPORTABLE OR PASSWORD NOT STRONG PLEASE RETAIT");
                            dos.flush();
                        }
                    } else {
                        dos.writeInt(0);
                        dos.writeUTF("SERVER :YOUR SENDED OBJECT IS NULL !!!");
                        dos.flush();
                        System.out.println("SERVER : YOU HAVE AN ERROR, YOUR CONNECTION FAILD");

                    }

                } else {

                    data = dis.readUTF();
                    try {
                        userInformation = (UserInformation) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    System.out.println(header);

                    if (!header.equals("") && !header.equals("*")) {

                        Socket reciver = this.socketServer.findUserSocket(header);
                        System.out.println(reciver);
                        if (reciver != null) {

                            try {
                                OutputStream outRecive = reciver.getOutputStream();
                                DataOutputStream dosRecive = new DataOutputStream(outRecive);
                                dosRecive.writeInt(1);
                                dosRecive.writeUTF(userInformation.getPseudo());
                                dosRecive.writeUTF(LocalDateTime.now().toString());
                                dosRecive.writeUTF(data);
                                dosRecive.flush();
                                isTrue = true;
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        } else {
                            dos.writeInt(2);
                            dos.writeUTF("SERVER:SORRY THE DESTINATIOn NOT FOUND");
                            dos.flush();
                        }
                    } else {

                        System.out.printf(" BROADCAST A MESSAGE TO THE SERVER");
                        Set<Entry<UserInformation, Socket>> users = this.socketServer.getCon_table().entrySet();
                        for (Entry<UserInformation, Socket> entry : users) {
                            if (entry.getKey().compareTo(userInformation) != 0) {
                                try {
                                    OutputStream outRecive = entry.getValue().getOutputStream();
                                    DataOutputStream dosRecive = new DataOutputStream(outRecive);
                                    dosRecive.writeInt(1);
                                    dosRecive.writeUTF(userInformation.getPseudo());
                                    dosRecive.writeUTF(LocalDateTime.now().toString());
                                    dosRecive.writeUTF(data);
                                    dosRecive.flush();
                                    isTrue = true;
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (this.client != null) {
                    this.client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
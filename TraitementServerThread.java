import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
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
                ObjectInputStream ois = new ObjectInputStream(in);
                ObjectOutputStream oos = new ObjectOutputStream(out);) {
            int i = 0;
            boolean isTrue = true;
            UserDAO user_orm = new UserDAO(this.socketServer.getConnect());
            UserInformation userInformation = null;
            MessageToDAO message_orm = new MessageToDAO(this.socketServer.getConnect());
            LinkedList<MessageTo> messages = null;
            while (isTrue) {

                i = dis.readInt();

                switch (i) {
                    case 1:
                        ServerLogs.printLog("SERVER : YOU CAN LOG IN");
                        userInformation = null;
                        try {
                            userInformation = (UserInformation) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                        userInformation = user_orm.find(userInformation.getPseudo());
                        if (userInformation != null) {

                            if (!this.socketServer.isLogedInOrInit(userInformation, this.client)) {
                                this.socketServer.addUser(userInformation, client);
                            }
                            ;
                            dos.writeInt(1);
                            dos.writeUTF("SERVER : YOUR CONNEXION HAS ESTABLISHED SUCCESFULLY");
                            dos.flush();
                            isTrue = false;

                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :YOUR OBJECT SEND IS NULL !!!");
                            dos.flush();
                            ServerLogs.printLog("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");
                        }

                        break;
                    case 2:

                        ServerLogs.printLog("SERVER : YOU CAN SING IN");
                        ServerLogs.printLog(dis.readUTF());
                        userInformation = null;
                        try {
                            userInformation = (UserInformation) ois.readObject();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        System.out.println(dis.readUTF());

                        if (userInformation != null) {

                            if (user_orm.create(userInformation)) {
                                System.out.println("user created :::" + userInformation.getUuid() );
                                this.socketServer.addUser(userInformation, this.client);
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
                            ServerLogs.printLog("SERVER : YOU HAVE AN ERROR IN YOUR CONNECTION FAILD");
                        }
                        break;

                    default:
                        ServerLogs.printLog("You have an error in your cmmande please retrait");
                        break;

                }

            }

            oos.writeObject(userInformation);
            oos.flush();

            LinkedList<UserInformation> amis = user_orm.findAll(userInformation.getUuid());
            oos.writeObject(amis);
            oos.flush();

            int result = dis.readInt();
            System.out.println(result);
            if (result == 0) {
                int second = dis.readInt() ;
                messages = message_orm.findAll(userInformation.getUuid(), amis.get(second).getUuid());
                oos.writeObject( messages == null ? new LinkedList<MessageTo>() : messages);
                oos.flush();
            }

            isTrue = true;
            String message = "", data = "";

            UserInformation friend = null;

            while (isTrue) {

                message = dis.readUTF();
                System.out.println(message);

                if (message.toUpperCase().equals("N")) {
                    isTrue = false;
                    userInformation = null;
                    try {
                        friend = (UserInformation) ois.readObject();
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
                            ServerLogs.printLog("SERVER : bye bye" + userInformation.getUuid() + " - "
                                    + userInformation.getPseudo() + " !!!!");
                            isTrue = false;
                        } else {
                            dos.writeInt(0);
                            dos.writeUTF("SERVER :SOMETHING WENT WRONG PLEASE RETRY!!!");
                            dos.flush();
                        }
                    } else {
                        dos.writeInt(0);
                        dos.writeUTF("SERVER : YOUR SENDED OBJECT IS NULL !!!");
                        dos.flush();
                        ServerLogs.printLog("SERVER : YOU HAVE AN ERROR, YOUR CONNECTION FAILD !!");
                    }

                } else {

                    try {
                        friend = (UserInformation) ois.readObject();
                        userInformation = (UserInformation) ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (!message.equals("")) {

                        Socket reciver = this.socketServer.findUserSocket(friend.getUuid());


                        MessageTo messageTo = new MessageTo(userInformation.getUuid(), friend.getUuid(), message,
                        LocalDateTime.now().toString(), false);
                        message_orm.create(messageTo);

                        dos.writeInt(1);
                        dos.writeUTF("you");
                        dos.writeUTF(messageTo.getMessage_date().toString());
                        dos.writeUTF(messageTo.getMessage());
                        dos.flush();


                        if (reciver != null) {
                            try {
                                OutputStream outRecive = reciver.getOutputStream();
                                DataOutputStream dosRecive = new DataOutputStream(outRecive);
                                dosRecive.writeInt(1);
                                dosRecive.writeUTF(userInformation.getPseudo() + " - " + userInformation.getUuid());
                                dosRecive.writeUTF(messageTo.getMessage_date().toString());
                                dosRecive.writeUTF(messageTo.getMessage());
                                dosRecive.flush();
                                isTrue = true;
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    } else {
                        
                        ServerLogs.printLog("BROADCAST A MESSAGE TO THE SERVER");
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

        } catch (

        FileNotFoundException e) {
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
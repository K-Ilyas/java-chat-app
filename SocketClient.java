import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Scanner;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SocketClient {

    final static int PORT_CON = 4040;
    private Socket socket = null;
    private Boolean isConnected = false;
    private Scanner scanf = new Scanner(System.in);
    private Boolean isLoged = false;
    private UserInformation userInformation = null;
    private Thread recive = null;
    private Thread send = null;

    private OutputStream out;
    private InputStream in;

    private DataInputStream dis;
    private DataOutputStream dos;
    private ObjectOutputStream bos;
    private ObjectInputStream ois;
    private SendMessage sendMessage = null;

    private LinkedList<UserInformation> userList = null;
    private LinkedList<MessageTo> messageList = null;

    public SocketClient() {

        this.socket = null;
        try {

            this.socket = new Socket(InetAddress.getLocalHost(), PORT_CON);
            this.isConnected = true;
            out = this.socket.getOutputStream();
            in = this.socket.getInputStream();
            dis = new DataInputStream(in);
            dos = new DataOutputStream(out);
            bos = new ObjectOutputStream(out);
            ois = new ObjectInputStream(in);
            sendMessage = new SendMessage(dos, bos, null, null, scanf);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UserInformation login(String pseudo, String password) {
        UserInformation user = null;
        try {
            dos.writeInt(1);
            dos.flush();
            user = new UserInformation(pseudo, password);
            bos.writeObject(user);
            bos.flush();
            int i = dis.readInt();
            API.printMessageClient(dis.readUTF());
            if (i == 0) {
                API.printMessageClient("PLEASE RETRY");
            } else {
                this.userInformation = (UserInformation) ois.readObject();
                this.isLoged = true;
            }
        } catch (IOException e) {
            API.printMessageClient(e.getMessage());
        } catch (ClassNotFoundException e) {
            API.printMessageClient(e.getMessage());
        }
        return this.userInformation;
    }

    public UserInformation singIn(String pseudo, String password) {
        try {
            if (!this.isLoged) {
                dos.writeInt(2);
                dos.writeUTF("OK-OK-OK-OK");
                dos.flush();
                this.userInformation = new UserInformation(pseudo, password);
                bos.writeObject(this.userInformation);
                bos.flush();
                dos.writeUTF("OK-OK-OK-OK");
                dos.flush();
                int i = dis.readInt();
                API.printMessageClient(dis.readUTF());
                if (i == 0) {
                    API.printMessageClient("PLEASE RETRY");
                } else {
                    this.userInformation = (UserInformation) ois.readObject();
                    this.isLoged = true;
                }
            } else
                API.printMessageClient("Soory you elaready LOG IN");
        } catch (IOException e) {
            API.printMessageClient(e.getMessage());
        } catch (ClassNotFoundException e) {
            API.printMessageClient(e.getMessage());
        }
        return this.userInformation;
    }

    public void launchThread() {
        this.recive = new Thread(new ReciveMessage(this.socket, this.userInformation));
        this.recive.start();
    }

    @SuppressWarnings("unchecked")
    public LinkedList<UserInformation> getUsers() {
        try {
            dos.writeInt(3);
            dos.flush();
            this.userList = (LinkedList<UserInformation>) ois.readObject();
        } catch (IOException e) {
            API.printMessageClient(e.getMessage());
        } catch (ClassNotFoundException e) {
            API.printMessageClient(e.getMessage());
        }
        return this.userList;
    }

    @SuppressWarnings("unchecked")
    public LinkedList<MessageTo> getMessages(UserInformation user, UserInformation friend) {
        try {
            dos.writeInt(4);
            dos.flush();
            bos.writeObject(user);
            bos.flush();
            bos.writeObject(friend);
            bos.flush();
            this.messageList = (LinkedList<MessageTo>) ois.readObject();
        } catch (IOException e) {
            API.printMessageClient(e.getMessage());
        } catch (ClassNotFoundException e) {
            API.printMessageClient(e.getMessage());
        }
        return this.messageList;
    }

    public void sendMessage(String message, UserInformation user, UserInformation friend) {
        try {
            this.sendMessage.setFriend(friend);
            dos.writeInt(5);
            dos.flush();
            dos.writeUTF(message);
            dos.flush();
            bos.writeObject(friend);
            bos.flush();
            bos.writeObject(user);
            bos.flush();
        } catch (IOException e) {
            API.printMessageClient(e.getMessage());
        }
    }

    public void brodcastMessage(String message, UserInformation user) {
        try {
            dos.writeInt(7);
            dos.flush();
            dos.writeUTF(message);
            dos.flush();
            bos.writeObject(user);
            bos.flush();
        } catch (IOException e) {
            API.printMessageClient(e.getMessage());
        }
    }

    public void logOut(UserInformation user) {
        try {
            dos.writeInt(6);
            dos.flush();
            bos.writeObject(user);
            bos.flush();
            this.isLoged = false;
        } catch (IOException e) {
            API.printMessageClient(e.getMessage());
        }
    }

    public void choix() {
        API.printMessageClient("!!-----------------------------!!");
        API.printMessageClient("!!------- POSIBLE CHOICE ------!!");
        API.printMessageClient("!!------- 1 : LOG IN   --------!!");
        API.printMessageClient("!!------- 2 : SING IN  --------!!");

    }

    public void choixMode() {
        API.printMessageClient("!!-----------------------------!!");
        API.printMessageClient("!!------- POSIBLE CHOICE ------!!");
        API.printMessageClient("!!-- 1 : CHAT WITH A FREIND  --!!");
        API.printMessageClient("!!----- 2 : JOIN A ROOM  ------!!");
    }

    public int decision() {

        int choix = 0;

        do {
            choix = scanf.nextInt();
            scanf.nextLine();
            if (choix < 1 || choix > 2)
                API.printMessageClient("Your choice is wrong please retry");

        } while (choix < 1 || choix > 2);
        return choix;
    }

    public void startConversation() throws ClassNotFoundException {

        if (this.isConnected) {

            try {
                OutputStream out = this.socket.getOutputStream();
                InputStream in = this.socket.getInputStream();

                DataInputStream dis = new DataInputStream(in);
                DataOutputStream dos = new DataOutputStream(out);
                ObjectOutputStream bos = new ObjectOutputStream(out);
                ObjectInputStream ois = new ObjectInputStream(in);

                int choix = 0;

                String pseudo = "", password = "", passwordConfirme = "";
                int i = 0;
                boolean isTrue = true;

                while (isTrue) {
                    this.choix();

                    choix = this.decision();

                    switch (choix) {
                        case 1:
                            if (!this.isLoged) {
                                int j = 0;
                                do {
                                    dos.writeInt(1);
                                    dos.flush();
                                    API.printMessageClient("Enter your pseudo ==>> ");
                                    pseudo = scanf.nextLine();
                                    API.printMessageClient("Enter your password ==>> ");
                                    password = scanf.nextLine();
                                    this.userInformation = new UserInformation(pseudo, password);
                                    bos.writeObject(this.userInformation);
                                    bos.flush();

                                    i = dis.readInt();
                                    API.printMessageClient(dis.readUTF());

                                    if (i == 0)
                                        API.printMessageClient("PLEASE RETRY");
                                    else {
                                        this.isLoged = true;
                                        isTrue = false;
                                    }
                                    if (j == 2) {
                                        API.printMessageClient("sorry your time expired");
                                    }
                                    j++;

                                } while (i == 0 && j < 3);
                            } else
                                System.err.println("Soory you elaready LOG IN");

                            break;

                        case 2:
                            if (!this.isLoged) {

                                do {
                                    dos.writeInt(2);
                                    dos.writeUTF("OK-OK-OK-OK");
                                    dos.flush();
                                    API.printMessageClient("Enter your pseudo ==>>");
                                    pseudo = scanf.nextLine();

                                    do {
                                        API.printMessageClient("Enter your password ==>>");
                                        password = scanf.nextLine();
                                        API.printMessageClient("confirme your password :");
                                        passwordConfirme = scanf.nextLine();

                                        if (!password.equals(passwordConfirme))
                                            API.printMessageClient("PASSWORD NOT LIKE PASSWORD CONFIRME PLEASE RETRY");
                                        if (password.length() < 5)
                                            API.printMessageClient(
                                                    "sorry your password length must be greather than 5");

                                    } while (password != passwordConfirme && password.length() < 5);

                                    this.userInformation = new UserInformation(pseudo, password);
                                    bos.writeObject(this.userInformation);
                                    bos.flush();
                                    dos.writeUTF("OK-OK-OK-OK");
                                    dos.flush();

                                    i = dis.readInt();
                                    API.printMessageClient(dis.readUTF());

                                    if (i == 0)
                                        API.printMessageClient("PLEASE RETRY");
                                    else {
                                        this.isLoged = true;
                                        isTrue = false;
                                    }

                                } while (i == 0);
                            } else
                                System.err.println("Soory you elaready LOG IN");

                            break;
                        default:
                            System.err.println("your choice is wrong !!");
                            break;
                    }

                }
                userInformation = (UserInformation) ois.readObject();

                @SuppressWarnings("unchecked")
                LinkedList<UserInformation> userList = (LinkedList<UserInformation>) ois.readObject();

                this.choixMode();
                choix = this.decision();

                switch (1) {
                    case 1:
                        API.printMessageClient("LIST OF USERS   :");
                        for (int index = 0; index < userList.size(); index++) {
                            UserInformation user = userList.get(index);

                            API.printMessageClient(
                                    "[" + (index + 1) + "] : " + user.getPseudo() + " - " + user.getUuid());
                        }

                        int amis = 0;

                        do {
                            amis = scanf.nextInt();
                            scanf.nextLine();
                            if (amis < 1 || amis > userList.size() + 1)
                                System.out.println("Your choice is wrong please retry");
                        } while (amis < 1 || amis > userList.size() + 1);

                        dos.writeInt(0);
                        dos.flush();
                        dos.writeInt(amis - 1);
                        dos.flush();

                        @SuppressWarnings("unchecked")
                        LinkedList<MessageTo> messageList = (LinkedList<MessageTo>) ois.readObject();
                        UserInformation friend = userList.get(amis - 1);

                        System.out.println("CONVERSATION WITH " + friend.getPseudo());
                        for (int index = 0; index < messageList.size(); index++) {
                            MessageTo message = messageList.get(index);
                            if (message.getIsDelated() == false) {
                                if (message.getUuid_sender().equals(this.userInformation.getUuid()))
                                    API.printMessageClient("[you ] : { message : " + message.getMessage() + ", date : "
                                            + message.getMessage_date().toString() + "}");
                                else
                                    API.printMessageClient("[" + message.getUuid_sender() + "-" + friend.getPseudo()
                                            + "] : { message : " + message.getMessage() + ", date : "
                                            + message.getMessage_date().toString() + "}");
                            }
                        }

                        break;

                    case 2:

                        // for rooms connection

                        break;

                    default:
                        break;
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else
            API.printMessageClient("THE CLIENT IS NOT CONNECTED");

    }

    public static void main(String[] args) throws ClassNotFoundException {
        SocketClient client = new SocketClient();
        // client.startConversation();

        UserInformation user = client.login("ILYAS", "ILYAS");

        System.out.println(user);

        LinkedList<UserInformation> userList = client.getUsers();

        for (int index = 0; index < userList.size(); index++) {
        System.out.println(userList.get(index));
        }

        LinkedList<MessageTo> messageList = client.getMessages(user,
        userList.get(0));

        for (int index = 0; index < messageList.size(); index++) {
        System.out.println(messageList.get(index));
        }

        client.sendMessage("Hello", user, userList.get(0));

        System.out.println("Message sent :#########################################################################");

        messageList = client.getMessages(user, userList.get(0));

        for (int index = 0; index < messageList.size(); index++) {
        System.out.println(messageList.get(index));
        }

        client.brodcastMessage("Hello", user);
        client.logOut(user);

        // Webcam webcam = Webcam.getDefault();
        // webcam.open();
        // BufferedImage image = webcam.getImage();

        // // Save the image (you can customize the filename)
        // File outputFile = new File("webcam_image.jpg");
        // try {
        //     ImageIO.write(image, "JPG", outputFile);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

        // // Close the webcam
        // webcam.close();
    }
}

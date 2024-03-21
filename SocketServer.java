
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class SocketServer {

    final static int PORT_CONN = 4040;
    private ServerSocket server = null;
    private boolean isConnected = false;
    private Hashtable<UserInformation, Socket> con_table = null;

    private Connection connect = null;

    public SocketServer() {
        this.server = null;
        try {
            this.server = new ServerSocket(PORT_CONN);
            this.isConnected = true;
            ServerLogs.printLog("SERVER : CONNECTED");
            this.connect = MySQLConnectSingleton.getInstance();
            this.con_table = new Hashtable<UserInformation, Socket>();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLogedInOrInit(UserInformation user, Socket soc) {

        if (!this.con_table.isEmpty()) {
            Enumeration<UserInformation> list = this.con_table.keys();
            while (list.hasMoreElements()) {
                if (list.nextElement().compareTo(user) == 0) {
                    this.con_table.put(user, soc);
                    return true;
                }

            }
            return false;
        } else
            return false;
    }

    public Socket findUserSocket(String uuid) {
        if (!this.con_table.isEmpty()) {

            Set<Entry<UserInformation, Socket>> list = this.con_table.entrySet();
            Iterator<Entry<UserInformation, Socket>> iterator = list.iterator();

            while (iterator.hasNext()) {


                Entry<UserInformation, Socket> user = iterator.next();
                System.out.println(String.format("'%s'", user.getKey().getUuid()));
                if (String.format("'%s'", user.getKey().getUuid()).equals(String.format("'%s'", uuid)))
                {
                    System.out.println(" ****************************** found");
                    return user.getValue();

                }
            }
        }
        return null;
    }

    public boolean isUserExist(UserInformation user) {

       

        if (!this.con_table.isEmpty()) {

            Enumeration<UserInformation> list = this.con_table.keys();

            while (list.hasMoreElements()) {

                if (list.nextElement().compareTo(user) == 0) {
                    return true;
                }
            }
            return false;
        } else
            return false;
    }

    public boolean addUser(UserInformation user, Socket soc) {
        if (!isUserExist(user)) {
            this.con_table.put(user, soc);
            ServerLogs.printLog("NEW SING IN :  " + "[" + user + "] \t" + soc);
            return true;
        }
        return false;
    }

    public boolean logOut(UserInformation user) {

        if (!this.con_table.isEmpty()) {

            Iterator<UserInformation> iterator = this.con_table.keySet().iterator();

            while (iterator.hasNext()) {
                UserInformation currentUser = iterator.next();
                if (currentUser.compareTo(user) == 0) {
                    System.out.println("-------------------- not good ----------------------");
                    iterator.remove();
                    return true;
                }
            }
            return false;
        } else
            return false;

    }

    public void startConversation() {

        ExecutorService executorService = Executors.newCachedThreadPool();
        if (this.isConnected) {
            Socket soc = null;
            for (;;) {
                try {

                    soc = this.server.accept();
                    executorService.submit(new TraitementServerThread(soc, this));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else
            ServerLogs.printLog("THE SERER IS NOT CONNECTED !!!");

    }

    public Hashtable<UserInformation, Socket> getCon_table() {
        return con_table;
    }

    public Connection getConnect() {
        return connect;
    }

    public static void main(String[] args) {
        SocketServer server = new SocketServer();
        server.startConversation();
    }
}

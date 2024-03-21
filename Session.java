import java.net.Socket;
import java.util.Scanner;

public class Session {
    private static UserInformation thisUser;
    public Socket socket = null;
    public Scanner scanf = new Scanner(System.in);

    public static void setLoggedInUser(UserInformation user) {
        thisUser = user;
    }

    public static UserInformation getLoggedInUser() {
        return thisUser;
    }

    public static boolean isLoggedIn() {
        return thisUser != null;
    }
}

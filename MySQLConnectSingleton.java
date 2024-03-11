import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectSingleton {

    private String url = "jdbc:mysql://localhost:3306/";
    private String user = "root";
    private String passwd = "Ilyas.99";
    private static Connection connect;

    private MySQLConnectSingleton() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection(url, user, passwd);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getInstance() {
        if (connect == null) {

            synchronized (MySQLConnectSingleton.class) {
                if (connect == null) {
                    new MySQLConnectSingleton();
                    System.out.println("INSTANCIATION DE LA CONNEXION SQL ! ");
                }
            }

        } else {
            System.out.println("CONNEXION SQL EXISTANTE ! ");
        }
        return connect;
    }

    // for test purposes :
    public static void main(String[] args) {
        MySQLConnectSingleton.getInstance();
    }
}
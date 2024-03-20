import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnectSingleton {

    private String url = "";
    private String user = "";
    private String passwd = "";
    private static Connection connect;

    private MySQLConnectSingleton() {
        try {
            this.url = AppConfig.getProperty("DATABASE_URL");
            this.user = AppConfig.getProperty("DATABASE_USERNAME");
            this.passwd = AppConfig.getProperty("DATABASE_PASSWORD");

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
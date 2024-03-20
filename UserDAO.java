import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.LinkedList;

public class UserDAO extends DAO<UserInformation> {

    public UserDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(UserInformation obj) {
        PreparedStatement statement = null;
        try {
            String query = "INSERT INTO user (user_uuid, username, email, hashpassword, image, isadmin) VALUES (UUID(), ?, ?, ?, ?, ?)";
            statement = this.connect.prepareStatement(query);
            statement.setString(1, obj.getPseudo());
            statement.setString(2, obj.getEmail());
            // Hash the password before storing it
            String hashedPassword = hashPassword(obj.getPassword());
            statement.setString(3, hashedPassword);
            statement.setString(4, obj.getImage());
            statement.setBoolean(5, obj.getIsadmin());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean delete(UserInformation obj) {
        PreparedStatement statement = null;
        try {
            // Check if user exists before deleting
            if (!userExists(obj.getUuid())) {
                System.out.println("User does not exist.");
                return false;
            }

            String query = "DELETE FROM user WHERE user_uuid = ?";
            statement = this.connect.prepareStatement(query);
            statement.setString(1, obj.getUuid());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User deleted successfully.");
                return true;
            } else {
                System.out.println("Failed to delete user.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean update(UserInformation obj) {
        PreparedStatement statement = null;
        try {

            if (!userExists(obj.getUuid())) {
                String query = "update user set  username =?, email=?, hashpassword=?, image =?, isadmin=? where user_uuid = ?";
                statement = this.connect.prepareStatement(query);
                statement.setString(1, obj.getPseudo());
                statement.setString(2, obj.getEmail());
                // Hash the password before storing it
                String hashedPassword = hashPassword(obj.getPassword());
                statement.setString(3, hashedPassword);
                statement.setString(4, obj.getImage());
                statement.setBoolean(5, obj.getIsadmin());
                statement.setString(6, obj.getUuid());
                statement.executeUpdate();
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean userExists(String userUUID) {
        try {
            ResultSet result = this.connect.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM user WHERE user_uuid = " + userUUID);
            if (result.first())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // a function to hash a password
    private String hashPassword(String password) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] salt = new byte[16];
            secureRandom.nextBytes(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            byte[] bytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserInformation find(String uuid) {
        UserInformation user = new UserInformation();
        try {
            ResultSet result = this.connect.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM user WHERE user_uuid = " + uuid);
            if (result.first())
                user = new UserInformation(
                        result.getString("user_uuid"),
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("hashpassword"),
                        result.getString("image"),
                        result.getBoolean("isadmin"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public LinkedList<UserInformation> findAll(String uuid) {
        LinkedList<UserInformation> users = new LinkedList<UserInformation>();
        try {
            ResultSet result = this.connect.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM user WHERE user_uuid <>" + uuid);
            while (result.next()) {
                users.add(new UserInformation(
                        result.getString("user_uuid"),
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("hashpassword"),
                        result.getString("image"),
                        result.getBoolean("isadmin")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}

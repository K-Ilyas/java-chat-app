import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.LinkedList;
import java.util.UUID;

public class UserDAO extends DAO<UserInformation> {

    public UserDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(UserInformation obj) {
        PreparedStatement statement = null;

        String uuid = UUID.randomUUID().toString();
        try {
            String query = "INSERT INTO user (uuid_user, username, email, hashpasword, image, isadmin) VALUES (?, ?, ?, ?, ?, ?)";
            statement = this.connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, uuid);
            statement.setString(2, obj.getPseudo());
            statement.setString(3, obj.getEmail());
            // Hash the password before storing it
            String hashedPassword = hashPassword(obj.getPassword());
            statement.setString(4, hashedPassword);
            statement.setString(5, obj.getImage());
            statement.setBoolean(6, obj.getIsadmin());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 0) {
                System.out.println("User created successfully.");
                UserInformation new_user = this.find(uuid);
                obj.setEmail(new_user.getEmail());
                obj.setIsadmin(new_user.getIsadmin());
                obj.setPseudo(new_user.getPseudo());
                obj.setUuid(new_user.getUuid());
                obj.setImage(new_user.getImage());
                
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

    @Override
    public boolean delete(UserInformation obj) {
        PreparedStatement statement = null;
        try {
            // Check if user exists before deleting
            if (!userExists(obj.getUuid())) {
                System.out.println("User does not exist.");
                return false;
            }

            String query = "DELETE FROM user WHERE uuid_user = ?";
            statement = this.connect.prepareStatement(query);
            statement.setString(1, obj.getUuid());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                ServerLogs.printLog("User deleted successfully.");
                return true;
            } else {
                ServerLogs.printLog("Failed to delete user.");
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
                String query = "update user set  username =?, email=?, hashpasword=?, image =?, isadmin=? where uuid_user = ?";
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
                    ResultSet.CONCUR_READ_ONLY)
                    .executeQuery(String.format("SELECT * FROM user WHERE uuid_user = '%s'", userUUID));
            if (result.first())
                return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // a function to hash a password
    public static String hashPassword(String password) {
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

    // generted a hash using
    public static boolean verifyPassword(String password, String storedHash) {
        // Generate a hash for the provided password
        String hashedPassword = hashPassword(password);

        // Compare the generated hash with the stored hash
        return hashedPassword.equals(storedHash);
    }

    @Override
    public UserInformation find(String uuid_username) {
        UserInformation user = null;
        try {
            ResultSet result = this.connect.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
                    .executeQuery(String.format("SELECT * FROM user WHERE uuid_user = '%s' or username = '%s' ",
                            uuid_username, uuid_username));

            if (result.first()) {
                user = new UserInformation(
                        result.getString("uuid_user"),
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("hashpasword"),
                        result.getString("image"),
                        result.getBoolean("isadmin"));

            } else
                return null;
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
                    ResultSet.CONCUR_READ_ONLY)
                    .executeQuery(String.format("SELECT * FROM user WHERE uuid_user <> '%s'", uuid));
            while (result.next()) {
                users.add(new UserInformation(
                        result.getString("uuid_user"),
                        result.getString("username"),
                        result.getString("email"),
                        result.getString("hashpasword"),
                        result.getString("image"),
                        result.getBoolean("isadmin")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}

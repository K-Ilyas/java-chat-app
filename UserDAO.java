import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends DAO<UserInformation> {

    public UserDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(UserInformation obj) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public boolean delete(UserInformation obj) {
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public boolean update(UserInformation obj) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
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

}

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class MessageToDAO extends DAO<MessageTo> {

    public MessageToDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(MessageTo obj) {
        PreparedStatement statement = null;
        try {
            String query = "INSERT INTO messageto (user_sender, user_reciver, message, message_date, isdelated ) VALUES (UUID(), ?, ?, ?, NOW(), ?)";
            statement = this.connect.prepareStatement(query);
            statement.setString(1, obj.getUuid_sender());
            statement.setString(2, obj.getUuid_reciver());
            statement.setString(3, obj.getMessage());
            statement.setBoolean(4, obj.getIsDelated());

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            obj.setMessage_date(generatedKeys.getDate("message_date"));

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
    public boolean delete(MessageTo obj) {
        PreparedStatement statement = null;
        try {

            String query = "DELETE FROM user WHERE uuid_sender = ? and uuid_reciver = ? ";
            statement = this.connect.prepareStatement(query);
            statement.setString(1, obj.getUuid_sender());
            statement.setString(2, obj.getUuid_reciver());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("message deleted successfully.");
                return true;
            } else {
                System.out.println("Failed to delete message.");
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
    public boolean update(MessageTo obj) {
        return false;
    }

    @Override
    public MessageTo find(String uuid) {
        return null;
    }

    public LinkedList<MessageTo> findAll(String uuid_sender,String uuid_reciver) {
        LinkedList<MessageTo> messages = new LinkedList<MessageTo>();
        try {
            ResultSet result = this.connect.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
                    .executeQuery("SELECT * FROM messageto WHERE uuid_sender = " + uuid_sender + " uuid_reciver = " + uuid_reciver);
            while (result.next()) {
                messages.add(new MessageTo(
                        result.getString("uuid_sener"),
                        result.getString("uuid_reciver"),
                        result.getString("message"),
                        result.getDate("message_date"),
                        result.getBoolean("isdelated")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

}

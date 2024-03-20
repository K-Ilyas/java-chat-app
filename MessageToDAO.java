import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.UUID;

public class MessageToDAO extends DAO<MessageTo> {

    public MessageToDAO(Connection conn) {
        super(conn);
    }

    @Override
    public boolean create(MessageTo obj) {
        PreparedStatement statement = null;
        String uuid = UUID.randomUUID().toString();
        try {
            
            String query = "INSERT INTO messageto (uuid_sender, uuid_reciver, message, message_date, isdelated ) VALUES ( ?, ?, ?, ?, ?)";

            statement = this.connect.prepareStatement(query);
            
            System.out.println("uuid_sender: " + obj.getUuid_sender() + " uuid_reciver: " + obj.getUuid_reciver());

            statement.setString(1, obj.getUuid_sender());
            statement.setString(2, obj.getUuid_reciver());
            statement.setString(3, obj.getMessage());
            statement.setDate(4, obj.getMessage_date());
            statement.setBoolean(5, obj.getIsDelated());
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

    public LinkedList<MessageTo> findAll(String uuid_sender, String uuid_reciver) {
        
        System.out.println("uuid_sender: " + uuid_sender + " uuid_reciver: " + uuid_reciver);

        LinkedList<MessageTo> messages = new LinkedList<MessageTo>();


        try {
            ResultSet result = this.connect.createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
                    .executeQuery(String.format(
                            "SELECT * FROM MESSAGETO WHERE (uuid_sender = '%s' and uuid_reciver = '%s') or (uuid_reciver = '%s' and uuid_sender = '%s')",
                            uuid_sender, uuid_reciver, uuid_sender, uuid_reciver));

            while (result.next()) {
                messages.add(new MessageTo(
                        result.getString("uuid_sender"),
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

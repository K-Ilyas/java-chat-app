import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class MessageRoomDAO extends DAO<Messageroom> {

  public MessageRoomDAO(Connection conn) {
    super(conn);
  
  }

  @Override
  public boolean create(Messageroom obj) {
    PreparedStatement statement = null;
    try {

      String query = "INSERT INTO messageroom (uuid_room,uuid_user,message,date,isDeleted) VALUES (?,?,?,NOW(),?)";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getUuid_room());
      statement.setString(2, obj.getUuid_user());
      statement.setString(3, obj.getMessage());
      statement.setBoolean(4, obj.getIsDeleted());
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
  
   public boolean messageExists(int messageId) {
  
    try {
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM messageroom WHERE message_id = " + messageId);

          if (result.first())
        return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }



  @Override
  public boolean delete(Messageroom obj) {
    PreparedStatement statement = null;

    try {
      if (!messageExists(obj.getMessage_id())) {
        System.out.println("Message does not exist.");
        return false;
      }

      String query = "DELETE FROM messageroom WHERE message_id = ?";   //here we should affected true to isDeleted in the database
      statement = this.connect.prepareStatement(query);
      statement.setInt(1, obj.getMessage_id());

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0) {
        System.out.println("Message deleted successfully.");
        return true;
      } else {
        System.out.println("Failed to delete Message.");
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
    
  }

  @Override
  public boolean update(Messageroom obj) {
    return false;
  }

  @Override
  public Messageroom find(String uuid) {
    return null;
  }


  //find all message in a room
  public LinkedList<Messageroom> findAllMessageInRoom(String uuid_room){
    LinkedList<Messageroom> list = new LinkedList<Messageroom>();
    try {
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT message FROM messageroom WHERE  uuid_room = " + uuid_room);
      while (result.next())
        list.add(new Messageroom(
          result.getInt("message_id"),
          result.getString("uuid_room"),
          result.getString("uuid_user"),
          result.getString("message"),
          result.getDate("date"),
          result.getBoolean("isDeleted")
          ));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }
}

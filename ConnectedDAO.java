import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class ConnectedDAO extends DAO<Connected> {

  public ConnectedDAO(Connection conn) {
    super(conn);
  }

  @Override
  public boolean create(Connected obj) {
    PreparedStatement statement = null;
    try {
      String query = "INSERT INTO connected (uuid_user,uuid_room) VALUES (?,?)";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getUuid_user());
      statement.setString(2, obj.getUuid_room());
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
  // disconnected
  public boolean delete(Connected obj) {
    PreparedStatement statement = null;
    try {
      String query = "DELETE FROM connected WHERE uuid_user = ? AND uuid_room = ?";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getUuid_user());
      statement.setString(2, obj.getUuid_room());
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
  public boolean update(Connected obj) {
      return false;
  }

  //find  room connected to a user
  @Override
  public Connected find(String uuid_user) {
    Connected connected = new Connected();
    try {
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM connected WHERE uuid_user = " + uuid_user);
      if (result.first())
        connected = new Connected(
            result.getString("uuid_user"),
            result.getString("uuid_room"));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connected;
  }

//find all user connected to a room
   public LinkedList<Connected> findAllUserInRoom(String uuid_room) {
    LinkedList<Connected> list = new LinkedList<Connected>();
    try {
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM connected WHERE  uuid_room = " + uuid_room);
      while (result.next())
        list.add(new Connected(
            result.getString("uuid_user"),
            result.getString("uuid_room")));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;

}
//find all room and user connected  
public LinkedList<Connected> findAllUserConnected() {
  LinkedList<Connected> list = new LinkedList<Connected>();
  try {
    ResultSet result = this.connect.createStatement(
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM connected ");
    while (result.next())
      list.add(new Connected(
          result.getString("uuid_user"),
          result.getString("uuid_room")));
  } catch (SQLException e) {
    e.printStackTrace();
  }
  return list;

}
}
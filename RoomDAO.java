import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class RoomDAO extends DAO<Room> {

  public RoomDAO(Connection conn) {
    super(conn);
  }

  @Override
  public boolean create(Room obj) {
    PreparedStatement statement = null;
    try {

      String query = "INSERT INTO room (uuid_room,roomname,image) VALUES (UUID(),?,?)";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getRoomname());
      statement.setString(2, obj.getImage());
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
  public boolean delete(Room obj) {
    PreparedStatement statement = null;

    try {
      // Check if room exists before deleting that is not possible to delete romm does not exist in interface but we essure that
      if (!roomExists(obj.getUuid_room())) {
        System.out.println("Room does not exist.");
        return false;
      }

      String query = "DELETE FROM room WHERE uuid_room = ?";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getUuid_room());

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0) {
        System.out.println("Room deleted successfully.");
        return true;
      } else {
        System.out.println("Failed to delete Room.");
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
  public boolean update(Room obj) {
    PreparedStatement statement = null;    
    try {
    if (!roomExists(obj.getUuid_room())) {
      String query = "UPDATE room SET roomname = ?, image = ? WHERE uuid_room = ?";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getRoomname());
      statement.setString(2, obj.getImage());
      statement.setString(3, obj.getUuid_room());
      statement.executeUpdate();  
    }
    else{ 
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

  return true ;
  }

  //information about room
  @Override
  public Room find(String uuid) {
    Room room = new Room();
    try {
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM room WHERE uuid_room = " + uuid);
      if (result.first())
        room = new Room(
            result.getString("uuid_room"),
            result.getString("roomname"),
            result.getString("image"));
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return room;
  }

  public boolean roomExists(String uuid_room) {
    try {
      // spécifient que le ResultSet sera insensible aux modifications et en lecture
      // seule.
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM room WHERE uuid_room = " + uuid_room);
      // Cette ligne vérifie si le ResultSet contient au moins une ligne de résultat.
      if (result.first())
        return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
//all room exesting
  public LinkedList<Room> findAllRoom() {
    LinkedList<Room> list = new LinkedList<Room>();
    try {
      ResultSet result = this.connect.createStatement(
          ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_READ_ONLY).executeQuery("SELECT * FROM room");
      while (result.next())
        list.add(new Room(
            result.getString("uuid_room"),
            result.getString("roomname"),
            result.getString("image")));
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return list;
  }
}

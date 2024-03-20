import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;


public class AmisDAO extends DAO<Amis> {

  public AmisDAO(Connection conn) {
    super(conn);
  
  }

  @Override
  public boolean create(Amis obj) {
  
    PreparedStatement statement = null;
    try {

      String query = "INSERT INTO amis (uuid_user,uuid_second_user) VALUES (?,?)";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getUuid_user());
      statement.setString(2, obj.getUuid_second_user());
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
  public boolean delete(Amis obj) {
    PreparedStatement statement = null;
    try {
      String query = "DELETE FROM amis WHERE uuid_user = ? AND uuid_second_user = ?";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, obj.getUuid_user());
      statement.setString(2, obj.getUuid_second_user());
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
  public boolean update(Amis obj) {
    return false;
  }

  @Override
  public Amis find(String uuid) {
    return null;
  }

  //find all friends of a user
  public LinkedList<Amis> findAllAmis(String uuid_user) {
    LinkedList<Amis> amis = new LinkedList<Amis>();
    PreparedStatement statement = null;
    ResultSet result = null;
    try {
      String query = "SELECT * FROM amis WHERE uuid_user = ?";
      statement = this.connect.prepareStatement(query);
      statement.setString(1, uuid_user);
      result = statement.executeQuery();
      while (result.next()) {
        Amis ami = new Amis();
        ami.setUuid_user(result.getString("uuid_user"));
        ami.setUuid_second_user(result.getString("uuid_second_user"));
        amis.add(ami);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (result != null) {
          result.close();
        }
        if (statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return amis;
  }
}

import java.io.Serializable;
import java.sql.Date;

public class Messageroom implements  Serializable{

  private int message_id;
  private String uuid_room;
  private String uuid_user;
  private String message;
  private Date date;
  private boolean isDeleted;

  public Messageroom(){
    this.message_id = 0;
    this.uuid_room = "";
    this.uuid_user = "";
    this.message = "";
    this.date = new Date(0);
    this.isDeleted = false;
  }

  public Messageroom(int message_id, String uuid_room, String uuid_user, String message, Date date, boolean isDeleted){
    this.message_id = message_id;
    this.uuid_room = uuid_room;
    this.uuid_user = uuid_user;
    this.message = message;
    this.date = date;
    this.isDeleted = isDeleted;
  }

  public int getMessage_id(){
    return this.message_id;
  }

  public String getUuid_room(){
    return this.uuid_room;
  }

  public String getUuid_user(){
    return this.uuid_user;
  }

  public String getMessage(){
    return this.message;
  }

  public Date getDate(){
    return this.date;
  }

  public boolean getIsDeleted(){
    return this.isDeleted;
  }

  public void setMessage_id(int message_id){
    this.message_id = message_id;
  }

  public void setUuid_room(String uuid_room){
    this.uuid_room = uuid_room;
  }

  public void setUuid_user(String uuid_user){
    this.uuid_user = uuid_user;
  }

  public void setMessage(String message){
    this.message = message;
  }
  
public void setDate(Date date){
    this.date = date;
  }

  public void setIsDeleted(boolean isDeleted){
    this.isDeleted = isDeleted;
  }





}

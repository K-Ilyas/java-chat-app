import java.io.Serializable;


public class Connected implements  Serializable {

  private String uuid_user;
  private String uuid_room;

  public Connected(){
    this.uuid_user = "";
    this.uuid_room = "";
  }

  public Connected(String uuid_user, String uuid_room){
    this.uuid_user = uuid_user;
    this.uuid_room = uuid_room;
  }

  public String getUuid_user(){
    return this.uuid_user;
  }

  public String getUuid_room(){
    return this.uuid_room;
  }

  public void setUuid_user(String uuid_user){
    this.uuid_user = uuid_user;
  }

  public void setUuid_room(String uuid_room){
    this.uuid_room = uuid_room;
  }

  
}

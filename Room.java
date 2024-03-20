import java.io.Serializable;


public class Room implements  Serializable {

  private String uuid_room;
  private String roomname;
  private String image;

  public Room(){
    this.uuid_room = "";
    this.roomname = "";
    this.image = "";
  }

  public Room(String uuid_room, String roomname, String image){
    this.uuid_room = uuid_room;
    this.roomname = roomname;
    this.image = image;
  }

  public String getUuid_room(){
    return this.uuid_room;
  }

  public String getRoomname(){
    return this.roomname;
  }

  public String getImage(){
    return this.image;
  }

  public void setUuid_room(String uuid_room){
    this.uuid_room = uuid_room;
  }

  public void setRoomname(String roomname){
    this.roomname = roomname;
  }

  public void setImage(String image){
    this.image = image;
  }
  

  
}

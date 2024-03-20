import java.io.Serializable;

public class Amis implements  Serializable{

  private String uuid_user;
  private String uuid_second_user;

  public Amis(){
    this.uuid_user = "";
    this.uuid_second_user = "";
  }

  public Amis(String uuid_user, String uuid_second_user){
    this.uuid_user = uuid_user;
    this.uuid_second_user = uuid_second_user;
  }

  public String getUuid_user(){
    return this.uuid_user;
  }

  public String getUuid_second_user(){
    return this.uuid_second_user;
  }

  public void setUuid_user(String uuid_user){
    this.uuid_user = uuid_user;
  }

  public void setUuid_second_user(String uuid_second_user){
    this.uuid_second_user = uuid_second_user;
  }

  

}

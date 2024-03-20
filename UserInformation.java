import java.io.Serializable;
import java.lang.Comparable;

public class UserInformation implements Comparable<UserInformation>, Serializable {

    private String uuid = "";
    private String pseudo = "";
    private String password = "";
    private String email = "";
    private String image = "";
    private Boolean isadmin = false;

    public UserInformation() {
        this.uuid = "";
        this.pseudo = "";
        this.password = "";
        this.email = "";
        this.image = "";
        this.isadmin = false;
    }

    public UserInformation(String pseudo, String password) {

        this.pseudo = pseudo;
        this.password = password;

    }

    public UserInformation(String uuid, String pseudo, String password, String email, String image, boolean isadmin) {
        this.uuid = uuid;
        this.pseudo = pseudo;
        this.password = password;
        this.email = email;
        this.image = image;
        this.isadmin = isadmin;
    }

    public int compareTo(UserInformation o) {
        if (o.getClass().equals(UserInformation.class)) {
            UserInformation cd = (UserInformation) o;
            return this.uuid.compareTo(cd.getUuid());
        }
        return -1;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public Boolean getIsadmin() {
        return isadmin;
    }

    public String getUuid() {
        return uuid;
    }

    public void setPesudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

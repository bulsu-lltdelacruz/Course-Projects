package admin;

public class Admin {
    private String AdminID;
    private String username;
    private String password;


    public Admin(String adminID, String username, String password) {
        this.AdminID = adminID;
        this.username = username;
        this.password = password;
    }


    public String getAdminID() {
        return AdminID;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return password;
    }

    public void setAdminID(String adminID) {
        this.AdminID = adminID;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPass(String password) {
        this.password = password;
    }
}

package model;

/**
 *
 * @author Hat262006
 */
public class TaiKhoan {

    private String user;
    private String pass;
    private String hoTen;
    private String email;
    private String pass2;

//Contructor
    public TaiKhoan(String user, String pass, String hoTen, String email, String pass2) {
        this.user = user;
        this.pass = pass;
        this.hoTen = hoTen;
        this.email = email;
        this.pass2 = pass2;
    }

//    Set+Get
    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getEmail() {
        return email;
    }

    public String getPass2() {
        return pass2;
    }

}

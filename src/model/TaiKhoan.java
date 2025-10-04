package model;
/**
 * @author Hat262006
 */
public class TaiKhoan {
    private String user;
    private String pass;
    private String hoTen;
    private String email;
    //Contructor
    public TaiKhoan(String user, String pass, String hoTen, String email) {
        this.user = user;
        this.pass = pass;
        this.hoTen = hoTen;
        this.email = email;
        
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

}

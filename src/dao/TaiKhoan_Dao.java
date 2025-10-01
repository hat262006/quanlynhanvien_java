package dao;

import dao.DBConnection;
import form.Login;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.TaiKhoan;


/**
 *
 * @author Hat262006
 */
public class TaiKhoan_Dao {

    // kiểm tra + đăng nhập
    public boolean checkLogin(String user, String pass) {
        String sql = "SELECT * FROM taikhoan WHERE username=? AND password=?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true; // Nếu đăng nhập thành công
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // đăng nhập thất bại
    }

    // đăng ký
    public boolean dangKy(TaiKhoan tk) {
        String checkUser = "SELECT * FROM taikhoan WHERE username = ? OR email = ?";
        String insert = "INSERT INTO taikhoan(username,password,hoten,email) VALUES(?,?,?,?)";
        try (Connection conn = DBConnection.getConnection()) {

            // Kiểm tra user/email đã tồn tại
            try (PreparedStatement ps = conn.prepareStatement(checkUser)) {
                ps.setString(1, tk.getUser());
                ps.setString(2, tk.getEmail());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return false; // user hoặc email đã tồn tại
                }
            }

            // Nếu chưa tồn tại thì thêm mới
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, tk.getUser());
                ps.setString(2, tk.getPass());
                ps.setString(3, tk.getHoTen());
                ps.setString(4, tk.getEmail());
                return ps.executeUpdate() > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //==============Thông báo==============
    public static boolean thongBao(JFrame parent, String message, String title) {
        String[] luaChon = {"Có", "Không"};
        int thongBao = JOptionPane.showOptionDialog(
                parent,
                message,
                title,
                JOptionPane.YES_NO_OPTION ,
                JOptionPane.QUESTION_MESSAGE ,
                null,
                luaChon,
                luaChon[0]);
        return thongBao == JOptionPane.YES_OPTION;
    }

    //===========Đăng xuất ==============
    public static void dangXuat(JFrame dongForm) {
        boolean thongBao = thongBao(dongForm ,
                "Bạn có chắc chắn muốn đăng xuất không?",
                "Xác nhận đăng xuất");
        if (thongBao) {
            Login Login = new Login();
            Login.setVisible(true);
            if (dongForm != null) {
                dongForm.dispose(); //Tắt form 
            }
        }

    }

}

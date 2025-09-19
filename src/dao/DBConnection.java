package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
//    Kết nối database
    private static final String URL = "jdbc:mysql://localhost:3306/db_qlnv";
    private static final String USER = "root";
    private static final String PASS = "Tuanlay123@";    

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Ket Noi Thanh Cong");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Ket Noi That Bai: " + e.getMessage());
        }
        return conn;
    }
}

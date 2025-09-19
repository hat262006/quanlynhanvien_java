package dao;

import dao.DBConnection;
import model.nhan_vien;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import service.ChamCong;

/**
 *
 * @author Hat262006
 */
public class TinhLuong_DAO {

    //Hàm lấy dữ liệu chấm công từ mã nv 
    public List<ChamCong> getChamCongByNV(String maNV) throws SQLException {
        List<ChamCong> dsChamCong = new ArrayList<>();
        String sql = "SELECT c.id,c.ma_nhan_vien, n.ho_ten, n.chuc_vu, \n"
                + " c.gio_lam, c.gio_tang_ca, c.hs_tang_ca, c.ngay, c.trang_thai\n"
                + "FROM cham_cong c \n"
                + "JOIN nhan_su n ON c.ma_nhan_vien = n.ma_nhan_vien\n"
                + "WHERE c.ma_nhan_vien = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChamCong cc = new ChamCong(
                            rs.getString("id"),
                            rs.getString("ma_nhan_vien"),
                            rs.getString("ho_ten"),
                            rs.getString("chuc_vu"),
                            rs.getFloat("gio_lam"),
                            rs.getFloat("gio_tang_ca"),
                            rs.getFloat("hs_tang_ca"),
                            rs.getDate("ngay"),
                            rs.getString("trang_thai")
                    );
                    dsChamCong.add(cc);
                }
            }
        }
        return dsChamCong;
    }

    //============== Tính Lương==============
    public double tinhLuongTheoChamCong(nhan_vien nv, List<ChamCong> dsChamCong) {
        double tongLuong = 0;
        for (ChamCong cc : dsChamCong) {
            tongLuong += nv.tinhLuong(cc);  // dùng đa hình
        }
        return tongLuong;
    }

    //==============Hàm thêm ==============
    public boolean luuBangLuong(String maNV, String tenNV, String chucVu,
            float tongGioLam, float tongTangCa,
            float hsTangCa, double tongLuong) {
        String sql = "INSERT INTO bangluong(ma_nhan_vien, ten_nv, chuc_vu, gio_Lam, tang_ca, hs_tang_ca, tong_luong) "
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ps.setString(2, tenNV);
            ps.setString(3, chucVu);
            ps.setFloat(4, tongGioLam);
            ps.setFloat(5, tongTangCa);
            ps.setFloat(6, hsTangCa);
            ps.setDouble(7, tongLuong);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //Ham kiem trung
    public String kiemTraTrung(String maNV) {
        String sql = "SELECT 1 FROM bangluong WHERE ma_nhan_vien = ? ";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Nhân viên này đã được chấm lương";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi kiểm tra trùng dữ liệu!";
        }

        return ""; // 
    }

    //    Hàm lấy nhân viên ra bảng và sắp xếp 
    public List<Object[]> getAll(String keyWord, String sapXepTheo, String thuTu, String chucVu) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT * FROM bangluong";
        // Lọc theo chức vụ
        if (!"Tất cả".equals(chucVu)) {
            sql += " WHERE chuc_vu = '" + chucVu + "'";
        }
        // nếu có keyword
        if (keyWord != null && !keyWord.trim().isEmpty()) {
            if (sql.contains("WHERE")) {
                sql += " AND ";
            } else {
                sql += " WHERE ";
            }
            sql += "(ma_nhan_vien LIKE '%" + keyWord.trim() + "%' "
                    + "OR ten_nv LIKE '%" + keyWord.trim() + "%')";
        }

        if (sapXepTheo != null && !sapXepTheo.isEmpty()) {
            sql += " ORDER BY ";
            switch (sapXepTheo) {
                case "Mã nhân viên":
                    sql += "ma_nhan_vien ";
                    break;
                case "Tên nhân viên":
                    sql += "ten_nv ";
                    break;
                case "Chức vụ":
                    sql += "chuc_vu ";
                    break;
                case "Số giờ tăng ca":
                    sql += "tang_ca ";
                    break;
                case "Giờ làm":
                    sql += "gio_Lam ";
                    break;
                case "Hệ số tăng ca":
                    sql += "hs_tang_ca ";
                    break;
                case "Tổng lương":
                    sql += "tong_luong ";
                    break;
                default:
                    sql += "ma_nhan_vien ";
            }
            sql += "Tăng dần".equals(thuTu) ? "ASC" : "DESC";
        }
       try (Connection conn = DBConnection.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    Object[] row = new Object[]{
                        rs.getString("ma_nhan_vien"),
                        rs.getString("ten_nv"),
                        rs.getString("chuc_vu"),
                        rs.getFloat("gio_Lam"),
                        rs.getFloat("tang_ca"),
                        rs.getFloat("hs_tang_ca"),
                        rs.getDouble("tong_luong")
                    };

                    list.add(row);
                }
            }
   
    catch (Exception e) {
            e.printStackTrace();
    }


return list;
        }

    }

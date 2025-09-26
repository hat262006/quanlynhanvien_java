package dao;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import service.ChamCong;

/**
 *
 * @author Hat262006
 */
public class ChamCong_DAO {

    //==============Hàm thêm ==============
    public boolean themChamCong(ChamCong cc) {
        String sql = "INSERT INTO cham_cong(ma_nhan_vien, ngay, gio_lam, gio_tang_ca, hs_tang_ca, trang_thai) "
                + "VALUES ( ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cc.getMaNV());
            ps.setDate(2, cc.getNgay());
            ps.setFloat(3, cc.getGioLam());
            ps.setFloat(4, cc.getGioTangCa());
            ps.setFloat(5, cc.getHsTangCa());
            ps.setString(6, cc.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    //    Hàm lấy nhân viên ra bảng và sắp xếp 
    public List<ChamCong> getAll(String keyWord, String sapXepTheo, String thuTu) {
        List<ChamCong> list = new ArrayList<>();
        String sql = "SELECT c.id, c.ma_nhan_vien, n.ho_ten, n.chuc_vu, "
                + "c.ngay, c.gio_lam, c.gio_tang_ca, c.hs_tang_ca, c.trang_thai "
                + "FROM cham_cong c "
                + "JOIN nhan_su n ON c.ma_nhan_vien = n.ma_nhan_vien";
        // join lấy từ nhân sự để chính xác
        // Nếu có keyword thêm WHERE
        if (keyWord != null && !keyWord.trim().isEmpty()) {
            sql += " WHERE c.ma_nhan_vien LIKE ? OR n.ho_ten LIKE ?";
        }
        // Thêm ORDER BY nếu có yêu cầu sắp xếp
        if (sapXepTheo != null && !sapXepTheo.isEmpty()) {
            sql += " ORDER BY ";
            switch (sapXepTheo) {
                case "Mã nhân viên":
                    sql += "ma_nhan_vien ";
                    break;
                case "Tên nhân viên":
                    sql += "ho_ten ";
                    break;
                case "Chức vụ":
                    sql += "chuc_vu ";
                    break;
                case "Số giờ tăng ca":
                    sql += "gio_tang_ca ";
                    break;
                case "Ngày làm":
                    sql += "ngay ";
                    break;
                case "Giờ làm":
                    sql += "gio_lam ";
                    break;
                case "Hệ số tăng ca":
                    sql += "hs_tang_ca ";
                    break;
                case "Trạng thái":
                    sql += "trang_thai ";
                    break;
                default:
                    sql += "ma_nhan_vien ";
            }

            // Thứ tự sắp xếp
            if ("Tăng dần".equals(thuTu)) {
                sql += "ASC";
            } else {
                sql += "DESC";
            }
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // Nếu có keyword
            if (keyWord != null && !keyWord.trim().isEmpty()) {
                ps.setString(1, "%" + keyWord.trim() + "%");
                ps.setString(2, "%" + keyWord.trim() + "%");
            }
             ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String id = rs.getString("id");
                String maNV = rs.getString("ma_nhan_vien");
                String tenNV = rs.getString("ho_ten");   // lấy từ nhan_su
                String chucVu = rs.getString("chuc_vu"); // lấy từ nhan_su
                // ngày
                java.sql.Date ngay = rs.getDate("ngay");
                float gioLam = rs.getFloat("gio_lam");
                float gioTangCa = rs.getFloat("gio_tang_ca");
                float hsTangCa = rs.getFloat("hs_tang_ca");
                String trangThai = rs.getString("trang_thai");

                // Tạo đối tượng ChamCong gioLam,
                ChamCong cc = new ChamCong(id, maNV, tenNV, chucVu,
                        gioLam, gioTangCa, hsTangCa,
                        ngay, trangThai);
                list.add(cc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    //Ham kiem trung
    public String kiemTraTrung(String maNV, java.sql.Date ngay) {
        String sql = "SELECT 1 FROM cham_cong WHERE ma_nhan_vien = ? AND ngay = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            ps.setDate(2, ngay);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return "Ngày " + ngay + " nhân viên này đã được chấm công";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi kiểm tra trùng dữ liệu!";
        }

        return ""; // rỗng = không trùng
    }

    // Hàm luu nhân viên 
    public boolean capNhatNhanVien(ChamCong cc) {
        // Câu lệnh SQL với 'ma_nhan_vien' là điều kiện WHERE
        String sql = "UPDATE cham_cong SET ngay = ?, gio_lam = ?, gio_tang_ca = ?, "
                + "hs_tang_ca = ?, trang_thai = ? "
                + "WHERE ma_nhan_vien = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Chuyển đổi ngày lam thành java.sql.Date
            // Gán giá trị vào PreparedStatement theo đúng thứ tự
            ps.setDate(1, cc.getNgay());
            ps.setFloat(2, cc.getGioLam());
            ps.setFloat(3, cc.getGioTangCa());
            ps.setFloat(4, cc.getHsTangCa());
            ps.setString(5, cc.getTrangThai());
            ps.setString(6, cc.getMaNV()); // Mã nhân viên (sử dụng để tìm bản ghi)

            // Thực hiện câu lệnh UPDATE
            int rowsAffected = ps.executeUpdate(); // Trả về số dòng bị ảnh hưởng
            if (rowsAffected > 0) {
                return true;  // Nếu có dòng bị thay đổi thì trả về true
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Nếu xảy ra lỗi thì trả về false
        }
    }

}

package dao;

import dao.DBConnection;
import model.nv_VanPhong;
import java.io.FileOutputStream;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.nhan_vien;
import model.nv_May;
import model.nv_ThoiVu;


public class QLNV_DAO {

    // Ham Thêm
    public boolean them(nhan_vien nv) {
        String sql = "INSERT INTO nhan_su(ma_nhan_vien, ho_ten, gioi_tinh, "
                + "chuc_vu, ngay_sinh, cccd, ngay_vao_lam, trinh_do, so_dien_thoai, trang_thai,anh) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // Xử lý ngày sinh
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDateSinh = LocalDate.parse(nv.getNgaySinh(), formatter);
            java.sql.Date sqlDateNs = java.sql.Date.valueOf(localDateSinh);

            // Xử lý ngày vào làm
            LocalDate localDateVaoLam = LocalDate.parse(nv.getNgayVaoLam(), formatter);
            java.sql.Date sqlDateVaoLam = java.sql.Date.valueOf(localDateVaoLam);
            ps.setString(1, nv.getMaNV());
            ps.setString(2, nv.getTenNV());
            ps.setString(3, nv.getGioiTinh());
            ps.setString(4, nv.getChucVu());
            ps.setDate(5, sqlDateNs);
            ps.setString(6, nv.getCccd());
            ps.setDate(7, sqlDateVaoLam);
            ps.setString(8, nv.getTrinhDo());
            ps.setString(9, nv.getSoDienThoai());
            ps.setString(10, nv.getTrangThai());
            ps.setString(11, nv.getAnh());

            return ps.executeUpdate() > 0; // trả về true nếu thêm thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Hàm lấy nhân viên và sắp xếp theo tiêu chí
    public List<nhan_vien> getAll(String keyword, String sapXepTheo, String thuTu) {
        List<nhan_vien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhan_su";
        // Nếu có keyword thêm WHERE
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql += " WHERE ma_nhan_vien LIKE ? OR ho_ten LIKE ?";
        }
        // Thêm ORDER BY nếu có yêu cầu sắp xếp
        if (sapXepTheo != null && !sapXepTheo.isEmpty()) {
            sql += " ORDER BY ";
            switch (sapXepTheo) {
                case "Mã Nhân Viên":
                    sql += "ma_nhan_vien ";
                    break;
                case "Tên":
                    sql += "ho_ten ";
                    break;
                case "Ngày vào làm":
                    sql += "ngay_vao_lam ";
                    break;
                case "Trạng thái":
                    sql += "trang_thai ";
                    break;
                default:
                    sql += "ma_nhan_vien ";
            }

            // Thứ tự sắp xếp
            if ("Tăng dần".equals(thuTu)) {
                sql += "ASC ";
            } else {
                sql += "DESC ";
            }
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            // Nếu có keyword
            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(1, "%" + keyword.trim() + "%");
                ps.setString(2, "%" + keyword.trim() + "%");
            }
            ResultSet rs = ps.executeQuery();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while (rs.next()) {
                String maNV = rs.getString("ma_nhan_vien");
                String tenNV = rs.getString("ho_ten");
                String gioiTinh = rs.getString("gioi_tinh");
                String chucVu = rs.getString("chuc_vu");
                String ngaySinh = rs.getDate("ngay_sinh").toLocalDate().format(formatter);
                String cccd = rs.getString("cccd");
                String ngayVaoLam = rs.getDate("ngay_vao_lam").toLocalDate().format(formatter);
                String trinhDo = rs.getString("trinh_do");
                String soDienThoai = rs.getString("so_dien_thoai");
                String trangThai = rs.getString("trang_thai");
                String anh = rs.getString("anh");
                nhan_vien nv = null;

                // Phân loại nhân viên theo chuc_vu
                switch (chucVu) {
                    case "Công nhân may":
                        nv = new nv_May(
                                0, 0, 0, 0, maNV, tenNV, gioiTinh, chucVu,
                                ngaySinh, cccd, ngayVaoLam,
                                trinhDo, soDienThoai, trangThai, anh
                        );
                        break;

                    case "Nhân viên thời vụ":
                        nv = new nv_ThoiVu(
                                0, maNV, tenNV, gioiTinh, chucVu,
                                ngaySinh, cccd, ngayVaoLam,
                                trinhDo, soDienThoai, trangThai, anh);
                        break;

                    case "Nhân viên văn phòng":
                        nv = new nv_VanPhong(
                                0, 0, 0, maNV, tenNV, gioiTinh, chucVu,
                                ngaySinh, cccd, ngayVaoLam,
                                trinhDo, soDienThoai, trangThai, anh
                        );
                        break;

                    default:
                        nv = new nv_May(
                                0, 0, 0, 0, maNV, tenNV, gioiTinh, chucVu,
                                ngaySinh, cccd, ngayVaoLam,
                                trinhDo, soDienThoai, trangThai, anh
                        );
                }

                list.add(nv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Ham kiem trung 
    public String kiemTraTrung(String maNV, String sdt, String cccd, String anh) {
        StringBuilder sb = new StringBuilder();

        String sql = "SELECT ma_nhan_vien, so_dien_thoai, cccd, anh "
                + "FROM nhan_su "
                + "WHERE ma_nhan_vien = ? OR so_dien_thoai = ? OR cccd = ? OR anh = ? ";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maNV);
            ps.setString(2, sdt);
            ps.setString(3, cccd);
            ps.setString(4, anh);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if (maNV.equals(rs.getString("ma_nhan_vien"))) {
                        if (!sb.toString().contains("Mã nhân viên đã tồn tại!")) {
                            sb.append("Mã nhân viên đã tồn tại!\n");
                        }
                    }
                    if (sdt.equals(rs.getString("so_dien_thoai"))) {
                        if (!sb.toString().contains("Số điện thoại đã tồn tại!")) {
                            sb.append("Số điện thoại đã tồn tại!\n");
                        }
                    }
                    if (cccd.equals(rs.getString("cccd"))) {
                        if (!sb.toString().contains("CCCD đã tồn tại!")) {
                            sb.append("CCCD đã tồn tại!\n");
                        }
                    }
                    if (anh.equals(rs.getString("anh"))) {
                        if (!sb.toString().contains("Ảnh này đã tồn tại!")) {
                            sb.append("Ảnh này đã tồn tại!\n");
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi khi kiểm tra trùng dữ liệu!";
        }

        return sb.toString(); // rỗng = không trùng
    }

// ============== Hàm xóa nhân viên ==============
    public boolean xoaNhanVien(String maNV, String form) throws SQLException {
        boolean kq = TaiKhoan_Dao.thongBao(null, "Bạn chắc chắn muốn xóa thông tin này?", "Xóa thông tin");
        if (!kq) {
            return false; // Người dùng chọn "Không"
        }

        String sql = null;
        switch (form) {
            case "QL_NS":
                sql = "DELETE FROM nhan_su WHERE ma_nhan_vien = ?";
                break;
            case "Cham_Cong":
                sql = "DELETE FROM cham_cong WHERE id = ?";
                break;
            case "Tinh_Luong":
                sql = "DELETE FROM bangluong WHERE ma_nhan_vien = ?";
                break;
            default:
                throw new SQLException("Form không hợp lệ: " + form);
        }

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNV);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    //  Hàm luu nhân viên 
    public boolean capNhatNhanVien(nhan_vien nv) {
        // Câu lệnh SQL với 'ma_nhan_vien' là điều kiện WHERE, và tham số sẽ được điền theo thứ tự đúng
        String sql = "UPDATE nhan_su SET ho_ten = ?, gioi_tinh = ?, chuc_vu = ?, "
                + "ngay_sinh = ?, cccd = ?, ngay_vao_lam = ?, trinh_do = ?, "
                + "so_dien_thoai = ?, trang_thai = ?, anh = ? WHERE ma_nhan_vien = ?";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Chuyển đổi ngày sinh và ngày vào làm thành java.sql.Date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDateSinh = LocalDate.parse(nv.getNgaySinh(), formatter);
            java.sql.Date sqlDateSinh = java.sql.Date.valueOf(localDateSinh);

            LocalDate localDateVaoLam = LocalDate.parse(nv.getNgayVaoLam(), formatter);
            java.sql.Date sqlDateVaoLam = java.sql.Date.valueOf(localDateVaoLam);

            // Gán giá trị vào PreparedStatement theo đúng thứ tự
            ps.setString(1, nv.getTenNV());        // Họ tên
            ps.setString(2, nv.getGioiTinh());     // Giới tính
            ps.setString(3, nv.getChucVu());       // Chức vụ
            ps.setDate(4, sqlDateSinh);            // Ngày sinh
            ps.setString(5, nv.getCccd());         // CCCD
            ps.setDate(6, sqlDateVaoLam);          // Ngày vào làm
            ps.setString(7, nv.getTrinhDo());     // Trình độ
            ps.setString(8, nv.getSoDienThoai()); // Số điện thoại
            ps.setString(9, nv.getTrangThai());    // Trạng thái
            ps.setString(10, nv.getAnh());           //Anh
            ps.setString(11, nv.getMaNV());        // Mã nhân viên (sử dụng để tìm bản ghi)

            // Thực hiện câu lệnh UPDATE
            int rowsAffected = ps.executeUpdate(); // Trả về số dòng bị ảnh hưởng
            if (rowsAffected > 0) {
                return true;  // Nếu có dòng bị thay đổi thì trả về true
            } else {
                // Nếu không có dòng bị ảnh hưởng 
                JOptionPane.showMessageDialog(null, "Mã nhân viên không thể chỉnh sửa, vui lòng kiểm tra lại!");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}

package model;

import java.sql.Date;
import model.nhan_vien;
import service.ChamCong;

/**
 * @author hat262006
 */
public class nv_VanPhong extends nhan_vien {

    private double luongCoBan;
    private double thuong;
    private double phat;

    //contructor
    public nv_VanPhong(double luongCoBan, double thuong, double phat,
            String maNV, String tenNV, String gioiTinh, String chucVu,
            Date ngaySinh, String cccd, Date ngayVaoLam, String trinhDo,
            String soDienThoai, String trangThai, String anh) {
        super(maNV, tenNV, gioiTinh, chucVu, ngaySinh, cccd, ngayVaoLam,
                trinhDo, soDienThoai, trangThai, anh);
        this.luongCoBan = luongCoBan;
        this.thuong = thuong;
        this.phat = phat;
    }
    //Get & set
    public double getLuongCoBan() {
        return luongCoBan;
    }
    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }
    public double getThuong() {
        return thuong;
    }
    public void setThuong(double thuong) {
        this.thuong = thuong;
    }
    public double getPhat() {
        return phat;
    }
    public void setPhat(double phat) {
        this.phat = phat;
    }
    // Tinh luong
    @Override
    public double tinhLuong(ChamCong cc) {
        int ngayDilam = 25; // Nhân viên Văn Phòng đi làm số ngày /1 tháng
        int gioLam = 8; // Một ngày công nhân may làm 8 
        int soGioLam = ngayDilam * gioLam; // Nhân viên đi làm số giờ /1 tháng
        double luongChinhThuc = luongCoBan / soGioLam * cc.getGioLam();
        double luongTangCa = luongCoBan / soGioLam * cc.getGioTangCa() * cc.getHsTangCa();
        return luongChinhThuc + luongTangCa + thuong - phat;
    }
}

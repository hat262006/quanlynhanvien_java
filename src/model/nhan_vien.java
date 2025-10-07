package model;

import java.time.LocalDate;
import java.sql.Date;
import service.ChamCong;

/*
 * @author Hat262006
 */
public abstract class nhan_vien {

    private String maNV;
    private String tenNV;
    private String gioiTinh;
    private String chucVu;
    private java.sql.Date ngaySinh;
    private String cccd;
    private java.sql.Date ngayVaoLam;
    private String trinhDo;
    private String soDienThoai;
    private String trangThai;
    private String anh;

    //Contructor
    public nhan_vien(String maNV, String tenNV, String gioiTinh, String chucVu,
            Date ngaySinh, String cccd, Date ngayVaoLam, String trinhDo,
            String soDienThoai, String trangThai, String anh) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.gioiTinh = gioiTinh;
        this.chucVu = chucVu;
        this.ngaySinh = ngaySinh;
        this.cccd = cccd;
        this.ngayVaoLam = ngayVaoLam;
        this.trinhDo = trinhDo;
        this.soDienThoai = soDienThoai;
        this.trangThai = trangThai;
        this.anh = anh;
    }
//    Set 

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setNgayVaoLam(Date ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public void setTrinhDo(String trinhDo) {
        this.trinhDo = trinhDo;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }
//Get

    public String getAnh() {
        return anh;
    }

    public String getMaNV() {
        return maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public String getChucVu() {
        return chucVu;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public Date getNgayVaoLam() {
        return ngayVaoLam;
    }

    public String getCccd() {
        return cccd;
    }

    public String getTrinhDo() {
        return trinhDo;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    //Tinh luong
    public abstract double tinhLuong(ChamCong cc);
}

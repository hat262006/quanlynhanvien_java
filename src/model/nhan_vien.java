package model;

import java.time.LocalDate;
import service.ChamCong;

/**
 *
 * @author Hat262006
 */
public abstract class nhan_vien {

    private String maNV;
    private String tenNV;
    private String gioiTinh;
    private String chucVu;
    private String ngaySinh;
    private String cccd;
    private String ngayVaoLam;
    private String trinhDo;
    private String soDienThoai;
    private String trangThai;
    private String anh;

    //Contructor

    public nhan_vien(String maNV, String tenNV, String gioiTinh, String chucVu, String ngaySinh, String cccd, String ngayVaoLam, String trinhDo, String soDienThoai, String trangThai, String anh) {
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

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public void setNgayVaoLam(String ngayVaoLam) {
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

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
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

    public String getNgaySinh() {
        return ngaySinh;
    }

    public String getCccd() {
        return cccd;
    }

    public String getNgayVaoLam() {
        return ngayVaoLam;
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



    //Tinh luong abstract
    public abstract double tinhLuong(ChamCong cc);
}

package model;

import java.sql.Date;
import service.ChamCong;
import model.nhan_vien;
/**
 *
 * @author Hat262006
 */ 
public class nv_May extends nhan_vien {
    private int sanLuong;
    private float donGia;
    private double luongCoDinh;
    private double phuCap;
    
    //Contructor 
    public nv_May(int sanLuong, float donGia, double luongCoDinh,
        double phuCap, String maNV, String tenNV, String gioiTinh,
        String chucVu, Date ngaySinh, String cccd, Date ngayVaoLam,
        String trinhDo, String soDienThoai, String trangThai, String anh) {
        super(maNV, tenNV, gioiTinh, chucVu, ngaySinh, cccd, ngayVaoLam,
              trinhDo, soDienThoai, trangThai, anh);
        this.sanLuong = sanLuong;
        this.donGia = donGia;
        this.luongCoDinh = luongCoDinh;
        this.phuCap = phuCap;
    }
 //Set & Get 
public int getSanLuong() {return sanLuong;}
public void setSanLuong(int sanLuong) {this.sanLuong = sanLuong;}
public float getDonGia() {return donGia;}
public void setDonGia(float donGia) {this.donGia = donGia;}
public double getLuongCoDinh() {return luongCoDinh;}
public void setLuongCoDinh(double luongCoDinh) {this.luongCoDinh = luongCoDinh;}
public double getPhuCap() {return phuCap;}
public void setPhuCap(double phuCap) {this.phuCap = phuCap;}
    @Override
    public double tinhLuong(ChamCong cc){
    int ngayDilam = 27; // Một công nhân may đi làm số ngày /1 tháng
    int soGioLam = ngayDilam * 8; // Công nhân đi làm số giờ /1 tháng
    double luongSanLuong = donGia * sanLuong;
    double luongChinhThuc = luongCoDinh/soGioLam * cc.getGioLam();
    double luongTangCa = luongCoDinh / soGioLam * cc.getGioTangCa() * cc.getHsTangCa();
    return luongSanLuong + luongChinhThuc + luongTangCa + phuCap;
    }
}

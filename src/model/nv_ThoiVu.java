package model;

import java.sql.Date;
import service.ChamCong;
import model.nhan_vien;
/**
 * @author Hat262006
 */
public class nv_ThoiVu extends nhan_vien {
    private double luongMotGio;
    //Contructor 
    public nv_ThoiVu(double luongMotGio, String maNV, String tenNV,
            String gioiTinh, String chucVu, Date ngaySinh, String cccd,
            Date ngayVaoLam, String trinhDo, String soDienThoai,
            String trangThai, String anh) {
        super(maNV, tenNV, gioiTinh, chucVu, ngaySinh, cccd, ngayVaoLam,
                trinhDo, soDienThoai, trangThai, anh);
        this.luongMotGio = luongMotGio;
    }
//Set & get 
    public double getLuongMotGio() {
        return luongMotGio;
    }
    public void setLuongMotGio(double luongMotGio) {
        this.luongMotGio = luongMotGio;
    }
    // Tinh luong
    @Override
    public double tinhLuong(ChamCong cc) {
        return (cc.getGioLam() * luongMotGio) + (cc.getGioTangCa()
                * cc.getHsTangCa() * luongMotGio);
    }
}

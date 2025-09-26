package service;

import java.sql.Date;
import java.time.LocalDate;

/**
 *
 * @author hat262006
 */
public class ChamCong {
    private String id;
    private String maNV;
    private String tenNV;
    private String chucVu;
    private float gioLam;
    private float gioTangCa;
    private float hsTangCa;
    private java.sql.Date ngay;
    private String trangThai;

    public ChamCong(String id, String maNV, String tenNV,
            String chucVu, float gioLam, float gioTangCa, float hsTangCa, Date ngay, String trangThai) {
        this.id = id;
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.chucVu = chucVu;
        this.gioLam = gioLam;
        this.gioTangCa = gioTangCa;
        this.hsTangCa = hsTangCa;
        this.ngay = ngay;
        this.trangThai = trangThai;
    }

    public String getId() {
        return id;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public float getGioLam() {
        return gioLam;
    }

    public void setGioLam(float gioLam) {
        this.gioLam = gioLam;
    }

    public float getGioTangCa() {
        return gioTangCa;
    }

    public void setGioTangCa(float gioTangCa) {
        this.gioTangCa = gioTangCa;
    }

    public float getHsTangCa() {
        return hsTangCa;
    }

    public void setHsTangCa(float hsTangCa) {
        this.hsTangCa = hsTangCa;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}

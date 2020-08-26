package com.example.hutechdrugapp.Model;

import java.io.Serializable;

public class Medicine implements Serializable {
 //   private String IdThuoc;
    private String ChiDinh;
    private String ChongChiDinh;
    private String HSD;
    private String HinhAnh;
    private String HoatChat;
    private String NongDo;
    private String PhanLoai;
    private String TacDung;
    private String TenThuoc;


    public Medicine() {

    }

    public Medicine(String chiDinh, String chongChiDinh, String HSD, String hinhAnh, String hoatChat, String nongDo, String phanLoai, String tacDung, String tenThuoc) {
        ChiDinh = chiDinh;
        ChongChiDinh = chongChiDinh;
        this.HSD = HSD;
        HinhAnh = hinhAnh;
        HoatChat = hoatChat;
        NongDo = nongDo;
        PhanLoai = phanLoai;
        TacDung = tacDung;
        TenThuoc = tenThuoc;
    }

    public String getChiDinh() {
        return ChiDinh;
    }

    public void setChiDinh(String chiDinh) {
        ChiDinh = chiDinh;
    }

    public String getChongChiDinh() {
        return ChongChiDinh;
    }

    public void setChongChiDinh(String chongChiDinh) {
        ChongChiDinh = chongChiDinh;
    }

    public String getHSD() {
        return HSD;
    }

    public void setHSD(String HSD) {
        this.HSD = HSD;
    }

    public String getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        HinhAnh = hinhAnh;
    }

    public String getHoatChat() {
        return HoatChat;
    }

    public void setHoatChat(String hoatChat) {
        HoatChat = hoatChat;
    }

    public String getNongDo() {
        return NongDo;
    }

    public void setNongDo(String nongDo) {
        NongDo = nongDo;
    }

    public String getPhanLoai() {
        return PhanLoai;
    }

    public void setPhanLoai(String phanLoai) {
        PhanLoai = phanLoai;
    }

    public String getTacDung() {
        return TacDung;
    }

    public void setTacDung(String tacDung) {
        TacDung = tacDung;
    }

    public String getTenThuoc() {
        return TenThuoc;
    }

    public void setTenThuoc(String tenThuoc) {
        TenThuoc = tenThuoc;
    }
}

package com.example.bin.sqlite.model;

public class NhanVien {

    public int id;
    public String ten;
    public String sdt;
    public byte[] hinh;

    public NhanVien() {
    }

    public NhanVien(int id, String ten, String sdt, byte[] hinh) {
        this.id = id;
        this.ten = ten;
        this.sdt = sdt;
        this.hinh = hinh;
    }
}

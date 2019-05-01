package com.example.bin.sqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

    public static String TB_NHANVIEN = "NhanVien";
    public static String TB_NHANVIEN_ID = "Id";
    public static String TB_NHANVIEN_TEN = "Ten";
    public static String TB_NHANVIEN_SDT = "SDT";
    public static String TB_NHANVIEN_HINH = "Hinh";

    public SQLite(Context context) {
        super(context, "sqlite.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String tbNhanVien = "CREATE TABLE "
                + TB_NHANVIEN + " ( "
                + TB_NHANVIEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + TB_NHANVIEN_TEN + " TEXT NOT NULL, "
                + TB_NHANVIEN_SDT + " TEXT NOT NULL, "
                + TB_NHANVIEN_HINH + " BLOB)";
        sqLiteDatabase.execSQL(tbNhanVien);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public SQLiteDatabase open() {
        return super.getWritableDatabase();
    }
}

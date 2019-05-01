package com.example.bin.sqlite.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bin.sqlite.R;
import com.example.bin.sqlite.database.Database;
import com.example.bin.sqlite.database.SQLite;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddActivity extends AppCompatActivity {
    @BindView(R.id.edtTen)
    EditText edtTen;
    @BindView(R.id.edtSdt)
    EditText edtSdt;
    @BindView(R.id.imageHinh)
    ImageView imgHinh;

    final int RESQUERT_CHOOSE_PHOTO = 123;
    final int RESQUERT_TAKE_PHOTO = 321;
    private String DATABASE_NAME = "sqlite.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btnLuu, R.id.btnHuy, R.id.btnCam, R.id.btnPic})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLuu:
                if (edtTen.getText().toString().equals("") || edtSdt.getText().toString().equals("") || imgHinh.getDrawable() == null){
                    Toast.makeText(this, "Hãy nhập liệu đi", Toast.LENGTH_SHORT).show();
                }else{
                    insertData();
                    Toast.makeText(this, "Đã thêm Nhân viên", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnHuy:
                finish();
                break;
            case R.id.btnCam:
                Intent intentcam = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentcam, RESQUERT_TAKE_PHOTO);
                break;
            case R.id.btnPic:
                Intent intentpic = new Intent(Intent.ACTION_PICK);
                intentpic.setType("image/*");
                startActivityForResult(intentpic, RESQUERT_CHOOSE_PHOTO);
                break;
            default:
                break;
        }
    }

    public void insertData(){
        String ten = edtTen.getText().toString();
        String sdt = edtSdt.getText().toString();
        byte[] hinh = getByteArrayFromImageView(imgHinh);

        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLite.TB_NHANVIEN_TEN, ten);
        contentValues.put(SQLite.TB_NHANVIEN_SDT, sdt);
        contentValues.put(SQLite.TB_NHANVIEN_HINH, hinh);

        SQLiteDatabase sqLiteDatabase = Database.initDatabase(this, DATABASE_NAME);
        sqLiteDatabase.insert("NhanVien", null, contentValues);

        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RESQUERT_CHOOSE_PHOTO){
                try {
                    Uri imgUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(imgUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgHinh.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if (requestCode == RESQUERT_TAKE_PHOTO){
                Bitmap bitmapcam = (Bitmap) data.getExtras().get("data");
                imgHinh.setImageBitmap(bitmapcam);
            }
        }
    }
    private byte[] getByteArrayFromImageView(ImageView imgv) {
        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}

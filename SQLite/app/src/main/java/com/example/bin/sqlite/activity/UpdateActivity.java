package com.example.bin.sqlite.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bin.sqlite.R;
import com.example.bin.sqlite.database.Database;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateActivity extends AppCompatActivity {

    @BindView(R.id.edtTen)
    EditText edtTenUP;
    @BindView(R.id.edtSdt)
    EditText edtSdtUP;
    @BindView(R.id.imageHinhUP)
    ImageView imgHinhUP;

    final int RESQUERT_CHOOSE_PHOTO = 123;
    final int RESQUERT_TAKE_PHOTO = 321;
    private String DATABASE_NAME = "sqlite.db";
    int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        init();
    }

    @OnClick({R.id.btnLuu, R.id.btnHuy, R.id.btnCam, R.id.btnPic})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnLuu:
                update();
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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

    public void update(){
        String ten = edtTenUP.getText().toString();
        String sdt = edtSdtUP.getText().toString();
        byte[] hinh = getByteArrayFromImageView(imgHinhUP);

        ContentValues contentValues = new ContentValues();
        contentValues.put("Ten", ten);
        contentValues.put("SDT", sdt);
        contentValues.put("Hinh", hinh);

        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        database.update("NhanVien", contentValues, "Id = ?", new String[]{id + ""});
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void init(){
        Intent intent = getIntent();
        id = intent.getIntExtra("ID", -1);
        SQLiteDatabase database = Database.initDatabase(this, DATABASE_NAME);
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien WHERE  Id = ?", new String[]{id + ""});
        cursor.moveToFirst();
        String ten = cursor.getString(1);
        String sdt = cursor.getString(2);
        byte[] hinh = cursor.getBlob(3);

        edtTenUP.setText(ten);
        edtSdtUP.setText(sdt);
        Bitmap bitmap = BitmapFactory.decodeByteArray(hinh, 0 , hinh.length);
        imgHinhUP.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == RESQUERT_CHOOSE_PHOTO){
                try{
                    Uri uri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imgHinhUP.setImageBitmap(bitmap);
                }catch (Exception e){
                    Log.d("choose photo", e + "");
                }
            }else if (requestCode == RESQUERT_TAKE_PHOTO){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgHinhUP.setImageBitmap(bitmap);
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

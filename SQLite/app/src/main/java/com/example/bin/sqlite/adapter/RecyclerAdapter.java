package com.example.bin.sqlite.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bin.sqlite.R;
import com.example.bin.sqlite.activity.UpdateActivity;
import com.example.bin.sqlite.database.Database;
import com.example.bin.sqlite.model.NhanVien;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<NhanVien> nhanVienList;
    private Activity context;
    private String DATABASE_NAME = "sqlite.db";

    public RecyclerAdapter(List<NhanVien> nhanVienList, Activity context) {
        this.nhanVienList = nhanVienList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_items, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        final NhanVien nv = nhanVienList.get(i);
        viewHolder.tvID.setText(String.valueOf(nv.id));
        viewHolder.tvTen.setText("Tên: " + nv.ten);
        viewHolder.tvSdt.setText("SĐT: " + nv.sdt);
        final Bitmap bitmapHinh = BitmapFactory.decodeByteArray(nhanVienList.get(i).hinh, 0, nhanVienList.get(i).hinh.length);
        viewHolder.imageView.setImageBitmap(bitmapHinh);
        viewHolder.btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(R.mipmap.ic_delete);
                builder.setTitle("Xóa");
                builder.setMessage("Bạn có muốn xóa?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        delete(nhanVienList.get(i).id);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        viewHolder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("ID", nhanVienList.get(i).id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nhanVienList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvID) TextView tvID;
        @BindView(R.id.tvTenDe) TextView tvTen;
        @BindView(R.id.tvSdt) TextView tvSdt;
        @BindView(R.id.btnXoa) ImageButton btnXoa;
        @BindView(R.id.btnSua) ImageButton btnSua;
        @BindView(R.id.imageView) ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void delete(int idNV){
        SQLiteDatabase database = Database.initDatabase(context, DATABASE_NAME);
        database.delete("NhanVien", "Id = ?", new String[]{idNV + ""});
        nhanVienList.clear();
        Cursor cursor = database.rawQuery("SELECT * FROM NhanVien", null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            String sdt = cursor.getString(2);
            byte[] hinh = cursor.getBlob(3);

            nhanVienList.add(new NhanVien(id, ten, sdt, hinh));
        }
        notifyDataSetChanged();
    }
}

package com.example.healthapp.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.DAO.ThoiTietDAO;
import com.example.healthapp.DAO.ViTriDAO;
import com.example.healthapp.Database.Connection;
import com.example.healthapp.Model.ThoiTiet;
import com.example.healthapp.R;
import com.example.healthapp.ThoiTietActivity;

import java.util.ArrayList;
import java.util.List;

public class TinhThanhAdapter extends RecyclerView.Adapter<TinhThanhAdapter.ViewHolder> {
    private List<String> danhSachTinhThanh;
    private List<String> danhSachTinhThanhFull;


    public TinhThanhAdapter(List<String> danhSachTinhThanh) {
        this.danhSachTinhThanh = danhSachTinhThanh;
        this.danhSachTinhThanhFull = new ArrayList<>(danhSachTinhThanh);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vitri, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String tenTinh = danhSachTinhThanh.get(position);
        holder.textViewTenTinh.setText(tenTinh);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Connection connection = Connection.getInstance(view.getContext());
                ThoiTietDAO db = connection.thoiTietDAO();

                ViTriDAO db2 = connection.viTriDAO();
                String viTri = db2.getViTri(tenTinh);

                ThoiTiet thoiTiet = db.getLastThoiTiet();
                if(thoiTiet!=null){
                    db.updateThoiTiet(viTri, thoiTiet.getId());
                }
                Intent intent = new Intent(view.getContext(), ThoiTietActivity.class);
                intent.putExtra("Check_OK", "OK");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(intent);
                ((Activity) view.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachTinhThanh.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewTenTinh;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTenTinh = itemView.findViewById(R.id.textViewTenTinh);
        }
    }
    public void filterList(ArrayList<String> filteredList) {
        danhSachTinhThanh = filteredList;
        notifyDataSetChanged();
    }
    public void resetList() {
        danhSachTinhThanh = new ArrayList<>(danhSachTinhThanhFull);
        notifyDataSetChanged();
    }
}


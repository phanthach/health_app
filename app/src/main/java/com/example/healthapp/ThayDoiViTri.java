package com.example.healthapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthapp.Adapter.TinhThanhAdapter;
import com.example.healthapp.DAO.ViTriDAO;
import com.example.healthapp.Database.Connection;

import java.util.ArrayList;
import java.util.List;

public class ThayDoiViTri extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TinhThanhAdapter adapter;
    private SearchView searchView;
    private ImageView image_back;
    private List<String> danhSachTinhThanh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thay_doi_vi_tri);

        recyclerView = findViewById(R.id.recyclerView);
        image_back = findViewById(R.id.image_back);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Connection connection = Connection.getInstance(getApplicationContext());
        ViTriDAO db = connection.viTriDAO();

        danhSachTinhThanh = db.getAllTenTinh();

        if(danhSachTinhThanh.size()==0){
            db.insetViTri();
            danhSachTinhThanh = db.getAllTenTinh();
        }
        adapter = new TinhThanhAdapter(danhSachTinhThanh);
        recyclerView.setAdapter(adapter);
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }


    private void filter(String text) {
        ArrayList<String> filteredList = new ArrayList<>();
        for (String item : danhSachTinhThanh) {
            if (item.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}
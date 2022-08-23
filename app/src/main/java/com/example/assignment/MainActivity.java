package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.assignment.Interface.JSONPlaceholder;
import com.example.assignment.adapter.RecyclerViewAdapter;
import com.example.assignment.model.PageInfo;
import com.example.assignment.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<UserInfo> userInfoArrayList;
    private ArrayAdapter<String> arrayAdapter;
    private SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Recycler view init
        recyclerView = findViewById(R.id.recycler_view);
        searchView = findViewById(R.id.search_box);
        searchView.clearFocus();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userInfoArrayList = new ArrayList<UserInfo>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://reqres.in/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JSONPlaceholder jsonPlaceholder = retrofit.create(JSONPlaceholder.class);
        Call<PageInfo> users = jsonPlaceholder.getUserInfo();
        getList(users);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filterList(s);
                return true;
            }
        });
    }

    private void filterList(String text) {
        List<UserInfo> filteredList = new ArrayList<UserInfo>();
        for (UserInfo userInfo : userInfoArrayList) {
            if (userInfo.getFirstName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(userInfo);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No User Found", Toast.LENGTH_LONG).show();
        }
        recyclerViewAdapter.setFilteredList(filteredList);
    }

    private void getList(Call<PageInfo> users) {
        users.enqueue(new Callback<PageInfo>() {
            @Override
            public void onResponse(Call<PageInfo> call, Response<PageInfo> response) {
                if (!response.isSuccessful())
                {
                    Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                for (UserInfo x : response.body().getData())
                {
                    userInfoArrayList.add(x);
                }
                recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this, userInfoArrayList);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onFailure(Call<PageInfo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "No Response", Toast.LENGTH_LONG).show();
            }
        });
    }
}
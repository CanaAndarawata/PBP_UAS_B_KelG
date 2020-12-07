package com.example.tubes_uas.UserCRUD;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.tubes_uas.Api.ApiClient;
import com.example.tubes_uas.Api.ApiInterface;
import com.example.tubes_uas.MainActivity;
import com.example.tubes_uas.Model.AllUserResponse;
import com.example.tubes_uas.Model.UserDAO;
import com.example.tubes_uas.Model.UserRecyclerAdapter;
import com.example.tubes_uas.Model.UserResponse;
import com.example.tubes_uas.R;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowListUserActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private RecyclerView recyclerView;
    private UserRecyclerAdapter recyclerAdapter;
    private List<UserDAO> users = new ArrayList<>();
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefresh;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list_user);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ShowListUserActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        searchView = findViewById(R.id.searchUser);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        shimmerFrameLayout = findViewById(R.id.shimmerLayout);
        shimmerFrameLayout.startShimmer();

        swipeRefresh.setRefreshing(true);
        loadUser();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUser();
            }
        });
    }

    public void loadUser() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<AllUserResponse> call = apiService.getAllUser("data");

        call.enqueue(new Callback<AllUserResponse>(){
            @Override
            public void onResponse(Call<AllUserResponse> call, Response<AllUserResponse> response) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.VISIBLE);
                shimmerFrameLayout.setVisibility(View.GONE);
                List<UserDAO> users = response.body().getUsers();
                generateDataList(users);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<AllUserResponse> call, Throwable t) {
                Toast.makeText(ShowListUserActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });
    }

    private void generateDataList(List<UserDAO> customerList) {
        recyclerView = findViewById(R.id.userRecyclerView);
        recyclerAdapter = new UserRecyclerAdapter(this, customerList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ShowListUserActivity.this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                recyclerAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recyclerAdapter.getFilter().filter(s);
                return false;
            }
        });
    }
}
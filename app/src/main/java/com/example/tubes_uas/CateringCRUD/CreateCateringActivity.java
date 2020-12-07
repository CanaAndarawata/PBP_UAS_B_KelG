package com.example.tubes_uas.CateringCRUD;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tubes_uas.Api.ApiClient;
import com.example.tubes_uas.Api.ApiInterface;
import com.example.tubes_uas.KosCRUD.CreateKosActivity;
import com.example.tubes_uas.Model.CateringResponse;
import com.example.tubes_uas.Model.KosResponse;
import com.example.tubes_uas.Model.UserResponse;
import com.example.tubes_uas.ProfileActivity;
import com.example.tubes_uas.R;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateCateringActivity extends AppCompatActivity {

    private ImageButton ibBack;
    private AutoCompleteTextView exposedDropdownHari, exposedDropdownPaket, exposedDropdownBulan;
    private MaterialButton btnCancel, btnCreate, btnEdit;
    private int sIdUser;
    private String sPaket = "", sHari = "", sBulan = "";
    private String[] saPaket = new String[] {"Nasi Babi", "Nasi Ayam", "Nasi Goreng"};
    private String[] saHari = new String[] {"1x Sehari", "2x Sehari", "3x Sehari"};
    private String[] saBulan = new String[] {"1 Minggu", "2 Minggu", "4 Minggu"};
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_catering);

        progressDialog = new ProgressDialog(this);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        exposedDropdownPaket = findViewById(R.id.edPaket);
        exposedDropdownHari = findViewById(R.id.edHari);
        exposedDropdownBulan = findViewById(R.id.edBulan);
        btnCancel = findViewById(R.id.btnCancel);
        btnCreate = findViewById(R.id.btnCreate);
        btnEdit = findViewById(R.id.btnEdit);

        Bundle bundle = getIntent().getExtras();
        sIdUser = bundle.getInt("id");
//        loadUserById(sIdUser);

        ArrayAdapter<String> adapterPaket = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saPaket);
        exposedDropdownPaket.setAdapter(adapterPaket);
        exposedDropdownPaket.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sPaket = saPaket[i];
            }
        });

        ArrayAdapter<String> adapterHari = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saHari);
        exposedDropdownHari.setAdapter(adapterHari);
        exposedDropdownHari.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sHari = saHari[i];
            }
        });

        ArrayAdapter<String> adapterBulan = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saBulan);
        exposedDropdownBulan.setAdapter(adapterBulan);
        exposedDropdownBulan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sBulan = saBulan[i];
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sPaket.isEmpty()){
                    exposedDropdownPaket.setError("Isikan dengan benar", null);
                    exposedDropdownPaket.requestFocus();
                } else if(sHari.isEmpty()){
                    exposedDropdownHari.setError("Isikan dengan benar", null);
                    exposedDropdownHari.requestFocus();
                } else if(sBulan.isEmpty()){
                    exposedDropdownBulan.setError("Isikan dengan benar", null);
                    exposedDropdownBulan.requestFocus();
                }else {
                    progressDialog.show();
                    saveCatering();
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent i = new Intent(CreateCateringActivity.this,EditCateringActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", sIdUser);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    private void saveCatering() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CateringResponse> add = apiService.createCatering(sPaket, sHari, sBulan);

        add.enqueue(new Callback<CateringResponse>() {
            @Override
            public void onResponse(Call<CateringResponse> call, Response<CateringResponse> response) {
                Toast.makeText(CreateCateringActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<CateringResponse> call, Throwable t) {
                Toast.makeText(CreateCateringActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void loadUserById(int sIdUser)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.getUserById(sIdUser, "data");

        ApiInterface apiServiceCatering = ApiClient.getClient().create(ApiInterface.class);
        Call<CateringResponse> callCatering = apiServiceCatering.getCateringById(sIdUser, "data");





        callCatering.enqueue(new Callback<CateringResponse>() {
            @Override
            public void onResponse(Call<CateringResponse> call, Response<CateringResponse> response) {
                sPaket = response.body().getData().getPaket();
                sHari = response.body().getData().getHari();
                sBulan = response.body().getData().getBulan();

                exposedDropdownPaket.setText(sPaket);
                exposedDropdownHari.setText(sHari);
                exposedDropdownBulan.setText(sBulan);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CateringResponse> call, Throwable t) {
                Toast.makeText(CreateCateringActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }




}
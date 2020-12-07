package com.example.tubes_uas.KosCRUD;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tubes_uas.Api.ApiClient;
import com.example.tubes_uas.Api.ApiInterface;
import com.example.tubes_uas.CateringCRUD.CreateCateringActivity;
import com.example.tubes_uas.CateringCRUD.EditCateringActivity;
import com.example.tubes_uas.Model.CateringResponse;
import com.example.tubes_uas.Model.KosResponse;
import com.example.tubes_uas.Model.UserResponse;
import com.example.tubes_uas.ProfileActivity;
import com.example.tubes_uas.R;
import com.example.tubes_uas.UserCRUD.CreateUserActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateKosActivity extends AppCompatActivity {
    private ImageButton ibBack;
    private AutoCompleteTextView exposedDropdownJenis, exposedDropdownFasilitas, exposedDropdownLama;
    private MaterialButton btnCancel, btnCreate, btnEdit;
    private int sIdUser;
    private String sFasilitas = "", sJenis = "", sLama = "";
    private String[] saJenis = new String[] {"Ekslusif", "Biasa"};
    private String[] saFasilitas = new String[] {"Kamar Mandi Dalam", "Kamar Mandi Luar"};
    private String[] saLama = new String[] {"1 Bulan", "6 Bulan", "1 Tahun"};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_kos);

        progressDialog = new ProgressDialog(this);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        exposedDropdownFasilitas = findViewById(R.id.edFasilitas);
        exposedDropdownJenis = findViewById(R.id.edJenis);
        exposedDropdownLama = findViewById(R.id.edLama);
        btnCancel = findViewById(R.id.btnCancel);
        btnCreate = findViewById(R.id.btnCreate);
        btnEdit = findViewById(R.id.btnEdit);


        ArrayAdapter<String> adapterFasilitas = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saFasilitas);
        exposedDropdownFasilitas.setAdapter(adapterFasilitas);
        exposedDropdownFasilitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sFasilitas = saFasilitas[i];
            }
        });

        ArrayAdapter<String> adapterJenis = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saJenis);
        exposedDropdownJenis.setAdapter(adapterJenis);
        exposedDropdownJenis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sJenis = saJenis[i];
            }
        });

        ArrayAdapter<String> adapterLama = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saLama);
        exposedDropdownLama.setAdapter(adapterLama);
        exposedDropdownLama.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sLama = saLama[i];
            }
        });

        Bundle bundle = getIntent().getExtras();
        sIdUser = bundle.getInt("id");
//        loadUserById(sIdUser);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sFasilitas.isEmpty()){
                    exposedDropdownFasilitas.setError("Isikan dengan benar", null);
                    exposedDropdownFasilitas.requestFocus();
                } else if(sJenis.isEmpty()){
                    exposedDropdownJenis.setError("Isikan dengan benar", null);
                    exposedDropdownJenis.requestFocus();
                } else if(sLama.isEmpty()){
                    exposedDropdownLama.setError("Isikan dengan benar", null);
                    exposedDropdownLama.requestFocus();
                }else {
                    progressDialog.show();
                    saveKos();
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent i = new Intent(CreateKosActivity.this, EditKosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", sIdUser);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    private void saveKos() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<KosResponse> add = apiService.createKos(sFasilitas, sJenis, sLama);

        add.enqueue(new Callback<KosResponse>() {
            @Override
            public void onResponse(Call<KosResponse> call, Response<KosResponse> response) {
                Toast.makeText(CreateKosActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<KosResponse> call, Throwable t) {
                Toast.makeText(CreateKosActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void loadUserById(int sIdUser)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.getUserById(sIdUser, "data");

        ApiInterface apiServiceKos = ApiClient.getClient().create(ApiInterface.class);
        Call<KosResponse> callKos = apiServiceKos.getKosById(sIdUser, "data");

        callKos.enqueue(new Callback<KosResponse>() {
            @Override
            public void onResponse(Call<KosResponse> call, Response<KosResponse> response) {
                if (response.code() == 200){
                    sJenis = response.body().getData().getJenis();
                    sFasilitas = response.body().getData().getFasilitas();
                    sLama = response.body().getData().getLama();
                }else {
                    sJenis = "Jenis";
                    sFasilitas = "Fasilitas";
                    sLama = "Lama";
                }

                exposedDropdownJenis.setText(sJenis);
                exposedDropdownFasilitas.setText(sFasilitas);
                exposedDropdownLama.setText(sLama);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<KosResponse> call, Throwable t) {
                Toast.makeText(CreateKosActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void updateUser()
    {
        ApiInterface apiServiceKos = ApiClient.getClient().create(ApiInterface.class);
        final Call<KosResponse> update = apiServiceKos.editKosById(sIdUser, exposedDropdownJenis.getText().toString(), exposedDropdownFasilitas.getText().toString(),  exposedDropdownLama.getText().toString());
        update.enqueue(new Callback<KosResponse>()
        {
            @Override
            public void onResponse(Call<KosResponse> call, Response<KosResponse> response)
            {
                Toast.makeText(CreateKosActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

                Intent intent = new Intent(CreateKosActivity.this, ProfileActivity.class);
                finish();
                startActivity(intent);


            }
            @Override
            public void onFailure(Call<KosResponse> call, Throwable t)
            {
                Toast.makeText(CreateKosActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
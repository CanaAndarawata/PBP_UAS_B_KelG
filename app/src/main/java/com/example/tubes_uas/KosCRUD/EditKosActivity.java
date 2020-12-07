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
import com.example.tubes_uas.CateringCRUD.EditCateringActivity;
import com.example.tubes_uas.Model.KosResponse;
import com.example.tubes_uas.Model.UserResponse;
import com.example.tubes_uas.ProfileActivity;
import com.example.tubes_uas.R;
import com.example.tubes_uas.UserCRUD.EditUserActivity;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditKosActivity extends AppCompatActivity {
    private int sIdUser;
    private ImageButton ibBack;
    private AutoCompleteTextView exposedDropdownFasilitas, exposedDropdownJenis, exposedDropdownLama;
    private MaterialButton btnCancel, btnUpdate;
    private String sNama, sEmail, sJenis = "", sFasilitas = "", sLama = "";
    private String[] saJenis = new String[] {"Ekslusif", "Biasa"};
    private String[] saFasilitas = new String[] {"Kamar Mandi Dalam", "Kamar Mandi Luar"};
    private String[] saLama = new String[] {"1 Bulan", "6 Bulan", "1 Tahun"};
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kos);

        progressDialog = new ProgressDialog(this);

        ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        exposedDropdownJenis = findViewById(R.id.edJenis);
        exposedDropdownFasilitas = findViewById(R.id.edFasilitas);
        exposedDropdownLama = findViewById(R.id.edLama);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);

        Bundle bundle = getIntent().getExtras();
        sIdUser = bundle.getInt("id");
        loadKosById(sIdUser);

        ArrayAdapter<String> adapterJenis = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saJenis);
        exposedDropdownJenis.setAdapter(adapterJenis);
        exposedDropdownJenis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sJenis = saJenis[i];
            }
        });

        ArrayAdapter<String> adapterFasilitas = new ArrayAdapter<>(Objects.requireNonNull(this),
                R.layout.list_item, R.id.item_list, saFasilitas);
        exposedDropdownFasilitas.setAdapter(adapterFasilitas);

        exposedDropdownFasilitas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sFasilitas = saFasilitas[i];
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

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sJenis.isEmpty()){
                    exposedDropdownJenis.setError("Isikan dengan benar", null);
                    exposedDropdownJenis.requestFocus();
                } else if(sFasilitas.isEmpty()){
                    exposedDropdownFasilitas.setError("Isikan dengan benar", null);
                    exposedDropdownFasilitas.requestFocus();
                } else if(sLama.isEmpty()){
                    exposedDropdownLama.setError("Isikan dengan benar", null);
                    exposedDropdownLama.requestFocus();
                }else {
                    progressDialog.show();
                    updateKos(sIdUser);
                }
            }
        });

        Intent mIntent = getIntent();
//        sIdUser = mIntent.getIntExtra("id");
        loadKosById(sIdUser);
    }

    private void updateKos(int id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<KosResponse> add = apiService.editKosById(id,  sJenis, sFasilitas, sLama);

        add.enqueue(new Callback<KosResponse>() {
            @Override
            public void onResponse(Call<KosResponse> call, Response<KosResponse> response) {
                Toast.makeText(EditKosActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(EditKosActivity.this, ProfileActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", sIdUser);
                i.putExtras(bundle);
                startActivity(i);
            }

            @Override
            public void onFailure(Call<KosResponse> call, Throwable t) {
                Toast.makeText(EditKosActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void loadKosById(int id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<KosResponse> add = apiService.getKosById(id, "data");

        add.enqueue(new Callback<KosResponse>() {
            @Override
            public void onResponse(Call<KosResponse> call, Response<KosResponse> response) {
//                sFasilitas = response.body().getUsers().get(0).getFasilitas();
//                sJenis = response.body().getUsers().get(0).getJenis();
//                sLama = response.body().getUsers().get(0).getLama();

                exposedDropdownFasilitas.setText(sFasilitas, false);
                exposedDropdownJenis.setText(sJenis, false);
                exposedDropdownLama.setText(sLama, false);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<KosResponse> call, Throwable t) {
                Toast.makeText(EditKosActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}
package com.example.tubes_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.tubes_uas.Api.ApiClient;
import com.example.tubes_uas.Api.ApiInterface;
import com.example.tubes_uas.CateringCRUD.CreateCateringActivity;
import com.example.tubes_uas.CateringCRUD.EditCateringActivity;
import com.example.tubes_uas.KosCRUD.CreateKosActivity;
import com.example.tubes_uas.Model.CateringResponse;
import com.example.tubes_uas.Model.KosResponse;
import com.example.tubes_uas.Model.UserDAO;
import com.example.tubes_uas.Model.UserResponse;
import com.example.tubes_uas.UserCRUD.CreateUserActivity;
import com.example.tubes_uas.UserCRUD.EditUserActivity;
import com.example.tubes_uas.UserCRUD.ShowListUserActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private MaterialTextView twEmail, twFasilitas, twJenis, twLama, twPaket, twHari, twBulan;
    private String sEmail, sFasilitas, sJenis, sLama, sPaket, sHari, sBulan;
    private int sIdUser;
    private CardView cvCreateKos, cvCreateCatering, btnDeleteKos, btnDeleteCatering;
    private MaterialButton btnLogout, btnEdit;
    private ProgressDialog progressDialog;
    private List<UserDAO> users;
    //Map
    private FloatingActionButton mapFab;
    private static final int REQUEST_CODE = 100;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        cvCreateKos = findViewById(R.id.cvCreateKos);
        cvCreateCatering = findViewById(R.id.cvCreateCatering);
        btnDeleteKos = findViewById(R.id.cvDeleteKos);
        btnDeleteCatering = findViewById(R.id.cvDeleteCatering);

        twEmail = findViewById(R.id.twEmail);
        twFasilitas = findViewById(R.id.twFasilitas);
        twJenis = findViewById(R.id.twJenis);
        twLama = findViewById(R.id.twLama);
        twPaket = findViewById(R.id.twPaket);
        twHari = findViewById(R.id.twHari);
        twBulan = findViewById(R.id.twBulan);
        btnLogout = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEdit);
        
        Bundle bundle = getIntent().getExtras();
        sIdUser = bundle.getInt("id");
        loadUserById(sIdUser);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, LogRegActivity.class);
                startActivity(i);
            }
        });

        btnDeleteKos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteKos(sIdUser);
            }
        });

        btnDeleteCatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCatering(sIdUser);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, PhotoActivity.class);
                Bundle bundle = new Bundle();
//                bundle.putString("id", users.getId());
                Intent mIntent = getIntent();
//                sIdUser = mIntent.getIntExtra("id");
                intent.putExtras(mIntent);
                startActivity(intent);
            }
        });

        cvCreateKos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, CreateKosActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", sIdUser);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        cvCreateCatering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, CreateCateringActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", sIdUser);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        //Map
//        Maps();
    }

    private void loadUserById(int sIdUser) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> call = apiService.getUserById(sIdUser, "data");

        ApiInterface apiServiceKos = ApiClient.getClient().create(ApiInterface.class);
        Call<KosResponse> callKos = apiServiceKos.getKosById(sIdUser, "data");

        ApiInterface apiServiceCatering = ApiClient.getClient().create(ApiInterface.class);
        Call<CateringResponse> callCatering = apiServiceCatering.getCateringById(sIdUser, "data");

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                sEmail = response.body().getData().getEmail();
                twEmail.setText(sEmail);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        callKos.enqueue(new Callback<KosResponse>() {
            @Override
            public void onResponse(Call<KosResponse> call, Response<KosResponse> response) {
                if (response.code() == 200){
                    if (response.body().getData() != null){
                        Toast.makeText(ProfileActivity.this, response.body().getData().getFasilitas(), Toast.LENGTH_SHORT).show();
                        sFasilitas = response.body().getData().getFasilitas();
                        sJenis = response.body().getData().getJenis();
                        sLama = response.body().getData().getLama();

                        twFasilitas.setText(sFasilitas);
                        twJenis.setText(sJenis);
                        twLama.setText(sLama);
                    }
                }
            }

            @Override
            public void onFailure(Call<KosResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

        callCatering.enqueue(new Callback<CateringResponse>() {
            @Override
            public void onResponse(Call<CateringResponse> call, Response<CateringResponse> response) {
                if (response.code() == 200){
                    if (response.body().getData() != null){
                        sPaket = response.body().getData().getPaket();
                        sHari = response.body().getData().getHari();
                        sBulan = response.body().getData().getBulan();

                        twPaket.setText(sPaket);
                        twHari.setText(sHari);
                        twBulan.setText(sBulan);
                    }
                }
            }

            @Override
            public void onFailure(Call<CateringResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void deleteKos(final int sIdUser) {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<KosResponse> call = apiService.deleteKosById(sIdUser);

            call.enqueue(new Callback<KosResponse>() {
                @Override
                public void onResponse(Call<KosResponse> call, Response<KosResponse> response) {
                    Toast.makeText(getApplicationContext(), "Kos berhasil dihapus", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<KosResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void deleteCatering(final int sIdUser) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<CateringResponse> call = apiService.deleteCateringById(sIdUser);

        call.enqueue(new Callback<CateringResponse>() {
            @Override
            public void onResponse(Call<CateringResponse> call, Response<CateringResponse> response) {
                Toast.makeText(getApplicationContext(), "Catering berhasil dihapus", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<CateringResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Maps(){
        LocationPermission();
        mapFab = findViewById(R.id.fab_map);
        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maps = new Intent(ProfileActivity.this, Peta.class);
                startActivity(maps);
            }
        });
    }

    @AfterPermissionGranted(REQUEST_CODE)
    private void LocationPermission(){
        String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};

        if(EasyPermissions.hasPermissions(this, permission)){
            Toast.makeText(this, "Akses Lokasi diizinkan", Toast.LENGTH_SHORT).show();
        }else {
            EasyPermissions.requestPermissions(this, "Akses Lokasi tidak diizinkan",
                    REQUEST_CODE, permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(REQUEST_CODE, permissions,grantResults,this);
    }


    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }


    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
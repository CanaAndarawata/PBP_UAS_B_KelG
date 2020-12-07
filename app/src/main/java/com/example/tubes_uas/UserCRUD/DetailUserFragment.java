package com.example.tubes_uas.UserCRUD;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.tubes_uas.Api.ApiClient;
import com.example.tubes_uas.Api.ApiInterface;
import com.example.tubes_uas.Model.UserDAO;
import com.example.tubes_uas.Model.UserResponse;
import com.example.tubes_uas.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailUserFragment extends DialogFragment {
    private TextView twNama, twEmail, twJenis, twFasilitas, twLama;
    private String sNama, sEmail, sJenis, sFasilitas, sLama;
    private int sIdUser;
    private ImageButton ibClose;
    private ProgressDialog progressDialog;
    private Button btnDelete, btnEdit;
    private List<UserDAO> users;

    public static DetailUserFragment newInstance() { return new DetailUserFragment(); }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.detail_user_fragment, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.show();

        ibClose = v.findViewById(R.id.ibClose);
        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        twNama      = v.findViewById(R.id.twNama);
        twEmail     = v.findViewById(R.id.twEmail);
        twJenis     = v.findViewById(R.id.twJenis);
        twFasilitas = v.findViewById(R.id.twFasilitas);
        twLama      = v.findViewById(R.id.twLama);

        btnDelete = v.findViewById(R.id.btnDelete);
        btnEdit = v.findViewById(R.id.btnEdit);

//        sIdUser = getArguments().getInt("id", "");
        loadUserById(sIdUser);

        return v;
    }

    private void loadUserById(int id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserResponse> add = apiService.getUserById(id, "data");

        add.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
//                sNama       = response.body().getUsers().get(0).getNama();
//                sEmail      = response.body().getUsers().get(0).getEmail();
//                sJenis      = response.body().getUsers().get(0).getJenis();
//                sFasilitas  = response.body().getUsers().get(0).getFasilitas();
//                sLama       = response.body().getUsers().get(0).getLama();

                twNama.setText(sNama);
                twEmail.setText(sEmail);
                twJenis.setText(sJenis);
                twFasilitas.setText(sFasilitas);
                twLama.setText(sLama);
                progressDialog.dismiss();

//                users = response.body().getUsers();
                final UserDAO user = users.get(0);

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteUser(sIdUser);
                    }
                });

                btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), EditUserActivity.class);
                        Bundle bundle = new Bundle();
//                        bundle.putString("id", user.getId());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void deleteUser(final int sIdUser) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Yakin ingin menghapus akun?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                        Call<UserResponse> call = apiService.deleteUserById(sIdUser);

                        call.enqueue(new Callback<UserResponse>() {
                            @Override
                            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                                Toast.makeText(getContext(), "User berhasil dihapus", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), ShowListUserActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<UserResponse> call, Throwable t) {
                                Toast.makeText(getContext(), "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
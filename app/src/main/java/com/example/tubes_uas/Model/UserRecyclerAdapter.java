package com.example.tubes_uas.Model;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tubes_uas.UserCRUD.DetailUserFragment;
import com.example.tubes_uas.R;

import java.util.ArrayList;
import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.RoomViewHolder> implements Filterable {
    private List<UserDAO> dataList;
    private List<UserDAO> filteredDataList;
    private Context context;

    public UserRecyclerAdapter(Context context, List<UserDAO> dataList) {
        this.dataList = dataList;
        this.filteredDataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycle_adapter_user, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRecyclerAdapter.RoomViewHolder holder, int position) {
        final UserDAO brg = filteredDataList.get(position);

        holder.mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
                DetailUserFragment dialog = new DetailUserFragment();
                dialog.show(manager, "dialog");

                Bundle args = new Bundle();
                args.putInt("id", brg.getId());
                dialog.setArguments(args);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder{
        private TextView twEmail;
        private LinearLayout mParent;

        public RoomViewHolder(@NonNull View itemView){
            super(itemView);
            twEmail = itemView.findViewById(R.id.twEmail);
            mParent = itemView.findViewById(R.id.linearLayout);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()){
                    filteredDataList = dataList;
                }else {
                    List<UserDAO> filteredList = new ArrayList<>();
                    for (UserDAO UserDAO : dataList){
                        if (UserDAO.getEmail().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(UserDAO);
                        }
                        filteredDataList = filteredList;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredDataList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredDataList = (List<UserDAO>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}

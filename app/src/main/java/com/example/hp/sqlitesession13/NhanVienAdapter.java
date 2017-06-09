package com.example.hp.sqlitesession13;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 06/06/2017.
 */

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {

    ArrayList<NhanVien> nhanViens;
    Context context;
    private ItemClickListener clickListener;

    public NhanVienAdapter(ArrayList<NhanVien> dataShops, Context context) {
        this.nhanViens = dataShops;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_item, parent, false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NhanVienAdapter.ViewHolder holder, int position) {
        holder.txtUsername.setText(nhanViens.get(position).getUsername());
        holder.txtEmail.setText(nhanViens.get(position).getEmail());
        holder.txtPhone.setText(nhanViens.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return nhanViens.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtUsername, txtEmail, txtPhone;
        Button btnUpdate;
        public ViewHolder(View itemView) {
            super(itemView);

            txtUsername = (TextView) itemView.findViewById(R.id.txtUsername);
            txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            txtPhone = (TextView) itemView.findViewById(R.id.txtPhone);
            btnUpdate = (Button) itemView.findViewById(R.id.btnUpdate);
            itemView.setOnClickListener(this);
            btnUpdate.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            intent.putExtra("username", txtUsername.getText().toString());
            view.getContext().startActivity(intent);

            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }



}

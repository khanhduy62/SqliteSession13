package com.example.hp.sqlitesession13;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by hp on 07/06/2017.
 */

public class ShowActivity extends AppCompatActivity implements View.OnClickListener, ItemClickListener{

    private ArrayList<NhanVien> nhanViens ;
    private RecyclerView recyclerView;
    private NhanVienAdapter nhanVienAdapter;
    private Button btnBack;
    private SqlLiteDbHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_activity);

        addControls();
        addEvents();

    }

    private void addEvents() {
        btnBack.setOnClickListener(this);
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                if (direction == ItemTouchHelper.LEFT) {    //if swipe left

                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this); //alert for confirm to delete
                    builder.setMessage("Are you sure to delete?");    //set message

                    builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nhanVienAdapter.notifyItemRemoved(position);    //item removed from recylcerview
                            dbHelper.deleteRowNhanVien(nhanViens.get(position).getUsername());
                            nhanViens.remove(position);  //then remove item

                            return;
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            nhanVienAdapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
                            nhanVienAdapter.notifyItemRangeChanged(position, nhanVienAdapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            return;
                        }
                    });  //show alert dialog

                    builder.setCancelable(false);
                    builder.show();
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview

        nhanVienAdapter.setClickListener(this);
    }

    private void addControls() {

        btnBack = (Button) findViewById(R.id.btnBack);

        recyclerView= (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        dbHelper = new SqlLiteDbHelper(this);
        dbHelper.openDataBase();

        nhanViens = (ArrayList<NhanVien>) getIntent().getSerializableExtra("list");
        nhanVienAdapter = new NhanVienAdapter(nhanViens, ShowActivity.this);
        recyclerView.setAdapter(nhanVienAdapter);

    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void onClick(View view, int position) {
        Log.i("position", position+"");
    }
}

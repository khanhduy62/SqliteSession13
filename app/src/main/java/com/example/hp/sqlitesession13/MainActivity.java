package com.example.hp.sqlitesession13;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private SqlLiteDbHelper dbHelper;
    private ArrayList<NhanVien> nhanViens ;
    private Button btnShow, btnAdd, btnUpdate;
    private EditText edtUsername, edtEmail, edtPhone;
    private String username, email, phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addControls();
        addEvents();


    }

    private void addEvents() {
        btnShow.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);

    }

    private void addControls() {
        btnShow = (Button) findViewById(R.id.btnShow);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);

        dbHelper = new SqlLiteDbHelper(this);
        dbHelper.openDataBase();
        nhanViens = new ArrayList<>();


    }

    @Override
    public void onClick(View v) {
        username = edtUsername.getText().toString();
        email = edtEmail.getText().toString();
        phone = edtPhone.getText().toString();
        switch (v.getId()){
            case R.id.btnShow:
                Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                nhanViens = dbHelper.get_ContactDetails();
                intent.putExtra("list", nhanViens);
                startActivity(intent);
                break;
            case R.id.btnAdd:
                checkFields(username, email, phone, 1);
                break;
            case R.id.btnUpdate:
                checkFields(username, email, phone, 2);
                break;
        }
    }

    private void checkFields(String username, String email, String phone, int value){
        boolean checkEmail = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!checkEmail){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //alert for confirm to delete
            builder.setMessage("Please input email correctly!!! ");    //set message

            builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() { //when click on DELETE
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).show();  //show alert dialog
        }else if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //alert for confirm to delete
            builder.setMessage("Please input all fields  !!! ");    //set message

            builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() { //when click on DELETE
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).show();  //show alert dialog
        }else{
            if(dbHelper.findExistUsername(username) && value != 2){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //alert for confirm to delete
                builder.setMessage("Exist username, please input another username !!! "+username);    //set message

                builder.setPositiveButton("Got it", new DialogInterface.OnClickListener() { //when click on DELETE
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();  //show alert dialog
            }else{
                if(value == 1){
                    dbHelper.insertDataNhanVien(new NhanVien(edtUsername.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString()));
                }
                if(value == 2){
                    dbHelper.updateUser(username, email, phone);
                }

                btnShow.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        nhanViens = dbHelper.get_ContactDetails();

        Intent intent = getIntent();


        btnUpdate.setVisibility(View.GONE);

        if(nhanViens!=null){
            btnShow.setVisibility(View.VISIBLE);
        }else{
            btnShow.setVisibility(View.GONE);
        }

//        Log.i("intent", intent.getStringExtra("username") == null ? "k":"co");
        if(intent.getStringExtra("username") != null && nhanViens != null){
            edtUsername.setText(intent.getStringExtra("username"));
            btnUpdate.setVisibility(View.VISIBLE);
            btnAdd.setVisibility(View.GONE);
            edtUsername.setEnabled(false);
            edtEmail.requestFocus();
        }else{
            btnUpdate.setVisibility(View.GONE);
            btnAdd.setVisibility(View.VISIBLE);
            edtUsername.setEnabled(true);
            edtUsername.setText("");
            edtEmail.setText("");
            edtPhone.setText("");
            edtUsername.requestFocus();
        }
    }
}

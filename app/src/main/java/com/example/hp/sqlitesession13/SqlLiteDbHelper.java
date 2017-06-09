package com.example.hp.sqlitesession13;

/**
 * Created by hp on 06/06/2017.
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlLiteDbHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "nhanvien.sqlite";
    private static final String DB_PATH_SUFFIX = "/databases/";
    static Context ctx;
    public SqlLiteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    // Getting single contact
    public ArrayList<NhanVien> get_ContactDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<NhanVien> contactList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM nhanvien", null);
        if (cursor != null && cursor.moveToFirst()){
            do{
                NhanVien cont = new NhanVien(cursor.getString(0),cursor.getString(1), cursor.getString(2));
                contactList.add(cont);
            }while (cursor.moveToNext());
            // return contact

            cursor.close();
            db.close();
            return  contactList;

        }
        return null;
    }

    public Cursor get_Contact(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM nhanvien",null);
        return cursor;
    }

    public void CopyDataBaseFromAsset() throws IOException{

        InputStream myInput = ctx.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = getDatabasePath();

        // if the path doesn't exist first, create it
        File f = new File(ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
        if (!f.exists())
            f.mkdir();

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }
    private static String getDatabasePath() {
        return ctx.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public SQLiteDatabase openDataBase() throws SQLException{
        File dbFile = ctx.getDatabasePath(DATABASE_NAME);

        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                System.out.println("Copying sucess from Assets folder");
            } catch (IOException e) {
                throw new RuntimeException("Error creating source database", e);
            }
        }

        return SQLiteDatabase.openDatabase(dbFile.getPath(), null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
    }

    public void insertDataNhanVien(NhanVien nhanVien){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("username", nhanVien.getUsername());
        values.put("email", nhanVien.getEmail());
        values.put("phone", nhanVien.getPhone());


        // Trèn một dòng dữ liệu vào bảng.
        db.insert("nhanvien", null, values);


        // Đóng kết nối database.
        db.close();
    }

    public void deleteRowNhanVien(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("nhanvien", "username" + " = ?",   new String[] { username });
        db.close();
    }

    public boolean findExistUsername(String username){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select * from nhanvien where username = '"+username.trim()+"'";
        Cursor cursor = db.rawQuery(sql,null);
        Log.i("ten ",username);
        if (cursor.getCount() > 0){
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }

    public void updateUser(String username, String email, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "update nhanvien set email = '"+email+"', phone = '"+phone+"' where username = '"+username+"'";
        ContentValues values = new ContentValues();
        values.put("phone",phone);
        values.put("email", email);
        db.update("nhanvien", values, "username=?", new String[] {username});
        Log.i("sql",sql);
        db.close();
    }

}

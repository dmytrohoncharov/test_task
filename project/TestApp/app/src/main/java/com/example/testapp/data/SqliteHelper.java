package com.example.testapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.testapp.data.objects.Customer;
import com.example.testapp.data.objects.CustomerClassification;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SqliteHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private Context mContext;

    //static vals for Customers table
    public static final String CUSTOMERS_DATABASE_NAME = "CustomersDBase";
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String CUSTOMER_ID = "id";
    public static final String CUSTOMER_NAME = "username";
    public static final String CUSTOMER_NIP = "nip";
    public static final String CUSTOMER_CITY = "city";
    public static final String CUSTOMER_DATE = "date";

    //static vals for classifications table
    public static final String CLASSIFICATIONS_DATABASE_NAME = "CustomerClassificationsDBase";
    public static final String TABLE_CLASSIFICATIONS = "classifications";
    public static final String CUSTOMER_CLASSIFICATION_NAME = "cl_name";
    public static final String CUSTOMER_CLASSIFICATION_TYPE = "cl_type";
    public static final String CUSTOMER_CLASSIFICATION_DESCRIPTION = "cl_description";

    //static vals for both tables
    public static final String CUSTOMER_CLASSIFICATION_ID = "classification_id";
    public static final int DATABASE_VERSION = 1;

    public static final String CREATE_CUSTOMERS_TABLE = " CREATE TABLE " + TABLE_CUSTOMERS
            + " ( "
            + CUSTOMER_ID + " INTEGER PRIMARY KEY, "
            + CUSTOMER_NAME + " TEXT, "
            + CUSTOMER_NIP + " TEXT, "
            + CUSTOMER_CITY + " TEXT, "
            + CUSTOMER_DATE + " TEXT,"
            + CUSTOMER_CLASSIFICATION_ID + " TEXT"
            + " ) ";

    public static final String CREATE_CLASSIFICATIONS_TABLE = " CREATE TABLE " + TABLE_CLASSIFICATIONS
            + " ( "
            + CUSTOMER_CLASSIFICATION_ID + " INTEGER PRIMARY KEY, "
            + CUSTOMER_CLASSIFICATION_NAME + " TEXT, "
            + CUSTOMER_CLASSIFICATION_TYPE + " TEXT, "
            + CUSTOMER_CLASSIFICATION_DESCRIPTION + " TEXT "
            + " ) ";


    public SqliteHelper(@Nullable Context context) {
        super(context, CUSTOMERS_DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CUSTOMERS_TABLE);
        db.execSQL(CREATE_CLASSIFICATIONS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
    }

    public void openDataBase() throws SQLException {
        db = getWritableDatabase();
    }

    public void close() {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    public void addCustomerToDB(Customer customer){
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_ID, customer.getCustomerId());
        values.put(CUSTOMER_NAME, customer.getName());
        values.put(CUSTOMER_NIP, customer.getNip());
        values.put(CUSTOMER_CITY, customer.getCity());
        values.put(CUSTOMER_DATE, new SimpleDateFormat("MM-dd-yyyy").format(customer.getDate()));
        values.put(CUSTOMER_CLASSIFICATION_ID, customer.getClassificationId());
        db.insert(TABLE_CUSTOMERS, null, values);
    }

    public Customer getCustomerFromDB(Integer id){
        Cursor cursor = db.rawQuery("SELECT * FROM customers WHERE id = ?", new String[]{id.toString().trim()});
        cursor.moveToFirst();
        String c_name = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME));
        String c_nip = cursor.getString(cursor.getColumnIndex(CUSTOMER_NIP));
        String c_city = cursor.getString(cursor.getColumnIndex(CUSTOMER_CITY));
        String date = cursor.getString(cursor.getColumnIndex(CUSTOMER_DATE));
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH);
        Date c_date = null;
        try {
            c_date = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String c_id = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
        String c_classification_id = cursor.getString(cursor.getColumnIndex(CUSTOMER_CLASSIFICATION_ID));

        return new Customer(c_name, c_nip, c_city, c_date, Integer.valueOf(c_id), Integer.valueOf(c_classification_id) );
    }

    public void addCustomerClassificationToDB(CustomerClassification clas){
        ContentValues values = new ContentValues();
        values.put(CUSTOMER_CLASSIFICATION_ID, clas.getClassificationId());
        values.put(CUSTOMER_CLASSIFICATION_NAME, clas.getName());
        values.put(CUSTOMER_CLASSIFICATION_TYPE, clas.getType());
        values.put(CUSTOMER_CLASSIFICATION_DESCRIPTION, clas.getDescription());
        db.insert(TABLE_CLASSIFICATIONS, null, values);
    }

    public CustomerClassification getCustomerClassificationFromDB(Integer id){
        Cursor cursor = db.rawQuery("SELECT * FROM classifications WHERE classification_id = ?", new String[]{id.toString().trim()});
        cursor.moveToFirst();
        String c_name = cursor.getString(cursor.getColumnIndex(CUSTOMER_CLASSIFICATION_NAME));
        String c_id = cursor.getString(cursor.getColumnIndex(CUSTOMER_CLASSIFICATION_ID));
        String c_type = cursor.getString(cursor.getColumnIndex(CUSTOMER_CLASSIFICATION_TYPE));
        String c_description = cursor.getString(cursor.getColumnIndex(CUSTOMER_CLASSIFICATION_DESCRIPTION));

        return new CustomerClassification(c_name, c_description, Integer.valueOf(c_type), Integer.valueOf(c_id));
    }

    public ArrayList<Customer> getAllCustomers(){
        ArrayList<Customer> arr = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMERS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 1;
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            arr.add(getCustomerFromDB(i));
            i++;
            cursor.moveToNext();
        }
        cursor.close();
        return arr;
    }

    public ArrayList<CustomerClassification> getAllCustomerClassifications(){
        ArrayList<CustomerClassification> arr = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_CLASSIFICATIONS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        int i = 1;
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            arr.add(getCustomerClassificationFromDB(i));
            i++;
            cursor.moveToNext();
        }
        cursor.close();
        return arr;
    }
}
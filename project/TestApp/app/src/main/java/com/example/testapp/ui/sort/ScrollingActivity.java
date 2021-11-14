package com.example.testapp.ui.sort;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import com.example.testapp.R;
import com.example.testapp.data.SqliteHelper;
import com.example.testapp.data.objects.Customer;
import com.example.testapp.data.objects.CustomerClassification;
import com.example.testapp.databinding.ActivityScrollingBinding;
import com.example.testapp.ui.models.DataBar;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ScrollingActivity extends AppCompatActivity {

    private ActivityScrollingBinding binding;
    private final Context ctx = this;
    private String sortedBy ="type";
    private List<DataBar> barArr = new ArrayList<DataBar>();
    private List<Customer> customerList = new ArrayList<Customer>();
    private List<CustomerClassification> customerClassificationList = new ArrayList<CustomerClassification>();
    private SqliteHelper sql = new SqliteHelper(ctx);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        //check if our sqlite database exists and if not - create some random info
        if (!(ctx.databaseList().length>0))
            addNewRandomCustomers(5);
        else
            showData();

        FloatingActionButton fab_add = binding.fabAdd;
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDefinedCustomer();
            }
        });

        FloatingActionButton fab_sort = binding.fabSort;
        fab_sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortBars();
            }
        });
    }

    private void addNewRandomCustomers(int i){
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] alphabetArr = alphabet.toCharArray();
        Random random = new Random();

        for(int j=0; j<i; j++ ){
            String barName = alphabetArr[random.nextInt(alphabet.length())]+"-name";
            int randtype = ThreadLocalRandom.current().nextInt(0, 3);
            int year = randBetween(1995, 2020);
            int month = randBetween(0, 11);

            GregorianCalendar gc = new GregorianCalendar(year, month, 1);
            int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));
            gc.set(year, month, day);
            Date barDate = gc.getTime();

            this.addNewCustomer(
                    barName,
                    Integer.valueOf(ThreadLocalRandom.current().nextInt(1000000000, 2100000000)).toString(),
                    alphabetArr[random.nextInt(alphabet.length())]+"-city",
                    "Example text description",
                    barDate,
                    randtype
            );
        }
    }

    private void showData(){
        sql.openDataBase();
        ArrayList<Customer> custArr = sql.getAllCustomers();
        ArrayList<CustomerClassification> custClasArr = sql.getAllCustomerClassifications();
        int k = custArr.size();
        for(int i = 0; i<k; i++){
            Customer cust = custArr.get(i);
            DataBar databar = new DataBar(ctx, cust.getName(), cust.getDate(), cust.getClassificationId(), cust, custClasArr.get(i) );
            barArr.add(databar);
            customerList.add(cust);
            customerClassificationList.add(custClasArr.get(i));
            binding.scrollContainer.mainContainer.addView(databar);
        }
        sql.close();
    }

    private int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

    private void sortBars(){
        binding.scrollContainer.mainContainer.removeAllViews();
        if(sortedBy.equals("type")){
            sortByName();
            this.sortedBy = "name";
        }else if(sortedBy.equals("name")){
            this.sortedBy = "date";
            sortByDate();
        }else{
            this.sortedBy = "type";
            sortByType();
        }
        Toast toast = Toast.makeText(ctx, "List sorted by "+this.sortedBy, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void sortByName(){
        Comparator<DataBar> compareByName = new Comparator<DataBar>() {
            @Override
            public int compare(DataBar t0, DataBar t1) {
                return t0.getName().compareTo(t1.getName());
            }
        };
        Collections.sort(barArr, compareByName);
        addSortedBars();
    }

    private void sortByType(){
        Collections.sort(barArr, new Comparator<DataBar>() {
            @Override
            public int compare(DataBar t1, DataBar t2) {
                return t1.getType() - t2.getType();
            }
        });
        addSortedBars();
    }

    private void sortByDate(){
        Collections.sort(barArr, new Comparator<DataBar>() {
            public int compare(DataBar t1, DataBar t2) {
                return t1.getDate().compareTo(t2.getDate());
            }
        });
        addSortedBars();
    }

    private void addSortedBars(){
        for (DataBar bar : barArr) {
            binding.scrollContainer.mainContainer.addView(bar);
        }
    }

    private void addDefinedCustomer(){

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.add_layout,null);

        EditText nameTextView = (EditText) popupView.findViewById(R.id.new_name);
        EditText nipTextView = (EditText) popupView.findViewById(R.id.new_nip);
        EditText cityTextView = (EditText) popupView.findViewById(R.id.new_city);
        EditText descriptionTextView = (EditText) popupView.findViewById(R.id.new_description);
        EditText dateTextView = (EditText) popupView.findViewById(R.id.new_date);
        RadioGroup radioGroup = (RadioGroup) popupView.findViewById(R.id.radioGroup);

        final Calendar myCalendar = Calendar.getInstance();
        final Date[] data = new Date[1];
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                data[0] = myCalendar.getTime();
                dateTextView.setText(sdf.format(myCalendar.getTime()));
            }
        };

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ctx, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button add_new = (Button)  popupView.findViewById(R.id.button_add);
        Button add_new_rand = (Button)  popupView.findViewById(R.id.button_add_random);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
                true
        );
        popupWindow.showAtLocation(binding.scrollContainer.mainContainer, Gravity.CENTER,0,0);

        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int type;
                if (radioGroup.getCheckedRadioButtonId()==R.id.r1) type = 0;
                else if (radioGroup.getCheckedRadioButtonId()==R.id.r2) type = 2;
                else if (radioGroup.getCheckedRadioButtonId()==R.id.r3) type = 1;
                else type = 0;
                if(checkedFields(nameTextView, nipTextView, cityTextView, descriptionTextView, dateTextView)){
                    addNewCustomer(
                        nameTextView.getText().toString().substring(0, 1).toUpperCase()+nameTextView.getText().toString().substring(1).toLowerCase(),
                        nipTextView.getText().toString(),
                        cityTextView.getText().toString(),
                        descriptionTextView.getText().toString(),
                        data[0],
                        type
                    );
                popupWindow.dismiss();
                }
            }
        });

        add_new_rand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewRandomCustomers(1);
                popupWindow.dismiss();
            }
        });
    }

    // TODO: implemet text watchers and listeners
    public Boolean checkedFields(EditText nameTextView, EditText nipTextView, EditText cityTextView, EditText descriptionTextView, EditText dateTextView){
        if(nameTextView.getText().length()<1) {
            Toast.makeText(ctx,"please fill in the customer's name field", Toast.LENGTH_SHORT).show();
            return false;
        }
        if( nipTextView.getText().length()<10 ){
            Toast.makeText(ctx,"please fill in the customer's nip field \n(10 symbols)", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(cityTextView.getText().length()<1) {
            Toast.makeText(ctx,"please fill in the customer's city field", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(descriptionTextView.getText().length()<1) {
            Toast.makeText(ctx,"please fill in the description field", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(dateTextView.getText().length()<1) {
            Toast.makeText(ctx,"please select a date", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void addNewCustomer(String name, String nip, String city, String description,  Date date, int type){
        Customer customer = new Customer(
                name, nip, city, date, this.customerList.size()+1, type);
        this.customerList.add(customer);
        CustomerClassification custcl = new CustomerClassification(name,description, type, this.customerClassificationList.size()+1);
        this.customerClassificationList.add(custcl);
        DataBar data = new DataBar(ctx, name, date, type, customer, custcl);
        barArr.add(data);
        binding.scrollContainer.mainContainer.addView(data);
        sql.openDataBase();
        sql.addCustomerToDB(customer);
        sql.addCustomerClassificationToDB(custcl);
        sql.close();
    }
}
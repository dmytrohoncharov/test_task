package com.example.testapp.ui.sort;

import android.content.Context;
import android.os.Bundle;

import com.example.testapp.data.objects.Customer;
import com.example.testapp.data.objects.CustomerClassification;
import com.example.testapp.databinding.ActivityScrollingBinding;
import com.example.testapp.ui.models.DataBar;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        addNewCustomers(4);
        FloatingActionButton fab_add = binding.fabAdd;
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCustomers(1);
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

    private void addNewCustomers(int i){
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] alphabetArr = alphabet.toCharArray();
        Random random = new Random();

        for(int j=0; j<i; j++ ){
            String barName = alphabetArr[random.nextInt(alphabet.length())]+"-name";
            int randtype = ThreadLocalRandom.current().nextInt(0, 3);
           // SimpleDateFormat dfDateTime  = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            int year = randBetween(1995, 2020);
            int month = randBetween(0, 11);

            GregorianCalendar gc = new GregorianCalendar(year, month, 1);
            int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));

            gc.set(year, month, day);
            Date barDate = gc.getTime();

            Customer customer = new Customer(
                    barName,
                    Integer.valueOf(ThreadLocalRandom.current().nextInt(1000000000, 2100000000)).toString(),
                            alphabetArr[random.nextInt(alphabet.length())]+"-city",
                            barDate,
                            this.customerList.size()+1,
                            randtype);
            this.customerList.add(customer);

            CustomerClassification custcl = new CustomerClassification(barName, randtype);
            this.customerClassificationList.add(custcl);

            DataBar data = new DataBar(ctx, barName, barDate, randtype, customer, custcl);
            binding.scrollContainer.mainContainer.addView(data);
            barArr.add(data);
        }
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
}
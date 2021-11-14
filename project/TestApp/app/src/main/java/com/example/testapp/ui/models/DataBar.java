package com.example.testapp.ui.models;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.testapp.R;
import com.example.testapp.data.objects.Customer;
import com.example.testapp.data.objects.CustomerClassification;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataBar extends FrameLayout {

    private Context ctx;
    private LinearLayout layout;
    private TextView nameView;
    private TextView dateView;
    private LinearLayout.LayoutParams layoutParams;
    public String name;
    public Date date;
    public int type;
    private Customer customer;
    private CustomerClassification classification;
    private int clasId;

    public DataBar(Context context, String name, Date date, int type, int clas_id) {
        super(context);
        this.name = name;
        this.date = date;
        this.type = type;
        this.clasId = clas_id;
        this.ctx = context;

        this.layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.layoutParams.setMargins(30, 40, 30, 40);
        this.layout = new LinearLayout(context);
        this.layout.setGravity(LinearLayout.VERTICAL);

        this.nameView = new TextView(context);
        this.dateView = new TextView(context);

        this.setSingleInfo(nameView,name);
        this.setSingleInfo(dateView, new SimpleDateFormat("MM-dd-yyyy").format(date));

        int[] colors = {Color.LTGRAY, Color.BLUE, Color.RED};
        this.layout.setBackgroundColor(colors[type]);
        this.addView(layout);
    }

    public DataBar(Context context, String name, Date date, int type, Customer customer, CustomerClassification classification) {
        this(context, name, date, type, classification.getClassificationId());
        this.customer = customer;
        this.classification = classification;
        this.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDescription();
            }
        });
    }

    private void setSingleInfo(TextView view, String str){
        view.setText(str);
        view.setTextSize(25);
        view.setTextColor(Color.BLACK);
        layout.addView(view, layoutParams);
    }

    public String getName(){
        return this.name;
    }

    public int getType(){
        return this.type;
    }

    public Date getDate(){
        return this.date;
    }

    private void openDescription(){

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.description,null);

        TextView nameTextView = (TextView) customView.findViewById(R.id.name);
        nameTextView.setText("Name: "+this.customer.getName());

        TextView classificationTextView = (TextView) customView.findViewById(R.id.classificationId);
        classificationTextView.setText("ClassificationId: "+Integer.valueOf(this.classification.getClassificationId()).toString());

        TextView customerIdTextView = (TextView) customView.findViewById(R.id.customerId);
        customerIdTextView.setText("CustomerId: "+Integer.valueOf(this.customer.getCustomerId()).toString());

        TextView nipTextView = (TextView) customView.findViewById(R.id.nip);
        nipTextView.setText("NIP: "+this.customer.getNip());

        TextView cityTextView = (TextView) customView.findViewById(R.id.city);
        cityTextView.setText("City: "+this.customer.getCity());

        TextView dateTextView = (TextView) customView.findViewById(R.id.date);
        dateTextView.setText("Date: "+new SimpleDateFormat("MM-dd-yyyy").format(this.customer.getDate()));

        TextView descriptionTextView = (TextView) customView.findViewById(R.id.description);
        descriptionTextView.setText("Description: "+this.classification.getDescription());

        PopupWindow popupWindow = new PopupWindow(
                customView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                true
        );
        popupWindow.showAtLocation(this.layout, Gravity.CENTER,0,0);
    }
}
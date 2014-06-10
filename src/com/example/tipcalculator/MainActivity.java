package com.example.tipcalculator;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.tipcalculator.VenmoLibrary.VenmoResponse;

public class MainActivity extends Activity {
    EditText etTotalAmt2;
    TextView tvTotalTip2;
    TextView tvTotalAmount2;
    TextView tvTotalPerPersonAmt2;
    SeekBar sbNumPeople2;
    TextView tvNumPeople2;
    RadioGroup rgTip2;
    EditText etCustomTip2;
    Button sendVenmoPayment;
    
    double tipPercent = .1;
    int numPeople = 1;
    double totalAmt = 0;
    double customTip = 0;
    Double totalAmountPerPerson = 0.0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        etTotalAmt2 = (EditText) findViewById(R.id.etTotalAmt);
        etTotalAmt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (!s.toString().startsWith("$")) {
                    etTotalAmt2.setText("$" + s.toString());

                    int textLength = etTotalAmt2.getText().length();
                    etTotalAmt2.setSelection(textLength, textLength);
                }

                if (!s.toString().equals("$") && !s.toString().equals("")) {
                    String amtWithoutDollarSign = s.subSequence(1, s.length()).toString();
                    if (!amtWithoutDollarSign.equals("")) {
                        totalAmt = Double.parseDouble(amtWithoutDollarSign.toString()); 
                    }
                } else {
                    totalAmt = 0;
                }
                calculate();
                
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
//                if (!s.toString().startsWith("$")) {
//                    etTotalAmt2.setText("$" + s);
//                 }
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
                
            }
        });
        
        tvTotalTip2 = (TextView) findViewById(R.id.tvTotalTip);
        
        etCustomTip2 = (EditText) findViewById(R.id.etCustomTip);
        etCustomTip2.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (!s.toString().equals("")) {
                    customTip = Double.parseDouble(s.toString()) * .01;
                } else {
                    customTip = 0;
                }
                calculate();
                
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub
                
            }
        });
        
        rgTip2 = (RadioGroup) findViewById(R.id.rgTip);
        rgTip2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.tenPercent) {
                    tipPercent = .1;
                } else if (checkedId == R.id.fifteenPercent) {
                    tipPercent = .15;
                } else if (checkedId == R.id.twentyPercent) {
                    tipPercent = .2;
                }
                
                calculate();

            }

        });
        
        tvTotalAmount2 = (TextView) findViewById(R.id.tvTotalAmount);
        
        tvTotalPerPersonAmt2 = (TextView) findViewById(R.id.tvTotalPerPersonAmt);
        

        
        tvNumPeople2 = (TextView) findViewById(R.id.tvNumPeople);
        
        sbNumPeople2 = (SeekBar) findViewById(R.id.sbNumPeople);
        sbNumPeople2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                if (progress > 0) {
                    numPeople = progress;
                } else {
                    numPeople = 1;
                }
                tvNumPeople2.setText(numPeople + "");
                calculate();
                
            }
        });
        
//        
//        sendVenmoPayment = (Button) findViewById(R.id.venmoButton);
//        sendVenmoPayment.setOnClickListener(new OnClickListener() {
//            
//            @Override
//            public void onClick(View v) {
//                try { 
//                    Intent venmoIntent = VenmoLibrary.openVenmoPayment("1759", "Tip Calculator", "sandra", totalAmountPerPerson.toString(), "Payment", "charge"); 
//                    startActivityForResult(venmoIntent, 1); 
//                    //1 is the requestCode we are using for Venmo. Feel free to change this to another number. 
//                } catch (android.content.ActivityNotFoundException e) {
//                //Venmo native app not install on device, so let's instead open a mobile web version of Venmo in a WebView 
//                    Intent venmoIntent = new Intent(MainActivity.this, VenmoWebViewActivity.class); 
//                    String venmo_uri = VenmoLibrary.openVenmoPaymentInWebView("1759", "Tip Calculator", "sandra", totalAmountPerPerson.toString(), "Payment", "charge"); 
//                    venmoIntent.putExtra("url", venmo_uri); 
//                    startActivityForResult(venmoIntent, 1); 
//                }
//            }
//        });

        

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        TextView numPeopleLabel = (TextView) findViewById(R.id.numPeopleLabel);
        numPeopleLabel.setTypeface(face); 
        
        TextView tipLabel = (TextView) findViewById(R.id.tipLabel);
        tipLabel.setTypeface(face); 
        
        TextView totalLabel = (TextView) findViewById(R.id.totalLabel);
        totalLabel.setTypeface(face); 
        
        TextView tipTotalLabel = (TextView) findViewById(R.id.tipTotalLabel);
        tipTotalLabel.setTypeface(face); 
        
        TextView tvTotalPerPerson = (TextView) findViewById(R.id.tvTotalPerPerson);
        tvTotalPerPerson.setTypeface(face); 
        
        TextView billTotalLabel = (TextView) findViewById(R.id.billTotalLabel);
        billTotalLabel.setTypeface(face); 
        
        
        Typeface pacificoFace = Typeface.createFromAsset(getAssets(), "fonts/Pacifico.ttf");
        TextView customTipLabel = (TextView) findViewById(R.id.customTipLabel);
        customTipLabel.setTypeface(pacificoFace); 
        
       

    }
    
    private void calculate() {
        Double tip = tipPercent;
        if (customTip > 0) {
           tip = customTip; 
        }
    
        DecimalFormat priceFormat = new DecimalFormat("#0.00");
        Double totalTip = tip * totalAmt;
        Double totalAmount = (tip * totalAmt) + totalAmt;
        totalAmountPerPerson = totalAmount / numPeople;
        
        tvTotalTip2.setText("$" + priceFormat.format(totalTip));
        tvTotalAmount2.setText("$" + priceFormat.format(totalAmount));
        tvTotalPerPersonAmt2.setText("$" + priceFormat.format(totalAmountPerPerson));
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case 1: { //1 is the requestCode we picked for Venmo earlier when we called startActivityForResult
                if(resultCode == RESULT_OK) {
                    String signedrequest = data.getStringExtra("signedrequest");
                    if(signedrequest != null) {
                        VenmoResponse response = (new VenmoLibrary()).validateVenmoPaymentResponse(signedrequest, "e6NXVtrC6NKqFEKyyszEXt9UNcNVu5La");
                        if(response.getSuccess().equals("1")) {
                            //Payment successful.  Use data from response object to display a success message
                            String note = response.getNote();
                            String amount = response.getAmount();
                        }
                    }
                    else {
                        String error_message = data.getStringExtra("error_message");
                        //An error ocurred.  Make sure to display the error_message to the user
                    }                               
                }
                else if(resultCode == RESULT_CANCELED) {
                    //The user cancelled the payment
                }
            break;
            }           
        }
    }
}

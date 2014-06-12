package com.example.tipcalculator;

import java.text.DecimalFormat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    EditText mTotalAmtInput;
    TextView mTotalTip;
    TextView mTotalAmount;
    TextView mTotalPerPerson;
    SeekBar mNumPeople;
    TextView mNumPeopleText;
    RadioGroup mTipRadioGroup;
    EditText mCustomTip;
    
    RadioButton tipButton1;
    RadioButton tipButton2;
    RadioButton tipButton3;
    
    double tipPercent = 0;
    int numPeople = 1;
    double totalAmt = 0;
    double customTip = 0;
    Double totalAmountPerPerson = 0.0;
    
    private static final int RESULT_SETTINGS = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mTotalAmtInput = (EditText) findViewById(R.id.etTotalAmt);
        mTotalAmtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                if (!s.toString().startsWith("$")) {
                    mTotalAmtInput.setText("$" + s.toString());

                    int textLength = mTotalAmtInput.getText().length();
                    mTotalAmtInput.setSelection(textLength, textLength);
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
            }
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
            }
        });
        
        mTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
        
        mTotalPerPerson = (TextView) findViewById(R.id.tvTotalPerPersonAmt);
        
        setupTipOptions();

        setupNumPeople();
        
        setTypefaces();
        updateTips();
        calculate();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void setupTipOptions() {
        tipButton1 = (RadioButton) findViewById(R.id.tenPercent);
        tipButton2 = (RadioButton) findViewById(R.id.fifteenPercent);
        tipButton3 = (RadioButton) findViewById(R.id.twentyPercent);
        
        updateTips();
        
        getCurrentlySelectedTipAmt();
        
        mTipRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int tip = (int) (tipPercent * 10);
                if (checkedId == R.id.tenPercent) {
                    String buttonText = tipButton1.getText().toString();
                    tip = Integer.parseInt(buttonText.substring(0, buttonText.length() - 1));
                    tipPercent = tip * .01;
                } else if (checkedId == R.id.fifteenPercent) {
                    String buttonText = tipButton2.getText().toString();
                    tip = Integer.parseInt(buttonText.substring(0, buttonText.length() - 1));
                    
                } else if (checkedId == R.id.twentyPercent) {
                    String buttonText = tipButton3.getText().toString();
                    tip = Integer.parseInt(buttonText.substring(0, buttonText.length() - 1));
                }
                tipPercent =  tip * .01;
                calculate();
            }

        });
        
        mTotalTip = (TextView) findViewById(R.id.tvTotalTip);
        
        mCustomTip = (EditText) findViewById(R.id.etCustomTip);
        mCustomTip.addTextChangedListener(new TextWatcher() {
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
            }
        });
    }
    
    private void setupNumPeople() {
        mNumPeopleText = (TextView) findViewById(R.id.tvNumPeople);
        
        mNumPeople = (SeekBar) findViewById(R.id.sbNumPeople);
        mNumPeople.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                    boolean fromUser) {
                if (progress > 0) {
                    numPeople = progress;
                } else {
                    numPeople = 1;
                }
                mNumPeopleText.setText(numPeople + "");
                calculate();
            }
        });
    }
    
    private void openSettings() {
        Intent settingsIntent = new Intent(MainActivity.this, TipSettingsActivity.class);
        startActivityForResult(settingsIntent, RESULT_SETTINGS);    
    }
    
    private void setTypefaces() {
        Typeface bebasNeueFace = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        TextView numPeopleLabel = (TextView) findViewById(R.id.numPeopleLabel);
        numPeopleLabel.setTypeface(bebasNeueFace); 
        
        TextView tipLabel = (TextView) findViewById(R.id.tipLabel);
        tipLabel.setTypeface(bebasNeueFace); 
        
        TextView totalLabel = (TextView) findViewById(R.id.totalLabel);
        totalLabel.setTypeface(bebasNeueFace); 
        
        TextView tipTotalLabel = (TextView) findViewById(R.id.tipTotalLabel);
        tipTotalLabel.setTypeface(bebasNeueFace); 
        
        TextView tvTotalPerPerson = (TextView) findViewById(R.id.tvTotalPerPerson);
        tvTotalPerPerson.setTypeface(bebasNeueFace); 
        
        TextView billTotalLabel = (TextView) findViewById(R.id.billTotalLabel);
        billTotalLabel.setTypeface(bebasNeueFace); 
        
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
        
        mTotalTip.setText("$" + priceFormat.format(totalTip));
        mTotalAmount.setText("$" + priceFormat.format(totalAmount));
        mTotalPerPerson.setText("$" + priceFormat.format(totalAmountPerPerson));
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RESULT_SETTINGS:
                updateTips();
                getCurrentlySelectedTipAmt();
                calculate();
                break;
        }
    }
    
    private void getCurrentlySelectedTipAmt() {
        mTipRadioGroup = (RadioGroup) findViewById(R.id.rgTip);
        int firstCheckedButton = mTipRadioGroup.getCheckedRadioButtonId();
        RadioButton checkedButton = (RadioButton) findViewById(firstCheckedButton);
        String buttonText = checkedButton.getText().toString();
        int tip = Integer.parseInt(buttonText.substring(0, buttonText.length() - 1));
        tipPercent = tip * .01;
    }
    
    private void updateTips() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        int tip1 = Integer.parseInt(sharedPrefs.getString("tip1", "10"));
        tipButton1.setText(tip1 + "%");
 
        int tip2 = Integer.parseInt(sharedPrefs.getString("tip2", "15"));
        tipButton2.setText(tip2 + "%");
        
        int tip3 = Integer.parseInt(sharedPrefs.getString("tip3", "20"));
        tipButton3.setText(tip3 + "%");
    }
}

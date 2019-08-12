package com.example.android.wallmart;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Response;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;

public class Payment extends AppCompatActivity {

    AppCompatButton paytmButton;
    AppCompatEditText amountText;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        init();

        if (ContextCompat.checkSelfPermission(Payment.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Payment.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }

    }

    private void init() {
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        button = findViewById(R.id.button2);
        paytmButton = findViewById(R.id.paytmButton);
        amountText = findViewById(R.id.amountText);
        paytmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              processPaytm();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = editText2.getText().toString();
                String Number = editText3.getText().toString();
                String Address = editText4.getText().toString();
                Toast.makeText(Payment.this, "Details Saved", Toast.LENGTH_LONG).show();
            }
        });
       }



    private void processPaytm() {

        String custId = generateString();
        String orderId = generateString();
        String callBackurl = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID="+ orderId;


    final Paytm paytm = new Paytm("QNjNke31559853576068","WAP",amountText.getText().toString().trim(),
            "WEBSTAGING",callBackurl,"Retail",orderId,custId);


        WebServiceCaller.getClient().getChecksum(paytm.getmId(), paytm.getOrderId(), paytm.getCustId()
                , paytm.getChannelId(), paytm.getTxnAmount(), paytm.getWebsite(), paytm.getCallBackUrl(), paytm.getIndustryTypeId()
        ).enqueue(new Callback<Checksum>() {


            @Override
            public void onResponse(Call<Checksum> call, retrofit2.Response<Checksum> response) {
                if (response.isSuccessful()) {
                    processToPay(response.body().getChecksumHash(), paytm);
                }
            }

            @Override
            public void onFailure(Call<Checksum> call, Throwable t) {

            }
        });
        }

    private void processToPay(String checksumHash, Paytm paytm) {
        PaytmPGService Service = PaytmPGService.getStagingService();

        HashMap<String, String> paramMap = new HashMap<String,String>();
        paramMap.put( "MID" , paytm.getmId());
// Key in your staging and production MID available in your dashboard
        paramMap.put( "ORDER_ID" , paytm.getOrderId());
        paramMap.put( "CUST_ID" , paytm.getCustId());
        paramMap.put( "CHANNEL_ID" , paytm.getChannelId());                                              //Same login user, can't CHANGE
        paramMap.put( "TXN_AMOUNT" , paytm.getTxnAmount());
        paramMap.put( "WEBSITE" , paytm.getWebsite());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "INDUSTRY_TYPE_ID" , paytm.getIndustryTypeId());
// This is the staging value. Production value is available in your dashboard
        paramMap.put( "CALLBACK_URL", paytm.getCallBackUrl());
        paramMap.put( "CHECKSUMHASH" , checksumHash);
        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);

        Service.startPaymentTransaction(this, true, true, new PaytmPaymentTransactionCallback() {
            /*Call Backs*/
            public void someUIErrorOccurred(String inErrorMessage) {}
            public void onTransactionResponse(Bundle inResponse) {
                Toast.makeText(Payment.this, inResponse.toString(), Toast.LENGTH_SHORT).show();
            }
            public void networkNotAvailable() {}
            public void clientAuthenticationFailed(String inErrorMessage) {}
            public void onErrorLoadingWebPage(int iniErrorCode, String inErrorMessage, String inFailingUrl) {}
            public void onBackPressedCancelTransaction() {}
            public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {}
        });
    }


    private String generateString(){
        String uuid = UUID.randomUUID().toString();
        return uuid.replaceAll("-","");
    }

}

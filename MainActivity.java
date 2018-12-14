package nfc.destroy.fourthnfc;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";

    //getter strings
    public static String sendlocs, sendsevcs, sendnme;


    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;

    TextView tvNFCContent;
    TextView message;
    String usrname;
    String locations;
    String services;
    String pidtext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);     //coo
        setContentView(R.layout.activity_main); //coo
        context = this;
        pidtext = "";
        usrname = "";
        services = "";
        locations = "";

        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
        message = (TextView) findViewById(R.id.textView3);

        /*btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myTag == null) {
                        Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                    } else {
                        write(message.getText().toString(), myTag);
                        Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });*/


        //Instantiating the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        readFromIntent(getIntent());

        final String url = "https://stats.dev.hack.gt/api/user/" + pidtext;
        //message.setText(url);
        //Request a string response from the provided URL
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,url,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String name = response.getString("name");
                    tvNFCContent.setText(name);
                    usrname = name;
                    sendnme = usrname;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        final String url2 = "https://unique-aloe-220018.appspot.com/api/locations/";
        //locations
        StringRequest objectRequest2 = new StringRequest(com.android.volley.Request.Method.GET ,url2,new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                    locations = response2;
                    sendlocs = locations;
                   // message.setText(locations + "\n\n" + services +"\n\n" + usrname);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        final String url3 = "https://unique-aloe-220018.appspot.com/api/services/";
        //Services
        StringRequest objectRequest3 = new StringRequest(com.android.volley.Request.Method.GET ,url3,new Response.Listener<String>() {
            @Override
            public void onResponse(String response3) {
                services = response3;
                sendsevcs = services;
               // message.setText(locations + "\n\n" + services +"\n\n" + usrname);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // message.setText("FAIL);
            }
        });
        objectRequest3.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        final String url5 = "https://unique-aloe-220018.appspot.com/api/serviceRequests/";
        //Services
        StringRequest objectRequest5 = new StringRequest(com.android.volley.Request.Method.GET ,url5,new Response.Listener<String>() {
            @Override
            public void onResponse(String response5) {
                services = response5;
                tvNFCContent.setText(response5);
                message.setText(services);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //message.setText("Gosh Darnded it");
            }
        });
        objectRequest5.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        String url4 = "https://unique-aloe-220018.appspot.com/api/serviceRequests/";
//
//        Map<String, String> params = new HashMap();
//        params.put("nfcID", pidtext);
//        params.put("location", LocationActivity.sending_this);
//
//        JSONObject parameters = new JSONObject(params);
//
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                message.setText("IT WORKED");
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                error.printStackTrace();
//                message.setText("IT FAILED");
//            }
//        });

//        Volley.newRequestQueue(this).add(jsonRequest);
        final String url4 = "https://unique-aloe-220018.appspot.com/api/serviceRequests/";
        //service request
        StringRequest postRequest4 = new StringRequest(Request.Method.POST, url4,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response4) {
                        String locations = response4;
                       // message.setText("response " + response4);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                       // message.setText("Error.Response" + error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                String temp = pidtext;
                String temp2 = LocationActivity.sending_this;
                //message.setText(temp + "    " + temp2);
                params.put("nfcID", "HELLOW");
                params.put("location", "JOSHES");
                return params;
            }
        };
        objectRequest2.setRetryPolicy(new DefaultRetryPolicy(
                100000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Add the request to the RequestQueue
        queue.add(objectRequest);
        queue.add(objectRequest2);
        queue.add(objectRequest3);
        queue.add(objectRequest5);
        //queue.add(postRequest4);

       // message.setText("asdfghjkl");

        //message.setText(pidtext);

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};

        Intent myIntent = new Intent(this, LocationActivity.class);
        startActivity(myIntent);
    }


    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }

        text = text.substring(text.indexOf("=") + 1);

        tvNFCContent.setText("NFC Content: " + text);
        pidtext = text;
    }



    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }
}





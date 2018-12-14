package nfc.destroy.fourthnfc;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LocationActivity extends AppCompatActivity {
    Context context;

    public static String sending_this; // sending this to activities ahead
    public static String locsStrings;
    public static String[] sendingIDs = new String[4];
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        final Button b1, b2, b3, b4;
        final EditText eT2, WT;
        eT2 = (EditText) findViewById(R.id.editText2);
        WT = (EditText) findViewById(R.id.WelcomeText);
        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);

        context = this;

        RequestQueue queue = Volley.newRequestQueue(context);

        final String url2 = "https://unique-aloe-220018.appspot.com/api/locations/";
        //locations
        StringRequest objectRequest2 = new StringRequest(com.android.volley.Request.Method.GET, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response2) {
                if(locsStrings == null) {
                    locsStrings = response2;
                }

                // code to parse

                String places[] = locsStrings.split(",");
                int j = 0;
                String putinButtons[] = new String[4];

                for (int i = 0; i < places.length; i = i + 2) {
                    putinButtons[j] = places[i+1].substring(places[i+1].indexOf(":") + 2, places[i+1].length()-2);
                    sendingIDs[j] = places[i].substring(places[i].indexOf(":") + 2, places[i].length()-2);
                    j++;
                }
                putinButtons[3] = putinButtons[3].substring(0,putinButtons[3].length()-1);
                b1.setText(putinButtons[0]);
                b2.setText(putinButtons[1]);
                b3.setText(putinButtons[2]);
                b4.setText(putinButtons[3]);
               // eT2.setEditableFactory(false);
               WT.setText("Welcome " + MainActivity.sendnme);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(objectRequest2);


        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sending_this = sendingIDs[0];
                // your handler code here
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sending_this = sendingIDs[1];
                // your handler code here
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sending_this = sendingIDs[2];
                // your handler code here
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sending_this = sendingIDs[3];
                // your handler code here
            }
        });


    } //end of onCreate()
    protected void onResume() {
        super.onResume();
        EditText WT = (EditText) findViewById(R.id.WelcomeText);
        WT.setText("Welcome " + MainActivity.sendnme);
    }
}
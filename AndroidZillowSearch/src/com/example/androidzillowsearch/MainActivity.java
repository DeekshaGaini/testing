package com.example.androidzillowsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
Intent intent;
public static JSONObject jsonObj;
	public static String jsonStr;
	String[] state= new String[60]; 
	private ProgressDialog pDialog;
	 String message;
	 String message2;
	 String message3;
    // URL to get contacts JSON
    private static String url = "http://cs-server.usc.edu:22917/t1t2.php";
    JSONArray property= null;
    ArrayList<HashMap<String, String>> propertySearch;
   
   TextView nomatch1;
    @Override
    protected void onCreate(Bundle savedInstancstateErr) {
        super.onCreate(savedInstancstateErr);
        setContentView(R.layout.activity_main);
        propertySearch = new ArrayList<HashMap<String, String>>();
       
        
        state[0]="Choose State";
        state[1]="AK";
        state[2]="AL";
        state[3]="AR";
        state[4]="AZ";
        state[5]="CA";
        state[6]="CO";
        state[7]="CT";
        state[8]="DC";
        state[9]="DE";
        state[10]="FL";
        state[11]="GA";
        state[12]="HI";
        state[13]="IA";
        state[14]="ID";
        state[15]="IL";
        state[16]="IN";
        state[17]="KS";
        state[18]="KY";
        state[19]="LA";
        state[20]="MA";
        state[21]="MD";
        state[22]="ME";
        state[23]="MI";
        state[24]="MN";
        state[25]="MO";
        state[26]="MS";
        state[27]="MT";
        state[28]="NC";
        state[29]="ND";
        state[30]="NE";
        state[31]="NH";
        state[32]="NJ";
        state[33]="NM";
        state[34]="NV";
        state[35]="NY";
        state[36]="OH";
        state[37]="OK";
        state[38]="OR";
        state[39]="PA";
        state[40]="RI";
        state[41]="SC";
        state[42]="SD";
        state[43]="TN";
        state[44]="TX";
        state[45]="UT";
        state[46]="VA";
        state[47]="VT";
        state[48]="WA";
        state[49]="WI";
        state[50]="WV";
        state[51]="WY";

        Spinner s = (Spinner)findViewById(R.id.statespinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, state);
        s.setAdapter(adapter); 
        
    
    final Button button = (Button) findViewById(R.id.search);
    button.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            // Perform action on click
        	
        	try{
        		
        		nomatch1=(TextView)findViewById(R.id.nomatch);
        		 //intent = new Intent(getApplicationContext(), DisplayProperty.class);
        	EditText addval = (EditText) findViewById(R.id.addet);
        	 message = addval.getText().toString();
        	//intent.putExtra("addressvalue", message);
        	EditText cityval = (EditText) findViewById(R.id.cityet);
        	 message2 = cityval.getText().toString();
        //intent.putExtra("cityvalue", message2);
        	 Spinner state = (Spinner) findViewById(R.id.statespinner);
        	 
              message3 = state.getSelectedItem().toString();
              TextView addressErr=(TextView)findViewById(R.id.addresserrtv);
              TextView cityErr=(TextView)findViewById(R.id.cityerrtv);
              TextView stateErr=(TextView)findViewById(R.id.stateerrtv);
             //intent.putExtra("statevalue", message3);
              if ((message.trim()).equals("") && (message2.trim()).equals("") && (message3).equals("Choose State")) {
                  //Show error message for all three
                  addressErr.setText("This field is required");
                  cityErr.setText("This field is required");
                  stateErr.setText("This field is required");
                  nomatch1.setText(" ");

              } else if ((message.trim()).equals("") && (message2.trim()).equals("") && !((message3).equals("Choose State"))) {
                  //show error for street and city
                  addressErr.setText("This field is required");
                  cityErr.setText("This field is required");
                  stateErr.setText("");
                  nomatch1.setText(" ");

              } else if (!((message.trim()).equals("")) && (message2.trim()).equals("") && (message3).equals("Choose State")) {
                  //show error for city and state
                  addressErr.setText("");
                  cityErr.setText("This field is required");
                  stateErr.setText("This field is required");
                  nomatch1.setText(" ");

              } else if ((message.trim()).equals("") && !((message2.trim()).equals("")) && (message3).equals("Choose State")) {
                  //show error for street and state
                  addressErr.setText("This field is required");
                  cityErr.setText("");
                  stateErr.setText("This field is required");
                  nomatch1.setText(" ");

              } else if (!((message.trim()).equals("")) && !((message2.trim()).equals("")) && (message3).equals("Choose State")) {
                  //show error for state
                  addressErr.setText("");
                  cityErr.setText("");
                  stateErr.setText("This field is required");
                  nomatch1.setText(" ");

              } else if (!((message.trim()).equals("")) && (message2.trim()).equals("") && !((message3).equals("Choose State"))) {
                  //show error for city
                  addressErr.setText("");
                  cityErr.setText("This field is required");
                  stateErr.setText("");
                  nomatch1.setText(" ");

              } else if ((message.trim()).equals("") && !((message2.trim()).equals("")) && !((message3).equals("Choose State"))) {
                  //show error for street
                  addressErr.setText("This field is required");
                  cityErr.setText("");
                  stateErr.setText("");
                  nomatch1.setText(" ");

              } else {
        	Toast toast1 = Toast.makeText(getApplicationContext(), message+message2+message3, Toast.LENGTH_SHORT);
            toast1.show();
           // new GetProperty().execute();
            new GetProperty().execute();
        	}
            /*Toast toast2 = Toast.makeText(getApplicationContext(), jsonStr, Toast.LENGTH_SHORT);
            toast2.show();*/
            //intent.putExtra("jsonstring", jsonObj.toString());
            //startActivity(intent);
            //  startActivity(intent);
             
        	}catch(Exception e){e.setStackTrace(null);}
        }
    });
    
}
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetProperty extends AsyncTask<Void, Void, Void> {
    	 
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
 
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            JsonHandler jh = new JsonHandler();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
            params.add(new BasicNameValuePair("address",message));
        	params.add(new BasicNameValuePair("city",message2));
        	params.add(new BasicNameValuePair("state",message3));
        	
            // Making a request to url and getting response
            jsonStr = jh.makeServiceCall(url, JsonHandler.GET, params);
            
            
            Log.d("Response: ", "> " + jsonStr);
 
            if (jsonStr != null) {
                try {
                     jsonObj = new JSONObject(jsonStr);
                     Log.d("ans",jsonObj.toString());
                     //Intent intent = new Intent(getApplicationContext(), DisplayProperty.class);

                    
                     
                     }
                catch (JSONException e) {
                    e.printStackTrace();
                }catch(Exception e){
                	e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
 
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
           // super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            try{
            	String s =jsonObj.getString("errorcode");
            	
            	if(s.equals("No exact match found--Verify that the given address is correct"))
    			{
    		Log.d("errorcode",jsonObj.getString("errorcode"));
    		 nomatch1=(TextView)findViewById(R.id.nomatch);
    		 TextView addressErr=(TextView)findViewById(R.id.addresserrtv);
             TextView cityErr=(TextView)findViewById(R.id.cityerrtv);
             TextView stateErr=(TextView)findViewById(R.id.stateerrtv);
             addressErr.setText(" ");
             cityErr.setText(" ");
             stateErr.setText(" ");
    		nomatch1.setText("No exact match found--Verify that the given address is correct");
    			}
       // new GetProperty().execute();
    	else {
    		nomatch1=(TextView)findViewById(R.id.nomatch);	
		nomatch1.setText(" ");
         intent = new Intent(getApplicationContext(), DisplayProperty.class);
        	
        	intent.putExtra("addressvalue", message);
        	
        intent.putExtra("cityvalue", message2);
        	 
            intent.putExtra("statevalue", message3);
        	
        	
           // new GetProperty().execute();
            
            /*Toast toast2 = Toast.makeText(getApplicationContext(), JsonHandler.url, Toast.LENGTH_SHORT);
            toast2.show();*/
            intent.putExtra("jsonstring", jsonStr);
            startActivity(intent);
             // startActivity(intent);
             
    	}}catch(Exception e){e.printStackTrace();}
            /**
             * Updating parsed JSON data into ListView
             * */
           /* ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, contactList,
                    R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
                            TAG_PHONE_MOBILE }, new int[] { R.id.name,
                            R.id.email, R.id.mobile });
 
            setListAdapter(adapter);
 */       }
 
    }

}

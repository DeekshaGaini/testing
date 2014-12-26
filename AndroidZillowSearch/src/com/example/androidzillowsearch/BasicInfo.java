package com.example.androidzillowsearch;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
 
public class BasicInfo extends Fragment  {
 
	
	ListView lv;
    Context context;
 String x[]=new String[16];
  String keys[]= new String[16];
  String fbname= new String();
    ArrayList prgmName;
    public String fbName()
    {
    	String s= new String();
    	try{
    		 s = MainActivity.jsonObj.getString("street")+","+MainActivity.jsonObj.getString("city")+","+MainActivity.jsonObj.getString("state")+"-"+MainActivity.jsonObj.getString("zipcode");
    	}catch(Exception e){e.printStackTrace();}
    	return s;
    }
    public String output()
    {
    	String s= new String();
    	try{
    		 s = MainActivity.jsonObj.getString("sign")+MainActivity.jsonObj.getString("valueChange");
    	}catch(Exception e){e.printStackTrace();}
    	return s;
    }
    public String link()
    {
    	String s= new String();
    	try{
    		 s = MainActivity.jsonObj.getString("homedetails");
    	}catch(Exception e){e.printStackTrace();}
    	return s;
    }
    public String picture()
    {
    	String s= new String();
    	try{
    		 s = MainActivity.jsonObj.getString("year1");
    	}catch(Exception e){e.printStackTrace();}
    	return s;
    }
    public String[] jsonValues(){
    	try{
    		 x[0]=MainActivity.jsonObj.getString("usecode");
    		 x[1]=MainActivity.jsonObj.getString("yearBuilt");
    		 x[2]=MainActivity.jsonObj.getString("lotSizeSqFt");
    		 x[3]=MainActivity.jsonObj.getString("finishedSqFt");
    		 x[4]=MainActivity.jsonObj.getString("bathrooms");
    		 x[5]=MainActivity.jsonObj.getString("bedrooms");
    		 x[6]=MainActivity.jsonObj.getString("taxAssessmentYear");
    		 x[7]=MainActivity.jsonObj.getString("taxAssessment");
    		 x[8]=MainActivity.jsonObj.getString("lastSoldPrice");
    		 x[9]=MainActivity.jsonObj.getString("lastSoldDate");
    		 x[10]=MainActivity.jsonObj.getString("estimateAmount");
    		 x[11]=MainActivity.jsonObj.getString("valueChange");
    		 x[12]=MainActivity.jsonObj.getString("allTimePropertyRange");
    		 x[13]=MainActivity.jsonObj.getString("restimateAmount");
    		 x[14]=MainActivity.jsonObj.getString("rvalueChange");
    		 x[15]=MainActivity.jsonObj.getString("allTimeRentRange");
    		 
    		 
    		 
    	}catch(JSONException e){e.printStackTrace();}
    	return x;
    }
    public String[] jsonKeys(){
    try{
    	keys[0]="Property type";
    	keys[1]="Year Built";
    	keys[2]="Lot Size";
    	keys[3]="Finished Area";
    	keys[4]="Bathrooms";
    	keys[5]="Bedrooms";
    	keys[6]="Tax Assessment Year";
    	keys[7]="Tax Assessment";
    	keys[8]="Last Sold Price";
    	keys[9]="Last Sold Date";
    	keys[10]="Zestimate � Property Estimate as of "+MainActivity.jsonObj.getString("estimateLastUpdate");
    	keys[11]="30 Days Overall Change";
    	keys[12]="All Time Property Change";
    	keys[13]="Rent Zestimate Valuation as of "+MainActivity.jsonObj.getString("restimateLastUpdate");
    	keys[14]="30 Days Rent Change";
    	keys[15]="All Time Rent Range";
    	  
    }catch(Exception e){e.printStackTrace();}
    return keys;
    }
   
    //public static String [] values={"c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
   
   
   
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {
 //Log.e("value of home details",x);
 x=jsonValues();
 keys=jsonKeys();
        View rootView = inflater.inflate(R.layout.activity_basic_info, container, false);
        context= rootView.getContext();
        TextView tv= (TextView)rootView.findViewById(R.id.details);
        tv.setText(Html.fromHtml("<a href="+link() +">"+fbName()+"</a> "));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        lv=(ListView) rootView.findViewById(R.id.listView);
        lv.setAdapter(new CustomAdapter(this, keys,x));
        ImageView b= (ImageView) rootView.findViewById(R.id.facebookimg);
		b.setImageResource(R.drawable.share_button);
		 b.setOnClickListener(new View.OnClickListener() {
	            @SuppressWarnings("deprecation")
	            public void onClick(View v) {
	                
	                
	                // Perform action on click
	                String APP_ID = "749771781737650";
	            //    String APP_ID = getString(R.string.APP_ID);
	                final Facebook fb= new Facebook(APP_ID);
	                
	                if(fb.isSessionValid())
	                {
	                    Log.v("hello", APP_ID);    
	                    try {
	                        fb.logout(v.getContext());
	                    } catch (MalformedURLException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    } catch (IOException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                }
	                else
	                {
	                    fb.authorize((Activity) v.getContext(), new Facebook.DialogListener() {
	                        
	      
	                        
	                        @Override
	                        public void onFacebookError(FacebookError e) {
	                            // TODO Auto-generated method stub
	                            Toast.makeText(container.getContext(), "fberror", Toast.LENGTH_LONG).show();

	                        }
	                        
	                        @Override
	                        public void onError(DialogError e) {
	                            // TODO Auto-generated method stub
	                            Toast.makeText(container.getContext(), "onerror", Toast.LENGTH_LONG).show();

	                        }
	                        
	                        @Override
	                        public void onComplete(Bundle values) {
	                            // TODO Auto-generated method stub
	                            Toast.makeText(container.getContext(), "complete", Toast.LENGTH_LONG).show();
	    
	                        }
	                        
	                        @Override
	                        public void onCancel() {
	                            // TODO Auto-generated method stub
	                            Toast.makeText(container.getContext(), "cancelled", Toast.LENGTH_LONG).show();
	                        }
	                    });
	                
	                
	                
	                    Bundle params = new Bundle();
	                    
	                    params.putString("name",fbName());
	                    params.putString("caption","Property information from Zillow.com");
	                    params.putString("description","Last sold price :"+ x[8]+"\n 30 day overall change "+ output());
	                    params.putString("link",link());
	                    params.putString("picture",picture());
	                    
	    fb.dialog(container.getContext(), "feed", params,new DialogListener() {
	                        
	                        @Override
	                        public void onFacebookError(FacebookError e) {
	                            // TODO Auto-generated method stub
	                            
	                        }
	                        
	                        @Override
	                        public void onError(DialogError e) {
	                            // TODO Auto-generated method stub
	                            
	                        }
	                        
	                        @Override
	                        public void onComplete(Bundle values) {
	                            // TODO Auto-generated method stub
	                            Toast.makeText(container.getContext(), "Completed!", Toast.LENGTH_LONG).show();

	                        }
	                        
	                        @Override
	                        public void onCancel() {
	                            // TODO Auto-generated method stub
	                        	Toast.makeText(container.getContext(), "cancelled!", Toast.LENGTH_LONG).show();
	                        }
	                    });
	                    
	                
	                
	                
	                
	                
	                }
	            }
	        });

        
     
        return rootView;
        
    }

    
}
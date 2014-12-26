package com.example.androidzillowsearch;


import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewSwitcher;
 
public class HistoricalZestimates extends Fragment {

	Button buttonNext, buttonPrevious;
	public static ImageSwitcher imageSwitcher;
	AsyncCaller asyn, async;
    Animation slide_in_left, slide_out_right;
    public static String images[];
    public static int counter=0;
    public static Drawable drawable;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
    	ScrollView sc = new ScrollView(getActivity());
        try {
        	images= new String[3];
            images[0] = MainActivity.jsonObj.getString("year1");
            images[1] = MainActivity.jsonObj.getString("year5");
            images[2] = MainActivity.jsonObj.getString("year10");
        }catch (Exception e){
           e.printStackTrace();
        }
        LinearLayout l = new LinearLayout(getActivity());
        l.setOrientation(LinearLayout.VERTICAL);

        buttonNext = new Button(getActivity());
        buttonNext.setText("Next");
        buttonPrevious = new Button(getActivity());
        buttonPrevious.setText("Previous");

        imageSwitcher = new ImageSwitcher(getActivity());
          	 
        slide_in_left = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_in_left);
        slide_out_right = AnimationUtils.loadAnimation(getActivity(),
                android.R.anim.slide_out_right);

        imageSwitcher.setInAnimation(slide_in_left);
        imageSwitcher.setOutAnimation(slide_out_right);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
            	
                ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                FrameLayout.LayoutParams params = new ImageSwitcher.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                imageView.setLayoutParams(params);
                return imageView;
            }
        });
        
        AsyncCaller async = new AsyncCaller();
        async.execute();
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                imageSwitcher.setInAnimation(slide_out_right);
                imageSwitcher.setOutAnimation(slide_in_left);

                if (counter == images.length - 1) {
                    counter = (images.length - 1);
                } else {
                    counter++;
                    asyn = new AsyncCaller();
                    asyn.execute();
                }
            }
        });
        buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (counter == 0) {
                
                    counter = 0;
                                    } else {
                    counter--;
                    asyn = new AsyncCaller();
                    asyn.execute();

                }
            }
        });
        l.addView(imageSwitcher);
        l.addView(buttonPrevious);
        l.addView(buttonNext);
        return l;
    }


private static class AsyncCaller extends AsyncTask<Void, Void, Void>
{
    String line;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //this method will be running on UI thread
    }
    @Override
    protected Void doInBackground(Void... params) {
        //this method will be running on background thread so don't update UI frome here
        //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
    try {
        URL url = new URL(images[counter]);
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        drawable =new BitmapDrawable(bmp);
    }catch(Exception e){
        Log.i("After the URL code", e+"");
    }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        //this method will be running on UI thread
 //       imageSwitcher.setImageResource(R.drawable.grid);
        imageSwitcher.setImageDrawable(drawable);
    }
}


}


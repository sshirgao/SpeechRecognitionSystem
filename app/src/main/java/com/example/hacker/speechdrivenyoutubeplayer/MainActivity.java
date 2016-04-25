package com.example.hacker.speechdrivenyoutubeplayer;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

import ai.api.AIConfiguration;
import ai.api.AIListener;
import ai.api.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;




public class MainActivity extends AppCompatActivity implements AIListener {
    public boolean flashlight_state = false;
    @Override
    public void onResult(AIResponse result) {
        Log.i("APIAI","Got Result");
        String temp = ""+result.getResult().getResolvedQuery();
        temp = temp.substring(temp.lastIndexOf("Action:")+1);
        Log.i("APIAI", temp);
        TextView txtv = (TextView) findViewById(R.id.informative);
        txtv.setText(temp);
        String[] comlist = new String[4];
        comlist = temp.split(" ");
        if (temp.toLowerCase().contains("play"))
        {
            if (temp.toLowerCase().contains("eagles"))
            {
                this.open_Youtube("HC7LkM2po7M");
//                this.call_youtube_api(comlist[1]);
            }
            else if (temp.toLowerCase().contains("country"))
            {
                this.open_Youtube("JCjXaEbrLdw");
//                this.call_youtube_api(comlist[1]);
            }
            else if (temp.toLowerCase().contains("pop"))
            {
                this.open_Youtube("N_Lbhv0xUY4");
//                this.call_youtube_api(comlist[1]);
            }
            else if (temp.toLowerCase().contains("latin"))
            {
                this.open_Youtube("9mQJaXwGPlg");
//                this.call_youtube_api(comlist[1]);
            }
        }
        else if(temp.toLowerCase().contains("exit"))
        {
            this.exit_app();
        }
        else if(temp.toLowerCase().contains("navigate"))
        {
//            this.open_gmaps();
            String share_string = temp.replaceAll("navigate","");
            share_string = share_string.replaceAll("to","");
            this.navigate_to(share_string);
        }
        else if(temp.toLowerCase().contains("share"))
        {
            String share_string = temp.replaceAll("share","");
            this.share_text(share_string);
        }
        else if(temp.toLowerCase().contains("date"))
        {
            this.make_toast();
        }
        else if(temp.toLowerCase().contains("settings"))
        {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
        }
        else if(temp.toLowerCase().contains("flashlight"))
        {
            this.start_flashlight();
        }
        else if(temp.toLowerCase().contains("call"))
        {
            this.open_dialer();
        }
        else if(temp.toLowerCase().contains("search"))
        {
            String share_string = temp.replaceAll("search","");
            share_string = temp.replaceAll("for","");
            this.open_chrome(share_string);
        }
        else if(temp.toLowerCase().contains("selfie"))
        {
            this.open_selfie_camera();
        }














    }

    @Override
    public void onError(AIError error) {
        Log.i("APIAI", "Error: " + error.getMessage());
        TextView txtv = (TextView) findViewById(R.id.informative);
        txtv.setText("Please try again");
    }

    public void open_selfie_camera()
    {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.rcplatform.selfiecamera");
        startActivity(launchIntent);
    }

    public void navigate_to(String text)
    {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q="+text));
        startActivity(intent);
    }


    @Override
    public void onAudioLevel(float level) {
//        Log.i("APIAI", String.valueOf(level));
    }

    public void open_chrome(String text)
    {

        try {
            Intent i = new Intent("android.intent.action.MAIN");
            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
            i.addCategory("android.intent.category.LAUNCHER");
            String url = "https://www.google.com/search?q="+text;
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        catch(ActivityNotFoundException e) {
            // Chrome is not installed
            String url = "https://www.google.com/search?q="+text;
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
        }
    }
    @Override
    public void onListeningStarted() {
        Log.i("APIAI", "Listening Now");
    }

    @Override
    public void onListeningCanceled() {
        Log.i("APIAI", "Listening Cancelled");
    }

    @Override
    public void onListeningFinished() {
        Log.i("APIAI", "Listening Finished");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        final AIConfiguration config = new AIConfiguration("7ffa32a4d13d4c8f95308dbccc364f77",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);

        final AIService aiService = AIService.getService(this, config);
        aiService.setListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View v){
//                open_Youtube(v);
                aiService.startListening();
            }
        });


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                aiService.stopListening();
//                Log.i("APIAI", "Listening Stopped");
//            }
//        }, 10000);


    }

    public void open_Youtube(String vid)
    {
        Intent intent = new Intent(this,YouTubeActivity.class);
        intent.putExtra("URL",vid);
        startActivity(intent);
    }

    public void open_gmaps()
    {
        Intent intent = new Intent(this,GoogleMaps.class);
        Log.i("APIAI","Starting GMAPS");
        startActivity(intent);
    }

    public void open_dialer()
    {
        Uri number = Uri.parse("tel:4804344017");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }



    public void exit_app()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void share_text(String text)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,text);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void make_toast()
    {
        DateFormat df = DateFormat.getDateInstance();
        df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = df.format(new Date());
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, gmtTime, duration);
        toast.show();
    }

    public void start_flashlight()
    {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        this.flashlight_state = !this.flashlight_state;
        try {
            Log.i("APIAI",String.valueOf(manager.getCameraIdList()[0]));
            Log.i("APIAI",String.valueOf(manager.getCameraIdList()[1]));
        }catch (Exception e){}
        try {
            manager.setTorchMode("0",this.flashlight_state);
        }catch (Exception e){}


    }


    private void call_youtube_api(String q)
    {
        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;
        try {
            url = new URL("https://www.googleapis.com/youtube/v3/search/?part=snippet&q="+q);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            String temp, response = "";
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }
            object = (JSONObject) new JSONTokener(response).nextValue();
            Log.i("APIAI",object.toString());
        } catch (Exception e) {
            Log.i("APIAI",e.getLocalizedMessage());
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

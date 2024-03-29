package com.example.helloworld;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	private static final String DEBUG_TAG = "HttpExample";
    private TextView textView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button connect = (Button) findViewById(R.id.Connect);
        textView = (TextView) findViewById(R.id.textView1);
        
        connect.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myClickHandler(v);
				
			}
		});
       
    }
    public void myClickHandler(View view) {
        // Gets the URL from the UI's text field.

        //String stringUrl = R.string.;
        ConnectivityManager connMgr = (ConnectivityManager) 
            getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
        	new DownloadWebpageTask().execute("http://10.0.2.2/AndroidJSON/index.php");
        } else {
        	textView.setText("No network connection available.");
        }	
    }	//end of myClickHandler
    
    class DownloadWebpageTask extends AsyncTask<String, Void, String> {	//Asyntask is loaded within the ui thread
        @Override
        protected String doInBackground(String... urls) {
              
            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            textView.setText(result);
       }
    }
    
    protected String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;
            
        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return JsonUtil.convertJson(contentAsString);
            
        // Makes sure that the InputStream is closed after the app is
        // finished using it.X
        } finally {
            if (is != null) {
                is.close();
            } 
        }
    }
    
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");        
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }	//potential none ui thread item.
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
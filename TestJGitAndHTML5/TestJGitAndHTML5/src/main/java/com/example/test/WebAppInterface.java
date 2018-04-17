package com.example.test;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;

public class WebAppInterface {
	Context mContext;

    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    
    /** Try...public void evaluateJavascript (String script, ValueCallback<String> resultCallback) within method */
    @JavascriptInterface	
    public String setVar(String x) {
    	return x;
    }
    

    /*Parameters: content: String to be written to file
     * 			file: file name to write to
     * If the file exists, the entire file will be
     * overwritten with the String content,
     * otherwise a new file will be created with the content.
     */
    @JavascriptInterface	
    public void saveToFile(String content, String file) {
    	//File file = new File(mContext.getFilesDir(), "preferences.txt");
    	
    	FileOutputStream outputStream;

    	try {
          android.util.Log.i("saveToFile(): ", content);
    	  outputStream = mContext.openFileOutput(file, Context.MODE_PRIVATE);
    	  outputStream.write(content.getBytes());
    	  outputStream.close();
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    	
    }
    
    //used in obsTpage4.htm line 97
    //	and data.js line 12
    @JavascriptInterface	
    public String loadFromFile(String filename) {
    	File file = new File(mContext.getFilesDir(), filename);
    	
    	String temp="";
    	try {
      	  FileInputStream fin = mContext.openFileInput(filename);
      	  int c;
      	  while( (c = fin.read()) != -1){
      	   temp = temp + Character.toString((char)c);
      	  }
      	  //string temp contains all the data of the file.
      	  fin.close();
      	  
    	} catch (Exception e) {
    	  e.printStackTrace();
    	}
    	return "from Android.loadFromFile(): " + temp;
    }
    
}

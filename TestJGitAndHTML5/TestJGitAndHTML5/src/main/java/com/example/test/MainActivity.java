package com.example.test;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {

	private WebView mWebView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mWebView = (WebView) findViewById(R.id.activity_main_webview);
		
		// Enable Javascript
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		
		//set webview to be only utf-8
		//set webview's dom to handle textarea
		//prevent overlay of html elements that interfere with each other
		//mWebView.getSettings().setUseWideViewPort(true);
		
		// bind Javascript to Java method
		mWebView.addJavascriptInterface(new WebAppInterface(this), "Android");
		
		mWebView.loadUrl("file:///android_asset/www/obsTpage1.htm");
		
		/* WebViewClient must be set BEFORE calling loadUrl! 
		mWebView.setWebViewClient(new WebViewClient() {
		    @Override
		    public void onPageFinished(WebView view, String url)
		    {
		        mWebView.loadUrl("javascript:(function() { " +
		                "document.getElementsByTagName('body')[0].style.color = 'green'; " +
		                "})()");
		        //mWebView.loadUrl("JavaScript: x = 'good bye';"); //doesn't work
		    }
		});
		*/
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

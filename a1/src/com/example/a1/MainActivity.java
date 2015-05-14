package com.example.a1;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import cn.sharesdk.js.ShareSDKUtils;

public class MainActivity extends Activity {
private WebView webView;
private EditText Ev1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WebView wvBody = new WebView(this);
		
		wvBody.getSettings().setDomStorageEnabled(true);   
		wvBody.getSettings().setDatabaseEnabled(true);
		wvBody.getSettings().setAllowFileAccess(true);  
		WebViewClient wvClient = new WebViewClient();
		wvBody.setWebViewClient(wvClient);
		wvBody.setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		
		// you must call the following line after the webviewclient is set into the webview
		ShareSDKUtils.prepare(wvBody, wvClient);
		//wvBody.getSettings().setJavaScriptEnabled(true);
		//wvBody.addJavascriptInterface(new JavaScriptInterface(), "ncp");  
		setContentView(wvBody);
		wvBody.loadUrl("http://192.168.43.168:8080/cf/index.html");
	}
	
	final class JavaScriptInterface {   
		  
	       public int callOnJs() {  
	                          
	          return 1000;  
	       }  
	       @JavascriptInterface
	       public void callOnJs2(String mode) {  
	         //TODO  
	    	   System.out.println("callonjs2222");
	       }  
	     
	  } 
}

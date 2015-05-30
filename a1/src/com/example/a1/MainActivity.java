package com.example.a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.sharesdk.js.ShareSDKUtils;

public class MainActivity extends Activity {
private WebView webView;
private MediaPlayer mediaPlayer = null; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WebView wvBody = new WebView(this);
		
		wvBody.getSettings().setDomStorageEnabled(true);   
		wvBody.getSettings().setDatabaseEnabled(true);
		wvBody.getSettings().setAllowFileAccess(true);  
		wvBody.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
		//wvBody.getSettings().setSupportZoom(false);   
		WebViewClient wvClient = new WebViewClient();
		wvBody.setWebViewClient(wvClient);
		wvBody.setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
	
		try {
			String a1 = "open";
			String a2 = "close";
			String a3 = "money";
			AssetFileDescriptor afdOpen = getResources().getAssets().openFd(a1+".wav");
			AssetFileDescriptor afdClose = getResources().getAssets().openFd(a2+".wav");
			AssetFileDescriptor afdMoney = getResources().getAssets().openFd(a3+".wav");
			Map<String,AssetFileDescriptor> audioMap = new HashMap<String,AssetFileDescriptor>();
			audioMap.put(a1, afdOpen);
			audioMap.put(a2, afdClose);
			audioMap.put(a3, afdMoney);
		// you must call the following line after the webviewclient is set into the webview
		ShareSDKUtils shareSDK = ShareSDKUtils.prepare(wvBody, wvClient,audioMap);
		//wvBody.getSettings().setJavaScriptEnabled(true);
		wvBody.addJavascriptInterface(new JavaScriptInterface(), "jscall");  
		setContentView(wvBody);
			
		//wvBody.loadUrl("http://192.168.43.168:8080/cf/index.html");	//vtion
		wvBody.loadUrl(getUrlString());//xiaomi wifi
		//wvBody.loadUrl("http://202.69.27.223:8081/cf/index.html");	//pingan-test-wifi

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public String getUrlString(){
		String path = "config.txt";
		//File file = new File(path);
		String urlString=null;
        BufferedReader reader = null;
        try {
           System.out.println("以行为单位读取文件内容，一次读一整行：");
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(path) ); 
            reader = new BufferedReader(inputReader);
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	urlString = tempString;
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                break;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }		
        return urlString;
	}
	 /* 重写onTouchEvent() */  
	  @Override
	  public boolean onTouchEvent(MotionEvent event) 
	  {  
	    System.out.println(""+event.getPointerCount());
	    

	    return super.onTouchEvent(event);
	  }
	  
	final class JavaScriptInterface {   
		  
	       public int callOnJs() {  
	                          
	          return 1000;  
	       }  
	       @JavascriptInterface
	       public void playAudio(String audioName) {  
  
	       }  
	     
	  } 
}

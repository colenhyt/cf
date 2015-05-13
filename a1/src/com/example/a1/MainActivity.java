package com.example.a1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.sharesdk.js.ShareSDKUtils;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {
private WebView webView;
private EditText Ev1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		webView = (WebView)findViewById(R.id.webView1);
	
		DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
          
        int width = dm.widthPixels;  
        int height = dm.heightPixels; 
        
		webView.getSettings().setDomStorageEnabled(true);   
		webView.getSettings().setDatabaseEnabled(true);
		webView.getSettings().setAllowFileAccess(true);  
		WebViewClient wvClient = new WebViewClient();
		webView.setWebViewClient(wvClient); 
		webView.setWebChromeClient(new WebChromeClient() {
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		ShareSDKUtils aa = ShareSDKUtils.prepare(webView, wvClient);
		webView.addJavascriptInterface(aa, "JSInterface");
		webView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");
		
//		webView = new WebView(this);
//		flushWebView("http://192.168.31.241:8080/cf/index.html"); //xiaomi-7e
//		flushWebView("http://192.168.8.101:8080/cf/index.html");	//huaweiwifi-colen
//		flushWebView("http://192.168.123.1:8080/cf/index.html");	//xiaomi-wifi
		flushWebView("http://192.168.43.168:8080/cf/index.html");	//vt-wifi
//		flushWebView("http://202.69.27.223:8081/cf/index.html");	//pingan-test-wifi
		
		
//		Button btn1 = (Button)findViewById(R.id.button1);
//		btn1.setOnClickListener(new Button.OnClickListener(){
//			public void onClick(View v)
//			{
//				flushWebView("http://localhost:8080/cf/index.html");
//			}
//		});
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}
	}

	private void flushWebView(String strUrl)
	{
		if (webView!=null)
		{
			try {
//				if (Build.VERSION.SDK_INT >= 16) 
				{
				Class<?> clazz = webView.getSettings().getClass();
				Method method = clazz.getMethod(
				"setAllowUniversalAccessFromFileURLs", boolean.class);
				if (method != null) {
				method.invoke(webView.getSettings(), true);
				}
				}
				} catch (IllegalArgumentException e) {
				e.printStackTrace();
				} catch (NoSuchMethodException e) {
				e.printStackTrace();
				} catch (IllegalAccessException e) {
				e.printStackTrace();
				} catch (InvocationTargetException e) {
				e.printStackTrace();
				}		

		webView.loadUrl(strUrl);
		}		
	}
	
	final class DemoJavaScriptInterface {
        DemoJavaScriptInterface() {
        }
        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        @JavascriptInterface
        public void clickOnAndroid(String str) {
           System.out.println("call from js"+str);
        }
    }
    /**
    * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
   final class MyWebChromeClient extends WebChromeClient {
       @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
           result.confirm();
           return true;
       }
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}

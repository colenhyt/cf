package com.example.a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.sharesdk.js.ShareSDKUtils;

public class MainActivity extends Activity {
private PackageManager mPackageManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WebView wvBody = new WebView(this);
		
		
		//pingan ios: https://itunes.apple.com/cn/app/ping-an-ren-shou/id549421060?mt=8
//		NSString *str = [NSString stringWithFormat:@"http://itunes.apple.com/us/app/id%d", 436957167];
//		[[UIApplication sharedApplication] openURL:[NSURL urlWithString:str]];
		// itms-apps://itunes.apple.com/app/id%@?mt=8 
		
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
	
		List<String> configs = getConfigStrings();
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
		ShareSDKUtils.prepare(wvBody, wvClient,audioMap);
		//wvBody.getSettings().setJavaScriptEnabled(true);
		wvBody.addJavascriptInterface(new JavaScriptInterface(), "jscall");  
		setContentView(wvBody);
			
		//wvBody.loadUrl("http://192.168.43.168:8080/cf/index.html");	//vtion
		String url = configs.get(0);
		url += "/index.html";
		wvBody.loadUrl(url);//xiaomi wifi
		//wvBody.loadUrl("http://202.69.27.223:8081/cf/index.html");	//pingan-test-wifi

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public List<String> getConfigStrings(){
		String path = "config.txt";
		List<String> strs = new ArrayList<String>();
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
            	strs.add(tempString);
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
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
        return strs;
	}
	
	//检查是否安装该app
    boolean isInstallApplication(Context context, String packageName){
        try {
            mPackageManager
                    .getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
         
    }
    
    //启动某个应用:
    void execApp(String packageName,String className){
    	ComponentName componetName = new ComponentName(  
                //这个是另外一个应用程序的包名  
    			packageName,  
                //这个参数是要启动的Activity  
    			className);  
         
            try {  
                Intent intent = new Intent();  
                intent.setComponent(componetName);  
                startActivity(intent);  
            } catch (Exception e) {  
//              Toast.makeText(getApplicationContext(), "可以在这里提示用户没有找到应用程序，或者是做其他的操作！", 0).show();  
                  
            }      	
    }
    
    private void openApp(String packageName) {
    	final PackageManager pm = getPackageManager();
    	PackageInfo pi = null;
		try {
			pi = getPackageManager().getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
    	resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    	resolveIntent.setPackage(pi.packageName);

    	List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);

    	ResolveInfo ri = apps.iterator().next();
    	if (ri != null ) {
    	String packageName2 = ri.activityInfo.packageName;
    	String className = ri.activityInfo.name;

    	Intent intent = new Intent(Intent.ACTION_MAIN);
    	intent.addCategory(Intent.CATEGORY_LAUNCHER);

    	ComponentName cn = new ComponentName(packageName2, className);

    	intent.setComponent(cn);
    	startActivity(intent);
    	}
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

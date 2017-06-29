package com.github.lzyzsd.jsbridge.example;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ValueCallback;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;

import java.util.HashMap;

public class MainActivity extends Activity implements OnClickListener {

    private final String TAG = "MainActivity";

    BridgeWebView webView;

    Button button;

    int RESULT_CODE = 0;

    ValueCallback<Uri> mUploadMessage;

    static class Location {
        String address;
    }

    static class User {
        String name;
        Location location;
        String testStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = (BridgeWebView) findViewById(R.id.webView);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(this);

        //webView.setDefaultHandler(new DefaultHandler());

//		webView.setWebChromeClient(new WebChromeClient() {
//
//			@SuppressWarnings("unused")
//			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
//				this.openFileChooser(uploadMsg);
//			}
//
//			@SuppressWarnings("unused")
//			public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
//				this.openFileChooser(uploadMsg);
//			}
//
//			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//				mUploadMessage = uploadMsg;
//				pickFile();
//			}
//		});

        webView.loadUrl("http://www.hisunfd.com/livepage/wap/noticebar/dailyPreview.html?reqauth=true");

        //webView.loadUrl("file:///android_asset/demo.html");

        //webView.loadUrl("http://www.hisunfd.com/livepage/wap/client/test/index.html");

//		webView.registerHandler("submitFromWeb", new BridgeHandler() {
//
//			@Override
//			public void handler(String data, CallBackFunction function) {
//				Log.i(TAG, "handler = submitFromWeb, data from web = " + data);
//                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
//			}
//
//		});

        webView.registerHandler("bridgeGetAppSetting", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.e("MainActivity", "handler--------" + data + Thread.currentThread().getName());


                HashMap<String, String> launchMap = new HashMap<String, String>();
                launchMap.put("enablePush", "1");
                launchMap.put("enableSound", "0");
                launchMap.put("enableVibrate", "0");

                HashMap<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("success", "0");
                resultMap.put("data", launchMap);


                for (int i = 0; i < 100; i++) {
                    String s = i + "";
                }


                function.onCallBack(new Gson().toJson(resultMap));
            }
        });


        User user = new User();
        Location location = new Location();
        location.address = "SDU";
        user.location = location;
        user.name = "大头鬼";


//
//        webView.send("hello");
//
//
//
//		webView.registerHandler("functionInJs", new BridgeHandler() {
//			@Override
//			public void handler(String data, CallBackFunction function) {
//				Log.e("MainActivity", "handlercccc1111--------"+data);
//			}
//		});
    }

    String resultJson = "{\"success\" : \"0\",\"data\" : {\"enablePush\" : \"1\",\"enableSound\" : \"1\"," +
            "\"enableVibrate\" : \"1\",}}";

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }

    @Override
    public void onClick(View v) {
        if (button.equals(v)) {
            //bridgeGetAppSetting
            webView.callHandler("bridgeGetAppSetting", resultJson, new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    // TODO Auto-generated method stub
                    //Log.i(TAG, "reponse data from js " + data);
                    Log.e("MainActivity", "onCallBack--------" + data);
                }
            });
        }
    }

}

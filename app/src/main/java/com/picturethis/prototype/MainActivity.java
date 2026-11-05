package com.picturethis.prototype;
import android.app.*;
import android.os.*;
import android.content.*;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.webkit.*;
import android.widget.Toast;
import java.io.*;
import org.json.JSONObject;

public class MainActivity extends Activity {
 WebView web; ValueCallback<Uri[]> chooser; byte[] pendingExport;
 @Override public void onCreate(Bundle b) {
  super.onCreate(b); web=new WebView(this); setContentView(web);
  web.getSettings().setJavaScriptEnabled(true); web.getSettings().setDomStorageEnabled(true);
  web.getSettings().setAllowFileAccess(false); web.getSettings().setAllowContentAccess(true);
  web.setWebViewClient(new WebViewClient(){
   @Override public WebResourceResponse shouldInterceptRequest(WebView v, WebResourceRequest r){
    if(r.getUrl().toString().equals("https://picturethis.local/")) try {return new WebResourceResponse("text/html","UTF-8",getAssets().open("index.html"));}catch(IOException e){return null;}
    return new WebResourceResponse("text/plain","UTF-8",new ByteArrayInputStream(new byte[0]));
   }
   @Override public boolean shouldOverrideUrlLoading(WebView v,WebResourceRequest r){return true;}
  });
  web.setWebChromeClient(new WebChromeClient(){
   @Override public boolean onShowFileChooser(WebView v,ValueCallback<Uri[]> cb,FileChooserParams p){
    if(chooser!=null)chooser.onReceiveValue(null); chooser=cb;
    Intent i=new Intent(Intent.ACTION_OPEN_DOCUMENT);i.setType("image/*");i.addCategory(Intent.CATEGORY_OPENABLE);
    try {startActivityForResult(i,10);}catch(ActivityNotFoundException e){chooser.onReceiveValue(null);chooser=null;}return true;
   }
  });
  web.addJavascriptInterface(new Bridge(),"Android");web.loadUrl("https://picturethis.local/");
 }
 public class Bridge {
  @JavascriptInterface public void capture(){runOnUiThread(()->{try{startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),11);}catch(ActivityNotFoundException e){Toast.makeText(MainActivity.this,"No camera application available. Import a photo instead.",Toast.LENGTH_LONG).show();}});}
  @JavascriptInterface public void saveImage(String data){
   try {byte[] bytes=Base64.decode(data,Base64.DEFAULT);if(bytes.length>20000000)return;runOnUiThread(()->{pendingExport=bytes;Intent i=new Intent(Intent.ACTION_CREATE_DOCUMENT);i.setType("image/png");i.addCategory(Intent.CATEGORY_OPENABLE);i.putExtra(Intent.EXTRA_TITLE,"picturethis-preview.png");startActivityForResult(i,12);});}catch(Exception e){runOnUiThread(()->Toast.makeText(MainActivity.this,"Export failed",Toast.LENGTH_LONG).show());}
  }
 }
 @Override protected void onActivityResult(int req,int result,Intent data){super.onActivityResult(req,result,data);
  if(req==10&&chooser!=null){chooser.onReceiveValue(result==RESULT_OK&&data!=null?new Uri[]{data.getData()}:null);chooser=null;}
  if(req==11&&result==RESULT_OK&&data!=null&&data.getExtras()!=null){Bitmap b=(Bitmap)data.getExtras().get("data");if(b!=null){ByteArrayOutputStream out=new ByteArrayOutputStream();b.compress(Bitmap.CompressFormat.JPEG,95,out);String s="data:image/jpeg;base64,"+Base64.encodeToString(out.toByteArray(),Base64.NO_WRAP);web.evaluateJavascript("loadPhoto("+JSONObject.quote(s)+",'Camera thumbnail')",null);}}
  if(req==12){if(result==RESULT_OK&&data!=null&&pendingExport!=null)try(OutputStream o=getContentResolver().openOutputStream(data.getData())){o.write(pendingExport);Toast.makeText(this,"Preview saved",Toast.LENGTH_SHORT).show();}catch(Exception e){Toast.makeText(this,"Could not save preview",Toast.LENGTH_LONG).show();}pendingExport=null;}
 }
 @Override public void onBackPressed(){new AlertDialog.Builder(this).setMessage("Close PictureThis? Your last saved project stays on this device.").setPositiveButton("Close",(d,w)->finish()).setNegativeButton("Stay",null).show();}
}

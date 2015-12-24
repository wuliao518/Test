package com.jiang.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.qcloud.PicCloud;
import com.qcloud.PornDetectInfo;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.tencent.upload.Const;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.PhotoUploadTask;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;

public class MainActivity extends AppCompatActivity {

    private Intent intent;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView) findViewById(R.id.image);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertView("上传图片", null, "取消", null,
                        new String[]{"拍照", "从手机相册选取"},
                        MainActivity.this, AlertView.Style.ActionSheet, new OnItemClickListener() {
                    public void onItemClick(Object o, int position) {
                        switch (position) {
                            case 0:
                                intent = new Intent();
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 0);
                                break;
                            case 1:
                                intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 1);
                                break;
                        }
                    }
                }).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == 0 || requestCode == 1) {
                Toast.makeText(getApplication(), "照片获取失败", Toast.LENGTH_SHORT).show();
            }
        } else {
            switch (requestCode) {
                case 0://拍照

                    Bitmap photo = data.getParcelableExtra("data");
                    File cameraFile = ImageUtil.saveBitmap(getApplication(), photo);
                    if (cameraFile != null) {

                    }
                    break;
                case 1://相册

                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + getPackageName() + "/file");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    file = new File(file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".png");
                    file = ImageUtil.photoImage(getApplication(), data, file);
                    image.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                    if (file != null) {
                        uploadFile(file.getAbsolutePath());
                    }
                    break;

            }
        }
    }


    private void uploadFile(String path) {
        UploadManager photoUploadMgr = null;
        photoUploadMgr = new UploadManager(getApplication(), "10015499",
                Const.FileType.Photo, "");

        PhotoUploadTask task = new PhotoUploadTask(path,
                new IUploadTaskListener() {

                    @Override
                    public void onUploadSucceed(final FileInfo result) {
                        Log.e("Demo", "upload succeed: " + result.url);


//                        OkHttpClient client = new OkHttpClient();
//                        Map<String,String> params = new HashMap<>();
//                        params.put("appid","10015499");
//                        params.put("bucket","demotest");
//                        params.put("url",result.url);
//
//                        FormEncodingBuilder formBody = new FormEncodingBuilder();
//                        Set<Map.Entry<String, String>> entry = params.entrySet();
//                        Iterator<Map.Entry<String, String>> it = entry.iterator();
//                        while (it.hasNext()) {
//                            Map.Entry<String, String> me = it.next();
//                            formBody.add(me.getKey(), me.getValue());
//                        }
//
//                        final Request request = new Request.Builder()
//                                .url("http://service.image.myqcloud.com/detection/pornDetect")
//                                .header("Host", "service.image.myqcloud.com")
//                                .header("Authorization", "lovepinyaoaccesskey")
//                                .header("Content-Type", "application/json")
//                                .header("Content-Length", formBody.toString().length()+"")
//                                .post(formBody.build())
//                                .build();
//                       client.newCall(request).enqueue(new Callback() {
//                           @Override
//                           public void onFailure(Request request, IOException e) {
//                               Log.e("jiang",e.getMessage());
//                           }
//
//                           @Override
//                           public void onResponse(Response response) throws IOException {
//                               Log.e("jiang",response.toString());
//                           }
//                       });

                        PicCloud cloud = new PicCloud(10015499,"AKIDmm2bjSZZoU0jNmKAjrZboW1sF7XalBlr",
                                "srj7oxebCUV0Y2RbWrj6KjlaUpVS0K7i","demotest");
                        PornDetectInfo info =cloud.pornDetect(result.url);
                        Log.e("jiang", "result="+info.result);





                    }

                    @Override
                    public void onUploadStateChange(ITask.TaskState state) {
                    }

                    @Override
                    public void onUploadProgress(long totalSize, long sendSize) {
                        long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
                        Log.e("Demo", "上传进度: " + p + "%");
                    }

                    @Override
                    public void onUploadFailed(final int errorCode, final String errorMsg) {
                        Log.e("Demo", "上传结果:失败! ret:" + errorCode + " msg:" + errorMsg);
                    }
                }
        );
        task.setBucket("demotest");
        String uuid = UUID.randomUUID() + "";
        task.setFileId("test_fileId_" + uuid); // 为图片自定义FileID(可选)
        task.setAuth(getSign("test_fileId_" + uuid));
        photoUploadMgr.upload(task);  // 开始上传
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

    private String getSign(String uid) {
        PicCloud cloud = new PicCloud(10015499,"AKIDmm2bjSZZoU0jNmKAjrZboW1sF7XalBlr",
                "srj7oxebCUV0Y2RbWrj6KjlaUpVS0K7i","demotest");
        return cloud.getSign(System.currentTimeMillis() / 1000 + 2592000);
//        String a = "";
//        String b = "demotest";
//        String k = "AKIDmm2bjSZZoU0jNmKAjrZboW1sF7XalBlr";
//        String e = "0";
//        String t = System.currentTimeMillis()/1000 + "";
//        String r = new Random().nextInt(10000) + "";
//        String u = "0";
//        String f = uid;
//        String str = "a=" + a + "&b=" + b + "&k=" + k + "&e=" + e + "&t=" + t + "&r=" + r + "&u=" + u + "&f=" + f;
//        Log.e("jiang", str);
//        try {
//            String sign = BaseCode.encode(HMACSHA1.getSignature(str.getBytes(),
//                    "srj7oxebCUV0Y2RbWrj6KjlaUpVS0K7i".getBytes()))+BaseCode.encode(str.getBytes());
//            Log.e("jiang",sign);
//            return sign;
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return "";

    }
}

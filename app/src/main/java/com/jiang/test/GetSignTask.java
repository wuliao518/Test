package com.jiang.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.tencent.upload.Const.FileType;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

public class GetSignTask extends AsyncTask<Void, Integer, String> {

	Context mContext;
	String mBucket;
	String mAppid;
	String mFileId;
	String result;
	String sign;
	String urlSign;
	private OnGetSignListener mListener;
	
	

	public interface OnGetSignListener {
		public void onSign(String sign);
	}

	public GetSignTask(Context context, String appid, FileType fileType, String bucket, String fileId,String url ,OnGetSignListener listener) {
		mContext = context;
		// mFileType = fileType;
		mFileId = fileId;
		mAppid = appid;
		mBucket = bucket;
		mListener = listener;
		urlSign = url;
		Log.d("jia", urlSign);
		// mDialog = new ProgressDialog(context);
		// mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	}

	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub

		//String s = "http://203.195.194.28/php/getsignv2.php"+ "?type=copy&fileid=" + mFileId;


		try {
			URL url = new URL(urlSign);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			InputStreamReader in = new InputStreamReader(
					urlConnection.getInputStream());
			BufferedReader buffer = new BufferedReader(in);
			String inpuLine = null;
			while ((inpuLine = buffer.readLine()) != null) {
				result = inpuLine + "\n";
			}
			JSONObject jsonData = new JSONObject(result);
			sign = jsonData.getString("sign");
		} catch (Exception e) {
			// TODO: handle exception
		}
		mListener.onSign(sign);
		return null;
	}

}

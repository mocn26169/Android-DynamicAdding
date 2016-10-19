package com.dynamicadding.android_dynamicadding;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
/**
 * 通过分组获取联系人
 * @url /contactuserapp/getcontactusersbygroup
 * @param groupid  分组id
 * @return
 */

/**
 * 通过部门分组
 * @url /contactuserapp/getcontactusersbydep
 * depName  部门名称
 * @return
 */
public class SelectFileActivity extends AppCompatActivity {

    private static final String TAG = "SelectFileActivity";
//    private static final String URL = "http://121.40.148.206:88/Data/Server.svc/zyUpload/Action.ashx";
//    private static final String URL = "http://121.40.148.206:88/JS/zyUpload/Action.ashx";
    private static final String URL = "http://121.40.148.206:8091/JS/zyUpload/Action.ashx";
//    private static final String URL = "http://121.40.86.160:8090/contactuserapp/getcontactusersbydep?depName=%E8%B4%B5%E9%98%B3%E5%B8%82%E5%9B%BD%E5%9C%9F%E8%B5%84%E6%BA%90%E5%B1%80%E4%BA%91%E5%B2%A9%E5%88%86%E5%B1%80";
//    private static final String URL = "http://121.40.86.160:8090/contactuserapp/getcontactusersbydep?depName=%E8%B4%B5%E9%98%B3%E5%B8%82%E5%9B%BD%E5%9C%9F%E8%B5%84%E6%BA%90%E5%B1%80%E4%BA%91%E5%B2%A9%E5%88%86%E5%B1%80";

    //回复
//   http://121.40.86.160:8090/eventapp/addrecy?eventid=35&eventcontent=%E4%BA%8B%E4%BB%B6111111111111111111111111111111111&recvphonenums=15908517851,13333333333,13628505300,18888888888,13333333333,18888888888,13628505300,15908517851,15019568265&recvcontactusers=%E9%80%9A%E8%AE%AF%E5%BD%95%E6%B5%8B%E8%AF%95%E5%91%98,%E4%B9%94%E5%B8%83%E6%96%AF,%E6%9C%B1%E9%B9%8F,2%E5%8F%B7%E4%BA%BA,%E4%B9%94%E5%B8%83%E6%96%AF,2%E5%8F%B7%E4%BA%BA,%E6%9C%B1%E9%B9%8F,%E9%80%9A%E8%AE%AF%E5%BD%95%E6%B5%8B%E8%AF%95%E5%91%98,15019568265

    private LinearLayout linear_select;
    private LinearLayout linear_parent;
    private Button btn_uplaod;

    private List<FileBean> lists = new ArrayList<FileBean>();
    private List<AsyncHttpClient> httpLists = new ArrayList<AsyncHttpClient>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_file);

        linear_select = (LinearLayout) findViewById(R.id.linear_select);
        linear_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btn_uplaod = (Button) findViewById(R.id.btn_uplaod);
        btn_uplaod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

        linear_parent = (LinearLayout) findViewById(R.id.linear_parent);
    }

    /**
     *上传文件
     */
    private void uploadFile() {
        if (lists==null || lists.size() ==0){
            Toast.makeText(this, "没有要上传的文件", Toast.LENGTH_SHORT).show();
        }else{
            Log.e(TAG,"开始上传----------");
            httpLists.clear();
            for (int i = 0; i <lists.size() ; i++) {
                FileBean bean = lists.get(i);
                try {
                    httpUpload(bean,URL);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"失败----------");
                }
            }

        }
    }


    public  void httpUpload(final FileBean bean, String url) throws Exception {

        //判断上次路径是否为空
        if (TextUtils.isEmpty(bean.getLocalUrl().trim())) {
//            Toast.makeText(context, "上次文件路径不能为空", Toast.LENGTH_LONG).show();
            Log.e(TAG,"上次文件路径不能为空");
        } else {
            //异步的客户端对象
            AsyncHttpClient client = new AsyncHttpClient();
            httpLists.add(client);

            //封装文件上传的参数
            RequestParams params = new RequestParams();
            //根据路径创建文件
            File file = new File(bean.getLocalUrl());
            try {
                //放入文件
                params.put("file", file);
            } catch (Exception e) {
                Log.e(TAG,"文件不存在----------");
            }
            //执行post请求
            client.post(url,params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    if (statusCode == 200) {
                        String s1=new String(responseBody);
                        Log.e(TAG,"上传成功----------"+s1);
                        bean.setWebUrl(s1);

                        FileBean bean;
                        boolean startOther = true;
                        for (int i = 0; i <lists.size() ; i++) {
                             bean = lists.get(i);
                            Log.e(TAG,"bean getWebUrl----------"+bean.getWebUrl());
                            if (TextUtils.isEmpty(bean.getWebUrl())){
                                startOther= false;
                            }
                        }

                        if (startOther){
                            doOthers();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {

                    Log.e(TAG,"上传失败,取消所有上传----------");
                    for (int i = 0; i <httpLists.size() ; i++) {
                        AsyncHttpClient http = httpLists.get(i);
                        http.cancelRequests(SelectFileActivity.this,true);
                    }
                    error.printStackTrace();
                }
            });
        }
    }


    private  void doOthers(){
        Log.e(TAG, "doOthers ==> ");
        FileBean bean;
        for (int i = 0; i <lists.size() ; i++) {
            bean = lists.get(i);
            Log.e(TAG,"bean getWebUrl22222----------"+bean.getWebUrl());
        }
    }

    /**
     * 打开文件选择器
     */
    private void showFileChooser() {
        /***
         * 这个是调用android内置的intent，来过滤图片文件   ，同时也可以过滤其他的
         */
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 2000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2000) {

                Uri uri = data.getData();
                Log.e(TAG, "uri = " + uri);

                String path = getRealFilePath(SelectFileActivity.this, uri);
                Log.i(TAG, "path=" + path);

                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(this, "无法获取文件", Toast.LENGTH_SHORT).show();
                } else {
                    FileBean bean = new FileBean();
                    bean.setLocalUrl(path);

                    String size = FileSizeUtil.getAutoFileOrFilesSize(path);
                    bean.setSize(size);

                    String[] array = path.split("/");
                    bean.setFileName(array[array.length - 1]);

                    File file = new File(path);
                    bean.setFile(file);

                    AddItem(linear_parent, bean);
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    /**
     * 添加item
     * @param parent
     * @param bean
     */
    private void AddItem(final LinearLayout parent, FileBean bean) {
        if (parent == null) {
            return;
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        final View view = LayoutInflater.from(this).inflate(R.layout.item_add_phone, null);//也可以从XML中加载布局
        view.setLayoutParams(lp);//设置布局参数

        TextView tv1 = (TextView) view.findViewById(R.id.tv_content);
        tv1.setText(bean.getFileName() + "(" + bean.getSize() + ")");

        Button btn_delete = (Button) view.findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent.removeView(view);

                FileBean bean = (FileBean) view.getTag();
                lists.remove(bean);

                for (int i = 0; i < lists.size(); i++) {
                    Log.i(TAG, "file=" + lists.get(i).toString() + "\n");
                }

            }
        });

        lists.add(bean);
        view.setTag(bean);
        parent.addView(view);

    }

    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 判断文件是否存在
     * @param strFile
     * @return
     */
    public boolean fileIsExists(String strFile)
    {
        try
        {
            File f=new File(strFile);
            if(!f.exists())
            {
                return false;
            }
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }


}

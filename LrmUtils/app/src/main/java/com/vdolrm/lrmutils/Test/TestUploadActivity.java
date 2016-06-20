package com.vdolrm.lrmutils.Test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.vdolrm.lrmutils.FileUtils.StorageUtil;
import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.upload.TestUpLoadPresenter;
import com.vdolrm.lrmutils.R;

import java.io.File;

public class TestUploadActivity extends AppCompatActivity {
    String TAG_NET = "lrmnet";
    private TestUpLoadPresenter presenter;
    private static final String app_url = "http://testapp.benniaoyasi.com/api.php?appid=1&m=api";
    public static final String url_ieltsstudy_myscore_edit_test = app_url + "&c=ncontent&a=zdycontent&strflag=1&mobile=15810910848&biaoshi=2&id=39&english=helloworld&chines=nil&devtype=android&version=2.0";

    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_upload);

        path = StorageUtil.getExternalRootPath()+"/startpage.png";
        path = StorageUtil.getExternalRootPath()+"/testandroid.mp3";
        File file = new File(path);
        String fileKey = "audio";

        presenter = new TestUpLoadPresenter();
        presenter.upload(url_ieltsstudy_myscore_edit_test, new OSIHttpDownloadCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                MyLog.d(TAG_NET, "onLoading total="+total+",current="+current );
            }

            @Override
            public void onException(Exception e) {
                MyLog.d(TAG_NET, "e=" + e.getMessage());
            }

            @Override
            public void onError(String errorMsg) {
                MyLog.d(TAG_NET,"errorMsg="+errorMsg);
            }


            @Override
            public void onResponse(String strResponse,String response) {
                MyLog.d(TAG_NET,"response="+response);
            }
        },file,fileKey);
    }
}

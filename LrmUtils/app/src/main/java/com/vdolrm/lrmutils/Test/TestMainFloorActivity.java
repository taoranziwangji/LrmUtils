package com.vdolrm.lrmutils.Test;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter.BaseMyAdapter;
import com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter.BaseViewHolder;
import com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter.OnRecyclerViewItemClickListener;
import com.vdolrm.lrmutils.Adapter.RecyclerViewAdapter.OnRecyclerViewScrollImageStateListener;
import com.vdolrm.lrmutils.BaseFloorActivity;
import com.vdolrm.lrmutils.FileUtils.StorageUtil;
import com.vdolrm.lrmutils.LogUtils.MyLog;
import com.vdolrm.lrmutils.OpenSourceUtils.img.OSBaseImageLoaderPresenter;
import com.vdolrm.lrmutils.OpenSourceUtils.img.TestImageLoaderPresenter;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.OSIHttpDownloadCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.download.TestDownLoadPresenter;
import com.vdolrm.lrmutils.OpenSourceUtils.net.http.OSIHttpLoaderCallBack;
import com.vdolrm.lrmutils.OpenSourceUtils.net.http.TestHttpLoaderPresenter;
import com.vdolrm.lrmutils.OpenSourceUtils.net.http.okhttp.TestOkHttpBean;
import com.vdolrm.lrmutils.OtherUtils.MapJsonUtil;
import com.vdolrm.lrmutils.R;
import com.vdolrm.lrmutils.UIUtils.UIUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//https://github.com/duzechao/DownloadManager 使用okhttp greendao 支持断点续传和暂停等功能的下载框架
public class TestMainFloorActivity extends BaseFloorActivity {
    //private static final String url = "http://test.benniaoyasi.cn/api.php?appid=1&m=api&c=category&a=listcategory&pid=1&devtype=android&version=1.4.0";
    private static final String url = "http://testapp.benniaoyasi.com/api.php?appid=1&m=api&c=ncategory&a=listcategory&pid=1&devtype=android&version=2.0";
   // private static final String urlOther = "http://publicobject.com/helloworld.txt";
    private static final String urlOther = "https://api.github.com/repos/square/okhttp/contributors";
    private static final String url_testImage = "http://img.my.csdn.net/uploads/201407/26/1406383299_1976.jpg";
    private static final String url_mp3 = "http://music-zhizhi.oss-cn-beijing.aliyuncs.com/%E6%BC%82%E6%B4%8B%E8%BF%87%E6%B5%B7%E6%9D%A5%E7%9C%8B%E4%BD%A0.mp3";
    private static final String url_baidu = "http://www.gogo-talk.com";
    private ImageView mImageView;
    private RecyclerView recyclerView;
    private OSBaseImageLoaderPresenter imageLoaderPresenter;
    private TestHttpLoaderPresenter httpLoaderPresenter;


    @Override
    public void initView() {
        setContentView(R.layout.lrmutils_activity_main);
    }

    @Override
    public void init() {
        recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mImageView = (ImageView) findViewById(R.id.mImageView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.showToastSafe("aaa");
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    @Override
    public void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void initEvent() {

        //测试viewpager
        //Intent intent = new Intent(this,TestFragmentViewPagerFloorActivity.class);
        //startActivity(intent);


        //测试baseMyAdapter(recyclerView)
        //testAdapter();

        //测试大图裁剪
        //Intent intent = new Intent(this,TestBigPicShowFloorActivity.class);
        //startActivity(intent);

        //测试MVP模式图片加载（picasso）
        // TestImageLoaderPresenter.getInstance(this).load(url_testImage, (ImageView) findViewById(R.id.mImageView));


       /* new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //测试fragment viewpager
                        Intent intent = new Intent(TestMainFloorActivity.this, TestUploadActivity.class);
                        UIUtils.startActivity(intent);
                    }
                });

            }
        }).start();*/


        //测试MVP模式网络请求（okhttp）
        //testOkHttp();
        testOkHttpOther();

        //测试文件下载
        //testFileDownLoad();

        //测试okhttp raw传输json
        String url = "http://139.129.203.213/front/api/user/register";
        String json = "{\n" +
                "    \"head\": {\n" +
                "        \"uid\": \"\",\n" +
                "        \"plat\": \"android\",\n" +
                "        \"st\": \"\",\n" +
                "        \"ver\": \"1.0\",\n" +
                "        \"imei\": \"869130020315334\",\n" +
                "        \"oc\": \"869130020315334\",\n" +
                "        \"tid\": \"\"\n" +
                "    },\n" +
                "    \"body\": {\n" +
                "        \"uname\": \"lrmname\",\n" +
                "        \"password\": \"lrmpassword\"\n" +
                "    }\n" +
                "}";
        //testRawJsonPost4Okhttp(url,json);

        //测试Android与JS交互
        //Intent in_js = new Intent(this,TestJaveJSFloorActivity.class);
        //startActivity(in_js);

        //测试okhttp的delete方法
        String url_delete = "https://app.wurener.com/v3/delete_address.json";
        Map<String,String> map_delete = new HashMap<>();
        map_delete.put("uid", "3037");
        map_delete.put("address_id", "1586");
        map_delete.put("app_key", "family_app");
        map_delete.put("from_client", "Android");
        map_delete.put("app_version", "3.0.8");
        map_delete.put("sign", "965eb77b219ad2187c60e5fcf93e85c6");
        //testDeleteAsync4Okhttp(url_delete,map_delete);

        //测试map与jsonObject相互转换工具类
        JSONObject jsonObject = MapJsonUtil.map2JsonObject(map_delete);
        MyLog.d("json="+jsonObject);
        HashMap<String, String> stringStringHashMap = MapJsonUtil.jsonObject2Map(jsonObject);
        MyLog.d("map="+stringStringHashMap);




    }

    private void testFileDownLoad() {
        new TestDownLoadPresenter().download(url_mp3, StorageUtil.getExternalRootPath(), "zabc.mp3", new OSIHttpDownloadCallBack<String>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                MyLog.d("正在下载 total=" + total + ",current=" + current);
            }


            @Override
            public void onException(Exception e) {
                MyLog.d("下载失败" + ",e=" + e.getMessage());
            }


            @Override
            public void onError(String errorMsg) {
                MyLog.d("下载失败" + errorMsg);
            }

            @Override
            public void onResponse(String strResponse,String response) {
                MyLog.d("下载成功" + response);
            }


        });
    }

    private void testOkHttp() {
        httpLoaderPresenter = new TestHttpLoaderPresenter();
        httpLoaderPresenter.getAsync("test",url, new OSIHttpLoaderCallBack<TestOkHttpBean>() {

            @Override
            public void onException(Exception e) {
                MyLog.d("okhttp error " + ",e=" + e.getMessage());
            }

            @Override
            public void onErrorGsonException(String response, Exception e) {

            }

            @Override
            public void onError(String errorMsg) {
                MyLog.d("okhttp error msg=" + errorMsg);
            }

            @Override
            public void onResponse(String strResponse,TestOkHttpBean response) {
                if (response != null) {
                    List<TestOkHttpBean.EdataEntity> list = response.getEdata();
                    if (list != null && list.size() > 0) {
                        for (TestOkHttpBean.EdataEntity bean : list) {
                            MyLog.d("okhttp gson ename" + list.indexOf(bean) + " = " + bean.getEname());
                        }
                    }
                } else {
                    MyLog.d("okhttp onResponse response=" + response);
                }
            }
        },null);

        //测试网络请求的取消
        //SystemClock.sleep(10);
        //httpLoaderPresenter.cancelRequest();
    }

    private void testOkHttpOther() {
        String url = "http://101.201.114.250:8090/api.php?r=skill/skill/SkillInfo";
        Map<String,String> mapHeader = new HashMap<>();
        mapHeader.put("sid","2f05fed8475d55dcabf938cb7d1a36f3");
        mapHeader.put("os","android");
        mapHeader.put("version","1");
        httpLoaderPresenter = new TestHttpLoaderPresenter();
        httpLoaderPresenter.getAsync("test",url, new OSIHttpLoaderCallBack<String>() {

            @Override
            public void onException(Exception e) {
                MyLog.d("okhttp error " + ",e=" + e.getMessage());
            }

            @Override
            public void onErrorGsonException(String response, Exception e) {

            }

            @Override
            public void onError(String errorMsg) {
                MyLog.d("okhttp error msg=" + errorMsg);
            }

            @Override
            public void onResponse(String strResponse,String response) {

                MyLog.d("okhttp onResponse response=" + response);

            }
        },mapHeader);

    }

    private void testRawJsonPost4Okhttp(String url,String json) {
        httpLoaderPresenter = new TestHttpLoaderPresenter();
        httpLoaderPresenter.postAsyncRaw("test",url, new OSIHttpLoaderCallBack<String>() {

            @Override
            public void onException(Exception e) {
                MyLog.d("okhttp error " + ",e=" + e.getMessage());
            }

            @Override
            public void onErrorGsonException(String response, Exception e) {

            }

            @Override
            public void onError(String errorMsg) {
                MyLog.d("okhttp error msg=" + errorMsg);
            }

            @Override
            public void onResponse(String strResponse,String response) {

                MyLog.d("okhttp onResponse response=" + response);

            }
        },json,null);

    }


    private void testDeleteAsync4Okhttp(String url,Map<String,String> map) {
        httpLoaderPresenter = new TestHttpLoaderPresenter();
        httpLoaderPresenter.deleteAsync("test",url, new OSIHttpLoaderCallBack<String>() {

            @Override
            public void onException(Exception e) {
                MyLog.d("okhttp error " + ",e=" + e.getMessage());
            }

            @Override
            public void onErrorGsonException(String response, Exception e) {

            }

            @Override
            public void onError(String errorMsg) {
                MyLog.d("okhttp error msg=" + errorMsg);
            }

            @Override
            public void onResponse(String strResponse,String response) {

                MyLog.d("okhttp onResponse response=" + response);

            }
        },map,null);

    }


    /**
     * recyclerView会自动处理回收，不用加tag也不会导致混乱
     */
    private void testAdapter() {
        List list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            MyBean bean = new MyBean();
            bean.setName("NAME" + i);
            bean.setUrl("http://pic.to8to.com/attch/day_160218/20160218_d968438a2434b62ba59dH7q5KEzTS6OH.png");
            list.add(bean);
        }

        recyclerView.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);//如果你需要显示的是横向滚动的列表或者竖直滚动的列表，则使用这个LayoutManager。显然，我们要实现的是ListView的效果，所以需要使用它。生成这个LinearLayoutManager之后可以设置他滚动的方向，默认竖直滚动，所以这里没有显式地设置。

        MyAdapter adapter = new MyAdapter(this, list);
        adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                UIUtils.showToastSafe("itemclick " + position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                UIUtils.showToastSafe("itemlongclick " + position);
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
        imageLoaderPresenter = TestImageLoaderPresenter.getInstance(this);
        recyclerView.addOnScrollListener(new OnRecyclerViewScrollImageStateListener(this, imageLoaderPresenter));
        //设置Item增加、移除动画 (不是初始时的动画 而是添加删除item时的动画)
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }


    class MyBean {
        String url;
        String name;
        MediaPlayer mediaPlayer;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public MediaPlayer getMediaPlayer() {
            return mediaPlayer;
        }

        public void setMediaPlayer(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;
        }
    }

    class MyAdapter extends BaseMyAdapter<MyBean, MyHolder> {
        private List<MyBean> list;

        public MyAdapter(Context c, List list) {
            super(c, list);
            this.list = list;
        }

        @Override
        public View getItemView(ViewGroup parent) {
            View v = UIUtils.inflate(R.layout.item_testrecyclerviewadapter);
            return v;
        }


        @Override
        public MyHolder getHolder(View itemView, View itemRootView, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            return new MyHolder(itemView, itemRootView, onRecyclerViewItemClickListener);
        }

        @Override
        public void onBind(final int position, final MyBean bean, View itemRootView, MyHolder holder) {
            //TextView tv_test = (TextView) itemRootView.findViewById(R.id.tv_test);
            //Button btn_play = (Button) itemRootView.findViewById(R.id.btn_play);


            holder.tv_test.setText(bean.getName());
            holder.btn_play.setText("play " + bean.getName());
            holder.btn_play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyLog.d("mp=" + bean.getMediaPlayer());

                    for (int i = 0; i < list.size(); i++) {
                        if (i != position) {//pause两次再start一次可能播放不了
                            MediaPlayer mp = list.get(i).getMediaPlayer();
                            if (mp != null && mp.isPlaying()) {
                                mp.pause();
                                mp.stop();
                                mp.release();
                                mp = null;
                                list.get(i).setMediaPlayer(null);
                                MyLog.d("释放" + list.get(i).getMediaPlayer());
                            }
                        }
                    }


                    if (bean.getMediaPlayer() == null) {
                        MediaPlayer mp;
                        AssetManager am = getAssets();
                        try {
                            AssetFileDescriptor afd = am.openFd("yezi.mp3");
                            mp = new MediaPlayer();
                            mp.setDataSource(afd.getFileDescriptor());
                            mp.prepare(); //准备

                            bean.setMediaPlayer(mp);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (bean.getMediaPlayer() != null) {
                        if (bean.getMediaPlayer().isPlaying()) {
                            bean.getMediaPlayer().pause();
                            MyLog.d("pause");
                        } else {
                            bean.getMediaPlayer().start();
                            MyLog.d("start" + position);
                        }
                    }
                }
            });
        }


    }


    class MyHolder extends BaseViewHolder {

        TextView tv_test;
        Button btn_play;

        public MyHolder(View itemView, View itemRootView, OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            super(itemView, itemRootView, onRecyclerViewItemClickListener);

            tv_test = (TextView) itemRootView.findViewById(R.id.tv_test);
            btn_play = (Button) itemRootView.findViewById(R.id.btn_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageLoaderPresenter != null) {
            imageLoaderPresenter.cancelTag(this);
        }

        TestApplication.getInstance().exit(this);
    }
}

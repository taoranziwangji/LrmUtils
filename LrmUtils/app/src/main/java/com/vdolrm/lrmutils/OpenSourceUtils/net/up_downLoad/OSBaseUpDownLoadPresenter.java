package com.vdolrm.lrmutils.OpenSourceUtils.net.up_downLoad;

/**
 * Created by Administrator on 2016/4/5.
 */
public abstract class OSBaseUpDownLoadPresenter {
    protected abstract OSIUpDownLoad getOsiUpDownLoadImpl();

    private OSIUpDownLoad osiHttpLoader;

    public OSBaseUpDownLoadPresenter() {
        osiHttpLoader = getOsiUpDownLoadImpl();
    }


    /**
     * 下载
     * @param url
     * @param destDir
     * @param fileName
     * @param callback
     */
    public void download(String url, String destDir, String fileName, OSIHttpDownloadCallBack callback) {
        if (osiHttpLoader != null)
            osiHttpLoader.download(url,destDir,fileName,callback);
    }
}

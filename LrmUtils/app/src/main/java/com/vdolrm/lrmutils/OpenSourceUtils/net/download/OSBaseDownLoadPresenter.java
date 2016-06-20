package com.vdolrm.lrmutils.OpenSourceUtils.net.download;

/**
 * Created by Administrator on 2016/4/5.
 */
public abstract class OSBaseDownLoadPresenter {
    protected abstract OSIDownLoad getOsiDownLoadImpl();

    private OSIDownLoad osiHttpLoader;

    public OSBaseDownLoadPresenter() {
        osiHttpLoader = getOsiDownLoadImpl();
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

package com.vdolrm.lrmutils;

import com.vdolrm.lrmutils.FileUtils.StorageUtil;

public class Contant {

	public static final String extensionName = ".temp";
	
	public static final String isCreateDesk = "isCreateDesk";//创建桌面快捷方式

    public static final String mulu_root = StorageUtil.getExternalRootPath()+"/lrmutils/";
	public static final String mulu_file_cache = mulu_root+"Cache/";
	public static final String mulu_file_cache2 = mulu_root+"Cache2/";
    public static final int maxPicSizeTest = 300;

	/*public static final String mulu_root = MemoryUtil.getExternalRootPath()+"/zTest/";
	public static final String mulu_chuti_anwser_pic = MemoryUtil.getExternalRootPath()+"/zTest/pic_tmp/";//部分机型（如T8830）必须一级一级mkdir
	public static final String mulu_file_cache = MemoryUtil.getExternalRootPath()+"/zTest/Cache/";
	
	
	public static final int maxPicSize = 300; //出题界面用到的高清图的最大尺寸(300----(0,600)) //最新研究为（300---（300,450]）即1.5max；  //最新研究是错误的，强转int直接舍弃小数点后面的数不是四舍五入
	public static final int heightPicSize = 370;//拍照或从相册选取时裁剪的最大高度为370（最大宽度为maxpicsize 480 ,这样4:3 比较好看）
	public static final int minPicSize = 80; //出题界面用到的缩略图图的尺寸*/
}

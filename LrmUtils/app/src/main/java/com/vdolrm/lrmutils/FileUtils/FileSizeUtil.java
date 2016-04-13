package com.vdolrm.lrmutils.FileUtils;


import com.vdolrm.lrmutils.LogUtils.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;

public class FileSizeUtil {
	
	/**取得文件大小
	 */   
	public static long getFileSize(File f) throws Exception {//取得文件大小
        if(f==null) {
            MyLog.e(MyLog.printBaseInfo() + "file is null");
            return 0;
        }
        long s=0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
           s= fis.available();
           
           fis.close();
        } else {
            f.createNewFile();
            MyLog.e(MyLog.printBaseInfo()+"文件不存在");
        }
        
        
       
        return s;
    }


    /**
     * 取得文件夹大小
     * @param f
     * @return 文件夹的size 单位为B
     * @throws Exception
     */
    public static long getFileDocumentSize(File f)throws Exception  // 递归
    {
        if(f==null) {
            MyLog.e(MyLog.printBaseInfo() + "file is null");
            return 0;
        }

    	long size = 0;
        if (f.exists()) {

            File[] fileList = f.listFiles();

            for (int i = 0; i < fileList.length; i++)

            {

                if (fileList[i].isDirectory())

                {

                    size = size + getFileDocumentSize(fileList[i]);

                } else

                {

                    size = size + fileList[i].length();

                }

            }
        }else{
            MyLog.e(MyLog.printBaseInfo()+"文件夹不存在");
        }

        return size; 
    }

    

    
    /**
     * 递归求取目录文件个数
	 */ 
    public static long getlist(File f){//递归求取目录文件个数
        if(f==null){
            MyLog.e(MyLog.printBaseInfo() + "file is null");
            return 0;
        }
        if (f.exists()) {
            long size = 0;
            File flist[] = f.listFiles();
            size = flist.length;
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getlist(flist[i]);
                    size--;
                }
            }
            return size;
        }else{
            MyLog.e(MyLog.printBaseInfo()+"文件夹不存在");
            return 0;
        }

    }
}
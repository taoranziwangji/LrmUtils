package com.vdolrm.lrmutils.FileUtils;


import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	
	 /** 
     * 保存图片文件为JPG
	  * @param path 要保存到的路径(不带名字)
	  * @param bm
     * @param fileName 要保存的文件名
     * @param isfileNameAFullName filename是完整路径+名字吗
     * @throws IOException
     */  
    public static boolean saveBitmapToFile(String path,Bitmap bm, String fileName,boolean isfileNameAFullName) throws IOException {
        
    	/*File rootFile = new File(Contant.mulu_root);
    	if(!rootFile.exists()){
    		rootFile.mkdir();
    	}*/
    	
    	File dirFile = new File(path);
        if(!dirFile.exists()){  
            dirFile.mkdir();  
        }  
        
        File myCaptureFile;
        if(isfileNameAFullName){
        	myCaptureFile = new File(fileName);
        }else{
        	myCaptureFile = new File(path + fileName);
        }
       
        if(myCaptureFile !=null){
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        if(bos !=null && bm!=null){
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();  
        bos.close();  
        
        return true;
        }
        }
		return false;
    } 
    
  
	
	

	/**
	 * 判断文件或文件夹是否存在*/
	public static boolean isFileExists(String path){
        try{
                File f=new File(path);
                if(!f.exists()){
                        return false;
                }
                
        }catch (Exception e) {
        	e.printStackTrace();
                return false;
        }
        return true;
	}

    /**
     * 判断文件或文件夹是否存在*/
    public static boolean isFileExists(File f){
        if(f==null)
            return false;
        try{
            if(!f.exists()){
                return false;
            }

        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * 获取文件夹内所有一级目录下的文件和文件夹
     * @param path 需要获取内部文件列表的目录
     * @param fileType 枚举类型，可以指定只获取某些格式的文件
     * @return 文件（夹）路径的集合
     */
    public static List<String> getAllFilePathFromSD(String path,FileType fileType) {
	 // 文件列表
	 List<String> fileList = new ArrayList<String>();

	 try{
	 // 得到该路径文件夹下所有的文件
	  File mfile = new File(path);
	  File[] files = mfile.listFiles();
	  if(files==null){
		  return fileList;
	  }

	 // 将所有的文件存入ArrayList中,并过滤所有图片格式的文件
	 for (int i = 0; i < files.length; i++) {
	  File file = files[i];
         if(fileType == FileType.IMG) {
             if (checkIsImageFile(file.getPath())) {
                 fileList.add(file.getPath());
             }
         }else if(fileType== FileType.ALL){
             fileList.add(file.getPath());
         }else{
             fileList.add(file.getPath());
         }

	 }
	 }catch(RuntimeException e){
		 e.printStackTrace();
	 }

	 // 返回得到的文件列表
	 return fileList;

	}


    /**
     *获取指定目录下 指定格式的枚举
     */
    public enum FileType{
        ALL,IMG,
    }


    /**
     * 检查扩展名，得到图片格式的文件
     * @param fName
     * @return
     */
    public static boolean checkIsImageFile(String fName) {
	 boolean isImageFile = false;

	 // 获取扩展名
	 String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
	   fName.length()).toLowerCase();
	 if (FileEnd.equals("jpg") || FileEnd.equals("gif")
	   || FileEnd.equals("png") || FileEnd.equals("jpeg")
	   || FileEnd.equals("bmp")) {
	  isImageFile = true;
	 } else {
	  isImageFile = false;
	 }

	 return isImageFile;

	}
	
	
	 /** 
     * 创建文件夹 
     * @param path 要创建的根目录
     */ 
	public static void createFileDirectory(String path){
		 File dirFirstFile=new File(path);//新建一级主目录
         if(!dirFirstFile.exists()){//判断文件夹目录是否存在
              dirFirstFile.mkdir();//如果不存在则创建
         }
	}
	
	
	/** 
     * 指定目录为非媒体文件夹
     *  
     * @param path .nomedia放置的的目录
     */ 
	public static void createNomediaFile(String path){
		try {
			boolean bb = new File(path, ".nomedia").createNewFile();//false为已经创建过了
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	
}

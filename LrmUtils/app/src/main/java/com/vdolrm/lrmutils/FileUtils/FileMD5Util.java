package com.vdolrm.lrmutils.FileUtils;

import android.text.TextUtils;
import android.util.Log;

import com.vdolrm.lrmutils.LogUtils.MyLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 获取文件md5值
 *
 */
public class FileMD5Util {
    private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    private static MessageDigest messageDigest = null;
    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 根据文件路径计算文件的MD5
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(String fileName) throws IOException {
        if(TextUtils.isEmpty(fileName)) {
            MyLog.e(MyLog.printSimpleBaseInfo()+"fileName is Empty");
            return "";
        }
        File f = new File(fileName);
        if(FileUtil.isFileExists(f)) {
            return getFileMD5String(f);
        }else{
            MyLog.e(MyLog.printSimpleBaseInfo()+"fileName is not exist");
            return "";
        }
    }

    /**
     * 根据文件对象计算文件的MD5
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                file.length());
        messageDigest.update(byteBuffer);
        return bufferToHex(messageDigest.digest());
    }
 
    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }
 
    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }
 
    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
 
   
}

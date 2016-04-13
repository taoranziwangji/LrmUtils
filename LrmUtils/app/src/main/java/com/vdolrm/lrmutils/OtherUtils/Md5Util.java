package com.vdolrm.lrmutils.OtherUtils;


import java.security.MessageDigest;

public class Md5Util {
    /**
     * 加密为MD5
     *
     * @param content
     * @return
     */
    public static String getMD5(String content) {

        try {

            MessageDigest digest = MessageDigest.getInstance("MD5");

            digest.update(content.getBytes());

            return getHashString(digest);


        } catch (Exception e) {

        }

        return null;

    }

    private static String getHashString(MessageDigest digest) {

        StringBuilder builder = new StringBuilder();

        for (byte b : digest.digest()) {

            builder.append(Integer.toHexString((b >> 4) & 0xf));

            builder.append(Integer.toHexString(b & 0xf));

        }

        return builder.toString().toLowerCase();

    }
}

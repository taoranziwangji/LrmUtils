package com.vdolrm.lrmutils.OtherUtils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

/**
 * 模糊匹配工具类
 * Created by vdo on 16/10/9.
 */

public class MatchBlurUtil {
    /**
     * 对关键字进行遍历,取每个字匹配到content中,只要包含这个关键字就标红
     *
     * @param keyWord 关键字
     * @param content 待标红的内容串
     * @return 标红后的字符串
     */
    public static SpannableString blurMatch(int color, String keyWord, String content) {
        /* int flags：取值有如下四个
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE：前后都不包括，即在指定范围的前面和后面插入新字符都不会应用新样式
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE ：前面不包括，后面包括。即仅在范围字符的后面插入新字符时会应用新样式
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE ：前面包括，后面不包括。
                Spannable.SPAN_INCLUSIVE_INCLUSIVE ：前后都包括。*/

        if (com.vdolrm.lrmutils.OtherUtils.StringUtils.isNotEmpty(keyWord)) {
            try {

                SpannableString styledText = new SpannableString(content);

                for (int i = 0; i < keyWord.length(); i++) {//关键字拆词依次匹配
                    String keyWordSub = keyWord.substring(i, i + 1);
                    int index = 0;
                    while (index > -1) {//寻遍遍历name,把所有的带关键字(由于加入了拆词,所以为keyWordSub)的字体都设为特殊颜色
                        index = content.indexOf(keyWordSub, index);
                        Log.d("lrm", "index=" + index + ",keyword length=" + (keyWordSub.length()) + ",length=" + (index + keyWordSub.length()));
                        if (index > -1) {
                            //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
                            styledText.setSpan(new ForegroundColorSpan(color), index, index + keyWordSub.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

                            index++;
                        } else {
                            break;
                        }
                    }
                }
                return styledText;
            } catch (RuntimeException e) {
                e.printStackTrace();
                return SpannableString.valueOf(content);
            }

        } else {
            return SpannableString.valueOf(content);
        }
    }
}

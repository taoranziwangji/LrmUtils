/*
 * Copyright (C) 2013 Antarix Tandon
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vdolrm.lrmutils.htmltextview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.text.TextUtils;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class UrlImageGetter implements ImageGetter {
	
	public abstract Drawable zoomDrawable(Drawable drawable);
	
    private Context c;
    private TextView container;

    /**
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     *
     * @param t
     * @param c
     */
    public UrlImageGetter(TextView t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        UrlDrawable urlDrawable = new UrlDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable which will asynchronously load the image specified in the src tag
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
        UrlDrawable urlDrawable;

        public ImageGetterAsyncTask(UrlDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            // set the correct bound according to the result from HTTP call
        	if(result == null)
        		return;
            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight());

            // change the reference of the current drawable to the result from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
     //       UrlImageGetter.this.container.invalidate();
            
            UrlImageGetter.this.container.requestLayout();
            UrlImageGetter.this.container.invalidate();
            UrlImageGetter.this.container.setText(UrlImageGetter.this.container.getText());
        }

        /**
         * Get the Drawable from URL
         *
         * @param urlString
         * @return
         */
        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = fetch(urlString);
                if(is==null){
                    return null;
                }
                Drawable drawable = Drawable.createFromStream(is, "src");
               // drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
               // return drawable;
                
                /*if(Contant.pingmu_width<500){//480
	                Drawable re = ImageProcessUtil.zoomDrawable(c,drawable, (int)(drawable.getIntrinsicWidth()*0.4), (int)(drawable.getIntrinsicHeight()*0.4));
	                re.setBounds(0, 0, 0 + re.getIntrinsicWidth(), 0 + re.getIntrinsicHeight());
	                return re;
                }else if(Contant.pingmu_width >=500 &&Contant.pingmu_width<700){//540
                	Drawable re = ImageProcessUtil.zoomDrawable(c,drawable, (int)(drawable.getIntrinsicWidth()*0.5), (int)(drawable.getIntrinsicHeight()*0.5));
                    re.setBounds(0, 0, 0 + re.getIntrinsicWidth(), 0 + re.getIntrinsicHeight());
                    return re;
                }else if(Contant.pingmu_width >=700 &&Contant.pingmu_width<1000){//720
                	Drawable re = ImageProcessUtil.zoomDrawable(c,drawable, (int)(drawable.getIntrinsicWidth()*0.7), (int)(drawable.getIntrinsicHeight()*0.7));
                    re.setBounds(0, 0, 0 + re.getIntrinsicWidth(), 0 + re.getIntrinsicHeight());
                    return re;
                }else if(Contant.pingmu_width >=1000){
                	Drawable re = ImageProcessUtil.zoomDrawable(c,drawable, (int)(drawable.getIntrinsicWidth()*1.3), (int)(drawable.getIntrinsicHeight()*1.3));
                    re.setBounds(0, 0, 0 + re.getIntrinsicWidth(), 0 + re.getIntrinsicHeight());
                    return re;
                }else{
                	drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0 + drawable.getIntrinsicHeight());
                	return drawable;
                }*/
                
                return zoomDrawable(drawable);
                
            } catch (Exception e) {
                return null;
            }
        }

        private InputStream fetch(String urlString) throws  IOException {
            if(TextUtils.isEmpty(urlString))return null;
//            DefaultHttpClient httpClient = new DefaultHttpClient();
//            HttpGet request = new HttpGet(urlString);
//            HttpResponse response = httpClient.execute(request);
//            return response.getEntity().getContent();

            //TODO android23去掉了httpclient 改用httpurlconnection 没测试
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            return in;
        }
    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }
    }

	
} 

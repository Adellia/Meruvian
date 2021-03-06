package com.lolaadellia.meruvian.task;

import android.content.Context;
import android.os.AsyncTask;

import com.lolaadellia.meruvian.R;
import com.lolaadellia.meruvian.entity.News;
import com.lolaadellia.meruvian.rest.RestVariables;
import com.lolaadellia.meruvian.service.ConnectionUtil;
import com.lolaadellia.meruvian.service.TaskService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Hanum on 24/12/2016.
 */

public class NewsPostTask extends AsyncTask<News, Void, JSONObject> {
    private TaskService service;
    private Context context;

    public NewsPostTask(TaskService service, Context context) {
        this.service = service;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(RestVariables.NEWS_POST_TASK);
    }

    @Override
    protected JSONObject doInBackground(News... params) {

        JSONObject json = new JSONObject();

        try {
            json.put("id", 0);
            json.put("title", params[0].getTitle());
            json.put("content", params[0].getContent());
            json.put("createDate", 0);
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.getHttpParams(15000, 15000));
            HttpPost httpPost = new HttpPost(RestVariables.SERVER_URL);
            httpPost.addHeader(new BasicHeader("Content-Type", "application/json"));
            httpPost.setEntity(new StringEntity(json.toString()));
            HttpResponse response = httpClient.execute(httpPost);
            json = new JSONObject(ConnectionUtil.convertEntityToString(response.getEntity()));
        } catch (IOException e) {
            e.printStackTrace();
            json = null;
        } catch (JSONException e) {
            e.printStackTrace();
            json = null;
        } catch (Exception e) {
            e.printStackTrace();
            json = null;
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                News news = new News();
                news.setId(jsonObject.getInt("id"));
                news.setContent(jsonObject.getString("content"));
                news.setTitle(jsonObject.getString("title"));
                news.setCreateDate(jsonObject.getLong("createDate"));
                service.onSuccess(RestVariables.NEWS_POST_TASK, news);
            } else {
                service.onError(RestVariables.NEWS_POST_TASK,
                        context.getString(R.string.failed_post_news));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(RestVariables.NEWS_POST_TASK,
                    context.getString(R.string.failed_post_news));
        }
    }
}

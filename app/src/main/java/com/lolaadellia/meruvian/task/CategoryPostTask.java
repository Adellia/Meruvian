package com.lolaadellia.meruvian.task;

import android.content.Context;
import android.os.AsyncTask;

import com.lolaadellia.meruvian.R;
import com.lolaadellia.meruvian.entity.Category;
import com.lolaadellia.meruvian.rest.CategoryVariables;
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
 * Created by Hanum on 27/12/2016.
 */

public class CategoryPostTask extends AsyncTask<Category, Void, JSONObject> {

    private TaskService service;
    private Context context;

    public CategoryPostTask(TaskService service, Context context) {
        this.service = service;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(CategoryVariables.CATEGORY_POST_TASK);
    }

    @Override
    protected JSONObject doInBackground(Category... params) {
        JSONObject json = new JSONObject();

        try {
            json.put("id", 0);
            json.put("name", params[0].getCategory());
            json.put("subCategory", params[0].getSubcategory());
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.getHttpParams(15000, 15000));
            HttpPost httpPost = new HttpPost(CategoryVariables.SERVER_URL);
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
                Category category = new Category();
                category.setCategory(jsonObject.getString("name"));
                category.setSubcategory(jsonObject.getString("subCategory"));
                service.onSuccess(CategoryVariables.CATEGORY_POST_TASK, category);
            } else {
                service.onError(CategoryVariables.CATEGORY_POST_TASK, context.getString(R.string.failed_post_category));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(CategoryVariables.CATEGORY_POST_TASK, context.getString(R.string.failed_post_category));
        }
    }


}

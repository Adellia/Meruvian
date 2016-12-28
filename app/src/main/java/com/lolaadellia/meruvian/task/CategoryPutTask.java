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
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Hanum on 27/12/2016.
 */

public class CategoryPutTask extends AsyncTask<Category, Void, JSONObject> {

    private Context context;
    private TaskService service;

    public CategoryPutTask(Context context, TaskService service) {
        this.context = context;
        this.service = service;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(CategoryVariables.CATEGORY_PUT_TASK);
    }

    @Override
    protected JSONObject doInBackground(Category... params) {
        JSONObject json = new JSONObject();
        try {
            json.put("id", params[0].getId());
            json.put("category", params[0].getCategory());
            json.put("subCategory", params[0].getSubcategory());
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.getHttpParams(15000, 15000));
            HttpPut httpPut = new HttpPut(CategoryVariables.SERVER_URL + "/" + params[0].getId());
            httpPut.addHeader(new BasicHeader("Content-Type", "application/json"));
            httpPut.setEntity(new StringEntity(json.toString()));
            HttpResponse response = httpClient.execute(httpPut);
            json = new JSONObject(ConnectionUtil.convertEntityToString(response.getEntity()));
        } catch (IOException e) {
            json = null;
            e.printStackTrace();
        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        } catch (Exception e) {
            json = null;
            e.printStackTrace();
        }
        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                Category category = new Category();
                category.setId(jsonObject.getInt("id"));
                category.setCategory(jsonObject.getString("name"));
                category.setSubcategory(jsonObject.getString("subCategory"));
                service.onSuccess(CategoryVariables.CATEGORY_PUT_TASK, category);
            } else {
                service.onError(CategoryVariables.CATEGORY_PUT_TASK, context.getString(R.string.failed_post_category));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(CategoryVariables.CATEGORY_PUT_TASK, context.getString(R.string.failed_post_category));
        }
    }
}

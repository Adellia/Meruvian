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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hanum on 27/12/2016.
 */

public class CategoryGetTask extends AsyncTask<String, Void, JSONArray> {

    public Context context;
    public TaskService service;

    public CategoryGetTask(Context context, TaskService service) {
        this.service = service;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(CategoryVariables.CATEGORY_GET_TASK);
    }

    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray json = null;
        try {
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.getHttpParams(15000, 15000));
            HttpGet httpGet = new HttpGet(CategoryVariables.SERVER_URL + "?title=" + params[0]);
            httpGet.setHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpGet);
            json = new JSONArray(ConnectionUtil.convertEntityToString(response.getEntity()));
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
    protected void onPostExecute(JSONArray jsonArray) {
        try {
            if (jsonArray.length() > 0) {
                List<Category> categories = new ArrayList<Category>();

                for (int index = 0; index < jsonArray.length(); index++) {
                    JSONObject json = jsonArray.getJSONObject(index);

                    Category category = new Category();
                    category.setId(json.getInt("id"));
                    category.setCategory(json.getString("name"));
                    category.setSubcategory(json.getString("subCategory"));

                    categories.add(category);
                }
                service.onSuccess(CategoryVariables.CATEGORY_GET_TASK, categories);
            } else {
                service.onError(CategoryVariables.CATEGORY_GET_TASK, context.getString(R.string.empty_category));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            service.onError(CategoryVariables.CATEGORY_GET_TASK, context.getString(R.string.failed_recieve_category));
        }
    }
}

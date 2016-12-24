package com.lolaadellia.meruvian.task;

import android.content.Context;
import android.os.AsyncTask;

import com.lolaadellia.meruvian.R;
import com.lolaadellia.meruvian.rest.RestVariables;
import com.lolaadellia.meruvian.service.ConnectionUtil;
import com.lolaadellia.meruvian.service.TaskService;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Hanum on 24/12/2016.
 */

public class NewsDeleteTask extends AsyncTask<String, Void, Boolean> {

    private TaskService service;
    private Context context;

    public NewsDeleteTask(TaskService service, Context context) {
        this.service = service;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        service.onExecute(RestVariables.NEWS_DELETE_TASK);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.getHttpParams(15000, 15000));
            HttpDelete httpDelete = new HttpDelete(RestVariables.SERVER_URL + "/" + params[0]);
            HttpResponse response = httpClient.execute(httpDelete);
            return response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean check) {
        if (check) {
            service.onSuccess(RestVariables.NEWS_DELETE_TASK, true);
        } else {
            service.onError(RestVariables.NEWS_DELETE_TASK, context.getString(R.string.failed_delete_news));
        }
    }
}

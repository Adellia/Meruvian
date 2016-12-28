package com.lolaadellia.meruvian;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lolaadellia.meruvian.adapter.NewsAdapter;
import com.lolaadellia.meruvian.content.database.adapter.NewsDatabaseAdapter;
import com.lolaadellia.meruvian.entity.Category;
import com.lolaadellia.meruvian.rest.CategoryVariables;
import com.lolaadellia.meruvian.service.TaskService;
import com.lolaadellia.meruvian.task.CategoryDeleteTask;
import com.lolaadellia.meruvian.task.CategoryGetTask;
import com.lolaadellia.meruvian.task.CategoryPostTask;
import com.lolaadellia.meruvian.task.CategoryPutTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CategoryActivity extends AppCompatActivity implements TaskService {

    private ListView listCategory;
    private Category news;
    private NewsDatabaseAdapter newsDatabaseAdapter;
    private EditText content;
    private EditText title;
    private NewsAdapter newsAdapter;

    private ProgressDialog progressDialog;
    private CategoryGetTask categoryGetTask;
    private CategoryPostTask categoryPostTask;
    private CategoryPutTask categoryPutTask;
    private CategoryDeleteTask categoryDeleteTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        listCategory = (ListView) findViewById(R.id.list_category);
        content = (EditText) findViewById(R.id.edit_content);
        title = (EditText) findViewById(R.id.edit_title);
        newsAdapter = new NewsAdapter(this, new ArrayList<Category>());
        listCategory.setAdapter(newsAdapter);

        listCategory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialogActions(i);

                return true;
            }
        });

        categoryGetTask = new CategoryGetTask(this, this);
        categoryGetTask.execute("");

    }

    private void dialogActions(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.action));
        builder.setItems(new String[]{getString(R.string.edit), getString(R.string.delete)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int location) {
                news = (Category) newsAdapter.getItem(position);

                if (news != null) {
                    if (location == 0) {
                        title.setText(news.getCategory());
                        content.setText(news.getSubcategory());

                        title.requestFocus();
                    } else if (location == 1) {
                        confirmDelete();
                    }
                }
            }
        });

        builder.create().show();
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.delete));
        builder.setMessage(getString(R.string.confirm_delete) + " '" + news.getCategory() + "' ?");
        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                categoryDeleteTask = new CategoryDeleteTask(CategoryActivity.this, CategoryActivity.this);
                categoryGetTask.execute(news.getId() + "");

                news = new Category();
            }
        });
        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                categoryGetTask = new CategoryGetTask(CategoryActivity.this, CategoryActivity.this);
                categoryGetTask.execute(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {

            if (news == null) {
                news = new Category();
            }

            news.setStatus(1);
            news.setSubcategory(content.getText().toString());
            news.setCategory(title.getText().toString());
            news.setCreateDate(new Date().getTime());

            if (news.getId() == -1) {
                categoryPostTask = new CategoryPostTask(this, this);
                categoryPostTask.execute(news);
            } else {
                categoryPutTask = new CategoryPutTask(this, this);
                categoryPutTask.execute(news);
            }

            news = new Category();
        } else if (item.getItemId() == R.id.action_refresh) {
            categoryGetTask = new CategoryGetTask(this, this);
            categoryGetTask.execute("");
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onExecute(int code) {
        if (progressDialog != null) {
            if (!progressDialog.isShowing()) {
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        if (categoryGetTask != null) {
                            categoryGetTask.cancel(true);
                        }
                    }
                });
                progressDialog.show();
            }
        } else {
            progressDialog = new ProgressDialog(this);
        }
    }

    @Override
    public void onSuccess(int code, Object result) {
        if (result != null) {
            if (code == CategoryVariables.CATEGORY_GET_TASK) {
                List<Category> newses = (List<Category>) result;
                newsAdapter.clear();
                newsAdapter.addNews(newses);
            } else if (code == CategoryVariables.CATEGORY_POST_TASK) {
                title.setText("");
                content.setText("");
                news = new Category();

                Category news = (Category) result;
                newsAdapter.addNews(news);
            } else if (code == CategoryVariables.CATEGORY_PUT_TASK) {
                title.setText("");
                content.setText("");
                news = new Category();

                categoryGetTask = new CategoryGetTask(this, this);
                categoryGetTask.execute("");
            } else if (code == CategoryVariables.CATEGORY_DELETE_TASK) {
                categoryGetTask = new CategoryGetTask(this, this);
                categoryGetTask.execute("");
            }
        }

        progressDialog.dismiss();

    }

    @Override
    public void onCancel(int code, String message) {
        progressDialog.dismiss();

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(int code, String message) {
        progressDialog.dismiss();

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }
}

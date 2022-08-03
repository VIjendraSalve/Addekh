package com.wht.addekho.Activties;

import static android.view.View.GONE;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wht.addekho.Adapter.TemplateListAdapter;
import com.wht.addekho.BaseActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.CheckNetwork;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.Model.Template;
import com.wht.addekho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TemplateListActivity extends BaseActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "intent123";
    PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    WindowManager.LayoutParams params;
    LinearLayout mainLayout;
    Button but;
    boolean click = true;
    FloatingActionButton btn_contact_us;


    private ArrayList<Template> productArrayList = new ArrayList<>();
    private ArrayList<Template> filteredproductArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private TemplateListAdapter mAdapter;
    private LinearLayout noRecordLayout, noConnectionLayout;
    private ProgressBar progressView, progressBar_endless;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page_count = 1, remainingCount = 0;
    private int shipFlag = 1;
    private Button btnRetry;
    //public static final String IMAGE_URL = "http://annadata.windhans.in/";
    private View retView;
    private Bundle bundle;
    private SearchView mSearchView;
    private String branch = "", flag = "", name = "", city = "", state = "";
    private Handler mHandler;
    private String query = "", vendor_type = "";
    private ImageView iv_gif_image;
    private EditText edt_ship_code;
    private String vendorID = "", image_path = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Templates");
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_baseline_arrow_back_24);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(upArrow);
        upArrow.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        noRecordLayout = (LinearLayout) findViewById(R.id.noRecordLayout);
        noConnectionLayout = (LinearLayout) findViewById(R.id.noConnectionLayout);
        btnRetry = (Button) findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);
        progressView = (ProgressBar) findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) findViewById(R.id.progressBar_endless);
        Log.d(TAG, "onCreate: 1");


    }

    private void getTemplate() {
        progressDialog.show();

        filteredproductArrayList.clear();
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        final Call<ResponseBody> result = api.PostTemplateList(
                SharedPref.getPrefs(TemplateListActivity.this, IConstant.USER_ID)
        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("my_tag", "onResponse11: " + output);
                    JSONObject jsonObject = new JSONObject(output);
                    boolean res = jsonObject.getBoolean("result");

                    // Log.d(TAG, "onResponse: " + object.getString("Message"));
                    if (res) {
                       /* remainingCount = Integer.parseInt(jsonObject.getString("remaining"));
                        Log.d(TAG, "onResponse: " + remainingCount);*/
                        image_path = jsonObject.getString("template_path");
                        JSONArray jsonArray = jsonObject.getJSONArray("templates");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                productArrayList.add(new Template(obj, image_path));
                                filteredproductArrayList.add(new Template(obj, image_path));
                            } catch (JSONException e) {
                                //Log.d("Progress Dialog","Progress Dialog");
                                e.printStackTrace();
                            }
                        }

                        progressDialog.dismiss();


                    } else {
                        progressDialog.dismiss();

                        progressView.setVisibility(GONE);
                        progressBar_endless.setVisibility(GONE);
                        noRecordLayout.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();


                    if (productArrayList.isEmpty()) {
                        noRecordLayout.setVisibility(View.VISIBLE);
                    } else {
                        noRecordLayout.setVisibility(GONE);
                    }
                    progressView.setVisibility(GONE);
                    progressBar_endless.setVisibility(GONE);
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressBar_endless.setVisibility(GONE);
                progressView.setVisibility(GONE);
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
            }
        });
    }

    public void check_connection() {
        if (CheckNetwork.isInternetAvailable(getApplicationContext()))  //if connection available
        {
            noConnectionLayout.setVisibility(GONE);
            noRecordLayout.setVisibility(GONE);
            init();
        } else {
          /*  Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.internet_not_avilable,
                    Snackbar.LENGTH_INDEFINITE).setAction("RETRY",
                    v -> check_connection()).show();*/
            noConnectionLayout.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        mHandler = new Handler();
        //bundle = getActivity().getIntent().getExtras();
        //    exhibitorsPojo = bundle.getParcelable(Constants.TOUR_PLAN_DATA);
        progressDialog = new ProgressDialog(TemplateListActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_template_list);

        mAdapter = new TemplateListAdapter(
                TemplateListActivity.this,
                filteredproductArrayList,
                vendor_type,
                vendorID);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && filteredproductArrayList.size() > 49) {
                        if (remainingCount > 0) {
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getTemplate();
                        }
                    }
                }
            }
        });


        productArrayList.clear();
        filteredproductArrayList.clear();
        mAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(GONE);
        getTemplate();

       /* recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && productArrayList.size() > 9) {
                        if (remainingCount > 0) {
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getTemplate();
                        }
                    }
                }
            }
        });

        productArrayList.clear();
        mAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(View.GONE);
        getTemplate();*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRetry) {
            //orderList.clear();
            check_connection();
        }
    }

    @Override
    public void onRefresh() {
        if (productArrayList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                productArrayList.clear();
                filteredproductArrayList.clear();
                mAdapter.notifyDataSetChanged();
                //page_count = dash1;count = dash1;
                page_count = 1;
                remainingCount = 0;
                //swipe=false;
                //getOrderList();
                getTemplate();
                //swipe=true;
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            progressBar_endless.setVisibility(GONE);
            remainingCount = 0;
            page_count = 1;
            //get_my_rides(2);
            getTemplate();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        page_count = 1;
        Log.d(TAG, "onCreate: 2");
        check_connection();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                //overridePendingTransition(R.animator.left_right, R.animator.right_left);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            //overridePendingTransition(R.animator.left_right, R.animator.right_left);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

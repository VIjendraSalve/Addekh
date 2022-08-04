package com.wht.addekho.Fragments;

import static android.view.View.GONE;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wht.addekho.Activties.CategorySearchActivity;
import com.wht.addekho.Activties.NotificationActivity;
import com.wht.addekho.Adapter.AdvertismentListAdapter;
import com.wht.addekho.Adapter.HomeCategoryAdapter;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Constant.IUrls;
import com.wht.addekho.Constant.Interface;
import com.wht.addekho.Helper.CheckNetwork;
import com.wht.addekho.Helper.ConnectionDetector;
import com.wht.addekho.Helper.Helper_Method;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.MainActivity;
import com.wht.addekho.Model.Advertisment;
import com.wht.addekho.Model.AllCategory;
import com.wht.addekho.Model.CategorySearchObject;
import com.wht.addekho.Notification.ActivityNotification;
import com.wht.addekho.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, HomeCategoryAdapter.OnItemClickListener {
    public static final int REQUEST_CODE = 1;
    private static final String TAG = "intent123";
    public static Activity _act;
    RelativeLayout rlSearch;
    View root;
    private ConnectionDetector connectionDetector;
    private TextView tvLocation;
    private ArrayList<CategorySearchObject> categoryObjectArrayList;
    private ArrayList<AllCategory> allCategoryArrayList = new ArrayList<>();
    private ArrayList<AllCategory> filteredallCategoryArrayList = new ArrayList<>();
    private String product_image = "";
    private RelativeLayout progress;
    //private RelativeLayout  noInternet;
    private RecyclerView rvCategory;
    private HomeCategoryAdapter homeCategoryAdapter;
    private GridLayoutManager gridLayoutManager;
    private RelativeLayout rlAreaSelections;
    private ArrayList<Advertisment> advertismentArrayList = new ArrayList<>();
    private ArrayList<Advertisment> filteredadvertismentArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private AdvertismentListAdapter mAdapter;
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
    private String query = "", areaID = "";
    private MainActivity mainActivity;
    private FragmentActivity getmAdapter;
    private HomeCategoryAdapter.OnItemClickListener listener;

    private EditText etWord;
    long delay = 1000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();
    private String keyWord = "All";

    ActivityResultLauncher<Intent> launchSomeActivity = null;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _act = getActivity();
        try {
            listener = (HomeCategoryAdapter.OnItemClickListener) _act;
        } catch (Exception e) {

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        _act = getActivity();
        connectionDetector = ConnectionDetector.getInstance(_act);
        Helper_Method.hideSoftInput(_act);
        rlSearch = root.findViewById(R.id.rlSearch);
        progress = (RelativeLayout) root.findViewById(R.id.progress);
        //noInternet = (RelativeLayout) root.findViewById(R.id.noInternet);
        rvCategory = (RecyclerView) root.findViewById(R.id.rvCategory);
        etWord = root.findViewById(R.id.etWord);

        progressDialog = new ProgressDialog(_act);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);

        ImageView iv_notifiction = root.findViewById(R.id.iv_notifiction);

        iv_notifiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i =  new Intent(_act, ActivityNotification.class);
                startActivity(i);
            }
        });



        etWord.addTextChangedListener(new TextWatcher() {
                                          @Override
                                          public void beforeTextChanged(CharSequence s, int start, int count,
                                                                        int after) {
                                          }

                                          @Override
                                          public void onTextChanged(final CharSequence s, int start, int before,
                                                                    int count) {
                                              //You need to remove this to run only once
                                              handler.removeCallbacks(input_finish_checker);

                                          }

                                          @Override
                                          public void afterTextChanged(final Editable s) {
                                              //avoid triggering event when text is empty
                                              if (s.length() > 0) {
                                                  keyWord = String.valueOf(s);
                                                  last_text_edit = System.currentTimeMillis();
                                                  handler.postDelayed(input_finish_checker, delay);
                                              } else {
                                                  keyWord = "";
                                                  getAdvertisment("",keyWord,"2");
                                              }
                                          }
                                      });

        getmAdapter = getActivity();
        rlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* Intent intent = new Intent(_act, CategorySearchActivity.class);
                startActivityForResult(intent, 900);
                _act.overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);*/
            }
        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_vendor_list);
        noRecordLayout = (LinearLayout) root.findViewById(R.id.noRecordLayout);
        noConnectionLayout = (LinearLayout) root.findViewById(R.id.noConnectionLayout);
        btnRetry = (Button) root.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = (ProgressBar) root.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) root.findViewById(R.id.progressBar_endless);
        launchSomeActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == 900) {
                            Intent data = result.getData();
                            Log.d("valueVIJ", "onActivityResult: " + data.getStringExtra("category_id"));
                            String categoryID = data.getStringExtra("category_id");
                            String categoryName = data.getStringExtra("category_name");
                            getAdvertisment(categoryID, "","1");
                            //etWord.setText(categoryName);
                            //Toast.makeText(mainActivity, ""+data.getStringExtra("category_id"), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        check_connection();

        return root;
    }

    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 600)) {
                // TODO: do what you need here
                // ............
                // ............
                getAdvertisment("", keyWord,"3");
                //Helper_Method.toaster(_act, "Webservice Call" + keyWord);
                //removePhoneKeypad();
            }
        }
    };

    private void PostEnquiryAPI() {

        progress.setVisibility(View.VISIBLE);
//        noInternet.setVisibility(View.INVISIBLE);
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.POSTCategoryList(
                SharedPref.getPrefs(_act, IConstant.USER_ID)
        );
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String output = "";
                try {
                    output = response.body().string();
                    Log.d("my_tag4534", "Response: " + output);
                    try {

                        JSONObject i = new JSONObject(output);
                        String stringCode = i.getString("result");

                        product_image = i.getString("category_path");

                        if (stringCode.equalsIgnoreCase("true")) {
                            JSONArray jsonArray = i.getJSONArray("categories");
                            for (int index = 0; index < jsonArray.length(); index++) {
                                try {
                                    //  page_no = 1;
                                    allCategoryArrayList.add(new AllCategory(jsonArray.getJSONObject(index), product_image));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    progress.setVisibility(View.INVISIBLE);
                                 //   noInternet.setVisibility(View.VISIBLE);

                                }
                            }

                            if (allCategoryArrayList.size() == 0) {

                                progress.setVisibility(View.INVISIBLE);
                           //     noInternet.setVisibility(View.VISIBLE);


                            } else {


                                if (allCategoryArrayList.size() > 3) {
                                    for (int j = 0; j < 3; j++) {
                                        filteredallCategoryArrayList.add(new AllCategory(allCategoryArrayList.get(j)));
                                    }
                                    filteredallCategoryArrayList.add(new AllCategory("All", "", "R.drawable.ic_view_all"));
                                } else {
                                    filteredallCategoryArrayList.addAll(allCategoryArrayList);
                                    filteredallCategoryArrayList.add(new AllCategory("All", "", "R.drawable.ic_view_all"));

                                }

                                homeCategoryAdapter = new HomeCategoryAdapter(getContext(), filteredallCategoryArrayList, new HomeCategoryAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(String id) {
                                        Log.d(TAG, "onItemClick: "+id);
                                        if(!id.equals("-1")) {
                                            getAdvertisment(id, "","4");
                                        }else {
                                            //getAdvertisment(id, "");
                                            Intent intent = new Intent(_act, CategorySearchActivity.class);
                                            launchSomeActivity.launch(intent);
                                            _act.overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
                                        }
                                    }
                                });



                                gridLayoutManager = new GridLayoutManager(getActivity(), 4);
                                gridLayoutManager.setOrientation(RecyclerView.VERTICAL); // set Horizontal Orientation
                                // categoryObjectArrayList.add(new HomeCategoryObj("MM", "More", "http://the-best.co.in/backend/upload/category/dot.png"));
                                rvCategory.setLayoutManager(gridLayoutManager);
                                rvCategory.setAdapter(homeCategoryAdapter);
                                homeCategoryAdapter.notifyDataSetChanged();
                                progress.setVisibility(View.INVISIBLE);
                         //       noInternet.setVisibility(View.INVISIBLE);
                                rvCategory.setVisibility(View.VISIBLE);

                            }


                        } else {


                            progress.setVisibility(View.INVISIBLE);
                      //      noInternet.setVisibility(View.VISIBLE);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                        progress.setVisibility(View.INVISIBLE);
                       // noInternet.setVisibility(View.VISIBLE);


                    }

                } catch (IOException e) {
                    e.printStackTrace();

                    progress.setVisibility(View.INVISIBLE);
                 //   noInternet.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Helper_Method.toaster(_act, t.getMessage());
                Log.d("Error", "onFailure: " + t.getMessage());

                if (t instanceof NetworkErrorException)
                    Helper_Method.warnUser(_act, "Network Error", getString(R.string.error_network), true);
                else if (t instanceof IOException)
                    Helper_Method.warnUser(_act, "Connection Error", getString(R.string.error_network), true);
                    //else if (t instanceof ServerError)
                    //   Helper_Method.warnUser(_act, "Server Error", getString(R.string.error_server), true);
                else if (t instanceof ConnectException)
                    Helper_Method.warnUser(_act, "No Connection Error", getString(R.string.error_connection), true);
                    //else if (t instanceof ConnectException)
                    //Helper_Method.warnUser(_act, "No Connection Error", getString(R.string.error_connection), true);
                else if (t instanceof TimeoutException)
                    Helper_Method.warnUser(_act, "Timeout Error", getString(R.string.error_timeout), true);
                else if (t instanceof SocketTimeoutException)
                    Helper_Method.warnUser(_act, "Timeout Error", getString(R.string.error_timeout), true);
                else
                    Helper_Method.warnUser(_act, "Unknown Error", getString(R.string.error_something_wrong), true);

            }
        });
    }

    private void getAdvertisment(String categoryID, String search, String id) {
        progressDialog.show();

        Log.d("getAdvertisment", "USER_ID: "+SharedPref.getPrefs(_act, IConstant.USER_ID));
        Log.d("getAdvertisment", "getAdvertisment: "+categoryID);
        Log.d("getAdvertisment", "getAdvertisment: "+search);
        Log.d("getAdvertisment", "id: "+id);


        filteredadvertismentArrayList.clear();
        advertismentArrayList.clear();
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostAddvertismentList(
                SharedPref.getPrefs(_act, IConstant.USER_ID),
                "2",
                categoryID,
                search
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
                        String stores_path = jsonObject.getString("stores_path");
                        String advt_path = jsonObject.getString("advt_path");
                        JSONArray jsonArray = jsonObject.getJSONArray("my_advertisements");
                        // Parsing json
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                advertismentArrayList.add(new Advertisment(obj, stores_path, advt_path));
                                filteredadvertismentArrayList.add(new Advertisment(obj, stores_path, advt_path));
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


                    if (filteredadvertismentArrayList.isEmpty()) {
                        noRecordLayout.setVisibility(View.VISIBLE);
                    } else
                        noRecordLayout.setVisibility(GONE);

                    mAdapter = new AdvertismentListAdapter(
                            _act,
                            filteredadvertismentArrayList);
                    recyclerView.setAdapter(mAdapter);
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
        if (CheckNetwork.isInternetAvailable(_act))  //if connection available
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




        mAdapter = new AdvertismentListAdapter(
                _act,
                filteredadvertismentArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(_act);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && filteredadvertismentArrayList.size() > 9) {
                        if (remainingCount > 0) {
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getAdvertisment("","","5");
                        }
                    }
                }
            }
        });


        advertismentArrayList.clear();
        filteredadvertismentArrayList.clear();
        mAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(GONE);
        PostEnquiryAPI();
        getAdvertisment("", "","6");


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
        if (advertismentArrayList.size() != 0) {
            int firstVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
            if (firstVisibleItemPosition == 0) {
                swipeRefreshLayout.setRefreshing(true);
                advertismentArrayList.clear();
                filteredadvertismentArrayList.clear();
                mAdapter.notifyDataSetChanged();
                //page_count = dash1;count = dash1;
                page_count = 1;
                remainingCount = 0;
                //swipe=false;
                //getOrderList();
                getAdvertisment("", "","7");
                //swipe=true;
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            progressBar_endless.setVisibility(GONE);
            remainingCount = 0;
            page_count = 1;
            //get_my_rides(2);
            getAdvertisment("", "","8");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        page_count = 1;

    }

    @Override
    public void onItemClick(String id) {
        Toast.makeText(_act, "11"+id, Toast.LENGTH_SHORT).show();
        /*if(!id.equals("-1")) {
            getAdvertisment(id, "");
        }else {
            Intent intent = new Intent(_act, CategorySearchActivity.class);
            startActivityForResult(intent, 900);
            _act.overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.activity_stay);
        }*/
    }
}
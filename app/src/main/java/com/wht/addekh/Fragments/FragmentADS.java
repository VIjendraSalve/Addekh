package com.wht.addekh.Fragments;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wht.addekh.Adapter.AdvertismentListAdapter;
import com.wht.addekh.Adapter.MyAdvertismentListAdapter;
import com.wht.addekh.Constant.IConstant;
import com.wht.addekh.Constant.IUrls;
import com.wht.addekh.Constant.Interface;
import com.wht.addekh.Helper.CheckNetwork;
import com.wht.addekh.Helper.SharedPref;
import com.wht.addekh.MainActivity;
import com.wht.addekh.Model.Advertisment;
import com.wht.addekh.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentADS extends Fragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private Button mBtnDiscoverAds, btnAdd;
    private MainActivity mainActivity;

    private View view;

    private static final String TAG = "intent123";
    PopupWindow popUp;
    LinearLayout layout;
    TextView tv;
    WindowManager.LayoutParams params;
    LinearLayout mainLayout;
    Button but;
    boolean click = true;
    FloatingActionButton btn_contact_us;
    private ArrayList<Advertisment> advertismentArrayList = new ArrayList<>();
    private ArrayList<Advertisment> filteredadvertismentArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private MyAdvertismentListAdapter mAdapter;
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
    private ImageView iv_gif_image;
    private EditText edt_ship_code;
    private ArrayList<String> vendorGstRegistered = new ArrayList<>();
    private Spinner spnr_yes_no;
    private String completed = "1"; // 3: completed, 1: pending


    public FragmentADS() {
        // Required empty public constructor
    }
    public static  FragmentADS newInstanse() {
        FragmentADS fragmentADS = new FragmentADS();
        return fragmentADS;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity=(MainActivity)context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);


    }

    private void initViews(View view) {
        mBtnDiscoverAds = view.findViewById(R.id.btnDiscover_ads);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_fragment_ads, container, false);

        noRecordLayout = (LinearLayout) view.findViewById(R.id.noRecordLayout);
        noConnectionLayout = (LinearLayout) view.findViewById(R.id.noConnectionLayout);
        btnRetry = (Button) view.findViewById(R.id.btnRetry);
        btnRetry.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        // swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(this);

        progressView = (ProgressBar) view.findViewById(R.id.progress_view);
        progressBar_endless = (ProgressBar) view.findViewById(R.id.progressBar_endless);


        // Inflate the layout for this fragment
        return view;
    }

    private void getAdvertisment() {
        progressDialog.show();
        Log.d(TAG, "getVendor: " + areaID);
        filteredadvertismentArrayList.clear();
        Interface api = IUrls.getRetrofit(IUrls.BASE_URL).create(Interface.class);
        Call<ResponseBody> result = api.PostAddvertismentList(
                SharedPref.getPrefs(mainActivity, IConstant.USER_ID),
                "1",
                "",
                query,
                String.valueOf(SharedPref.getPrefs(getActivity(),IConstant.MyCurrentLatitude)),
                String.valueOf(SharedPref.getPrefs(getActivity(),IConstant.MyCurrentLongitude))
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
                        String stores_path =  jsonObject.getString("stores_path");
                        String advt_path =  jsonObject.getString("advt_path");
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
        if (CheckNetwork.isInternetAvailable(mainActivity))  //if connection available
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
        progressDialog = new ProgressDialog(mainActivity);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        progressDialog.setCancelable(true);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_vendor_list);
        mAdapter = new MyAdvertismentListAdapter(mainActivity, filteredadvertismentArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mainActivity);
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
                            getAdvertisment();
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
        getAdvertisment();

       /* recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition >= recyclerView.getAdapter().getItemCount() - 1 && advertismentArrayList.size() > 9) {
                        if (remainingCount > 0) {
                            page_count++;
                            progressBar_endless.setVisibility(View.VISIBLE);
                            getAdvertisment();
                        }
                    }
                }
            }
        });

        advertismentArrayList.clear();
        mAdapter.notifyDataSetChanged();
        progressView.setVisibility(View.VISIBLE);
        progressBar_endless.setVisibility(View.GONE);
        getAdvertisment();*/
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
                getAdvertisment();
                //swipe=true;
            }
        } else {
            swipeRefreshLayout.setRefreshing(false);
            progressBar_endless.setVisibility(GONE);
            remainingCount = 0;
            page_count = 1;
            //get_my_rides(2);
            getAdvertisment();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        page_count = 1;
        check_connection();
    }


}
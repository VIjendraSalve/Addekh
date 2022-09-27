package com.wht.addekh.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IUrls
{

    public static Dispatcher dispatcher;
    public static final String BASE_URL = "http://wetap.in/addekh/";

    public static final String URL_PROFILE = "update_profile";
    public static final String URL_LOGIN = "user_login";
    public static final String URL_OTP_VERIFY = "otp_verification";
    public static final String URL_RESEND_OTP = "resend_otp";
    public static final String URL_CATEGORY = "user_dashboard";
    public static final String URL_CATEGORY_LISTING_SEARCH = "category_listing";
    public static final String URL_MYSTORE_LISTING = "my_store";
    public static final String URL_ADDMYSTORE = "add_update_store";
    public static final String URL_ADDMYADDS = "add_update_advertisement";

    public static final String URL_PROFILE_UPDATE = "app_update_profile";
    public static final String URL_PROFILE_UPDATE_IMAGE = "app_update_profile_image";
    public static final String URL_POLL = "app_polls_question";
    public static final String URL_APPLY_VOTE = "app_polls_apply_vote";
    public static final String URL_ABOUT_US = "app_about_us";

    public static final String URL_FORGET_PASSWORD = "vendor_app_vendor_forget_password";
    public static final String URL_CHANGE_PASSWORD = "vendor_app_change_password";
    public static final String URL_BLOG_CATEGORY = "vendor_app_blogs_categories";
    public static final String URL_BLOGLIST = "vendor_app_blog_crud";
    public static final String URL_SUBCATEGORY = "get_categories_by_level";
    public static final String URL_Brands = "get_brands";
    public static final String URL_Product = "get_products";
    public static final String URL_TEMPLATE_LIST = "app_get_template";
    public static final String URL_CREATE_BANNER = "app_create_template";
    public static final String URL_BANNER_LIST = "app_my_template";
    public static final String URL_MY_ADDS = "advertisement_list";
    public static final String URL_ADDS_STORE_WISE = "advertisement_list";
    public static final String URL_ADDS_FavouriteADD = "advt_favourite_unfavourite";
    public static final String URL_Report_ADD = "report_store_adv";
    public static final String URL_MY_FAVOURITE = "my_favourite_advts";
    public static final String URL_TEMPLATE_CATEGORY = "app_template_categories";
    public static final String URL_ADMIN_TEMPLATE = "app_offer_template_list";
    public static final String URL_ADD_TEMPLATE_FIELDS = "add_update_advertisement";




    public static Retrofit getRetrofit(String BASE_URL) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(50, TimeUnit.SECONDS);
        httpClient.readTimeout(50, TimeUnit.SECONDS);
        httpClient.writeTimeout(50, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);
        Retrofit.Builder builder = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL);


        dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        httpClient.dispatcher(dispatcher);

        Retrofit retrofit = builder.client(httpClient.build()).build();
        Interface service = retrofit.create(Interface.class);
        return retrofit;
    }

    private static Retrofit getRetroClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static Interface getApiService() {
        return getRetroClient().create(Interface.class);
    }

}

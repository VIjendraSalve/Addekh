package com.wht.addekho.Constant;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by User on 2/15/2020.
 */

public interface Interface {


//    @GET(IUrls.URL_BANNER)
//    Call<ResponseBody> PostImageBanner();
//
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_NEWS_LIST)
//    public Call<ResponseBody> PostNewsList(
//            @Field("category_id") String category_id,
//            @Field("search") String search,
//            @Field("page_no") int page_no);


//    @FormUrlEncoded
//    @POST(IUrls.URL_USERLIST)
//    public Call<ResponseBody> PostUserList(
//            @Field("type") String type,
//            @Field("taluka_id") String taluka_id,
//            @Field("village_id") String village_id);
//
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_NEWS_VIDEOLIST)
//    public Call<ResponseBody> PostNewsVideoList(
//            @Field("category_id") String category_id,
//            @Field("search") String search,
//            @Field("page_no") int page_no);

    @FormUrlEncoded
    @POST(IUrls.URL_POLL)
    public Call<ResponseBody> PostPollList(
            @Field("search") String search,
            @Field("page_no") int page_no,
            @Field("user_id") String user_id
    );


    @FormUrlEncoded
    @POST(IUrls.URL_APPLY_VOTE)
    public Call<ResponseBody> PostPollApplyVote(
            @Field("question_id") String question_id,
            @Field("question_option_answer") String question_option_answer,
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(IUrls.URL_TEMPLATE_LIST)
    public Call<ResponseBody> PostTemplateList(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST(IUrls.URL_BANNER_LIST)
    public Call<ResponseBody> PostBannerList(
            @Field("user_id") String user_id
    );

    @Multipart
    @POST(IUrls.URL_CREATE_BANNER)
    Call<ResponseBody> POSTCreateBanner   (@Part("user_id") RequestBody user_id,
                                               @Part("template_id") RequestBody template_id,
                                               @Part("businessname") RequestBody businessname,
                                               @Part("email") RequestBody email,
                                               @Part("mobile") RequestBody mobile,
                                               @Part("discountType") RequestBody discountType,
                                               @Part("discountAmount") RequestBody discountAmount,
                                               @Part("offerName") RequestBody offerName,
                                               @Part("offerDescription") RequestBody offerDescription,
                                               @Part("offerValidUpto") RequestBody offerValidUpto,
                                               @Part MultipartBody.Part file);





    @FormUrlEncoded
    @POST(IUrls.URL_PROFILE)
    public Call<ResponseBody> PostUpdateProfile(
            @Field("user_id") String user_id,
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("gender") String gender);


    @Multipart
    @POST(IUrls.URL_PROFILE)
    Call<ResponseBody> PostUpdateProfile(
            @Part("user_id") RequestBody user_id,
            @Part("first_name") RequestBody first_name,
            @Part("last_name") RequestBody last_name,
            @Part("email") RequestBody email,
            @Part("gender") RequestBody gender,
            @Part MultipartBody.Part store_logo);


    @FormUrlEncoded
    @POST(IUrls.URL_LOGIN)
    public Call<ResponseBody> PostLogin(
            @Field("contact_number") String contact_number);


    @FormUrlEncoded
    @POST(IUrls.URL_OTP_VERIFY)
    public Call<ResponseBody> PostOtpVerify(
            @Field("contact_number") String contact_number,
            @Field("otp_number") String otp_number,
            @Field("notification_token") String notification_token,
            @Field("device") String device,
            @Field("lat") String lat,
            @Field("lng") String lng);

    @FormUrlEncoded
    @POST(IUrls.URL_RESEND_OTP)
    public Call<ResponseBody> PostResendOtp(
            @Field("contact_number") String contact_number);

    @FormUrlEncoded
    @POST(IUrls.URL_CATEGORY)
    Call<ResponseBody> POSTCategoryList(
            @Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(IUrls.URL_CATEGORY_LISTING_SEARCH)
    Call<ResponseBody> PostCategoryCategorySearch(
            @Field("search") String search);



    @FormUrlEncoded
    @POST(IUrls.URL_MYSTORE_LISTING)
    Call<ResponseBody> PostMyStorelist(
            @Field("user_id") String user_id,
            @Field("search") String search,
            @Field("is_my_store") String is_my_store, // 1: my 0:all
            @Field("page_no") int page_no);


    @Multipart
    @POST(IUrls.URL_ADDMYSTORE)
    Call<ResponseBody> PostAddMystore(
            @Part("user_id") RequestBody user_id,
            @Part("store_name") RequestBody store_name,
            @Part("action") RequestBody action,
            @Part("category_id") RequestBody category_id,
            @Part("store_address") RequestBody store_address,
            @Part("store_contact_number") RequestBody store_contact_number,
            @Part("store_lat") RequestBody store_lat,
            @Part("store_lng") RequestBody store_lng,
            @Part("store_desc") RequestBody store_desc,
            @Part("store_id") RequestBody store_id,
            @Part MultipartBody.Part store_logo);

    @Multipart
    @POST(IUrls.URL_ADDMYADDS)
    Call<ResponseBody> PostAddMyAdvertisment(
            @Part("user_id") RequestBody user_id,
            @Part("action") RequestBody action,
            @Part("category_id") RequestBody category_id,
            @Part("advt_title") RequestBody advt_title,
            @Part("lat") RequestBody lat,
            @Part("lng") RequestBody lng,
            @Part("advt_desc") RequestBody advt_desc,
            @Part("tags") RequestBody tags,
            @Part("last_date") RequestBody last_date,
            @Part("store_id") RequestBody store_id,
            @Part MultipartBody.Part store_logo);



    @FormUrlEncoded
    @POST(IUrls.URL_PROFILE_UPDATE)
    public Call<ResponseBody> PostProfileUpdate(
            @Field("user_id") String user_id,
            @Field("full_name") String full_name,
            @Field("email") String email);



    @Multipart
    @POST(IUrls.URL_PROFILE_UPDATE_IMAGE)
    Call<ResponseBody> POSTUpdateProfileImage(@Part("user_id") RequestBody user_id,
                                              @Part MultipartBody.Part file);





    @FormUrlEncoded
    @POST(IUrls.URL_FORGET_PASSWORD)
    public Call<ResponseBody> PostForgetPassword(
            @Field("role_email") String role_email);

    @FormUrlEncoded
    @POST(IUrls.URL_CHANGE_PASSWORD)
    public Call<ResponseBody> PostChangePassword(
            @Field("role_id") String role_id,
            @Field("old_password") String old_password,
            @Field("new_password") String new_password);




    @GET(IUrls.URL_BLOG_CATEGORY)
    Call<ResponseBody> getBlogCategoryList();



    @FormUrlEncoded
    @POST(IUrls.URL_BLOGLIST)
    public Call<ResponseBody> PostBlogList(
            @Field("action") String action,
            @Field("user_id") String user_id,
            @Field("blog_category") String blog_category,
            @Field("blog_title") String blog_title,
            @Field("author") String author,
            @Field("description") String description,
            @Field("blog_id") String blog_id
    );








    @FormUrlEncoded
    @POST(IUrls.URL_SUBCATEGORY)
    Call<ResponseBody> POSTSubcategoryList(
            @Field("user_id") String user_id,
            @Field("category_id") String category_id,
            @Field("category_level") String category_level);

    @FormUrlEncoded
    @POST(IUrls.URL_Brands)
    Call<ResponseBody> POSTBrandList(
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(IUrls.URL_Product)
    Call<ResponseBody>POSTProductList(
            @Field("user_id") String user_id,
            @Field("category_id") String category_id,
            @Field("brand_id") String brand_id,
            @Field("search_val") String search_val,
            @Field("page_no") int page_no);


    @FormUrlEncoded
    @POST(IUrls.URL_MY_ADDS)
    public Call<ResponseBody> PostAddvertismentList(
            @Field("user_id") String user_id,
            @Field("action") String action, ///1: My 2: All
            @Field("category_id") String category_id,
            @Field("search") String search
    );

    @FormUrlEncoded
    @POST(IUrls.URL_ADDS_STORE_WISE)
    public Call<ResponseBody> PostAddvertismentListStoreWise(
            @Field("user_id") String user_id,
            @Field("action") String action, ///1: My 2: All
            @Field("category_id") String category_id,
            @Field("search") String search,
            @Field("page") int page,
            @Field("store_id") String store_id
    );

    @FormUrlEncoded
    @POST(IUrls.URL_ADDS_FavouriteADD)
    public Call<ResponseBody> PostAddvertismentAddToFavourite(
            @Field("user_id") String user_id,
            @Field("advt_id") String advt_id
    );

    @FormUrlEncoded
    @POST(IUrls.URL_MY_FAVOURITE)
    public Call<ResponseBody> PostMyFavourite(
            @Field("user_id") String user_id,
            @Field("page") int page,
            @Field("search") String search
    );


//    @FormUrlEncoded
//    @POST(IUrls.URL_OTP_CHECK)
//    Call<ResponseBody> getCheckOtp(
//            @Field("contact_number") String contact_number,
//            @Field("otp_number") String otp_number,
//            @Field("notification_token") String notification_token,
//            @Field("device") String device);


//    @FormUrlEncoded
//    @POST(IUrls.URL_RESEND_OTP)
//    public Call<ResponseBody> POSTResendOtp(
//            @Field("contact_number") String mobile);
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_STATE_LIST)
//    Call<ResponseBody> POSTStateList(
//            @Field("country_id") String country_id,
//            @Field("lang") String lang);
//
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_CITY_LIST)
//    Call<ResponseBody> POSTCityList(
//            @Field("state_id") String state_id,
//            @Field("lang") String lang);
//
//    @GET(IUrls.URL_RELATION_LIST)
//    Call<ResponseBody> getRelationList();
//
//
//    @GET(IUrls.URL_EDUCATIONAL_LIST)
//    Call<ResponseBody> getEducationalList();
//
//
//    @GET(IUrls.URL_PROFESSION_LIST)
//    Call<ResponseBody> getProfessionList();
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_APP_DASHBOARD)
//    Call<ResponseBody> POSTAppDashboard(
//            @Field("category_level") String category_level,
//            @Field("category_id") String category_id,
//            @Field("lang") String lang,
//            @Field("search") String search);
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_PRODUCT_LIST)
//    Call<ResponseBody> POSTAllProductList(
//            @Field("user_id") String user_id,
//            @Field("product_id") String product_id,
//            @Field("category_id") String category_id,
//            @Field("city_id") String city_id,
//            @Field("search") String search,
//            @Field("page_no") String page_no,
//            @Field("lang") String lang);
//
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_BUSINESS_DETAILS)
//    Call<ResponseBody> POSTBussinessDetails(
//            @Field("user_id") String user_id,
//            @Field("item_id") String item_id,
//            @Field("lang") String lang);
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_CENSUS_MEMBER_LIST)
//    Call<ResponseBody> POSTCensusMemberList(@Field("user_id") String user_id);
//
//
//    @FormUrlEncoded
//    @POST(IUrls.URL_ADD_CENSUS_MEMBER)
//    Call<ResponseBody> POSTAddMember(@Field("user_id") String user_id,
//                                     @Field("first_name") String first_name,
//                                     @Field("last_name") String last_name,
//                                     @Field("relation") String relation,
//                                     @Field("gender") String gender,
//                                     @Field("aadhar_card") String aadhar_card,
//                                     @Field("age") String age,
//                                     @Field("blood_group") String blood_group,
//                                     @Field("city") String city,
//                                     @Field("district") String district,
//                                     @Field("state") String state,
//                                     @Field("address") String address,
//                                     @Field("profession") String profession,
//                                     @Field("marital_status") String marital_status,
//                                     @Field("education") String education,
//                                     @Field("mobile_number") String mobile_number,
//                                     @Field("email_id") String email_id);



/*


    @GET(IUrls.URL_INSTRUCTIONS)
    Call<ResponseBody> getInstructions();

    @FormUrlEncoded
    @POST(IUrls.URL_MY_ORDERS)
    Call<ResponseBody> POSTMyOrder(@Field("user_id") String user_id);*/

   /*




    @FormUrlEncoded
    @POST(IUrls.URL_VARIANTS_LIST)
    Call<ResponseBody> POSTAllVariants(
            @Field("user_id") String user_id,
            @Field("category_id") String category_id);




    @FormUrlEncoded
    @POST(IUrls.URL_CATEGORY_WISE_PRODUCTS)
    Call<ResponseBody> POSTCategoryWiseProducts(
            @Field("user_id") String user_id,
            @Field("vendor_id") String vendor_id);





    @FormUrlEncoded
    @POST(IUrls.URL_REMOVE_FROM_CART)
    Call<ResponseBody> POSTRemoveFromCart(
            @Field("user_id") String user_id,
            @Field("vendor_id") String vendor_id,
            @Field("product_id") String item_id);


    @FormUrlEncoded
    @POST(IUrls.URL_CART_LIST)
    public Call<ResponseBody> POSTCartList(@Field("user_id") String user_id,
                                           @Field("vendor_id") String vendor_id);

    @FormUrlEncoded
    @POST(IUrls.URL_CART_COUNT)
    public Call<ResponseBody> POSTCartCount(@Field("user_id") String user_id,
                                            @Field("vendor_id") String vendor_id);


    @FormUrlEncoded
    @POST(IUrls.URL_PLACE_ORDER)
    Call<ResponseBody> POSTPlaceOrder(@Field("user_id") String user_id,
                                      @Field("vendor_id") String vendor_id,
                                      @Field("discount_price") String discount_price,
                                      @Field("gst_price") String gst_price,
                                      @Field("grand_total") String grand_total,
                                      @Field("total_price") String total_price,
                                      @Field("zone_id") String zone_id,
                                      @Field("region_id") String region_id,
                                      @Field("call_id") String call_id,
                                      @Field("order_type") String order_type,
                                      @Field("lat") String lat,
                                      @Field("lng") String lng,
                                      @Field("products") JSONArray jsonArray);






    @FormUrlEncoded
    @POST(IUrls.URL_CHANGE_PASSWORD)
    Call<ResponseBody> POSTChangePassword(@Field("user_id") String user_id,
                                          @Field("password") String password,
                                          @Field("new_password") String new_password);


    @FormUrlEncoded
    @POST(IUrls.URL_NEW_CATEGORY_LIST)
    Call<ResponseBody> POSTNewCategoryList(
            @Field("user_id") String user_id,
            @Field("category_id") String category_id);




    @FormUrlEncoded
    @POST(IUrls.URL_NEW_PRODUCT_LIST)
    Call<ResponseBody> POSTProductListByCategory(
            @Field("user_id") String user_id,
            @Field("vendor_id") String vendor_id,
            @Field("product_id") String product_id,
            @Field("category_id") String category_id,
            @Field("variant_id") String variant_id,
            @Field("search") String search,
            @Field("page_no") String page_no,
            @Field("zone_id") String zone_id);


    @FormUrlEncoded
    @POST(IUrls.URL_PREVIOUS_ORDER_LIST)
    Call<ResponseBody> POSTPreOrderList(@Field("user_id") String user_id,
                                        @Field("vendor_id") String vendor_id,
                                        @Field("search") String search,
                                        @Field("page_no") String page_no);


    @FormUrlEncoded
    @POST(IUrls.URL_LEAVE_LIST)
    Call<ResponseBody> POSTLeaveList(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(IUrls.URL_LEAVE_APPLICATION)
    Call<ResponseBody> POSTLeaveApplication(@Field("user_id") String user_id,
                                            @Field("leave_id") String billable,
                                            @Field("reason") String invoice,
                                            @Field("leave_from") String rate,
                                            @Field("leave_to") String task_id,
                                            @Field("return_date") String startdate);


    @FormUrlEncoded
    @POST(IUrls.URL_UPDATE_ORDER_STATUS)
    Call<ResponseBody> POSTUpdateOrderStatus(
            @Field("user_id") String user_id,
            @Field("order_id") String order_id,
            @Field("status") String status);



    @FormUrlEncoded
    @POST(IUrls.URL_ZONE_LIST)
    Call<ResponseBody> POSTZoneList(
            @Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(IUrls.URL_REGION_LIST)
    Call<ResponseBody> POSTRegionList(
            @Field("user_id") String user_id,
            @Field("zone_id") String zone_id,
            @Field("search") String search);


    @FormUrlEncoded
    @POST(IUrls.URL_AREA_LIST)
    Call<ResponseBody> POSTAreaList(
            @Field("user_id") String user_id,
            @Field("search") String search,
            @Field("zone_id") String zone_id,
            @Field("region_id") String region_id);


    @FormUrlEncoded
    @POST(IUrls.URL_CHECK_IN)
    public Call<ResponseBody> postCheckIn(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(IUrls.URL_CHECK_OUT)
    public Call<ResponseBody> postCheckOut(@Field("user_id") String user_id,
                                           @Field("reason_id") String reason_id,
                                           @Field("reason") String reason,
                                           @Field("checkin_id") String checkin_id);


    @FormUrlEncoded
    @POST(IUrls.URL_TIME_LINE)
    Call<ResponseBody> POSTTimeLine(@Field("user_id") String user_id,
                                    @Field("area_id") String area_id);

    @FormUrlEncoded
    @POST(IUrls.URL_NEW_TIME_LINE)
    Call<ResponseBody> POSTNewTimeLine(@Field("user_id") String user_id);


    @FormUrlEncoded
    @POST(IUrls.URL_SCHEDULE_CALL)
    Call<ResponseBody> POSTCreateSchedule(@Field("user_id") String user_id,
                                          @Field("zone_id") String zone_id,
                                          @Field("area_id") String area_id,
                                          @Field("area_name") String area_name);

    @FormUrlEncoded
    @POST(IUrls.URL_RESCHEDULE_REASON)
    Call<ResponseBody> POSTRescheduledReason(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST(IUrls.URL_RESCHEDULE_CALL)
    Call<ResponseBody> POSTRescheduleCall(@Field("user_id") String user_id,
                                          @Field("call_id") String call_id,
                                          @Field("vendor_id") String vendor_id,
                                          @Field("zone_id") String zone_id,
                                          @Field("area_id") String area_id,
                                          @Field("date") String date,
                                          @Field("reschedule_reason") String reschedule_reason);

    @FormUrlEncoded
    @POST(IUrls.URL_UPDATE_LOCATION)
    public Call<ResponseBody> updateLocation(
            @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(IUrls.URL_CHECKOUT_REASONS_LIST)
    Call<ResponseBody> POSTCheckoutReason(@Field("user_id") String user_id);*/





   /* ,
    @Field("vendors") JSONArray jsonArray*/



  /*











    @GET(IUrls.URL_ESTORE_DASHBOARD_BANNERS)
    Call<ResponseBody> getEstoreBanners();

    @GET(IUrls.URL_ESTORE_DASHBOARD_CATEGORY)
    Call<ResponseBody> getEstoreCategory();

    @GET(IUrls.URL_PRE_SCHOOL_EVENT_MANAGEMENT)
    Call<ResponseBody> getPreschoolEventBanner();

    @GET(IUrls.URL_PRE_SCHOOL_BRANDING_AND_MARKETING)
    Call<ResponseBody> getPreschoolEventBrandingAndMarketing();



    @GET(IUrls.URL_ESTORE_DASHBOARD_SUB_CATEGORY_PRODUCT)
    Call<ResponseBody> getEstoreSubCategoryProduct(
            @Query("category_id") String catId,
            @Query("subcat_id") String sub_catId
    );





    @GET(IUrls.URL_PRESCHOOL_OWNER_SMART_CIRRCULUM_CATEGORY)
    Call<ResponseBody> getPreschoolOwnerSmartCirriculumCategory(
            @Query("pre_owner_dash_id") String catId
    );

    @GET(IUrls.URL_PRESCHOOL_OWNER_SMART_CIRRCULUM_WEEK)
    Call<ResponseBody> getPreschoolOwnerSmartCirriculumCategoryWeeks(
            @Query("smart_curriculum_id") String catId
    );

    @GET(IUrls.URL_PRESCHOOL_OWNER_SMART_WORK_SHEET_PDF)
    Call<ResponseBody> getPreschoolOwnerSmartWorksheetPDF(
            @Query("smart_worksheet_id") String catId
    );

    @GET(IUrls.URL_PRESCHOOL_OWNER_SMART_TRAINING_OPTIONS)
    Call<ResponseBody> getPreschoolOwnerSmartTrainingOptions(
            @Query("pre_owner_dash_id") String catId
    );




    @FormUrlEncoded
    @POST(IUrls.URL_PRESCHOOLOWNER_REG)
    public Call<ResponseBody> registerPreschoolOwner(@Field("username") String username,
                                                     @Field("password") String password,
                                                     @Field("email") String email,
                                                     @Field("mobile") String mobile,
                                                     @Field("preschool_name") String preschool_name,
                                                     @Field("address") String address,
                                                     @Field("no_of_preschoolars_pg") String no_of_preschoolars_pg,
                                                     @Field("no_of_preschoolars_nur") String no_of_preschoolars_nur,
                                                     @Field("no_of_preschoolars_jkg") String no_of_preschoolars_jkg,
                                                     @Field("no_of_preschoolars_skg") String no_of_preschoolars_skg,
                                                     @Field("fees_range") String fees_range,
                                                     @Field("additional_services") String additional_services);

    @GET(IUrls.URL_PRESCHOOL_SMART_CURRICULUM_NEW_WEBCALL)
    Call<ResponseBody> getPreschoolOwnerSmartCurriculumNewWebcall(
            @Query("user_id") String user_id
    );

    @GET(IUrls.URL_PRESCHOOL_ESTORE_PRODUCT)
    Call<ResponseBody> getEstoreProductList();

    @GET(IUrls.URL_PRESCHOOL_ESTORE_PRODUCT_BY_CATEGORY_ID)
    Call<ResponseBody> getProductByCategoryId(
            @Query("category_id") String category_id
    );

    @GET(IUrls.URL_WORKSHEET_STUDY_MATERIAL_LIST)
    Call<ResponseBody> getWorksheetList(
            @Query("smart_worksheet_id") String smart_worksheet_id

    );

    @GET(IUrls.URL_PRESCHOOL_SMART_CURRICULUM_ALl_COURCE_IMAGES)
    Call<ResponseBody> getSmartCurriculamAllCourceImage(
            @Query("smart_curriculum_id") String smart_curriculum_id

    );

    @GET(IUrls.URL_SMART_CURICULAM_WEEK_LIST)
    Call<ResponseBody> getCuriculamWeekList(
            @Query("smart_curriculum_id") String smart_curriculum_id,
            @Query("session_id") String session_id
    );

    @GET(IUrls.URL_SMART_CURICULAM_WEEK_WISE_IMAGE)
    Call<ResponseBody> getWeekWiseImage(
            @Query("smart_curriculum_week_id") String smart_curriculum_week_id
    );

    @GET(IUrls.URL_SMART_ASSESSMENT_MANUAL)
    Call<ResponseBody> get3yrAssessmentManual(
            @Query("smart_assessment_id") String smart_assessment_id

    );



    @GET(IUrls.URL_SMART_ASSESSMENT_GRADING)
    Call<ResponseBody> getAssessmentGradient(
            @Query("smart_assessment_id") String smart_assessment_id

    );

    @GET(IUrls.URL_SMART_TRANING_BANNER)
    Call<ResponseBody> getSmartTraining(
            @Query("join_training_type_id") String join_training_type_id

    );


    @GET(IUrls.URL_SMART_ASSESSMENT_ORDER_KIT_VALUES)
    Call<ResponseBody> getAssessmentOrderValues();

    @GET(IUrls.URL_STATIONARY_LIST)
    Call<ResponseBody> getStatinaryList();

    @GET(IUrls.URL_MY_ORDER_CURRICULUM)
    Call<ResponseBody> getMyOrdersCurriculum(
            @Query("curriculum_user_id") String user_id,
            @Query("order_status") String order_status

    );


    @GET(IUrls.URL_MY_PREVIOUS_ORDER)
    Call<ResponseBody> getMyPreviousOrder(
            @Query("user_id") String user_id,
            @Query("login_type") String login_type,
            @Query("menu_id") String menu_id,
            @Query("order_status") String order_status

    );

    @GET(IUrls.URL_MY_PREVIOUS_ORDER_DETAILS)
    Call<ResponseBody> getMyPreviousOrderDetails(
            @Query("user_id") String user_id,
            @Query("login_type") String login_type,
            @Query("common_order_id") String common_order_id,
            @Query("order_status") String order_status

    );



    @GET(IUrls.URL_CANCEL_MY_ORDER)
    Call<ResponseBody> getCancelStatus(
            @Query("curriculum_user_id") String curriculum_user_id,
            @Query("curriculum_order_id") String curriculum_order_id,
            @Query("status") String order_status

    );

    @FormUrlEncoded
    @POST(IUrls.URL_SMART_CURICULAM_ORDER_DETAILS)

    public Call<ResponseBody> getCurriculamOrder(@Field("curriculum_user_id") String curriculum_user_id,
                                                 @Field("curriculum_order_person_name") String curriculum_order_person_name,
                                                 @Field("curriculum_order_person_contact") String curriculum_order_person_contact,
                                                 @Field("curriculum_order_person_email") String curriculum_order_person_email,
                                                 @Field("curriculum_order_person_address") String curriculum_order_person_address,
                                                 @Field("curriculum_order_person_delivery_address") String curriculum_order_person_delivery_address,
                                                 @Field("curriculum_pg_total_qty") String curriculum_pg_total_qty,
                                                 @Field("curriculum_pg_total_amount") String curriculum_pg_total_amount,
                                                 @Field("curriculum_nur_total_qty") String curriculum_nur_total_qty,
                                                 @Field("curriculum_nur_total_amount") String curriculum_nur_total_amount,
                                                 @Field("curriculum_jrkg_total_qty") String curriculum_jrkg_total_qty,
                                                 @Field("curriculum_jrkg_total_amount") String curriculum_jrkg_total_amount,
                                                 @Field("curriculum_srkg_total_qty") String curriculum_srkg_total_qty,
                                                 @Field("curriculum_srkg_total_amount") String curriculum_srkg_total_amount,
                                                 @Field("curriculum_total_amount") String curriculum_total_amount,
                                                 @Field("curriculum_discount_percentage") String curriculum_discount_percentage,
                                                 @Field("curriculum_breaking_price") String curriculum_breaking_price,
                                                 @Field("curriculum_delivery_charges") String curriculum_delivery_charges,
                                                 @Field("curriculum_base_amount") String curriculum_base_amount,
                                                 @Field("curriculum_total_items") String curriculum_total_items,
                                                 @Field("curriculum_cgst") String curriculum_cgst,
                                                 @Field("curriculum_sgst") String curriculum_sgst,
                                                 @Field("curriculum_payable_amount") String curriculum_payable_amount);


    @FormUrlEncoded
    @POST(IUrls.URL_SMART_WORKSHEET_ORDER_DETAILS)
    public Call<ResponseBody> getWorksheetOrder(@Field("worksheet_user_id") String worksheet_user_id,
                                                @Field("worksheet_order_person_name") String worksheet_order_person_name,
                                                @Field("worksheet_order_person_contact") String worksheet_order_person_contact,
                                                @Field("worksheet_order_person_email") String worksheet_order_person_email,
                                                @Field("worksheet_order_person_address") String worksheet_order_person_address,
                                                @Field("worksheet_order_person_delivery_address") String worksheet_order_person_delivery_address,
                                                @Field("worksheet_pg_total_qty") String worksheet_pg_total_qty,
                                                @Field("worksheet_pg_total_amount") String worksheet_pg_total_amount,
                                                @Field("worksheet_nur_total_qty") String worksheet_nur_total_qty,
                                                @Field("worksheet_nur_total_amount") String worksheet_nur_total_amount,
                                                @Field("worksheet_jrkg_total_qty") String worksheet_jrkg_total_qty,
                                                @Field("worksheet_jrkg_total_amount") String worksheet_jrkg_total_amount,
                                                @Field("worksheet_srkg_total_qty") String worksheet_srkg_total_qty,
                                                @Field("worksheet_srkg_total_amount") String worksheet_srkg_total_amount,
                                                @Field("worksheet_total_amount") String worksheet_total_amount,
                                                @Field("worksheet_discount_percentage") String worksheet_discount_percentage,
                                                @Field("worksheet_breaking_price") String worksheet_breaking_price,
                                                @Field("worksheet_delivery_charges") String worksheet_delivery_charges,
                                                @Field("worksheet_base_amount") String worksheet_base_amount,
                                                @Field("worksheet_total_items") String worksheet_total_items,
                                                @Field("worksheet_cgst") String worksheet_cgst,
                                                @Field("worksheet_sgst") String worksheet_sgst,
                                                @Field("worksheet_payable_amount") String worksheet_payable_amount);

    @FormUrlEncoded
    @POST(IUrls.URL_SMART_ASSESSMENT_ORDER_DETAILS)
    public Call<ResponseBody> getAssessmentsOrder(@Field("assessment_user_id") String assessment_user_id,
                                                  @Field("assessment_order_person_name") String assessment_order_person_name,
                                                  @Field("assessment_order_person_contact") String assessment_order_person_contact,
                                                  @Field("assessment_order_person_email") String assessment_order_person_email,
                                                  @Field("assessment_order_person_address") String assessment_order_person_address,
                                                  @Field("assessment_order_person_delivery_address") String assessment_order_person_delivery_address,
                                                  @Field("third_yr_assessment_kit_total_qty") String third_yr_assessment_kit_total_qty,
                                                  @Field("third_yr_assessment_kit_total_amount") String third_yr_assessment_kit_total_amount,
                                                  @Field("fourth_yr_assessment_kit_total_qty") String fourth_yr_assessment_kit_total_qty,
                                                  @Field("fourth_yr_assessment_kit_total_amount") String fourth_yr_assessment_kit_total_amount,
                                                  @Field("assessment_total_amount") String assessment_total_amount,
                                                  @Field("assessment_discount_percentage") String assessment_discount_percentage,
                                                  @Field("assessment_breaking_price") String assessment_breaking_price,
                                                  @Field("assessment_delivery_charges") String assessment_delivery_charges,
                                                  @Field("assessment_base_amount") String assessment_base_amount,
                                                  @Field("assessment_total_items") String assessment_total_items,
                                                  @Field("assessment_cgst") String assessment_cgst,
                                                  @Field("assessment_sgst") String assessment_sgst,
                                                  @Field("assessment_payable_amount") String assessment_payable_amount);

    @FormUrlEncoded
    @POST(IUrls.URL_JOIN_TRAINING_CONNTACT_US_BY_MAIL)
    public Call<ResponseBody> getContactByMail(@Field("user_id") String user_id,
                                               @Field("username") String username,
                                               @Field("email") String email,
                                               @Field("mobile") String mobile,
                                               @Field("training_on_mobile") String training_on_mobile,
                                               @Field("workshops") String workshops,
                                               @Field("courses_and_diploma") String courses_and_diploma
    );


    @GET(IUrls.URL_PROFILE)
    Call<ResponseBody> getProfile(
            @Query("user_id") String user_id,
            @Query("user_type") String user_type


    );




    @FormUrlEncoded
    @POST(IUrls.URL_PROFILE_UPDATE)
    public Call<ResponseBody> preschoolOwner_ProfileUpdate(@Field("user_id") String user_id,
                                                           @Field("user_type") String user_type,
                                                           @Field("username") String username,
                                                           @Field("address") String address,
                                                           @Field("preschool_name") String preschool_name);


    @GET(IUrls.URL_ESTORE_NEW_DASHBOARD_PRODUCTS)
    Call<ResponseBody> getEstoreNewDashboardProductlist(
            @Query("user_id") String user_id,
            @Query("login_type") String login_type,
            @Query("menu_id") String menu_id
    );

  *//*  @GET(IUrls.URL_CLASSES_INFO)
    Call<ResponseBody> getClassInfo();


    @GET(IUrls.URL_FACULTY)
    Call<ResponseBody> getFacultyInfo();

    @GET(IUrls.URL_SUBJECT)
    Call<ResponseBody> getSubjectInfo();

    @GET(IUrls.URL_PARENTS_DASHBOARD_GRID)
    Call<ResponseBody> getParentsDashboardgrid();*//*


     *//* @GET(IUrls.URL_CLASSES_INFO)
    Call<List<UserBannerObj>> getBanners();*//*


    @FormUrlEncoded
    @POST(IUrls.URL_STATIONARY_ANNUAL_SPORTS_DAY_CERTIFICATE_FORM)

    public Call<ResponseBody> getAnnualSportsFinalDayCertificateForm(@Field("user_id") String user_id,
                                                                     @Field("stationary_type_id") String stationary_type_id,
                                                                     @Field("design_id") String design_id,
                                                                     @Field("stationary_school_detail_id") String stationary_school_detail_id,
                                                                     @Field("student_name") String student_name,
                                                                     @Field("student_group") String student_group,
                                                                     @Field("student_rollno") String student_rollno,
                                                                     @Field("academic_year") String academic_year,
                                                                     @Field("competition_name") String competition_name,
                                                                     @Field("competition_date") String competition_date,
                                                                     @Field("rank") String rank
    );

    @FormUrlEncoded
    @POST(IUrls.URL_STATIONARY_UPDATE_ANNUAL_SPORTS_DAY_CERTIFICATE_FORM)
    public Call<ResponseBody> getUpdateAnnualSportsFinalDayCertificateForm(@Field("user_id") String user_id,
                                                                           @Field("stationary_type_id") String stationary_type_id,
                                                                           @Field("aspfinal_id") String aspfinal_id,
                                                                           @Field("student_name") String student_name,
                                                                           @Field("student_group") String student_group,
                                                                           @Field("student_rollno") String student_rollno,
                                                                           @Field("academic_year") String academic_year,
                                                                           @Field("competition_name") String competition_name,
                                                                           @Field("competition_date") String competition_date,
                                                                           @Field("rank") String rank
    );

    @GET(IUrls.URL_STATIONARY_TYPE_WISE_DESIGN_LIST)
    Call<ResponseBody> getStatinaryTypeWiseDesign(
            @Query("stationary_id") String stationary_id,
            @Query("stationary_type_id") String stationary_type_id


    );

    @GET(IUrls.URL_STATIONARY_EXPANDABLE_LIST)
    Call<StationaryPojo> getStationaryList();

    @GET(IUrls.URL_STATIONARY_STUDENT_ID_AND_PROGRESS_LIST)
    Call<ResponseBody> getStudentAndProgressList(
            @Query("user_id") String user_id,
            @Query("stationary_type_id") String stationary_type_id,
            @Query("design_id") String design_id,
            @Query("stationary_school_detail_id") String stationary_school_detail_id

    );


    @GET(IUrls.URL_STATIONARY_COMMON_ANNUAL_SPORTS_FINAL_LIST)
    Call<ResponseBody> getCommonSportsAnualFinalList(
            @Query("user_id") String user_id,
            @Query("stationary_type_id") String stationary_type_id,
            @Query("design_id") String design_id,
            @Query("stationary_school_detail_id") String stationary_school_detail_id

    );


    @GET(IUrls.URL_STATIONARY_SUMMER_CAMP_LIST)
    Call<ResponseBody> getSummerCampList(
            @Query("user_id") String user_id,
            @Query("stationary_type_id") String stationary_type_id,
            @Query("design_id") String design_id,
            @Query("stationary_school_detail_id") String stationary_school_detail_id

    );

    @GET(IUrls.URL_STATIONARY_TRANSFER_CERTIFICATE_LIST)
    Call<ResponseBody> getTrasferCertificateList(

            @Query("user_id") String user_id,
            @Query("stationary_type_id") String stationary_type_id,
            @Query("design_id") String design_id,
            @Query("stationary_school_detail_id") String stationary_school_detail_id

    );


    @GET(IUrls.URL_STATIONARY_CANCEL_ANNUL_SPORTS_FINAL)
    Call<ResponseBody> getCancelAnnualSportsFinalDayCertificate(

            @Query("user_id") String user_id,
            @Query("stationary_type_id") String stationary_type_id,
            @Query("design_id") String design_id,
            @Query("stationary_school_detail_id") String stationary_school_detail_id

    );


    @GET(IUrls.URL_STATIONARY_CANCEL_ID_CARD_PROG_CARD)
    Call<ResponseBody> getCancelIDCardProgressCard(

            @Query("user_id") String user_id,
            @Query("stationary_type_id") String stationary_type_id,
            @Query("design_id") String design_id,
            @Query("stationary_school_detail_id") String stationary_school_detail_id

    );


    @GET(IUrls.URL_STATIONARY_CANCEL_TRANSFER_CERTIFICATE)
    Call<ResponseBody> getCancelTransferCertificate(

            @Query("user_id") String user_id,
            @Query("stationary_type_id") String stationary_type_id,
            @Query("design_id") String design_id,
            @Query("stationary_school_detail_id") String stationary_school_detail_id

    );


    @GET(IUrls.URL_STATIONARY_CANCEL_SUMMER_CAMP_CERTIFICATE)
    Call<ResponseBody> getCancelSummerCampCertificate(

            @Query("user_id") String user_id,
            @Query("stationary_type_id") String stationary_type_id,
            @Query("design_id") String design_id,
            @Query("stationary_school_detail_id") String stationary_school_detail_id

    );

    @FormUrlEncoded
    @POST(IUrls.URL_STATIONARY_SUMMER_CAMP_CERTIFICATE_FORM)

    public Call<ResponseBody> getSummerCampCertificateForm(@Field("user_id") String user_id,
                                                           @Field("stationary_type_id") String stationary_type_id,
                                                           @Field("design_id") String design_id,
                                                           @Field("stationary_school_detail_id") String stationary_school_detail_id,
                                                           @Field("student_name") String student_name,
                                                           @Field("summer_camp_year") String summer_camp_year,
                                                           @Field("summer_camp_start_date") String summer_camp_start_date,
                                                           @Field("summer_camp_end_date") String summer_camp_end_date,
                                                           @Field("competition_rank") String competition_rank
    );

    @FormUrlEncoded
    @POST(IUrls.URL_UPDATE_STATIONARY_SUMMER_CAMP_CERTIFICATE_FORM)

    public Call<ResponseBody> getUpdateSummerCampCertificateForm(@Field("user_id") String user_id,
                                                                 @Field("stationary_type_id") String stationary_type_id,
                                                                 @Field("summer_camp_id") String summer_camp_id,
                                                                 @Field("student_name") String student_name,
                                                                 @Field("summer_camp_year") String summer_camp_year,
                                                                 @Field("summer_camp_start_date") String summer_camp_start_date,
                                                                 @Field("summer_camp_end_date") String summer_camp_end_date,
                                                                 @Field("competition_rank") String competition_rank
    );

    @FormUrlEncoded
    @POST(IUrls.URL_STATIONARY_TRANSFER_CERTIFICATE_FORM)


    public Call<ResponseBody> getTransferCertificateForm(@Field("user_id") String user_id,
                                                         @Field("stationary_type_id") String stationary_type_id,
                                                         @Field("design_id") String design_id,
                                                         @Field("stationary_school_detail_id") String stationary_school_detail_id,
                                                         @Field("student_name") String student_name,
                                                         @Field("father_name") String father_name,
                                                         @Field("mother_name") String mother_name,
                                                         @Field("birth_date") String birth_date,
                                                         @Field("academic_year") String academic_year,
                                                         @Field("present_group_name") String present_group_name,
                                                         @Field("promoted_group") String promoted_group,
                                                         @Field("reason") String reason,
                                                         @Field("remark") String remark

    );

    @FormUrlEncoded
    @POST(IUrls.URL_STATIONARY_UPDATE_TRANSFER_CERTIFICATE_FORM)


    public Call<ResponseBody> getUpdateTransferCertificateForm(
            @Field("user_id") String user_id,
            @Field("stationary_type_id") String stationary_type_id,
            @Field("transfer_certficate_id") String transfer_certficate_id,
            @Field("student_name") String student_name,
            @Field("father_name") String father_name,
            @Field("mother_name") String mother_name,
            @Field("birth_date") String birth_date,
            @Field("academic_year") String academic_year,
            @Field("present_group_name") String present_group_name,
            @Field("promoted_group") String promoted_group,
            @Field("reason") String reason,
            @Field("remark") String remark

    );


    @GET(IUrls.URL_STATIONARY_PRE_SCHOOL_LIST)
    Call<ResponseBody> getSchoolList(
            @Query("user_id") String user_id
    );



    //New Web Calls

    @GET(IUrls.URL_CURRICULUM_NEW_PRODUCT_WEBCALL)
    Call<ResponseBody> getCurriculumNewProductList(
            @Query("menu_id") String menu_id,
            @Query("category_id") String category_id,
            @Query("user_id") String user_id,
            @Query("login_type") String login_type
    );

    @FormUrlEncoded
    @POST(IUrls.URL_CURRICULUM_ADD_PRODUCT_TO_CART)
    public Call<ResponseBody> postAddProductInList(@Field("user_id") String user_id,
                                                   @Field("login_type") String login_type,
                                                   @Field("menu_id") String menu_id,
                                                   @Field("product_id") String product_id,
                                                   @Field("cart_status") String cart_status);



    @GET(IUrls.URL_CART_PRODUCT_LIST)
    public Call<ResponseBody> getUserCartList(
            *//*@Query("menu_id") String menu_id,*//*
            @Query("user_id") String user_id,
            @Query("login_type") String login_type
    );

    @FormUrlEncoded
    @POST(IUrls.URL_COMMON_ORDER)
    public Call<ResponseBody> postTotalCartProduct(
            *//*  @Field("menu_id") String menu_id,*//*
            @Field("user_id") String user_id,
            @Field("login_type") String login_type,
            @Field("order_person_name") String order_person_name,
            @Field("order_person_contact") String order_person_contact,
            @Field("order_person_email") String order_person_email,
            @Field("order_person_address") String order_person_address,
            @Field("order_person_delivery_address") String order_person_delivery_address,
            @Field("order_receiver_name") String order_receiver_name,
            @Field("delivery_charges") String delivery_charges,
            @Field("pincode") String pincode);

    @GET(IUrls.URL_CONNTACT_US_BY_MAIL_LIST)
    Call<ResponseBody> getAllMailContactList(
            @Query("user_id") String user_id,
            @Query("login_type") String login_type

    );

    @GET(IUrls.URL_ESTORE_BANNERS)
    Call<ResponseBody> getEstoreBannersList();


    @FormUrlEncoded
    @POST(IUrls.URL_CONNTACT_US_BY_MAILFORM)
    public Call<ResponseBody> getCommonContactByMail(@Field("user_id") String user_id,
                                                     @Field("login_type") String login_type,
                                                     @Field("menu_name") String menu_name,
                                                     @Field("username") String username,
                                                     @Field("email") String email,
                                                     @Field("mobile") String mobile
    );*/


}

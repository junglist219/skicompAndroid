package de.skicomp.network;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.skicomp.BuildConfig;
import de.skicomp.SessionManager;
import de.skicomp.enums.FriendshipStatus;
import de.skicomp.models.Friend;
import de.skicomp.models.Position;
import de.skicomp.models.SkiArea;
import de.skicomp.models.User;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by benjamin.schneider on 09.05.17.
 */

public class SkiService {

    public static final String APIURL = BuildConfig.API_URL;
    private static final String X_API_TOKEN = "4CCED9344D3192";

    private static final long CONNECTION_TIMEOUT = 15000;
    private static final long READ_TIMEOUT = 15000;

    private static SkiService instance;

    private final SkiServiceApi mSkiServiceApi;

    private String authToken;
    private String passwordForAuthorization;

    private SkiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIURL)
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mSkiServiceApi = retrofit.create(SkiServiceApi.class);
    }

    public static SkiService createInstance() {
        synchronized (SkiService.class) {
            if (SkiService.instance == null) {
                SkiService.instance = new SkiService();
            }
            return SkiService.instance;
        }
    }

    public static SkiService getInstance() {
        synchronized (SkiService.class) {
            return SkiService.instance;
        }
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.networkInterceptors().add(createLoggingInterceptor());
        builder.interceptors().add(createRequestInterceptor());
        builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS);
        return builder.build();
    }

    private Interceptor createRequestInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder requestBuilder = chain.request().newBuilder();

                requestBuilder.addHeader("x-api-token", X_API_TOKEN);
                if (authToken != null) {
                    requestBuilder.addHeader("x-auth-token", authToken);
                } else if (passwordForAuthorization != null) {
                    requestBuilder.addHeader("Authorization", passwordForAuthorization);
                } else if (!SessionManager.getInstance().getPassword().isEmpty()) {
                    requestBuilder.addHeader("Authorization", SessionManager.getInstance().getPassword());
                }

                Request request = requestBuilder.build();
                Response response = chain.proceed(request);

                if (response.isSuccessful()) {
                    String xAuthToken = response.header("x-auth-token");
                    if (xAuthToken != null && xAuthToken.isEmpty()) {
                        authToken = xAuthToken;
                    }
                }

                return response;
            }
        };
    }

    private Interceptor createLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return httpLoggingInterceptor;
    }


    public void setPasswordForAuthorization(String passwordForAuthorization) {
        this.passwordForAuthorization = passwordForAuthorization;
    }

    public void reset() {
        authToken = null;
        passwordForAuthorization = null;
    }

    //----------------------------------------------------------------------------------------------
    // REQUESTS
    //----------------------------------------------------------------------------------------------
    public void register(Callback<User> callback, User user) {
        Call<User> registerRequest = mSkiServiceApi.register(user);
        registerRequest.enqueue(callback);
    }

    public void getUser(Callback<User> callback, String username, String password) {
        setPasswordForAuthorization(password);
        Call<User> getUserRequest = mSkiServiceApi.getUser(username);
        getUserRequest.enqueue(callback);
    }

    public void updateUser(Callback<User> callback, String username, User user) {
        Call<User> updateUserRequest = mSkiServiceApi.putUser(username, user);
        updateUserRequest.enqueue(callback);
    }

    public void deleteUser(Callback<ResponseBody> callback, String username) {
        Call<ResponseBody> deleteUserRequest = mSkiServiceApi.deleteUser(username);
        deleteUserRequest.enqueue(callback);
    }

    public void updatePosition(Callback<ResponseBody> callback, String username, Position position) {
        Call<ResponseBody> updatePositionRequest = mSkiServiceApi.updatePosition(username, position.getLatitude(), position.getLongitude(), position.getTimestamp());
        updatePositionRequest.enqueue(callback);
    }

    public void forgotPassword(Callback<ResponseBody> callback, String username) {
        Call<ResponseBody> forgotPasswordRequest = mSkiServiceApi.forgotPassword(username);
        forgotPasswordRequest.enqueue(callback);
    }

    public void updateProfilePicture(Callback<ResponseBody> callback, String username, Bitmap profilePicture) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        profilePicture.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] profilePictureByte = byteArrayOutputStream.toByteArray();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), profilePictureByte);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "", reqFile);

        Call<ResponseBody> updateProfilePictureRequest = mSkiServiceApi.updateProfilePicture(username, body);
        updateProfilePictureRequest.enqueue(callback);
    }

    public void getFriends(Callback<List<Friend>> callback, String username) {
        Call<List<Friend>> getFriendsRequest = mSkiServiceApi.getFriends(username);
        getFriendsRequest.enqueue(callback);
    }

    public void updateFriendStatus(Callback<ResponseBody> callback, String username, String toUsername, FriendshipStatus friendshipStatus) {
        Call<ResponseBody> updateFriendStatusRequest = mSkiServiceApi.postFriendRequest(username, toUsername, friendshipStatus);
        updateFriendStatusRequest.enqueue(callback);
    }

    public void searchUser(Callback<List<Friend>> callback, String username, String searchString) {
        Call<List<Friend>> searchUserRequest = mSkiServiceApi.searchUser(username, searchString);
        searchUserRequest.enqueue(callback);
    }

    public void getSkiAreas(Callback<List<SkiArea>> callback) {
        Call<List<SkiArea>> skiAreaRequest = mSkiServiceApi.getSkiAreas();
        skiAreaRequest.enqueue(callback);
    }

    public void downloadSkiArea(Callback<ResponseBody> callback, String fileName) {
        Call<ResponseBody> downloadSkiAreaRequest = mSkiServiceApi.getSkiArea(fileName);
        downloadSkiAreaRequest.enqueue(callback);
    }
}

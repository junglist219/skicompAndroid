package de.skicomp.network;

import java.util.List;

import de.skicomp.enums.FriendshipStatus;
import de.skicomp.models.Friend;
import de.skicomp.models.SkiArea;
import de.skicomp.models.User;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by benjamin.schneider on 09.05.17.
 */

public interface SkiServiceApi {

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("users/register")
    Call<User> register(@Body User user);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("users/{username}")
    Call<User> putUser(@Path("username") String username,
                       @Body User user);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @DELETE("users/{username}")
    Call<ResponseBody> deleteUser(@Path("username") String username);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("users/{username}/searchUser")
    Call<List<Friend>> searchUser(@Path("username") String username,
                                  @Query("search") String searchString);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("users/{username}/updatePosition")
    Call<ResponseBody> updatePosition(@Path("username") String username,
                                      @Query("latitude") Double latitude,
                                      @Query("longitude") Double longitude,
                                      @Query("timestamp") long timestamp);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("users/{username}/forgotPassword")
    Call<ResponseBody> forgotPassword(@Path("username") String username);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("users/{username}/friends")
    Call<List<Friend>> getFriends(@Path("username") String username);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("users/{username}/friends")
    Call<ResponseBody> postFriendRequest(@Path("username") String username,
                                         @Query("toUser") String toUser,
                                         @Query("status") FriendshipStatus friendshipStatus);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("skiareas")
    Call<List<SkiArea>> getSkiAreas();

    @Streaming
    @GET("skiarea/{skiareaName}.kml")
    Call<ResponseBody> getSkiArea(@Path("skiareaName") String skiareaName);

    @Multipart
    @POST("users/{username}/updateProfilePicture")
    Call<ResponseBody> updateProfilePicture(@Path("username") String username,
                                            @Part MultipartBody.Part image);
}

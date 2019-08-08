package com.truonglam.tl_hotel.webservice;

import com.truonglam.tl_hotel.model.Box;
import com.truonglam.tl_hotel.model.ClusterRoomUpdating;
import com.truonglam.tl_hotel.model.HotelBackground;
import com.truonglam.tl_hotel.model.HotelInformation;
import com.truonglam.tl_hotel.model.HotelServiceResponse;
import com.truonglam.tl_hotel.model.Image;
import com.truonglam.tl_hotel.model.PasswordChanging;
import com.truonglam.tl_hotel.model.Room;
import com.truonglam.tl_hotel.model.RoomCluster;
import com.truonglam.tl_hotel.model.RoomClusterResponse;
import com.truonglam.tl_hotel.model.RoomUpdating;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface IService {

    @POST("/api/user/login")
    Call<HotelInformation> getInformation(@Query("username") String username,
                                          @Query("password") String password);

    @POST("/api/user/changePassword")
    Call<Void> changePassword(@Header("token") String token,
                              @Body PasswordChanging passwordChanging);


    /**
     * CRUD Service
     */

    @GET("/api/user/getService")
    Call<HotelServiceResponse> getServices(@Header("token") String token,
                                           @Query("id") String hotelId);

    @POST("api/service/delete")
    Call<Void> deleteService(@Header("token") String token,
                             @Query("service_id") String serviceId);

    @POST("api/service/update")
    Call<Void> updateService(@Header("token") String token);

    /**
     * CRUD Room Cluster
     */

    @GET("/api/hotel/cumphong")
    Call<RoomClusterResponse> getClusterRooms(@Header("token") String token,
                                              @Query("id") String id);

    @POST("/api/cumphong/create")
    Call<Void> createRoomCluster(@Header("token") String token,
                                 @Body RoomCluster roomCluster);

    @POST("api/cumphong/update")
    Call<Void> updateClusterRoom(@Header("token") String token,
                                 @Body ClusterRoomUpdating clusterRoomUpdating);

    @POST("api/cumphong/delete")
    Call<Void> deleteClusterRoom(@Header("token") String token,
                                 @Query("cumphong_id") String cumphongId);


    /**
     * CRUD Room
     */
    @GET("/api/hotel/getListRoom")
    Call<List<Room>> getRoomByIdCP(@Header("token") String token,
                                   @Query("idcp") String idcp);


    @POST("/api/phong/create")
    Call<Void> createRoom(@Header("token") String token,
                          @Body Room room);

    @POST("api/phong/update")
    Call<Void> updateRoom(@Header("token") String token,
                          @Body RoomUpdating roomUpdating);

    @POST("api/phong/delete")
    Call<Void> deleteRoom(@Header("token") String token,
                          @Query("phong_id") String phongId);


    /**
     * Get Hotel Background
     */

    @GET("api/user/getBackground")
    Call<HotelBackground> getBackground(@Header("token") String token,
                                        @Query("hotel_id") String hotelId);

    /**
     * Upload a photo to server
     */

    @Multipart
    @POST("api/background/upload")
    Call<Void> uploadPhoto(
            @Header("token") String token,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part file);


    @Multipart
    @POST("api/user/changeAvatar")
    Call<Void> changeAvatar(
            @Header("token") String token,
            @Part("id") RequestBody id,
            @Part MultipartBody.Part file);



    /**
     * Delete photo background
     */

    @POST("api/background/delete")
    Call<Void> deleteBackground(@Header("token") String token,
                                @Body Image image);


    @GET("api/user/getBox")
    Call<List<Box>> getBoxByRoomId(@Header("token") String token,
                                   @Query("phong_id") String roomId);


}
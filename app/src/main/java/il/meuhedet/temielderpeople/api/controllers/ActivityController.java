package il.meuhedet.temielderpeople.api.controllers;

import java.util.List;

import il.meuhedet.temielderpeople.api.dto.ActivityDTO;
import il.meuhedet.temielderpeople.api.dto.UserDTO;
import il.meuhedet.temielderpeople.api.dto.UserRegistrationDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ActivityController {

    @POST("activities")
    Call<ActivityDTO> createActivity(@Body ActivityDTO userRegistrationDTO,
                                     @Query("user_id") String userId);

    @GET("activities/{activity_id}")
    Call<ActivityDTO> getActivityById(@Query("user_id") String userId,
                                      @Path("activity_id") Long activityId);

    @PUT("activities/{activity_id")
    Call<ActivityDTO> updateActivity(@Query("user_id") String userId,
                                     @Path("activity_id") Long activityId,
                                     @Body ActivityDTO newActivityDTO);

    @PUT("activities/{activity_id}")
    Call<ActivityDTO> removeActivity(@Query("user_id") String userId,
                                     @Path("activity_id") Long activityId);

    @GET("activities")
    Call<List<ActivityDTO>> getAllActivitiesByUserId(@Query("user_id") String userId);


}

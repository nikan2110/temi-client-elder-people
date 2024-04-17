package il.meuhedet.temielderpeople.api.controllers;

import il.meuhedet.temielderpeople.api.dto.UserDTO;
import il.meuhedet.temielderpeople.api.dto.UserRegistrationDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserController {

    @POST("users")
    Call<UserDTO> createUser(@Body UserRegistrationDTO userRegistrationDTO);

    @GET("users/{user_id}")
    Call<UserDTO> getUserById(@Path("user_id") String userId);

    @PUT("users/{user_id}")
    Call<UserDTO> updateUserById(@Path("user_id") String userId);

    @DELETE("users/{user_id}")
    Call<UserDTO> removeUserById(@Path("user_id") String userId);
}

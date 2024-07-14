package il.meuhedet.temielderpeople.api.controllers;


import java.util.Map;

import il.meuhedet.temielderpeople.api.dto.ChatGPTAnswerDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatGPTController {

    @POST("query")
    Call<ChatGPTAnswerDTO> askQuestion(@Body Map<String, String> question);

}

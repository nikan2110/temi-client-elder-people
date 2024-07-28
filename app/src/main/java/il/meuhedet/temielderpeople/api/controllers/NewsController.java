package il.meuhedet.temielderpeople.api.controllers;

import java.util.List;

import il.meuhedet.temielderpeople.api.dto.ArticleDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsController {

    @GET("news")
    Call<List<ArticleDTO>> getTopTwoNewsByTheme(@Query("theme") String theme);
}

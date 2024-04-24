package il.meuhedet.temielderpeople.api.controllers;

import java.util.List;

import il.meuhedet.temielderpeople.api.dto.ActivityDTO;
import il.meuhedet.temielderpeople.api.dto.ArticleDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NewsController {

    @GET("news")
    Call<List<ArticleDTO>> getTopFiveNewsByTheme(@Query("theme") String theme);
}

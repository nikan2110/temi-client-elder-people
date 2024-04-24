package il.meuhedet.temielderpeople.api.services;

import android.util.Log;

import java.util.List;
import il.meuhedet.temielderpeople.api.controllers.NewsController;
import il.meuhedet.temielderpeople.api.dto.ArticleDTO;
import il.meuhedet.temielderpeople.api.retrofit.RetrofitClientTemiServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsService {

    Retrofit retrofit = RetrofitClientTemiServer.getClient();
    NewsController newsController = retrofit.create(NewsController.class);

    public void getTopFiveNewsByTheme(String theme) {
        newsController.getTopFiveNewsByTheme(theme).enqueue(new Callback<List<ArticleDTO>>() {
            @Override
            public void onResponse(Call<List<ArticleDTO>> call, Response<List<ArticleDTO>> response) {
                if (response.code() == 200) {
                    List<ArticleDTO> articlesDTO = response.body();
                    if (articlesDTO != null && !articlesDTO.isEmpty()) {
                        for (ArticleDTO articleDTO: articlesDTO) {
                            Log.i("article description", articleDTO.getDescription());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ArticleDTO>> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
    }
}

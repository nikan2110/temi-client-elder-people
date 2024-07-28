package il.meuhedet.temielderpeople.api.services;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

//import com.robotemi.sdk.Robot;
//import com.robotemi.sdk.TtsRequest;

import java.util.List;
import il.meuhedet.temielderpeople.api.controllers.NewsController;
import il.meuhedet.temielderpeople.api.dto.ArticleDTO;
import il.meuhedet.temielderpeople.api.retrofit.RetrofitClientTemiServer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsService {
//        implements Robot.TtsListener {

    Retrofit retrofit = RetrofitClientTemiServer.getClient();
    NewsController newsController = retrofit.create(NewsController.class);
    List<ArticleDTO> articles = null;
    int currentArticleIndex = 0;
//    Robot robot = Robot.getInstance();


    public void getTopTwoNewsByTheme(String theme) {
//        robot.addTtsListener(this);
        newsController.getTopTwoNewsByTheme(theme).enqueue(new Callback<List<ArticleDTO>>() {
            @Override
            public void onResponse(Call<List<ArticleDTO>> call, Response<List<ArticleDTO>> response) {
                Log.i("onResponse", "Response received");
                if (response.code() == 200) {
                    List<ArticleDTO> articlesDTO = response.body();
                    if (articlesDTO != null && !articlesDTO.isEmpty()) {
                        articles = articlesDTO;

//                        robot.speak(TtsRequest.create(articles.get(currentArticleIndex).getDescription()));
                    }
                }
            }
            @Override
            public void onFailure(Call<List<ArticleDTO>> call, Throwable t) {
                Log.i("error", t.getMessage());
            }
        });
    }

//    @Override
//    public void onTtsStatusChanged(@NonNull TtsRequest ttsRequest) {
//        if (ttsRequest.getStatus() == TtsRequest.Status.COMPLETED) {
//            if (ttsRequest.getSpeech().equals(articles.get(currentArticleIndex).getDescription())) {
//                currentArticleIndex++;
//                if (currentArticleIndex < articles.size()) {
//                    robot.speak(TtsRequest.create(articles.get(currentArticleIndex).getDescription()));
//                } else {
//                    robot.removeTtsListener(this);
//                }
//            }
//        }
//    }
}

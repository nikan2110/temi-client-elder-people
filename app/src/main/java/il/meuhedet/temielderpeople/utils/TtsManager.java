//package il.meuhedet.temielderpeople.utils;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import com.robotemi.sdk.Robot;
//import com.robotemi.sdk.TtsRequest;
//
//import il.meuhedet.temielderpeople.api.services.NewsService;
//import il.meuhedet.temielderpeople.ui.VideoActivity;
//
//public class TtsManager implements Robot.TtsListener {
//    private static TtsManager instance;
//    private final Robot robot;
//    private boolean shouldContinueListening = false;
//    private Context context;
//    private NewsService newsService = new NewsService();
//
//    private TtsManager(Context context) {
//        this.robot = Robot.getInstance();
//        this.context = context;
//    }
//
//    public static synchronized TtsManager getInstance(Context context) {
//        if (instance == null) {
//            instance = new TtsManager(context);
//        }
//        return instance;
//    }
//
//    public void setShouldContinueListening(boolean shouldContinue) {
//        this.shouldContinueListening = shouldContinue;
//    }
//
//    public void speak(TtsRequest request, boolean continueAfter) {
//        robot.addTtsListener(this);
//        setShouldContinueListening(continueAfter);
//        robot.speak(request);
//    }
//
//    @Override
//    public void onTtsStatusChanged(TtsRequest ttsRequest) {
//        Log.i("TtsManagerTtsRequest", ttsRequest.getStatus().toString());
//        Log.i("TtsManagerTtsshouldContinueListening", "" + shouldContinueListening);
//        if (ttsRequest.getStatus() == TtsRequest.Status.COMPLETED) {
//            if (ttsRequest.getSpeech().equals("בוקר טוב, נתחיל את הבוקר בכיף עם פעילות גופנית")) {
//                robot.removeTtsListener(this);
//                startVideoActivity(context);
//            }
//            if (ttsRequest.getSpeech().equals("עכשיו הגיע הזמן לקצת חדשות")) {
//                robot.removeTtsListener(this);
//                newsService.getTopTwoNewsByTheme("Israel");
//            }
//
//            if (ttsRequest.getSpeech().equals(", הנה תזכורת שיש אצלי," +
//                    " היום יש יום הולדת לנכדה, בוא נתקשר אליה")) {
//                Log.i("contacts", robot.getAllContact().toString());
//                robot.removeTtsListener(this);
//                robot.startTelepresence("nikita",
//                        "1b1b95112a0b58a28e13aefb1340fcb8");
//            }
//            if (shouldContinueListening) {
//                robot.removeTtsListener(this);
//                robot.wakeup();
//            }
//        }
//    }
//
//    private void startVideoActivity(Context context) {
//        Intent videoIntent = new Intent(context, VideoActivity.class);
//        videoIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(videoIntent);
//    }
//}
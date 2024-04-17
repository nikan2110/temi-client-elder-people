package il.meuhedet.temielderpeople.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import il.meuhedet.temielderpeople.database.converters.DayOfWeekConverter;
import il.meuhedet.temielderpeople.database.dao.ActivityRepository;
import il.meuhedet.temielderpeople.database.dao.UserRepository;
import il.meuhedet.temielderpeople.database.models.Activity;
import il.meuhedet.temielderpeople.database.models.User;

@Database(entities = {User.class, Activity.class}, version = 1, exportSchema = false)
@TypeConverters({DayOfWeekConverter.class})

public abstract class AppDatabase extends RoomDatabase {

    public abstract UserRepository userRepository();
    public abstract ActivityRepository activityRepository();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "elder_people")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}

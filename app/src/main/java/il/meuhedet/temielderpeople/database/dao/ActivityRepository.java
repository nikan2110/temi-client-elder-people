package il.meuhedet.temielderpeople.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import il.meuhedet.temielderpeople.database.models.Activity;
@Dao
public interface ActivityRepository {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertActivities(List<Activity> activities);

    @Query("SELECT * FROM activities WHERE user_identity_number = :userId")
    LiveData<List<Activity>> getActivitiesForUser(String userId);
}

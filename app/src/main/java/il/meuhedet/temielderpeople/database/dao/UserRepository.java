package il.meuhedet.temielderpeople.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import il.meuhedet.temielderpeople.database.models.User;
import il.meuhedet.temielderpeople.database.models.UserWithActivities;
import lombok.Data;

@Dao
public interface UserRepository {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Transaction
    @Query("SELECT * FROM users WHERE identityNumber = :identityNumber")
    LiveData<UserWithActivities> getUserWithActivities(String identityNumber);
}

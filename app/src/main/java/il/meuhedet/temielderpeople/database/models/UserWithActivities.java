package il.meuhedet.temielderpeople.database.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import lombok.ToString;

@ToString
public class UserWithActivities {

    @Embedded
    public User user;

    @Relation(
            parentColumn = "identityNumber",
            entityColumn = "user_identity_number"
    )
    public List<Activity> activities;
}

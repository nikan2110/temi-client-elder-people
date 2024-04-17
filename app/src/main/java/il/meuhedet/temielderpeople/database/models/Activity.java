package il.meuhedet.temielderpeople.database.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Set;

import il.meuhedet.temielderpeople.database.converters.DayOfWeekConverter;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "activities",
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "identityNumber",
                childColumns = "user_identity_number",
                onDelete = ForeignKey.CASCADE))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString
public class Activity {
    @PrimaryKey
    public int id;
    public String title;
    public String description;
    public String time; // Время сохраняем как строку

    @ColumnInfo(name = "user_identity_number")
    public String userIdentityNumber; // Внешний ключ

    @TypeConverters(DayOfWeekConverter.class)
    public Set<String> days;
}

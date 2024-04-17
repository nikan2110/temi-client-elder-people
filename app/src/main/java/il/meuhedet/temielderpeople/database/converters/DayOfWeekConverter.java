package il.meuhedet.temielderpeople.database.converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

public class DayOfWeekConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static String fromDaySet(Set<String> days) {
        return days == null ? null : gson.toJson(days);
    }

    @TypeConverter
    public static Set<String> toDaySet(String daysString) {
        if (daysString == null) {
            return Collections.emptySet();
        }
        Type setType = new TypeToken<Set<String>>(){}.getType();
        return gson.fromJson(daysString, setType);
    }
}

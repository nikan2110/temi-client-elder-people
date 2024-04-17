package il.meuhedet.temielderpeople.api.dto;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ActivityDTO {

    private Long id;
    private String title;
    private String description;
    private String time;
    private Set<DayOfWeek> days;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }

    public Set<DayOfWeek> getDays() {
        return days;
    }

    public ActivityDTO(String title, String description, String time, Set<DayOfWeek> days) {
        this.title = title;
        this.description = description;
        this.time = time;
        this.days = days;
    }

    @Override
    public String toString() {
        return "ActivityDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", time=" + time +
                ", days=" + days +
                '}';
    }
}

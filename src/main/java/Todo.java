import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {
    private long id;
    private String title;
    private boolean isCompleted;
}

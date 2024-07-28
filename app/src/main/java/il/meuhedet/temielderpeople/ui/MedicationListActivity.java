package il.meuhedet.temielderpeople.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.widget.Toast;

//import com.robotemi.sdk.Robot;
//import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.Arrays;

import il.meuhedet.temielderpeople.R;

public class MedicationListActivity extends AppCompatActivity {

    private ArrayList<String> medications;
    private ArrayAdapter<String> adapter;
//    private Robot robot = Robot.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);
        setTitle("Medications to Take");

        Button closeButton = findViewById(R.id.closeButton);

        closeButton.setOnClickListener(v -> finish());

        medications = new ArrayList<>(Arrays.asList("אספירין", "איבורפן", "אקמול"));

        ListView listView = findViewById(R.id.listViewMedications);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medications);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            askMedicationConfirmation(medications.get(position));
        });

        Toast.makeText(this, "Please check your medication " +
                "list and confirm each as you take them.", Toast.LENGTH_LONG).show();
    }


    private void askMedicationConfirmation(String medication) {
//        robot.speak(TtsRequest.create("האם לקחת את ה" + medication + " ?"));
        Toast.makeText(this, "האם לקחת את ה" + medication + " ?", Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Medication");
        builder.setMessage("האם לקחת את ה" + medication + "?");

        builder.setPositiveButton("כן לקחתי", (dialog, which) -> {
            medications.remove(medication);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "מצויין, עדכנתי את האחות שלקחת את התרופה "
                    + medication + ".", Toast.LENGTH_LONG).show();
//            robot.speak(TtsRequest.create("מצויין, עדכנתי את האחות שלקחת את התרופה " + medication + "."));
        });

        builder.setNegativeButton("לא", (dialog, which) -> {
            Toast.makeText(this, "אני אזכיר לך שוב בעוד חצי שעה לקחת " +
                    medication + ".", Toast.LENGTH_LONG).show();
//            robot.speak(TtsRequest.create("אני אזכיר לך שוב בעוד חצי שעה לקחת " + medication + "."));
        });

        builder.create().show();
    }
}
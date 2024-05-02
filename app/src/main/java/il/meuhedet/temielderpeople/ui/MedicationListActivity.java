package il.meuhedet.temielderpeople.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;

import java.util.ArrayList;
import java.util.Arrays;

import il.meuhedet.temielderpeople.R;

public class MedicationListActivity extends AppCompatActivity {

    private ArrayList<String> medications;
    private ArrayAdapter<String> adapter;

    private Robot robot = Robot.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);
        setTitle("Medications to Take");

        Button closeButton = findViewById(R.id.closeButton);

        closeButton.setOnClickListener(v -> finish());

        medications = new ArrayList<>(Arrays.asList("Aspirin", "Ibuprofen", "Paracetamol"));
        ListView listView = findViewById(R.id.listViewMedications);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medications);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            askMedicationConfirmation(medications.get(position));
        });

        robot.speak(TtsRequest.create("Please check your medication list " +
                "and confirm each as you take them."));
    }

    private void askMedicationConfirmation(String medication) {
        robot.speak(TtsRequest.create("Did you take your" + medication + " ?"));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Medication");
        builder.setMessage("Did you take your " + medication + "?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            medications.remove(medication);
            adapter.notifyDataSetChanged();
            robot.speak(TtsRequest.create("Great, I've noted that you took your " + medication + "."));
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            robot.speak(TtsRequest.create("Please remember to take your " + medication + "."));
        });

        builder.create().show();
    }
}
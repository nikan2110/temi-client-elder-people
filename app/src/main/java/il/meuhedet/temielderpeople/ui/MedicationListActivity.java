package il.meuhedet.temielderpeople.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import java.util.ArrayList;
import java.util.Arrays;

import il.meuhedet.temielderpeople.R;

public class MedicationListActivity extends AppCompatActivity {

    private ArrayList<String> medications;
    private ArrayAdapter<String> adapter;

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

        Toast.makeText(this, "Please check your medication list and confirm each as you take them.", Toast.LENGTH_LONG).show();
    }

    private void askMedicationConfirmation(String medication) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Medication");
        builder.setMessage("Did you take your " + medication + "?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            medications.remove(medication);
            adapter.notifyDataSetChanged();
            Toast.makeText(this, "Great, I've noted that you took your " + medication + ".", Toast.LENGTH_LONG).show();
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            Toast.makeText(this, "Please remember to take your \" + medication + \".", Toast.LENGTH_LONG).show();
        });

        builder.create().show();
    }
}
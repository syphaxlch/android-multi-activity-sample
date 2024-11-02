package uqac.dim.mymafag;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView selectedMafagTextView;
    private TextView linkTextView;
    private Button buttonOpenLink; // Déclaration du bouton


    // Déclaration de l'ActivityResultLauncher
    private ActivityResultLauncher<Intent> startForResult;
    private int selectedMafagIndex = -1; // Pour garder la trace du choix sélectionné

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonOpenLink = findViewById(R.id.buttonOpenLink); // Initialisation du bouton
        selectedMafagTextView = findViewById(R.id.selectedMafagTextView);
        linkTextView = findViewById(R.id.linkTextView);

        // Initialisation de l'ActivityResultLauncher
        startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String mafagName = result.getData().getStringExtra("mafagName");
                        String mafagUrl = result.getData().getStringExtra("mafagUrl");
                        selectedMafagIndex = result.getData().getIntExtra("selectedMafagIndex", -1); // Enregistrez l'index

                        selectedMafagTextView.setText(mafagName);
                        linkTextView.setText(mafagUrl);
                        buttonOpenLink.setEnabled(true);
                        buttonOpenLink.setOnClickListener(v -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mafagUrl));
                                startActivity(browserIntent);

                        });

                    }
                }
        );

        Button buttonFaireUnChoix = findViewById(R.id.buttonFaireUnChoix);
        buttonFaireUnChoix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("selectedMafagIndex", selectedMafagIndex); // Passer l'index sélectionné
                startForResult.launch(intent); // Lancement de la seconde activité
            }
        });


    }
}

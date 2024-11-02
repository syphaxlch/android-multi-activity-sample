package uqac.dim.mymafag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private static final String EXTRA_MAFAG_NAME = "mafagName";
    private static final String EXTRA_MAFAG_URL = "mafagUrl";

    private String[] mafagNames = {"Amazon", "ChatGPT", "Azure", "GitHub", "Gmail"};
    private String[] mafagUrls = {
            "https://www.amazon.com",
            "https://www.openai.com/chatgpt",
            "https://azure.microsoft.com",
            "https://github.com",
            "https://www.gmail.com"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        int selectedMafagIndex = getIntent().getIntExtra("selectedMafagIndex", -1);

        // Désactivation de l'image sélectionnée
        if (selectedMafagIndex >= 0) {
            @SuppressLint("DiscouragedApi") int selectedButtonId = getResources().getIdentifier("imageButton" + selectedMafagIndex, "id", getPackageName());
            ImageButton selectedButton = findViewById(selectedButtonId);
            if (selectedButton != null) {
                selectedButton.setEnabled(false);
                selectedButton.setAlpha(0.5f);
            }
        }

        // Initialisation des ImageButtons
        for (int i = 0; i < mafagNames.length; i++) {
            String buttonId = "imageButton" + i;
            @SuppressLint("DiscouragedApi") int resID = getResources().getIdentifier(buttonId, "id", getPackageName());
            ImageButton imageButton = findViewById(resID);

            int finalI = i; // Variable pour le clic
            imageButton.setOnClickListener(v -> {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(EXTRA_MAFAG_NAME, mafagNames[finalI]);
                resultIntent.putExtra(EXTRA_MAFAG_URL, mafagUrls[finalI]);
                resultIntent.putExtra("selectedMafagIndex", finalI); // Passer l'index sélectionné
                setResult(RESULT_OK, resultIntent);
                finish(); // Ferme la SecondActivity et retourne à la MainActivity
            });
        }
    }
}

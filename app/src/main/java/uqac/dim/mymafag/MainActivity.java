package uqac.dim.mymafag;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

    private TextView selectedMafagTextView;
    private TextView linkTextView;
    private Button buttonOpenLink; // Déclaration du bouton
    //Declaration des parametres par default
    String defaultMafagName = "Amazon";
    String defaultMafagUrl = "https://www.amazon.com";



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
        Button buttonNotifier = findViewById(R.id.buttonNotifier); // Initialisation du bouton Notifier

        // Affichage des valeurs par défaut au démarrage
        selectedMafagTextView.setText(defaultMafagName);
        linkTextView.setText(defaultMafagUrl);

        // Géstion du clic sur le bouton pour ouvrir le lien par défaut
        buttonOpenLink.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(defaultMafagUrl));
            startActivity(browserIntent);
        });

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

        // Créer le canal de notification
        createNotificationChannel();

        // Gérer le clic sur le bouton Notifier
        buttonNotifier.setOnClickListener(v -> {
            sendNotification();
        });
    }

    private void createNotificationChannel() {
        String channelId = "my_channel_id";
        CharSequence name = "MAFAG Notifications";
        String description = "Notifications for MAFAG selections";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void sendNotification() {
        String mafagName = selectedMafagTextView.getText().toString();
        String mafagUrl = linkTextView.getText().toString();

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mafagUrl));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE); // Ajout du drapeau FLAG_IMMUTABLE

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "my_channel_id")
                .setSmallIcon(R.drawable.ic_notification) // Assurez-vous que le nom correspond à votre icône
                .setContentTitle(mafagName) // Utiliser le nom du MAFAG sélectionné
                .setContentText("Ouvrez le lien : " + mafagUrl)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true); // La notification disparaît lorsque l'utilisateur clique dessus

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build()); // L'identifiant de la notification est 1
    }

}

package com.example.flashcard;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Flashcard> flashcards = new ArrayList<>();
    private int currentIndex = 0;
    private boolean showingAnswer = false;

    private TextView flashcardText;
    private Button showAnswerButton, nextButton, prevButton, addButton, editButton, deleteButton;
    private CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI
        flashcardText = findViewById(R.id.flashcardText);
        showAnswerButton = findViewById(R.id.showAnswerButton);
        nextButton = findViewById(R.id.nextButton);
        prevButton = findViewById(R.id.prevButton);
        addButton = findViewById(R.id.addButton);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        cardView = findViewById(R.id.cardView);

        // Sample flashcards
        flashcards.add(new Flashcard("What is the capital of France?", "Paris"));
        flashcards.add(new Flashcard("What is 2 + 2?", "4"));
        flashcards.add(new Flashcard("What is the largest planet?", "Jupiter"));
        flashcards.add(new Flashcard("Who wrote 'Romeo and Juliet'?", "William Shakespeare"));
        flashcards.add(new Flashcard("What is the boiling point of water?", "100°C"));
        flashcards.add(new Flashcard("What is the chemical symbol for gold?", "Au"));
        flashcards.add(new Flashcard("Who painted the Mona Lisa?", "Leonardo da Vinci"));
        flashcards.add(new Flashcard("What is the fastest land animal?", "Cheetah"));
        flashcards.add(new Flashcard("What is the smallest prime number?", "2"));
        flashcards.add(new Flashcard("What is the square root of 16?", "4"));
        flashcards.add(new Flashcard("What is the tallest mountain in the world?", "Mount Everest"));
        flashcards.add(new Flashcard("Who discovered penicillin?", "Alexander Fleming"));
        flashcards.add(new Flashcard("What is the main language spoken in Brazil?", "Portuguese"));
        flashcards.add(new Flashcard("What is the hardest natural substance?", "Diamond"));
        flashcards.add(new Flashcard("What is the currency of Japan?", "Yen"));
        flashcards.add(new Flashcard("Who is known as the father of computers?", "Charles Babbage"));
        flashcards.add(new Flashcard("What is the largest ocean on Earth?", "Pacific Ocean"));
        flashcards.add(new Flashcard("What is the process by which plants make food?", "Photosynthesis"));
        flashcards.add(new Flashcard("What is the capital of Australia?", "Canberra"));
        flashcards.add(new Flashcard("Who wrote '1984'?", "George Orwell"));
        flashcards.add(new Flashcard("What is the chemical formula for water?", "H2O"));
        flashcards.add(new Flashcard("What is the largest mammal?", "Blue Whale"));
        flashcards.add(new Flashcard("What is the freezing point of water?", "0°C"));
        flashcards.add(new Flashcard("Who painted the ceiling of the Sistine Chapel?", "Michelangelo"));
        flashcards.add(new Flashcard("What is the capital of Canada?", "Ottawa"));
        flashcards.add(new Flashcard("What is the smallest continent?", "Australia"));
        flashcards.add(new Flashcard("Who invented the telephone?", "Alexander Graham Bell"));
        flashcards.add(new Flashcard("What is the longest river in the world?", "Nile"));
        flashcards.add(new Flashcard("What is the main gas in the Earth's atmosphere?", "Nitrogen"));
        flashcards.add(new Flashcard("What is the capital of Italy?", "Rome"));

        updateFlashcardView();

        showAnswerButton.setOnClickListener(v -> {
            if (flashcards.size() == 0) return;
            flipCard();
        });

        nextButton.setOnClickListener(v -> {
            if (flashcards.size() == 0) return;
            currentIndex = (currentIndex + 1) % flashcards.size();
            showingAnswer = false;
            updateFlashcardView();
        });

        prevButton.setOnClickListener(v -> {
            if (flashcards.size() == 0) return;
            currentIndex = (currentIndex - 1 + flashcards.size()) % flashcards.size();
            showingAnswer = false;
            updateFlashcardView();
        });

        addButton.setOnClickListener(v -> showAddEditDialog(false));
        editButton.setOnClickListener(v -> showAddEditDialog(true));
        deleteButton.setOnClickListener(v -> {
            if (flashcards.size() == 0) return;
            flashcards.remove(currentIndex);
            if (currentIndex >= flashcards.size()) currentIndex = flashcards.size() - 1;
            showingAnswer = false;
            updateFlashcardView();
        });
    }

    private void updateFlashcardView() {
        if (flashcards.size() == 0) {
            flashcardText.setText("No flashcards. Add one!");
            showAnswerButton.setEnabled(false);
            nextButton.setEnabled(false);
            prevButton.setEnabled(false);
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
        } else {
            Flashcard card = flashcards.get(currentIndex);
            flashcardText.setText(showingAnswer ? card.getAnswer() : card.getQuestion());
            showAnswerButton.setText(showingAnswer ? "Show Question" : "Show Answer");
            showAnswerButton.setEnabled(true);
            nextButton.setEnabled(true);
            prevButton.setEnabled(true);
            editButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
    }

    private void showAddEditDialog(boolean isEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(isEdit ? "Edit Flashcard" : "Add Flashcard");

        View dialogView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
        EditText questionInput = new EditText(this);
        questionInput.setHint("Question");
        questionInput.setInputType(InputType.TYPE_CLASS_TEXT);
        EditText answerInput = new EditText(this);
        answerInput.setHint("Answer");
        answerInput.setInputType(InputType.TYPE_CLASS_TEXT);

        if (isEdit && flashcards.size() > 0) {
            Flashcard card = flashcards.get(currentIndex);
            questionInput.setText(card.getQuestion());
            answerInput.setText(card.getAnswer());
        }

        // Use a vertical LinearLayout to stack the inputs
        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.addView(questionInput);
        layout.addView(answerInput);
        builder.setView(layout);

        builder.setPositiveButton(isEdit ? "Save" : "Add", (dialog, which) -> {
            String question = questionInput.getText().toString().trim();
            String answer = answerInput.getText().toString().trim();
            if (!question.isEmpty() && !answer.isEmpty()) {
                if (isEdit && flashcards.size() > 0) {
                    flashcards.set(currentIndex, new Flashcard(question, answer));
                } else {
                    flashcards.add(new Flashcard(question, answer));
                    currentIndex = flashcards.size() - 1;
                }
                showingAnswer = false;
                updateFlashcardView();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void flipCard() {
        // Set camera distance for 3D effect
        float scale = getResources().getDisplayMetrics().density;
        cardView.setCameraDistance(12000 * scale); // More 3D
        int duration = 400; // Slower
        float originalElevation = cardView.getCardElevation();
        float elevated = originalElevation + 24 * scale; // Higher shadow

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(cardView, "rotationY", 0f, 90f);
        animator1.setDuration(duration);
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                cardView.setCardElevation(elevated);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                showingAnswer = !showingAnswer;
                updateFlashcardView();
                cardView.setRotationY(-90f);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(cardView, "rotationY", -90f, 0f);
                animator2.setDuration(duration);
                animator2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        cardView.setCardElevation(originalElevation);
                    }
                });
                animator2.start();
            }
        });
        animator1.start();
    }
}
package com.example.learning.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.learning.R;
import com.example.learning.model.Question;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> questionList;
    private Map<Integer, Integer> selectedAnswers = new HashMap<>(); // key = position, value = selected option index

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.textViewQuestion.setText(question.getQuestionText());

        // Very important: Clear existing listeners first
        holder.radioGroupOptions.setOnCheckedChangeListener(null);
        holder.radioGroupOptions.removeAllViews();

        // Create radio buttons dynamically
        for (int i = 0; i < question.getOptions().length; i++) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(question.getOptions()[i]);
            radioButton.setId(View.generateViewId());  // ✅ Safe unique ID

            // Important: set tag to remember option index
            radioButton.setTag(i);

            holder.radioGroupOptions.addView(radioButton);
        }

        // Restore previous selection if user already answered
        if (selectedAnswers.containsKey(position)) {
            int selectedOptionIndex = selectedAnswers.get(position);

            for (int i = 0; i < holder.radioGroupOptions.getChildCount(); i++) {
                RadioButton rb = (RadioButton) holder.radioGroupOptions.getChildAt(i);
                if ((int) rb.getTag() == selectedOptionIndex) {
                    rb.setChecked(true);
                    break;
                }
            }
        }

        // New listener
        holder.radioGroupOptions.setOnCheckedChangeListener((group, checkedId) -> {
            View checkedButton = group.findViewById(checkedId);
            if (checkedButton != null) {
                int selectedOptionIndex = (int) checkedButton.getTag();
                selectedAnswers.put(position, selectedOptionIndex);
            }
        });
    }


    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion;
        RadioGroup radioGroupOptions;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestionText);
            radioGroupOptions = itemView.findViewById(R.id.radioGroupOptions);
        }
    }

    // This method is needed for TaskDetailFragment
    public int getSelectedAnswer(int position) {
        return selectedAnswers.getOrDefault(position, -1); // return -1 if no selection
    }
}

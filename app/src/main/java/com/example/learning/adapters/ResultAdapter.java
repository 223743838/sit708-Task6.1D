package com.example.learning.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.learning.R;
import com.example.learning.model.Question;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private List<Question> resultList;

    public ResultAdapter(List<Question> resultList) {
        this.resultList = resultList;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        Question question = resultList.get(position);
        holder.textViewQuestion.setText(question.getQuestionText());
        holder.textViewCorrectAnswer.setText("Correct: " + question.getOptions()[question.getCorrectAnswerIndex()]);
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    static class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion, textViewCorrectAnswer;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewResultQuestion);
            textViewCorrectAnswer = itemView.findViewById(R.id.textViewResultCorrectAnswer);
        }
    }
}

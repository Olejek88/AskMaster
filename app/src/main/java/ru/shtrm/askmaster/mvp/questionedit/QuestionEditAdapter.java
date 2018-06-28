package ru.shtrm.askmaster.mvp.questionedit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.mvp.images.ImageGridAdapter;

public class QuestionEditAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private final Question aQuestion;

    QuestionEditAdapter(@NonNull Context context, @NonNull Question question) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.aQuestion = question;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new QuestionViewHolder(
                inflater.inflate(R.layout.item_question_edit, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        QuestionViewHolder vh = (QuestionViewHolder) holder;
        vh.textViewTitle.setText(aQuestion.getTitle());
        vh.textViewText.setText(aQuestion.getText());
        vh.photoGridView.setAdapter(new ImageGridAdapter(context, aQuestion.getImages()));
        vh.photoGridView.invalidateViews();
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    /**
     * A header view holder of recyclerView.
     */
    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        EditText textViewText;
        EditText textViewTitle;
        GridView photoGridView;

        QuestionViewHolder(View itemView) {
            super(itemView);
            textViewText = itemView.findViewById(R.id.questionText);
            textViewTitle = itemView.findViewById(R.id.questionTitle);
            photoGridView = itemView.findViewById(R.id.gridview);
        }

    }
}

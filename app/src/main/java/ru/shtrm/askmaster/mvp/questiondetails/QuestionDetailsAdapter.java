package ru.shtrm.askmaster.mvp.questiondetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.RealmList;
import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.component.Timeline;
import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.data.Question;
import ru.shtrm.askmaster.util.MainUtil;

public class QuestionDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private final Question aQuestion;

    private final List<Answer> answers;

    private static final int TYPE_HEADER = 0x00;
    private static final int TYPE_NORMAL = 0x01;
    private static final int TYPE_START = 0x02;
    private static final int TYPE_FINISH = 0x03;
    private static final int TYPE_SINGLE = 0x04;

    QuestionDetailsAdapter(@NonNull Context context, @NonNull Question question) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.aQuestion = question;

        this.answers = new ArrayList<>();
        answers.addAll(question.getAnswers());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(
                    inflater.inflate(R.layout.item_details_header, parent, false));
        }
        return new AnswersViewHolder(
                inflater.inflate(R.layout.item_answer, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) {
            HeaderViewHolder vh = (HeaderViewHolder) holder;
            vh.textViewTitle.setText(aQuestion.getTitle());
            vh.textViewText.setText(aQuestion.getText());
            // TODO добавляем сюда рейтинг пользователя
            if (aQuestion.getUser()!=null)
                vh.textViewAuthor.setText(aQuestion.getUser().getName());
        } else {
            Answer item = answers.get(position - 1);
            AnswersViewHolder viewHolder = (AnswersViewHolder) holder;
            String sDate =
                    new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getDate());

            if (getItemViewType(position) == TYPE_SINGLE) {
                viewHolder.timeLine.setStartLine(null);
                viewHolder.timeLine.setFinishLine(null);
            } else if (getItemViewType(position) == TYPE_START) {
                viewHolder.timeLine.setStartLine(null);
            } else if (getItemViewType(position) == TYPE_FINISH) {
                viewHolder.timeLine.setFinishLine(null);
            }

            viewHolder.textViewDate.setText(sDate);
            viewHolder.textViewAuthor.setText(item.getUser().getName());
            viewHolder.imageViewAuthor.setImageBitmap(
                    MainUtil.getBitmapByPath(
                            MainUtil.getPicturesDirectory(context),item.getUser().getAvatar()));
        }

    }

    @Override
    public int getItemCount() {
        // Including a header.
        return aQuestion.getAnswers().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1 && position == answers.size()) {
            // The list may contains only one item.
            return TYPE_SINGLE;
        } else if (position == 1) {
            return TYPE_START;
        } else if (position == answers.size()) {
            return TYPE_FINISH;
        }
        return TYPE_NORMAL;
    }

    /**
     * DO NOT cast a RealmList to List or you will got some unexpected bugs.
     * @param list The RealmList.
     */
    public void updateData(@NonNull RealmList<Answer> list) {
        this.answers.clear();
        // See {@link QuestionDetailsAdapter#QuestionDetailsAdapter}
        this.answers.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * The package status view holder of recyclerView.
     */
    public class AnswersViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewUser;
        private TextView textViewDate;
        private TextView textViewText;
        private TextView textViewAuthor;
        private ImageView imageViewAuthor;
        private GridView photoGridView;
        private Timeline timeLine;

        AnswersViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = (AppCompatTextView) itemView.findViewById(R.id.answerAuthor);
            textViewText = (AppCompatTextView) itemView.findViewById(R.id.answerText);
            //timeLine = (Timeline) itemView.findViewById(R.id.tim);
            imageViewAuthor = itemView.findViewById(R.id.answerAuthorImage);
            photoGridView = itemView.findViewById(R.id.answerPhotoGrid);
        }
    }

    /**
     * A header view holder of recyclerView.
     */
    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor;
        TextView textViewText;
        TextView textViewTitle;
        TextView textViewDate;
        GridView photoGridView;

        HeaderViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.questionAuthor);
            textViewText = itemView.findViewById(R.id.questionText);
            textViewTitle = itemView.findViewById(R.id.questionTitle);
            textViewDate = itemView.findViewById(R.id.questionDate);
            photoGridView = itemView.findViewById(R.id.gridview);
        }

    }
}

package ru.shtrm.askmaster.mvp.questiondetails;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.data.Answer;
import ru.shtrm.askmaster.interfaces.OnRecyclerViewItemClickListener;
import ru.shtrm.askmaster.util.MainUtil;

public class AnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @NonNull
    private final Context context;

    @NonNull
    private final LayoutInflater inflater;

    @NonNull
    private List<Answer> list;
    
    @Nullable
    private OnRecyclerViewItemClickListener listener;

    AnswersAdapter(@NonNull Context context, @NonNull List<Answer> list) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AnswerViewHolder(inflater.inflate(R.layout.item_answer,
                parent, false), listener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Answer item = list.get(position);
        AnswerViewHolder pvh = (AnswerViewHolder) holder;
        String sDate = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.US).format(item.getDate());
        pvh.textViewDate.setText(sDate);
        pvh.textViewAnswerTitle.setTypeface(null, Typeface.BOLD);
        pvh.textViewAnswerTitle.setText(item.getTitle());
        pvh.textViewText.setText(item.getText());
        if (item.getUser()!=null)
            if (item.getUser().getAvatar()!=null)
                pvh.circleImageView.setImageBitmap(MainUtil.getBitmapByPath(
                    MainUtil.getPicturesDirectory(context),item.getUser().getAvatar()));
            else
                pvh.textViewAvatar.setText(item.getTitle().substring(0,1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * The view holder of package in home list.
     */
    public class AnswerViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {

        AppCompatTextView textViewAnswerTitle;
        AppCompatTextView textViewDate;
        AppCompatTextView textViewText;
        AppCompatTextView textViewAvatar;
        ImageView imageViewAnswer;
        CircleImageView circleImageView;

        private OnRecyclerViewItemClickListener listener;

        public AnswerViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            //textViewAnswerTitle = itemView.findViewById(R.id.answerTitle);
            textViewDate = itemView.findViewById(R.id.answerDate);
            textViewText = itemView.findViewById(R.id.answerText);
            textViewAvatar = itemView.findViewById(R.id.textViewAvatar);
            imageViewAnswer = itemView.findViewById(R.id.imageViewAnswer);
            circleImageView = itemView.findViewById(R.id.circleImageView);

            this.listener = listener;
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.listener != null) {
                listener.OnItemClick(v, getLayoutPosition());
            }
        }

        /**
         * Create the context menu.
         * @param menu The context menu.
         * @param v The view.
         * @param menuInfo The menu information.
         */
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            if (menu != null) {
                //((MainActivity)context).setSelectedPackageId(list.get(getLayoutPosition()).getId());
                //menu.add(Menu.NONE, R.id.action_share, 0, R.string.share);
            }
        }
    }

}

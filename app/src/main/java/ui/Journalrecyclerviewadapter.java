package ui;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self.Journallist;
import com.example.self.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import model.Journal;

public class Journalrecyclerviewadapter extends RecyclerView.Adapter<Journalrecyclerviewadapter.ViewHolder> {

    public Context context;
    public List<Journal>journalList;

    public Journalrecyclerviewadapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public Journalrecyclerviewadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view=LayoutInflater.from(context).inflate(R.layout.journal_row,parent,false);

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Journalrecyclerviewadapter.ViewHolder holder, int position) {
        Journal journal= journalList.get(position);
        holder.title.setText(journal.getTitle());
        holder.thought.setText(journal.getThought());
        String url=journal.getImgurl();
        Picasso.get().load(url).placeholder(R.drawable.sunshine2).fit().into(holder.img);
        String timeago= (String) DateUtils.getRelativeTimeSpanString(journal.getTimestamp().getSeconds()*1000);
        holder.dateadded.setText(timeago);
        holder.name.setText(journal.getUsername());
    }

    @Override
    public int getItemCount() {
        return journalList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name,title,thought,dateadded;
        public ImageView img;
        public ImageButton imageButton;
        String userid;
        String username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.journalrow_title);
            thought=itemView.findViewById(R.id.journalthought_title);
            dateadded=itemView.findViewById(R.id.timestamp);
            img=itemView.findViewById(R.id.journalrow_img);
            name=itemView.findViewById(R.id.journalrow_username);
            imageButton=itemView.findViewById(R.id.journalrow_sharebtn);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    context.startActivity(new Intent(context,));
                }
            });
        }
    }
}

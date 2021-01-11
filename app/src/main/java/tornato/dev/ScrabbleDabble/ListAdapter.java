package tornato.dev.ScrabbleDabble;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter
        extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private List<ListItem> listItems;

    public ListAdapter(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListItem listItem = listItems.get(position);
        holder.score.setText(listItem.getScore());
        holder.title.setText(listItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return this.listItems.size();
    }

    public void filterWords(List<ListItem> listItem) {
        this.listItems = listItem;
        notifyDataSetChanged();
    }

    public static class ListViewHolder extends RecyclerView.ViewHolder {

        public TextView score;
        public TextView title;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.score = itemView.findViewById(R.id.text_view_score);
            this.title = itemView.findViewById(R.id.text_view_title);
        }
    }
}

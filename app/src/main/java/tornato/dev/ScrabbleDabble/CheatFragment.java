package tornato.dev.ScrabbleDabble;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CheatFragment extends Fragment {

    private final Processor processor;
    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<ListItem> listWords;
    private List<ListItem> fullList;

    public CheatFragment(Processor processor) {
        this.processor = processor;
        this.listWords = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CheatFragment(List<ListItem> words, Processor processor) {
        this.listWords = words;
        this.fullList = words;
        this.processor = processor;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_cheat, container, false);
        buildRecyclerView(view);

        this.listWords = createListItem(processor.wordList);

        EditText editText = view.findViewById(R.id.edit_query_cheat_fragment);
        editText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                        //
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {
                        //
                    }

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.toString().equals("")) {
                            adapter.filterWords(fullList);
                        } else if (s.length() < 9) {
                            filter(s.toString());
                        } else {
                            List<ListItem> newList = new ArrayList<>();
                            newList.add(new ListItem(":(", "Input too long"));
                            adapter.filterWords(newList);
                        }
                    }
                }
        );
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filter(String text) {
        List<ListItem> listItems = createListItem(processor.cheat(text));
        adapter.filterWords(listItems);
    }

    private List<ListItem> createListItem(List<Word> wordList) {
        List<ListItem> listItems = new ArrayList<>();
        for (Word w : wordList) {
            listItems.add(new ListItem("" + w.getScore(), w.getWord()));
        }
        return listItems;
    }

    private List<ListItem> createListItem(Queue<Word> wordList) {
        List<ListItem> listItems = new ArrayList<>();
        for (Word w : wordList) {
            listItems.add(new ListItem("" + w.getScore(), w.getWord()));
        }
        return listItems;
    }

    public void buildRecyclerView(View view) {
        this.recyclerView = view.findViewById(R.id.recycler_cheat);
        this.recyclerView.setHasFixedSize(true);
        this.layoutManager = new LinearLayoutManager(this.getContext());
        this.adapter = new ListAdapter(this.listWords);
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setAdapter(this.adapter);
    }
}

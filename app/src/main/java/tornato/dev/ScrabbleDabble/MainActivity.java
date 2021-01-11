package tornato.dev.ScrabbleDabble;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity
        extends AppCompatActivity
        implements SearchView.OnQueryTextListener {

    public Processor processor;
    private CheatFragment cheatFragment;
    private ValidateFragment validateFragment;
    private AboutFragment aboutFragment;

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationListener = item -> {
        Fragment selectedFragment = null;
        if (R.id.nav_cheat == item.getItemId()) {
            item.setChecked(true);
            selectedFragment = this.cheatFragment;
        } else if (R.id.nav_validate == item.getItemId()) {
            item.setChecked(true);
            selectedFragment = this.validateFragment;
        } else if (R.id.nav_about == item.getItemId()) {
            item.setChecked(true);
            selectedFragment = this.aboutFragment;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, Objects.requireNonNull(selectedFragment))
                .commit();
        return false;
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.processor =
                new Processor(
                        getResources().openRawResource(R.raw.english_dictionary),
                        getResources().openRawResource(R.raw.character_values)
                );

        System.out.println(processor.wordList);
        // set to start display on the cheat fragment

        List<ListItem> lists = createListItem(processor.wordList);

        this.cheatFragment = new CheatFragment(lists, processor);
        this.validateFragment = new ValidateFragment(processor);
        this.aboutFragment = new AboutFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, cheatFragment)
                .commit();

        // gives bottom nav it's functionality
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(navigationListener);
    }

    /**
     * Called when the user submits the query. This could be due to a key press on the
     * keyboard or due to pressing a submit button.
     * The listener can override the standard behavior by returning true
     * to indicate that it has handled the submit request. Otherwise return false to
     * let the SearchView handle the submission by launching any associated intent.
     *
     * @param query the query text that is to be submitted
     * @return true if the query has been handled by the listener, false to let the
     * SearchView perform the default action.
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Called when the query text is changed by the user.
     *
     * @param newText the new content of the query text field.
     * @return false if the SearchView should perform the default action of showing any
     * suggestions if available, true if the action was handled by the listener.
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public List<ListItem> createListItem(Queue<Word> wordList) {
        List<ListItem> listItems = new ArrayList<>();
        while (!wordList.isEmpty()) {
            Word w = wordList.remove();
            listItems.add(new ListItem("" + w.getScore(), w.getWord()));
        }
        return listItems;
    }
}

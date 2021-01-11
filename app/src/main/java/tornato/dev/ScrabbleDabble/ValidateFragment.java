package tornato.dev.ScrabbleDabble;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class ValidateFragment extends Fragment {

    private final Processor processor;
    private TextView textView;

    public ValidateFragment(Processor processor) {
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
        View view = inflater.inflate(R.layout.fragment_validate, container, false);
        this.textView = (TextView) view.findViewById(R.id.validate_text);
        textView.setText(R.string.validate);

        EditText editText = view.findViewById(R.id.edit_query_validate_fragment);
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
                            textView.setTextColor(Color.GRAY);
                            textView.setText(R.string.validate);
                        } else if (processor.lookup(s.toString())) {
                            textView.setTextColor(Color.GREEN);
                            textView.setText(R.string.found);
                        } else {
                            textView.setTextColor(Color.RED);
                            textView.setText(R.string.not_found);
                        }
                    }
                }
        );
        return view;
    }
}

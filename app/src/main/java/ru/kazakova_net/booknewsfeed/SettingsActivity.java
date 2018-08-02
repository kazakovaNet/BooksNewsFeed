package ru.kazakova_net.booknewsfeed;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }
    
    public static class BookNewsPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);
            
            EditText pageSizeEditText = ((EditTextPreference) findPreference(getString(R.string.settings_page_size_key)))
                    .getEditText();
            pageSizeEditText.setFilters(new InputFilter[]{new InputFilterMinMax(getContext(), 1, 50)});
            
            Preference pageSize = findPreference(getString(R.string.settings_page_size_key));
            bindPreferenceSummaryToValue(pageSize);
            
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
        }
        
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // The code in this method takes care of updating
            // the displayed preference summary after it has been changed
            String stringValue = newValue.toString();
            
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            
            return true;
        }
        
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            
            String preferenceString = preferences.getString(preference.getKey(), "");
            
            onPreferenceChange(preference, preferenceString);
        }
    }
    
    private static class InputFilterMinMax implements InputFilter {
        
        private int mMin;
        private int mMax;
        private Context mContext;
        
        InputFilterMinMax(Context context, int min, int max) {
            mContext = context;
            mMin = min;
            mMax = max;
        }
        
        InputFilterMinMax(Context context, String min, String max) {
            mContext = context;
            mMin = Integer.parseInt(min);
            mMax = Integer.parseInt(max);
        }
        
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                
                if (isInRange(mMin, mMax, input)) {
                    return null;
                } else {
                    Toast.makeText(mContext, "The value must be between " + mMin + " and " + mMax,
                            Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            
            return "";
        }
    
        /**
         * Returns whether the value entered by the user is in the specified interval
         *
         * @param a minimum allowable value
         * @param b maximum allowable value
         * @param c value entered by the user
         * @return is value in range
         */
        
        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}

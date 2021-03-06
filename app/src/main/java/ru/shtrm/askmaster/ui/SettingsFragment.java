package ru.shtrm.askmaster.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.util.PushUtil;
import ru.shtrm.askmaster.util.SettingsUtil;
import ru.shtrm.askmaster.util.TimeFormatUtil;

public class SettingsFragment extends PreferenceFragmentCompat {
    private Activity mainActivityConnector = null;

    private Preference prefStartTime, prefsEndTime, prefAlert;
    private Preference prefNotificationInterval, prefNavigationBar;

    private SharedPreferences sp;

    private int startHour, startMinute;
    private int endHour, endMinute;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_prefs);

        initPrefs();

        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        // Set the do-not-disturb-mode time initial range:
        // from 23:00 to 6:00
        startHour = sp.getInt(SettingsUtil.KEY_DO_NOT_DISTURB_MODE_START_HOUR, 22);
        startMinute = sp.getInt(SettingsUtil.KEY_DO_NOT_DISTURB_MODE_START_MINUTE, 0);
        prefStartTime.setSummary(TimeFormatUtil.formatTimeIntToString(startHour, startMinute));

        endHour = sp.getInt(SettingsUtil.KEY_DO_NOT_DISTURB_MODE_END_HOUR, 8);
        endMinute = sp.getInt(SettingsUtil.KEY_DO_NOT_DISTURB_MODE_END_MINUTE, 0);
        prefsEndTime.setSummary(TimeFormatUtil.formatTimeIntToString(endHour, endMinute));

        prefStartTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Show the time picker dialog
                TimePickerDialog dialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        // Save the hour and minute value to shared preferences
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(SettingsUtil.KEY_DO_NOT_DISTURB_MODE_START_HOUR, hourOfDay);
                        editor.putInt(SettingsUtil.KEY_DO_NOT_DISTURB_MODE_START_MINUTE, minute);
                        editor.apply();
                        // Update ui
                        prefStartTime.setSummary(TimeFormatUtil.formatTimeIntToString(hourOfDay, minute));
                    }
                    // The final params setting true means that it is 24 hours mode.
                }, startHour, startMinute, true);
                dialog.show(mainActivityConnector.getFragmentManager(), "StartTimePickerDialog");

                return true;
            }
        });

        prefsEndTime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // Show the time picker dialog
                TimePickerDialog dialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                        if (startHour == endHour && startMinute == endMinute) {
                            Toast.makeText(getContext(),
                                    R.string.set_end_time_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // Save the hour and minute value to shared preferences
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt(SettingsUtil.KEY_DO_NOT_DISTURB_MODE_END_HOUR, hourOfDay);
                        editor.putInt(SettingsUtil.KEY_DO_NOT_DISTURB_MODE_END_MINUTE, minute);
                        editor.apply();
                        prefsEndTime.setSummary(TimeFormatUtil.formatTimeIntToString(hourOfDay, minute));
                    }
                    // The final params setting true means that it is 24 hours mode.
                }, endHour, endMinute, true);
                dialog.show(mainActivityConnector.getFragmentManager(), "StartTimePickerDialog");

                return true;
            }
        });

        prefAlert.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean value = (boolean) newValue;
                if (value) {
                    PushUtil.startReminderService(getContext());
                } else {
                    PushUtil.stopReminderService(getContext());
                }
                return true;
            }
        });

        prefNotificationInterval.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(SettingsUtil.KEY_NOTIFICATION_INTERVAL, (String)newValue);
                editor.apply();
                PushUtil.restartReminderService(getContext());
                return true;
            }
        });
    }

    /**
     * Init the preferences.
     */
    private void initPrefs() {
        prefStartTime = findPreference("do_not_disturb_mode_start");
        prefsEndTime = findPreference("do_not_disturb_mode_end");
        prefAlert = findPreference("alert");
        prefNotificationInterval = findPreference("notification_interval");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivityConnector = getActivity();
        // TODO решить что делать если контекст не приехал
        if (mainActivityConnector==null)
            onDestroyView();
    }
}

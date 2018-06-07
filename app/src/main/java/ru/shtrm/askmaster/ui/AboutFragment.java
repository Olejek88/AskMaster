package ru.shtrm.askmaster.ui;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import ru.shtrm.askmaster.BuildConfig;
import ru.shtrm.askmaster.R;
import ru.shtrm.askmaster.customtabs.CustomTabsHelper;

import static android.content.Context.CLIPBOARD_SERVICE;

public class AboutFragment extends PreferenceFragmentCompat {

    private Preference prefRate, prefLicenses,
                prefSourceCode, prefSendAdvices, prefDonate;

    private Preference prefVersion;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_prefs);

        initPrefs();

        prefVersion.setSummary(BuildConfig.VERSION_NAME);

        // Rate
        prefRate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex){
                    showError();
                }
                return true;
            }
        });

        // Licenses
        prefLicenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), PrefsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(PrefsActivity.EXTRA_FLAG, PrefsActivity.FLAG_LICENSES);
                startActivity(intent);
                return true;
            }
        });

        // Donate
        prefDonate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setTitle(R.string.donate);
                dialog.setMessage(getString(R.string.donate_msg));
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // add the alipay account to clipboard
                        ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("text", getString(R.string.donate_account));
                        manager.setPrimaryClip(clipData);
                        dialog.dismiss();
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                return true;
            }
        });

    }

    /**
     * Init the preferences.
     */
    private void initPrefs() {
        prefVersion = findPreference("version");
        prefRate = findPreference("rate");
        prefLicenses = findPreference("licenses");
        prefSourceCode = findPreference("source_code");
        prefSendAdvices = findPreference("send_advices");
        prefDonate = findPreference("donate");
    }

    private void showError() {
        Toast.makeText(getContext(), R.string.something_wrong, Toast.LENGTH_SHORT).show();
    }

}

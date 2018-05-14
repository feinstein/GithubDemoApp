package br.com.feinstein.technicaltest_mf.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Utils {
    /**
     * Anbre uma URL no navegador do dispositivo.
     * @param url a ser aberta no navegador do dispositivo.
     * @param context necessario para enviar o {@link Intent}.
     */
    public static void openWebPage(String url, Context context) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}

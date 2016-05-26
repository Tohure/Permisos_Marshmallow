package com.example.permisosenmarshmallow;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View vista;
    private static final int SOLICITUD_PERMISO_WRITE_CALL_LOG = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vista = findViewById(R.id.content_main);
        borrarLlamada();
    }

    private void borrarLlamada() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            getContentResolver().delete(CallLog.Calls.CONTENT_URI, "number='555555555'", null);
            Snackbar.make(vista, "Llamadas borradas del registro.", Snackbar.LENGTH_SHORT).show();
        } else {
            solicitarPermisoBorrarLlamada();
        }
    }

    private void solicitarPermisoBorrarLlamada() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALL_LOG)) {
            Snackbar.make(vista, "Sin el permiso administrar llamadas no puedo borrar llamadas del registro.", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_CALL_LOG}, SOLICITUD_PERMISO_WRITE_CALL_LOG);
                    }
                }
            }).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALL_LOG}, SOLICITUD_PERMISO_WRITE_CALL_LOG);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_WRITE_CALL_LOG) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                borrarLlamada();
            } else {
                Snackbar.make(vista, "Sin el permiso, no puedo realizar la acción", Snackbar.LENGTH_SHORT).show();
            }
        }
    }
}
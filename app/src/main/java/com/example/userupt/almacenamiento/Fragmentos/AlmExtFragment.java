package com.example.userupt.almacenamiento.Fragmentos;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userupt.almacenamiento.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by USERUPT on 22/04/2017.
 */

public class AlmExtFragment extends Fragment {

    final String fichero = Environment.getExternalStorageDirectory() + "/Documento.txt";
    Context context;

    // Atributos para permisos
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.alm_externo,container,false);
    }

    @Override
    public void onResume(){
        super.onResume();

        actualizarEtiqueta();

        context = getContext();
        final EditText editar = (EditText)getActivity().findViewById(R.id.txt_editar);

        Button agregar = (Button)getActivity().findViewById(R.id.btn_agregar);

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarArchivo(editar.getText().toString(),context);
                editar.setText("");
                actualizarEtiqueta();
            }
        });

    }

    public void guardarArchivo(String datos, Context context){
        String stadoSD = Environment.getExternalStorageState();

        if (!stadoSD.equals(Environment.MEDIA_MOUNTED)){
            Snackbar.make(getView(),"No es posible escribir" + "en la memoria externa", Snackbar.LENGTH_SHORT).show();
            return;
        }
        try{
            verifyStoragePermissions(getActivity());
            FileOutputStream fileOutputStream = new FileOutputStream(fichero,true);

            String texto = datos + "\n";
            fileOutputStream.write(texto.getBytes());
            Toast.makeText(context,"Cadena Guardada", Toast.LENGTH_LONG).show();
            fileOutputStream.close();

        }catch (Exception e){
            Log.e("App ficheros",e.getMessage(),e);
        }
    }

    public Vector<String> obtenerData(Context context){
        Vector<String> result = new Vector<>();
        String stadoSD =  Environment.getExternalStorageState();
        if (!stadoSD.equals(Environment.MEDIA_MOUNTED) && !stadoSD.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            Snackbar.make(getView(),"No es posible escribir" + "en la memoria externa", Snackbar.LENGTH_SHORT).show();
            return result;
        }
        try{
            verifyStoragePermissions(getActivity());

            FileInputStream f = new FileInputStream(fichero);

            BufferedReader entrada = new BufferedReader(new InputStreamReader(f));

            String linea;
            do{
                linea = entrada.readLine();
                if (linea != null){
                    result.add(linea);
                }
            }while (linea != null);
            f.close();
        }catch (Exception e){
            Log.e("App ficheros",e.getMessage(),e);
        }
        return result;
    }

    public void actualizarEtiqueta(){
        context = getContext();
        TextView archivo = (TextView)getActivity().findViewById(R.id.txt_view);
        Vector<String> data = obtenerData(context);
        archivo.setText(data.toString());
    }


    public static void verifyStoragePermissions(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED){
            // NO hay permiso
            ActivityCompat.requestPermissions(activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }


}

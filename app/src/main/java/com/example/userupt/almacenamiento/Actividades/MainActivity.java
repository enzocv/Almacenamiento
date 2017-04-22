package com.example.userupt.almacenamiento.Actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.example.userupt.almacenamiento.Fragmentos.AlmExtFragment;
import com.example.userupt.almacenamiento.Fragmentos.AlmIntFragment;
import com.example.userupt.almacenamiento.R;

import es.dmoral.coloromatic.ColorOMaticDialog;
import es.dmoral.coloromatic.IndicatorMode;
import es.dmoral.coloromatic.OnColorSelectedListener;
import es.dmoral.coloromatic.colormode.ColorMode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        int color = sharedPreferences.getInt(getString(R.string.sp_color_bar),0);
        if (color != 0){
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            window.setNavigationBarColor(color);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            new ColorOMaticDialog.Builder()
                    .initialColor(Color.BLACK)
                    .colorMode(ColorMode.ARGB)
                    .indicatorMode(IndicatorMode.HEX)
                    .onColorSelected(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(@ColorInt int i) {
                            SharedPreferences sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(getString(R.string.sp_color_bar),i);
                            editor.commit();
                            Window window = MainActivity.this.getWindow();
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(i);
                            window.setNavigationBarColor(i);

                        }
                    })
                    .showColorIndicator(true)
                    .create()
                    .show(getSupportFragmentManager(),"ColorPickerDialog");
            return true;
        }else
            if (id == R.id.action_almint){
            AlmIntFragment almIntFragment = new AlmIntFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else
            if (id == R.id.action_almext){
                AlmExtFragment almExtFragment = new AlmExtFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container,almExtFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

}

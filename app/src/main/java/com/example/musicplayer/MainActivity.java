package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.l1);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                Dexter.withContext(MainActivity.this)
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                ArrayList<File> mysongs = fetchSongs(Environment.getExternalStorageDirectory());
                                String [] items = new String[mysongs.size()];
                                for (int i =0;i<mysongs.size();i++){
                                    items[i] = mysongs.get(i).getName().replace("mp3","");
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                        Intent intent = new Intent(getApplicationContext(),playsong.class);
                                        String currentsong = listView.getItemAtPosition(position).toString();
                                        intent.putExtra("songlist",mysongs);
                                        intent.putExtra("currentsong",currentsong);
                                        intent.putExtra("position",position);
                                        startActivity(intent);
                                    }
                                });

                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();

                            }
                        })
                        .check();
            }
            public ArrayList<File> fetchSongs(File file){
                ArrayList arrayList = new ArrayList();
                File [] songs = file.listFiles();
                if (songs !=null){
                    for (File myfile: songs){
                        if (!myfile.isHidden() && myfile.isDirectory()){
                            arrayList.addAll(fetchSongs(myfile));
                        }
                        else{
                            if (myfile.getName().endsWith(".mp3")){
                                arrayList.add(myfile);
                            }
                        }
                    }
                }
                return arrayList;
            }
//            },10);
//        }
}
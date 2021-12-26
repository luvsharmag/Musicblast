package com.example.musicblast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
       listView  = findViewById(R.id.listview);
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ArrayList<File> music = fetchsongs(Environment.getExternalStorageDirectory());
                        String[] items = new String[music.size()];
                        for(int i = 0; i< music.size() ; i++){
                            items[i] = music.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,items);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this,playsong.class);
                                String crs = listView.getItemAtPosition(position).toString();
                                intent.putExtra("all music",music);
                                intent.putExtra("music name",crs);
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
   public ArrayList<File> fetchsongs(File file){
        ArrayList arrayList = new ArrayList();
        File[] myfile  = file.listFiles();
        if(myfile!=null){
            for(File myfiles :myfile){
                if(!myfiles.isHidden()&&myfiles.isDirectory()){
                    arrayList.addAll(fetchsongs(myfiles));
                }else{
                    if(myfiles.getName().endsWith(".mp3")&&!myfiles.getName().startsWith(".")){
                        arrayList.add(myfiles);
                    }
                }
            }
        }
        return arrayList;
   }
}
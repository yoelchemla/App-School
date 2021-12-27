package com.example.myapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class gradesStudent extends AppCompatActivity {

    private FirebaseUser fUser;
    private FirebaseDatabase database;
    private FirebaseAuth sAuth;
    private DatabaseReference Lessons;
    String currID;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades_student);

        database = FirebaseDatabase.getInstance();
        sAuth = FirebaseAuth.getInstance();

        Lessons = database.getReference("Lessons");
        listView = (ListView) findViewById(R.id.gradeslistview);

        Toast.makeText(gradesStudent.this, "getUid: " + sAuth.getUid(), Toast.LENGTH_LONG).show();


        List<Map<String, String>> listArray = new ArrayList<>();


        // ArrayAdapter arrayAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,LessonList);

        Lessons.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.child("students_CourseGrade").hasChild(Objects.requireNonNull(sAuth.getUid())))
                    {
                        String lesson_name=ds.getKey();
                        String grade=Objects.requireNonNull(ds.child("students_CourseGrade").
                                child(Objects.requireNonNull(sAuth.getUid())).getValue()).toString();


                        Map<String, String> listItem = new HashMap<>();
                        listItem.put("titleKey", lesson_name);
                        listItem.put("detailKey", grade);
                        listArray.add(listItem);

                    }
                }
                SimpleAdapter simpleAdapter = new SimpleAdapter(gradesStudent.this, listArray,
                        android.R.layout.simple_list_item_2,
                        new String[] {"titleKey", "detailKey" },
                        new int[] {android.R.id.text1, android.R.id.text2 });
                listView.setAdapter(simpleAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
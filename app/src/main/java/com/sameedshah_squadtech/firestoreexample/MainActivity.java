package com.sameedshah_squadtech.firestoreexample;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";


  FirebaseFirestore db = FirebaseFirestore.getInstance();
  DocumentReference noteRef = db.collection("Notebook").document("My First Note");
  //DocumentReference noteRef = db.document("Notebook/My First Note");

    EditText edt_text_title,edt_text_desc;
    TextView textview_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_text_desc = findViewById(R.id.edt_text_desc);
        edt_text_title = findViewById(R.id.edt_text_title);
        textview_data = findViewById(R.id.text_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
      noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if(e != null){

                    Toast.makeText(MainActivity.this, "Error While loading!", Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onEvent: " + e.toString());
                    return;
                }
                
                if(documentSnapshot.exists()){


                    Note note = documentSnapshot.toObject(Note.class);

                    String title= note.getTitle();
                    String desc = note.getDescription();

                    textview_data.setText("Title : " + title + "\n" + "Description : " + desc);
                }else {
                    textview_data.setText("");
                }
            }
        });
    }



    public void saveNote(View view){

        String title = edt_text_title.getText().toString();
        String description = edt_text_desc.getText().toString();

//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//        note.put(KEY_DESCRIPTION, description);

        Note note = new Note(title,description);


        //db.collection("Notebook/My First Note"); you can also use this method
        noteRef.set(note).addOnCompleteListener(
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Notes Uploaded", Toast.LENGTH_SHORT).show();
                        }else{

                            Toast.makeText(MainActivity.this,task.getException().getMessage().toString(),
                                    Toast.LENGTH_LONG);
                            Log.d(TAG, "Error : " +task.getException().getMessage().toString() );
                        }
                    }
                }
        );

    }

    public void  updateDesc(View view){
        String description = edt_text_desc.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put(KEY_DESCRIPTION,description);

        noteRef.set(map, SetOptions.merge());
        //noteRef.update(map);
    }

    public void loadNote(View view){

        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){

//                            String title = documentSnapshot.getString(KEY_TITLE);
//                            String desc = documentSnapshot.getString(KEY_DESCRIPTION);
//
//                           // Map<String, Object> map = documentSnapshot.getData();

                            Note note = documentSnapshot.toObject(Note.class);

                            String title= note.getTitle();
                            String desc = note.getDescription();

                            textview_data.setText("Title : " + title + "\n" + "Description : " + desc);

                        }else{
                            Toast.makeText(MainActivity.this, "Data doesn't exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void deleteDescription(View view){

        //Map<String, Object> desc = new HashMap<>();
       // desc.put(KEY_DESCRIPTION, FieldValue.delete());
     //   noteRef.update(desc);
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());


    }


    public void deleteNote(View view){

        noteRef.delete();
    }
}

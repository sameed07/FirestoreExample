package com.sameedshah_squadtech.firestoreexample;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference notebookRef = db.collection("Notebook");
    DocumentReference noteRef = db.collection("Notebook").document("My First Note");
    //DocumentReference noteRef = db.document("Notebook/My First Note");

    EditText edt_text_title, edt_text_desc, edt_text_priority;
    TextView textview_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_text_desc = findViewById(R.id.edt_text_desc);
        edt_text_title = findViewById(R.id.edt_text_title);
        edt_text_priority = findViewById(R.id.edt_text_priority);
        textview_data = findViewById(R.id.text_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
//      noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//
//                if(e != null){
//
//                    Toast.makeText(MainActivity.this, "Error While loading!", Toast.LENGTH_SHORT).show();
//
//                    Log.d(TAG, "onEvent: " + e.toString());
//                    return;
//                }
//
//                if(documentSnapshot.exists()){
//
//
//                    Note note = documentSnapshot.toObject(Note.class);
//
//                    String title= note.getTitle();
//                    String desc = note.getDescription();
//
//                    textview_data.setText("Title : " + title + "\n" + "Description : " + desc);
//                }else {
//                    textview_data.setText("");
//                }
//            }
//        }); this method is just for single value
        notebookRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null) {
                    return;
                }
                String data = "";
                for (QueryDocumentSnapshot dataSnapshot : queryDocumentSnapshots) {

                    Note note = dataSnapshot.toObject(Note.class);

                    note.setDocumentId(dataSnapshot.getId());
                    String title = note.getTitle();
                    String desc = note.getDescription();
                    int priority = note.getPriority();
                    String documentId = note.getDocumentId();


                    data += "title: " + title + "\nDescription: " + desc + "\nID: " + documentId +
                            "\nPriority: " + priority + "\n\n";
                }
                textview_data.setText(data);
            }
        });
    }


    public void saveNote(View view) {

        String title = edt_text_title.getText().toString();
        String description = edt_text_desc.getText().toString();

        if (edt_text_priority.length() == 0) {
            edt_text_priority.setText("0");
        }
        int priority = Integer.parseInt(edt_text_priority.getText().toString());


//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//        note.put(KEY_DESCRIPTION, description);

        Note note = new Note(title, description, priority);


        //db.collection("Notebook/My First Note"); you can also use this method
//        noteRef.set(note).addOnCompleteListener(
//                new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(MainActivity.this, "Notes Uploaded", Toast.LENGTH_SHORT).show();
//                        }else{
//
//                            Toast.makeText(MainActivity.this,task.getException().getMessage().toString(),
//                                    Toast.LENGTH_LONG);
//                            Log.d(TAG, "Error : " +task.getException().getMessage().toString() );
//                        }
//                    }
//                }
//        ); just for single value we use this method

        //for multiple we use this method
        notebookRef.add(note);

    }

    public void updateDesc(View view) {
        String description = edt_text_desc.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put(KEY_DESCRIPTION, description);

        noteRef.set(map, SetOptions.merge());
        //noteRef.update(map);
    }

    public void loadNote(View view) {

//        noteRef.get()
//                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                    @Override
//                    public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                        if(documentSnapshot.exists()){
//
////                            String title = documentSnapshot.getString(KEY_TITLE);
////                            String desc = documentSnapshot.getString(KEY_DESCRIPTION);
////
////                           // Map<String, Object> map = documentSnapshot.getData();
//
//                            Note note = documentSnapshot.toObject(Note.class);
//
//                            String title= note.getTitle();
//                            String desc = note.getDescription();
//
//                            textview_data.setText("Title : " + title + "\n" + "Description : " + desc);
//
//                        }else{
//                            Toast.makeText(MainActivity.this, "Data doesn't exists", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                }); for single value we use this method

        Task task = notebookRef.whereLessThan("priority", 2)
//                .whereEqualTo("title","Aa")
                .orderBy("priority")
                .get();

        Task task2 = notebookRef.whereGreaterThan("priority", 2)
                .orderBy("priority")
                .get();

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(task, task2);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {

                String data = "";
                for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        Note note = documentSnapshot.toObject(Note.class);
                        note.setDocumentId(documentSnapshot.getId());
                        String title = note.getTitle();
                        String description = note.getDescription();
                        String documentId = note.getDocumentId();
                        int priority = note.getPriority();

                        data += "title: " + title + "\nDescription: " + description + "\nID: " + documentId +
                                "\nPriority: " + priority + "\n\n";
                    }
                }
                textview_data.setText(data);
            }
        });
    }

    public void deleteDescription(View view) {

        //Map<String, Object> desc = new HashMap<>();
        // desc.put(KEY_DESCRIPTION, FieldValue.delete());
        //   noteRef.update(desc);
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());


    }


    public void deleteNote(View view) {

        noteRef.delete();
    }
}

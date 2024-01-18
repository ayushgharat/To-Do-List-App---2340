package com.example.project1;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.project1.utils.ClassDbHelper;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private ClassDbHelper dbHelper;
    private SimpleCursorAdapter cursorAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootview = inflater.inflate(R.layout.fragment_home, container, false);

        // Find ListView or another appropriate view
        ListView listView = rootview.findViewById(R.id.listView);

        // Set up database helper
        dbHelper = new ClassDbHelper(requireContext());

        // Set up cursor adapter
        String[] fromColumns = {ClassDbHelper.COLUMN_CLASS_NAME, ClassDbHelper.COLUMN_START_TIME, ClassDbHelper.COLUMN_END_TIME, ClassDbHelper.COLUMN_INSTRUCTOR_NAME};
        int[] toViews = {R.id.classNameTextView, R.id.startTimeTextView, R.id.endTimeTextView, R.id.instructorNameTextView};
        cursorAdapter = new SimpleCursorAdapter(requireContext(), R.layout.list_item_layout, null, fromColumns, toViews, 0);
        listView.setAdapter(cursorAdapter);

        // Update UI when there is a change in the database
        updateUI();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Cursor cursor = dbHelper.getClassById(l);

//                if (cursor != null && cursor.moveToFirst()) {
//
//                    // Extract data from the cursor
//                    @SuppressLint("Range") String className = cursor.getString(cursor.getColumnIndex(ClassDbHelper.COLUMN_CLASS_NAME));
////                    String startTime = cursor.getString(cursor.getColumnIndex(ClassDbHelper.COLUMN_START_TIME));
////                    String endTime = cursor.getString(cursor.getColumnIndex(ClassDbHelper.COLUMN_END_TIME));
////                    String instructorName = cursor.getString(cursor.getColumnIndex(ClassDbHelper.COLUMN_INSTRUCTOR_NAME));
//
//                    // Display data in TextViews
//                    Toast.makeText(getActivity(), className, Toast.LENGTH_SHORT).show();
//
//                    // Close the cursor
//                    cursor.close();
//                }

                Bundle bundle = new Bundle();
                bundle.putLong("id", l);

                ClassDetailsFragment classDetailsFragment = new ClassDetailsFragment();
                classDetailsFragment.setArguments(bundle);

                // Use FragmentManager to add the new fragment to the container
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, classDetailsFragment)
                        .addToBackStack(null)  // Optional: Add to back stack for back navigation
                        .commit();

            }
        });

        Button fab = rootview.findViewById(R.id.add_class_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an instance of the new fragment
                AddNewClassFragment addNewClassFragment = new AddNewClassFragment();

                // Use FragmentManager to add the new fragment to the container
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, addNewClassFragment)
                        .addToBackStack(null)  // Optional: Add to back stack for back navigation
                        .commit();
            }
        });

        return rootview;
    }

    private void updateUI() {
        // Query the database for all classes
        Cursor cursor = dbHelper.getAllClasses();

        // Update the cursor in the adapter to refresh the UI
        cursorAdapter.changeCursor(cursor);
    }
}
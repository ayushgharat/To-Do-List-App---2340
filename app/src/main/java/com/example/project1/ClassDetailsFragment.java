package com.example.project1;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project1.utils.ClassDbHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ClassDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ClassDbHelper dbHelper;
    private ImageButton delete;
    private ImageButton edit;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private long id;

    public ClassDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassDetailsFragment newInstance(String param1, String param2) {
        ClassDetailsFragment fragment = new ClassDetailsFragment();
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
            id = getArguments().getLong("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_class_details, container, false);

        TextView classNameTV = rootView.findViewById(R.id.cd_class_name_text);
        TextView startTimeTV = rootView.findViewById(R.id.cd_start_time);
        TextView endTimeTV = rootView.findViewById(R.id.cd_end_time);
        TextView instructorNameTV = rootView.findViewById(R.id.cd_instructor_name);
        delete = rootView.findViewById(R.id.cd_delete);
        edit = rootView.findViewById(R.id.cd_edit);

        // Set up database helper
        dbHelper = new ClassDbHelper(requireContext());

        Cursor cursor = dbHelper.getClassById(id);

        if (cursor != null && cursor.moveToFirst()) {
            // Extract data from the cursor
            @SuppressLint("Range") String className = cursor.getString(cursor.getColumnIndex(ClassDbHelper.COLUMN_CLASS_NAME));
            @SuppressLint("Range") String startTime = cursor.getString(cursor.getColumnIndex(ClassDbHelper.COLUMN_START_TIME));
            @SuppressLint("Range") String endTime = cursor.getString(cursor.getColumnIndex(ClassDbHelper.COLUMN_END_TIME));
            @SuppressLint("Range") String instructorName = cursor.getString(cursor.getColumnIndex(ClassDbHelper.COLUMN_INSTRUCTOR_NAME));

            // Display data in TextViews
            classNameTV.setText(className);
            startTimeTV.setText(startTime);
            endTimeTV.setText(endTime);
            instructorNameTV.setText(instructorName);

            // Close the cursor
            cursor.close();
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteClass(id);
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                // Remove the current fragment from the back stack
                fragmentManager.popBackStack();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dbHelper.close();

        return rootView;
    }
}
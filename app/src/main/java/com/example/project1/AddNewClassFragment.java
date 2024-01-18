package com.example.project1;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project1.utils.ClassDbHelper;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewClassFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewClassFragment extends Fragment {

    private TextInputEditText classNameEditText;
    private TextInputEditText startTimeEditText;
    private TextInputEditText endTimeEditText;
    private TextInputEditText instructorEditText;
    private Button submitButton;

    private ClassDbHelper dbHelper;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewClassFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewClassFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewClassFragment newInstance(String param1, String param2) {
        AddNewClassFragment fragment = new AddNewClassFragment();
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
        View rootview = inflater.inflate(R.layout.fragment_add_new_class, container, false);

        classNameEditText = rootview.findViewById(R.id.class_name_input);
        startTimeEditText = rootview.findViewById(R.id.start_time_input);
        endTimeEditText = rootview.findViewById(R.id.end_time_input);
        instructorEditText = rootview.findViewById(R.id.instructor_name_input);
        submitButton = rootview.findViewById(R.id.submit_class_button);

        setUpTimePickerDialog(startTimeEditText);
        setUpTimePickerDialog(endTimeEditText);

        // Set up database helper
        dbHelper = new ClassDbHelper(requireContext());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSubmitButtonClick();
            }
        });

        return rootview;
    }

    private void setUpTimePickerDialog(final TextInputEditText editText) {
        final Calendar calendar = Calendar.getInstance();

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        requireContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                editText.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                            }
                        },
                        hour,
                        minute,
                        true
                );

                timePickerDialog.show();
            }
        });
    }


    private void handleSubmitButtonClick() {
        // Perform error handling for all inputs
        if (isInputValid()) {
            // Process the inputs
            String className = classNameEditText.getText().toString();
            String startTime = startTimeEditText.getText().toString();
            String endTime = endTimeEditText.getText().toString();
            String instructorName = instructorEditText.getText().toString();

            // Perform further actions (e.g., send data to server, navigate to another screen)
            // Get a writable database
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(ClassDbHelper.COLUMN_CLASS_NAME, className);
            values.put(ClassDbHelper.COLUMN_START_TIME, startTime);
            values.put(ClassDbHelper.COLUMN_END_TIME, endTime);
            values.put(ClassDbHelper.COLUMN_INSTRUCTOR_NAME, instructorName);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(ClassDbHelper.TABLE_CLASSES, null, values);

            // Display a success message or handle errors
            if (newRowId != -1) {
                // Data insertion successful

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

                // Remove the current fragment from the back stack
                fragmentManager.popBackStack();


            } else {
                // Handle error (e.g., display an error message)
                Toast.makeText(getActivity(), "Error: unsuccessful write to DB", Toast.LENGTH_SHORT).show();
            }

            // Close the database connection
            db.close();

            // Display a success message
            Toast.makeText(requireContext(), "Submission successful", Toast.LENGTH_SHORT).show();
        } else {
            // Display an error message
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to validate all inputs
    private boolean isInputValid() {
        return !classNameEditText.getText().toString().isEmpty()
                && !startTimeEditText.getText().toString().isEmpty()
                && !endTimeEditText.getText().toString().isEmpty()
                && !instructorEditText.getText().toString().isEmpty();
    }
}
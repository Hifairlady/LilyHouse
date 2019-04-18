package com.example.lilyhouse.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.lilyhouse.R;
import com.google.android.material.button.MaterialButton;

public class FilterDialogFragment extends DialogFragment {

    private static final String TAG = "===FilterDialogFragment";
    private static final String OLD_REQUEST_CODES_PARAM = "OLD_REQUEST_CODES_PARAM";
    int subjectCode = 0, groupCode = 0, statusCode = 0, regionCode = 0, sortCode = 0, pageCode = 0;
    private Spinner spinnerSubjects, spinnerRegion, spinnerStatus;
    private MaterialButton btnCancel, btnApply;
    private OnFilterApplyListener mOnFilterApplyListener;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_apply_filter:
                    if (mOnFilterApplyListener == null) {
                        Log.d(TAG, "onClick: NULL");
                        dismiss();
                        break;
                    }
                    int[] newRequestCodes = new int[6];
                    newRequestCodes[0] = subjectCode;
                    newRequestCodes[1] = groupCode;
                    newRequestCodes[2] = statusCode;
                    newRequestCodes[3] = regionCode;
                    newRequestCodes[4] = sortCode;
                    newRequestCodes[5] = 0;
                    mOnFilterApplyListener.onFilterApply(newRequestCodes);
                    dismiss();
                    break;

                case R.id.btn_cancel_filter:
                    dismiss();
                    break;

                default:
                    break;
            }
        }
    };

    private AdapterView.OnItemSelectedListener subjectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemSelected: " + position + " id: " + id);
            subjectCode = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener regionListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemSelected: " + position + " id: " + id);
            regionCode = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private AdapterView.OnItemSelectedListener statusListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "onItemSelected: " + position + " id: " + id);
            statusCode = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public FilterDialogFragment() {
    }

    public static FilterDialogFragment newInstance(int[] oldRequestCodes) {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putIntArray(OLD_REQUEST_CODES_PARAM, oldRequestCodes);
        filterDialogFragment.setArguments(args);
        return filterDialogFragment;
    }

    public void setOnFilterApplyListener(OnFilterApplyListener mOnFilterApplyListener) {
        this.mOnFilterApplyListener = mOnFilterApplyListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int[] oldRequestCodes = getArguments().getIntArray(OLD_REQUEST_CODES_PARAM);
            if (oldRequestCodes != null && oldRequestCodes.length == 6) {
                subjectCode = oldRequestCodes[0];
                groupCode = oldRequestCodes[1];
                statusCode = oldRequestCodes[2];
                regionCode = oldRequestCodes[3];
                sortCode = oldRequestCodes[4];
                pageCode = oldRequestCodes[5];
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.filter_dialog_layout, container, false);
        spinnerSubjects = rootView.findViewById(R.id.spinner_subject);
        spinnerRegion = rootView.findViewById(R.id.spinner_region);
        spinnerStatus = rootView.findViewById(R.id.spinner_status);
        btnCancel = rootView.findViewById(R.id.btn_cancel_filter);
        btnApply = rootView.findViewById(R.id.btn_apply_filter);

        btnCancel.setOnClickListener(mOnClickListener);
        btnApply.setOnClickListener(mOnClickListener);

        spinnerSubjects.setSelection(subjectCode);
        spinnerRegion.setSelection(regionCode);
        spinnerStatus.setSelection(statusCode);

        spinnerSubjects.setOnItemSelectedListener(subjectListener);
        spinnerRegion.setOnItemSelectedListener(regionListener);
        spinnerStatus.setOnItemSelectedListener(statusListener);

        return rootView;
    }

    public interface OnFilterApplyListener {
        void onFilterApply(int[] requestCodes);
    }

}

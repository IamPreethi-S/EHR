package com.example.ehr;

import static android.text.TextUtils.concat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class LaboratoryAdapter extends RecyclerView.Adapter<LaboratoryViewHolder> {


    List<LaboratoryPendingTestsModel> testList;
    LaboratoryFragment testFragment;
    TextChangeCallback callback;

    public LaboratoryAdapter(List<LaboratoryPendingTestsModel> testList, LaboratoryFragment testFragment, TextChangeCallback callback) {
        this.testList = testList;
        this.testFragment = testFragment;
        this.callback = callback;
    }


    @Override
    public int getItemViewType(final int position) {
        return R.layout.laboratory_pending_test_view;
    }


    @NonNull
    @Override
    public LaboratoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new LaboratoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LaboratoryViewHolder holder, int position) {
        LaboratoryPendingTestsModel testModel = testList.get(position);
        String testname = testModel.getTestname();
        String firstname = testModel.getFirstname().concat(" ").concat(testModel.getLastname());
        //String lastname = testModel.getLastname();
        String testreport = testModel.getTestreport();
        String testid = testModel.getTestid();

        holder.testname.setText(testname);
        holder.firstname.setText(firstname);
        //holder.lastname.setText(lastname);
        holder.testid.setText(testid);
        holder.testreport.setText(testreport);
        holder.testreport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (charSequence.equals(" "))
            {
                return;
                //Toast.makeText(testFragment.getActivity(), "Please update test report status",Toast.LENGTH_SHORT).show();
            }
            testModel.set(holder.getAdapterPosition(), String.valueOf(charSequence));
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        holder.Addtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View editLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.laboratory_pending_test_view,null);
                EditText Testreport = editLayout.findViewById(R.id.laboratory_test_report);
                Testreport.setText(testModel.getTestreport());

                String testreport = Testreport.getText().toString();

                testModel.setTestreport(testreport);
                testFragment.onAddtestClicked(testModel);

            }
        });

    }
    @Override
    public int getItemCount()
    {
        return testList.size();
    }

    public interface TextChangeCallback {
        void textChangedAt(int position, LaboratoryPendingTestsModel text);
    }
}

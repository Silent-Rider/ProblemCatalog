package com.example.problem_catalog.gui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.problem_catalog.R;
import com.example.problem_catalog.model.entities.Problem;
import com.example.problem_catalog.viewmodel.AppViewModel;

import java.util.ArrayList;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private AppViewModel viewModel;
    private ViewGroup sidePanel;
    private Button updateButton;
    private EditText searchEditText;
    private RecyclerViewAdapter adapter;
    private boolean isSidePanelVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustActivity();
        initializeActivity();
        launchActivity();
    }

    private void launchActivity(){
        viewModel.getProblemsLiveData().observe(this, problems ->
            adapter.setProblemsList(problems.stream().map(Problem::getName).collect(Collectors.toList())));
        ImageButton menuButton = findViewById(R.id.menuButton);
        ImageButton closeButton = findViewById(R.id.closeButton);
        menuButton.setOnClickListener(v -> toggleSidePanel());
        closeButton.setOnClickListener(v -> toggleSidePanel());
        updateButton.setOnClickListener(v -> viewModel.updateData());
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(s).trim();
                if (!text.isEmpty()) {
                    viewModel.reduceProblemsByRegex(text);
                } else {
                    viewModel.resetProblems();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initializeActivity(){
        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        sidePanel = findViewById(R.id.sidePanel);
        updateButton = findViewById(R.id.updateButton);
        searchEditText = findViewById(R.id.searchEditText);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }

    private void adjustActivity(){
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void toggleSidePanel() {
        if (isSidePanelVisible) {
            sidePanel.setVisibility(View.GONE);
            isSidePanelVisible = false;
        } else {
            sidePanel.setVisibility(View.VISIBLE);
            isSidePanelVisible = true;
        }
    }
}
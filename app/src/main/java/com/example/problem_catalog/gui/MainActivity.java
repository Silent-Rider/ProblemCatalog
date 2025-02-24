package com.example.problem_catalog.gui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private boolean isSidePanelVisible = false;
    //Activity
    private ImageButton openPanelButton;
    private EditText searchEditText;
    private RecyclerViewAdapter adapter;
    // Side panel
    private ViewGroup sidePanel;
    private ImageButton closePanelButton;
    private Button updateButton;
    private Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adjustActivity();
        initializeActivity();
        launchActivity();
    }

    private void launchActivity(){
        viewModel.resetProblems();
        openPanelButton.setOnClickListener(v -> toggleSidePanel());
        closePanelButton.setOnClickListener(v -> toggleSidePanel());
        updateButton.setOnClickListener(v -> viewModel.updateData());
        aboutButton.setOnClickListener(v -> showAboutDialog(false));
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = String.valueOf(s).trim();
                if (!text.isEmpty()) viewModel.reduceProblemsByRegex(text);
                else viewModel.resetProblems();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initializeActivity(){
        openPanelButton = findViewById(R.id.openPanelButton);
        searchEditText = findViewById(R.id.searchEditText);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        sidePanel = findViewById(R.id.sidePanel);
        updateButton = findViewById(R.id.updateButton);
        closePanelButton = findViewById(R.id.closePanelButton);
        aboutButton = findViewById(R.id.aboutButton);

        viewModel = new ViewModelProvider(this).get(AppViewModel.class);
        viewModel.getProblemsLiveData().observe(this, problems ->
                adapter.setProblemsList(problems.stream().map(Problem::getName).collect(Collectors.toList())));
        viewModel.getErrorLiveData().observe(this, error -> showAboutDialog(true));
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

    private void showAboutDialog(boolean isError) {
        String title = isError ? getString(R.string.errorTitle) : getString(R.string.aboutTitle);
        String message = isError ? viewModel.getErrorLiveData().getValue() :
                getString(R.string.aboutMessage);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.ok), (dialog, which) ->
                    dialog.dismiss())
                .create()
                .show();
    }
}
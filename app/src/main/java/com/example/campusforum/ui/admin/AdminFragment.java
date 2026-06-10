package com.example.campusforum.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.adapter.CategoryAdminAdapter;
import com.example.campusforum.model.Category;
import com.example.campusforum.repository.CategoryRepository;
import com.example.campusforum.ui.home.HomeFragment;

import java.util.List;

public class AdminFragment extends Fragment {

    private EditText nameInput;
    private EditText descriptionInput;
    private Button addButton;
    private RecyclerView categoriesRecyclerView;
    private View emptyStateView;
    private CategoryRepository categoryRepository;
    private CategoryAdminAdapter categoryAdapter;
    private boolean addingCategory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categoryRepository = new CategoryRepository(requireContext());
        if (!categoryRepository.isCurrentUserAdmin()) {
            denyAccess();
            return;
        }

        bindViews(view);
        setupCategories();
        addButton.setOnClickListener(button -> addCategory());
        loadCategories();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (categoryRepository != null && categoryRepository.isCurrentUserAdmin()) {
            loadCategories();
        }
    }

    private void bindViews(View view) {
        nameInput = view.findViewById(R.id.admin_category_name_input);
        descriptionInput = view.findViewById(R.id.admin_category_description_input);
        addButton = view.findViewById(R.id.admin_category_add_button);
        categoriesRecyclerView = view.findViewById(R.id.admin_categories_recycler_view);
        emptyStateView = view.findViewById(R.id.admin_categories_empty_state);
    }

    private void setupCategories() {
        categoryAdapter = new CategoryAdminAdapter(this::confirmDeleteCategory);
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        categoriesRecyclerView.setNestedScrollingEnabled(false);
        categoriesRecyclerView.setAdapter(categoryAdapter);
    }

    private void addCategory() {
        if (addingCategory) {
            return;
        }

        nameInput.setError(null);
        addingCategory = true;
        addButton.setEnabled(false);
        CategoryRepository.CategoryActionResult result = categoryRepository.createCategory(
                nameInput.getText().toString(),
                descriptionInput.getText().toString());
        addingCategory = false;
        addButton.setEnabled(true);

        Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
        if (result.isSuccess()) {
            nameInput.setText("");
            descriptionInput.setText("");
            loadCategories();
        } else {
            nameInput.setError(result.getMessage());
        }
    }

    private void loadCategories() {
        List<Category> categories = categoryRepository.getActiveCategories();
        categoryAdapter.updateCategories(categories);
        boolean hasCategories = !categories.isEmpty();
        categoriesRecyclerView.setVisibility(hasCategories ? View.VISIBLE : View.GONE);
        emptyStateView.setVisibility(hasCategories ? View.GONE : View.VISIBLE);
    }

    private void confirmDeleteCategory(Category category) {
        if (category == null) {
            return;
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.cf_admin_category_delete_title)
                .setMessage(getString(R.string.cf_admin_category_delete_confirm, category.getName()))
                .setNegativeButton(R.string.cf_action_cancel, null)
                .setPositiveButton(R.string.cf_action_delete,
                        (dialog, which) -> deleteCategory(category))
                .show();
    }

    private void deleteCategory(Category category) {
        CategoryRepository.CategoryActionResult result =
                categoryRepository.deleteCategory(category.getId());
        Toast.makeText(requireContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
        if (result.isSuccess()) {
            loadCategories();
        }
    }

    private void denyAccess() {
        Toast.makeText(requireContext(), R.string.cf_admin_error_access_required, Toast.LENGTH_SHORT).show();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, new HomeFragment())
                .commit();
    }
}

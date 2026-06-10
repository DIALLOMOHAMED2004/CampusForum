package com.example.campusforum.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusforum.R;
import com.example.campusforum.adapter.TopicAdapter;
import com.example.campusforum.dao.CategoryDao;
import com.example.campusforum.dao.TopicDao;
import com.example.campusforum.model.Category;
import com.example.campusforum.model.Topic;
import com.example.campusforum.ui.topic.CreateTopicActivity;
import com.example.campusforum.ui.topic.TopicDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final long ALL_CATEGORIES_ID = -1L;

    private EditText searchInput;
    private LinearLayout categoryChipsContainer;
    private RecyclerView topicsRecyclerView;
    private View emptyStateView;
    private TextView emptyTitleText;
    private TextView emptyMessageText;
    private TopicAdapter topicAdapter;
    private CategoryDao categoryDao;
    private TopicDao topicDao;
    private List<Category> categories = new ArrayList<>();
    private long selectedCategoryId = ALL_CATEGORIES_ID;
    private String currentSearchQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchInput = view.findViewById(R.id.home_search_input);
        categoryChipsContainer = view.findViewById(R.id.home_category_chips_container);
        topicsRecyclerView = view.findViewById(R.id.home_topics_recycler_view);
        emptyStateView = view.findViewById(R.id.home_empty_state);
        emptyTitleText = view.findViewById(R.id.home_empty_title);
        emptyMessageText = view.findViewById(R.id.home_empty_message);
        categoryDao = new CategoryDao(requireContext());
        topicDao = new TopicDao(requireContext());
        topicAdapter = new TopicAdapter(null, this::openTopicDetail);

        topicsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        topicsRecyclerView.setAdapter(topicAdapter);
        setupSearch();
        view.findViewById(R.id.home_create_topic_button).setOnClickListener(button ->
                openCreateTopic());

        loadCategories();
        loadTopics();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (topicDao != null) {
            loadTopics();
        }
    }

    private void loadTopics() {
        List<Topic> topics = getFilteredTopics();
        if (topics == null) {
            topics = new ArrayList<>();
        }
        topicAdapter.updateTopics(topics);

        boolean hasTopics = !topics.isEmpty();
        topicsRecyclerView.setVisibility(hasTopics ? View.VISIBLE : View.GONE);
        emptyStateView.setVisibility(hasTopics ? View.GONE : View.VISIBLE);
        if (!hasTopics) {
            updateEmptyStateText();
        }
    }

    private List<Topic> getFilteredTopics() {
        boolean hasSearch = !currentSearchQuery.trim().isEmpty();
        boolean hasCategoryFilter = selectedCategoryId != ALL_CATEGORIES_ID;

        if (hasSearch && hasCategoryFilter) {
            return topicDao.searchActiveTopicsByCategory(currentSearchQuery, selectedCategoryId);
        }
        if (hasSearch) {
            return topicDao.searchActiveTopics(currentSearchQuery);
        }
        if (hasCategoryFilter) {
            return topicDao.getActiveTopicsByCategory(selectedCategoryId);
        }
        return topicDao.getActiveTopics();
    }

    private void updateEmptyStateText() {
        boolean hasSearch = !currentSearchQuery.trim().isEmpty();
        boolean hasCategoryFilter = selectedCategoryId != ALL_CATEGORIES_ID;

        if (hasSearch && hasCategoryFilter) {
            emptyTitleText.setText(R.string.cf_home_empty_search_title);
            emptyMessageText.setText(R.string.cf_home_empty_search_category_message);
            return;
        }
        if (hasSearch) {
            emptyTitleText.setText(R.string.cf_home_empty_search_title);
            emptyMessageText.setText(R.string.cf_home_empty_search_message);
            return;
        }
        if (hasCategoryFilter) {
            emptyTitleText.setText(R.string.cf_home_empty_category_title);
            emptyMessageText.setText(R.string.cf_home_empty_category_message);
            return;
        }

        emptyTitleText.setText(R.string.cf_home_empty_title);
        emptyMessageText.setText(R.string.cf_home_empty_message);
    }

    private void setupSearch() {
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                currentSearchQuery = text.toString();
                loadTopics();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void loadCategories() {
        List<Category> loadedCategories = categoryDao.getActiveCategories();
        categories = loadedCategories == null ? new ArrayList<>() : loadedCategories;
        renderCategoryChips();
    }

    private void renderCategoryChips() {
        categoryChipsContainer.removeAllViews();
        addCategoryChip(getString(R.string.cf_home_filter_all), ALL_CATEGORIES_ID, true);

        for (Category category : categories) {
            addCategoryChip(category.getName(), category.getId(), false);
        }
    }

    private void addCategoryChip(String label, long categoryId, boolean firstChip) {
        boolean selected = selectedCategoryId == categoryId;
        int style = selected
                ? R.style.Widget_CampusForum_Chip_Selected
                : R.style.Widget_CampusForum_Chip_Default;
        TextView chip = new TextView(requireContext(), null, 0, style);
        chip.setText(label);
        chip.setGravity(Gravity.CENTER);
        chip.setSingleLine(true);
        chip.setSelected(selected);
        chip.setOnClickListener(view -> {
            selectedCategoryId = categoryId;
            renderCategoryChips();
            loadTopics();
        });

        int chipHeight = getResources().getDimensionPixelSize(R.dimen.cf_chip_height);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                chipHeight);
        if (!firstChip) {
            layoutParams.setMarginStart(getResources().getDimensionPixelSize(R.dimen.cf_spacing_2));
        }
        categoryChipsContainer.addView(chip, layoutParams);
    }

    private void openTopicDetail(Topic topic) {
        if (topic == null || topic.getId() <= 0) {
            return;
        }
        Intent intent = new Intent(requireContext(), TopicDetailActivity.class);
        intent.putExtra(TopicDetailActivity.EXTRA_TOPIC_ID, topic.getId());
        startActivity(intent);
    }

    private void openCreateTopic() {
        startActivity(new Intent(requireContext(), CreateTopicActivity.class));
    }
}

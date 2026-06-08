package com.example.campusforum.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.campusforum.R;
import com.example.campusforum.repository.AuthRepository;
import com.example.campusforum.ui.auth.LoginActivity;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.profile_logout_button).setOnClickListener(v -> confirmLogout());
    }

    private void confirmLogout() {
        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.cf_profile_logout)
                .setMessage("Voulez-vous vraiment vous déconnecter ?")
                .setNegativeButton(R.string.cf_action_cancel, null)
                .setPositiveButton(R.string.cf_profile_logout, (dialog, which) -> logout())
                .show();
    }

    private void logout() {
        new AuthRepository(requireContext()).logout();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}

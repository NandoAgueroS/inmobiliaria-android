package com.example.inmobiliarialab3.ui.logout;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliarialab3.MainActivity;
import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentLogoutBinding;
import com.example.inmobiliarialab3.ui.login.LoginActivity;

public class LogoutFragment extends Fragment {

    private FragmentLogoutBinding binding;
    private LogoutViewModel logoutViewModel;

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLogoutBinding.inflate(inflater, container, false);
        logoutViewModel = new ViewModelProvider(this).get(LogoutViewModel.class);

        View root = binding.getRoot();

        new AlertDialog.Builder(requireContext())
                .setTitle("Salir")
                .setMessage("¿Seguro que quiere salir?")
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutViewModel.eliminarToken();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigate(R.id.nav_inicio);

                    }
                })
                .show();

       logoutViewModel.getMTokenEliminado().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
           @Override
           public void onChanged(Boolean aBoolean) {
               Intent intent = new Intent(getContext(), LoginActivity.class);
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
               requireActivity().finish();
           }
       });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
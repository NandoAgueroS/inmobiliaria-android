package com.example.inmobiliarialab3.ui.inquilinos;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.inmobiliarialab3.R;
import com.example.inmobiliarialab3.databinding.FragmentInmueblesBinding;
import com.example.inmobiliarialab3.databinding.FragmentInquilinosBinding;

public class InquilinosFragment extends Fragment {

    private InquilinosViewModel mViewModel;
    private FragmentInquilinosBinding binding;

    public static InquilinosFragment newInstance() {
        return new InquilinosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInquilinosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
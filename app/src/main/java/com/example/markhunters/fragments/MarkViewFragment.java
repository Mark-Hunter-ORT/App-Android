package com.example.markhunters.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.markhunters.R;
import com.example.markhunters.model.Mark;

public class MarkViewFragment extends MarkFragment {

    private Mark mark;

    public MarkViewFragment(int markId) {
        /* todo acá habría que resolver el fetch de la mark al restClient y asignarle el resultado a 'mark'
        restClient.getMark(markId, new DaoCallback<Marca>() {
            @Override
            public void onCallbackInstance(Marca mark) {
                if (mark != null) {
                    this.mark = mark;
                }
            }
         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mark_view, container, false);
        /*
        todo popular la vista con los datos fetcheados de 'mark'
        root.findViewById...
         */
        return root;
    }
}
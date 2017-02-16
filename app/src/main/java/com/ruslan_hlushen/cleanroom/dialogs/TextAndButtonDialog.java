package com.ruslan_hlushen.cleanroom.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruslan_hlushen.cleanroom.R;

/**
 * Created by Ruslan on 31.01.2017.
 */

public class TextAndButtonDialog extends DialogFragment {

    public static final String Tag = "TextAndButtonDialog";
    TextView textViewTitle;
    String title;
    private OnButtonClickListener onButtonClickListener;
    private ImageView imageView, imageViewClose;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.DialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        dialog.getWindow().getDecorView().setSystemUiVisibility(getActivity().getWindow().getDecorView().getSystemUiVisibility());
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(true);

        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.dialog_text_and_one_button, null);

        initViews(v);
        setOnClickListeners();

        return v;
    }


    @Override
    public void onDestroyView() {

        Dialog dialog = getDialog();

        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }


    private void initViews(View view) {

        textViewTitle = (TextView) view.findViewById(R.id.dialog_text_and_one_button_textViewTitle);
        imageViewClose = (ImageView) view.findViewById(R.id.dialog_text_and_one_button_closeImage);
        imageView = (ImageView) view.findViewById(R.id.dialog_text_and_one_button_imageView);
        imageView.setImageResource(R.drawable.ic_arrow_forward);

        textViewTitle.setText(title);
    }


    private void setOnClickListeners() {

        imageView.setOnClickListener(view -> onButtonClickListener.onButtonClick());
        imageViewClose.setOnClickListener(view -> dismiss());
    }


    public TextAndButtonDialog setParams(String title, OnButtonClickListener onButtonClickListener) {

        this.title = title;
        this.onButtonClickListener = onButtonClickListener;
        return this;
    }


    public interface OnButtonClickListener {

        public void onButtonClick();
    }
}
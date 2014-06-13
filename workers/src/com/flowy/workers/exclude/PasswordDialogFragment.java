package com.flowy.workers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class PasswordDialogFragment extends DialogFragment {

	public static PasswordDialogFragment newInstance(int title) {
		PasswordDialogFragment frag = new PasswordDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");

		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_password_dialog, null);
		return new AlertDialog.Builder(getActivity())
		.setTitle(title)
		.setView(view)
		.setPositiveButton(android.R.string.ok, 
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				((ChooseEmployeeActivity) getActivity()).doPositiveClick(R.id.btn_passLogin);
			}
		})
		.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				((ChooseEmployeeActivity) getActivity()).doNegativeClick();
			}
		})
		.create();
	}

//	@Override 
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		View v = inflater.inflate(R.layout.fragment_password_dialog, container, false);
//		View tv = v.findViewById(R.id.tv_userName);
//		((TextView)tv).setText("Instance of DialogFragment");
//		return v;
//	}

}

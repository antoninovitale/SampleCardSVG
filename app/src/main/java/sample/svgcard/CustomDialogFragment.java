package sample.svgcard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment {

	public static CustomDialogFragment newInstance(String title) {
		CustomDialogFragment frag = new CustomDialogFragment();
		Bundle args = new Bundle();
		args.putString("text", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String text = getArguments().getString("text");
		return new AlertDialog.Builder(getActivity())
				.setTitle("")
				.setMessage(text)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
							}
						}).create();
	}
	
}
package sample.svgcard;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import sample.svgcard.qrencoder.Contents;
import sample.svgcard.qrencoder.QRCodeEncoder;

/**
 * fragment utilizzato per visualizzare un qr code
 * 
 * @author a.vitale
 * 
 */
public class QRCodeFragment extends DialogFragment {

	public static QRCodeFragment newInstance(String title) {
		QRCodeFragment frag = new QRCodeFragment();
		Bundle args = new Bundle();
		args.putString("code", title);
		frag.setArguments(args);
		frag.setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme);
		return frag;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.qr_code, container, false);
		ImageView imageView = (ImageView) v.findViewById(R.id.qrCode);
		String qrData = getArguments().getString("code");
		int qrCodeDimension = 600;
		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qrData, null,
		        Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(), qrCodeDimension);		
		try {
		    Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
		    imageView.setImageBitmap(bitmap);
		} catch (WriterException e) {
		    Log.e("Error", e.getMessage());
		}
		return v;
	}

}
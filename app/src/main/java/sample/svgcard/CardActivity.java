package sample.svgcard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import sample.svgcard.application.AppStartupManager;
import sample.svgcard.svgparser.CustomSVGParser;
import sample.svgcard.svgparser.model.GNode;
import sample.svgcard.svgparser.model.ImageNode;
import sample.svgcard.svgparser.model.SVGNode;
import sample.svgcard.svgparser.model.TextNode;
import sample.svgcard.utils.Flip3dAnimation;
import sample.svgcard.utils.SwapViews;
import sample.svgcard.utils.UpDownAnimation;
import sample.svgcard.utils.Utils;

public class CardActivity extends SherlockFragmentActivity {
    private final static String TAG = CardActivity.class.getSimpleName();
    private final static int START_ROTATION_DEGREE = 0;
    private final static int END_ROTATION_DEGREE = 90;
    private final static int ROTATION_DURATION = 500;
    private final static int ROTATION_REPEAT_COUNT = 0;
    private final static String CARD_ACTIVATED = "activated";
    private final static String CARD_DEACTIVATED = "deactivated";
    private final static String CARD_FRONT_ACTIVATED = "front-activated";
    private final static String CARD_FRONT_DEACTIVATED = "front-deactivated";
    private final static String CARD_BACK = "back";
    private final static String CARD_OTHER = "other";
    private final static String CARD_ACTIONS = "actions";
    private static final String FEATURE_XML = "/feature.xml";
    private static final String DATI_JSON = "/dati.json";
    private boolean isFirstView = true;
    private ParserTask parserTask;
    private GetOtherInfoTask getOtherInfoTask;
    private AssetManager assetManager;
    private SVGNode svgNode;
    private RelativeLayout front_card_container, back_card_container;
    private LinearLayout other_info_container;
    private String card_status;
    private JSONObject card_data;
    private String tokenToSend;
    private LinearLayout other_container;
    private String card_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        getCardStatus();
        setActionBarOptions();
        getAppResources();
        front_card_container = (RelativeLayout) findViewById(R.id.front_card_container);
        back_card_container = (RelativeLayout) findViewById(R.id.back_card_container);
        front_card_container.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppStartupManager.isFlipAllowed()) {
                    if (card_status.equalsIgnoreCase(CARD_ACTIVATED) || card_status.equalsIgnoreCase(CARD_BACK)) {
                        applyRotation(v, back_card_container, START_ROTATION_DEGREE, END_ROTATION_DEGREE, isFirstView);
                        toggleView();
                    }
                }
            }
        });
        other_info_container = (LinearLayout) findViewById(R.id.other_info_container);
        other_container = (LinearLayout) findViewById(R.id.other_container);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (getOtherInfoTask != null && !getOtherInfoTask.isCancelled())
            getOtherInfoTask.cancel(true);
        if (parserTask != null && !parserTask.isCancelled()) parserTask.cancel(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.card, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.i(TAG, "onPrepareOptionsMenu");
        MenuItem action_qrcode = menu.findItem(R.id.action_qrcode);
        MenuItem action_refresh = menu.findItem(R.id.action_refresh);
        if (card_status.equalsIgnoreCase(CARD_ACTIVATED)) {
            action_qrcode.setEnabled(true);
            action_refresh.setEnabled(true);
        } else if (card_status.equalsIgnoreCase(CARD_DEACTIVATED)) {
            action_qrcode.setEnabled(false);
            action_refresh.setEnabled(false);
        } else if (card_status.equalsIgnoreCase(CARD_BACK)) {
            action_qrcode.setEnabled(true);
            action_refresh.setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent resultData = new Intent();
                setResult(RESULT_OK, resultData);
                CardActivity.this.finish();
                break;
            case R.id.action_refresh:
                refresh();
                break;
            case R.id.action_qrcode:
                if (!tokenToSend.equalsIgnoreCase("")) showQRCodeFragment(tokenToSend);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent resultData = new Intent();
        setResult(RESULT_OK, resultData);
        CardActivity.this.finish();
    }

    private void refresh() {
        Log.i(TAG, "refresh");
        resetViews();
        executeGetOtherInfoTask(card_name + DATI_JSON);
    }

    private void getAppResources() {
        assetManager = getAssets();
    }

    private void initializeViews() {
        if (!card_status.equalsIgnoreCase(CARD_DEACTIVATED)) {
            other_container.setVisibility(View.VISIBLE);
        } else {
            other_container.setVisibility(View.GONE);
        }
    }

    private void getCardStatus() {
        card_name = "card";
        card_status = CARD_ACTIVATED;
    }

    private void resetViews() {
        front_card_container.removeAllViews();
        back_card_container.removeAllViews();
        other_info_container.removeAllViews();
    }

    private void setActionBarOptions() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayShowCustomEnabled(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
    }

    private void executeGetOtherInfoTask(String data) {
        getOtherInfoTask = new GetOtherInfoTask();
        getOtherInfoTask.execute(data);
    }

    private void executeParserTask() {
        parserTask = new ParserTask();
        try {
            parserTask.execute(assetManager.open(card_name + FEATURE_XML));
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void toggleView() {
        if (isFirstView) {
            card_status = CARD_BACK;
            isFirstView = false;
        } else {
            card_status = CARD_ACTIVATED;
            isFirstView = true;
        }
    }

    private void applyRotation(final View view1, final View view2, final float start, final float end,
                               final boolean isFirst) {
        final float centerX = view1.getWidth() / 2;
        final float centerY = view1.getHeight() / 2;
        Log.i("WIDGET_POS", "centerX:" + centerX + "/centerY:" + centerY);
        final Flip3dAnimation rotation = new Flip3dAnimation(start, end,
                centerX, centerY);
        int duration = ROTATION_DURATION * Math.abs((int) (end - start)) / 90;
        rotation.setDuration(duration);
        rotation.setRepeatMode(Animation.RESTART);
        rotation.setRepeatCount(ROTATION_REPEAT_COUNT);
        rotation.setFillAfter(true);
        rotation.setZAdjustment(Animation.ZORDER_TOP);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                AppStartupManager.setFlipAllowed(false);
                UpDownAnimation upDownAnimation = new UpDownAnimation(0, -2 * Utils.convertDpToPx(CardActivity.this, (int) getResources().getDimension(R.dimen.activity_vertical_margin)), other_container.getWidth() / 2, other_container.getHeight() / 2);
                upDownAnimation.setDuration(ROTATION_DURATION * Math.abs((int) (end - start)) / 90);
                upDownAnimation.setRepeatMode(Animation.REVERSE);
                upDownAnimation.setRepeatCount(1);
                upDownAnimation.setZAdjustment(Animation.ZORDER_TOP);
                upDownAnimation.setInterpolator(new DecelerateInterpolator());
                other_container.startAnimation(upDownAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.i(TAG, "onAnimationEnd start SwapViews");
                view1.post(new SwapViews(CardActivity.this, isFirst, view1, view2, start, end));
            }
        });
        if (isFirst) {
            view1.startAnimation(rotation);
        } else {
            view2.startAnimation(rotation);
        }
    }

    private class ParserTask extends AsyncTask<InputStream, Void, SVGNode> {

        @Override
        protected SVGNode doInBackground(InputStream... arg0) {
            Log.i(TAG, "start parser");
            CustomSVGParser customSVGParser = new CustomSVGParser(arg0[0]);
            return customSVGParser.parseDocument();
        }

        @Override
        protected void onPostExecute(SVGNode result) {
            super.onPostExecute(result);
            Log.i(TAG, "end parser");
            svgNode = result;
            if (svgNode != null) {
                updateCardDataAndOtherInfo();
                initializeViews();
            }
        }

    }

    private class GetOtherInfoTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            return getDataFromJson(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result != null) {
                card_data = result;
                executeParserTask();
            } else {
                Intent resultData = new Intent();
                resultData.putExtra("result", "The card is not available");
                setResult(Activity.RESULT_CANCELED, resultData);
                CardActivity.this.finish();
            }
        }

    }

    private void updateCardDataAndOtherInfo() {
        drawCard();
        updateOtherInfo();
    }

    private void drawCard() {
        int card_width = Integer.parseInt(svgNode.getWidth().split("px")[0]);
        int card_height = Integer.parseInt(svgNode.getHeight().split("px")[0]);
        int front_card_width = Utils.convertPxToDp(this, front_card_container.getLayoutParams().width);
        int front_card_height = Utils.convertPxToDp(this, front_card_container.getLayoutParams().height);
        double x_scale = (double) card_width / front_card_width;
        double y_scale = (double) card_height / front_card_height;
        Log.i(TAG, "w:" + front_card_width + "/h:" + front_card_height);
        Log.i(TAG, "xscale:" + x_scale + "/yscale:" + y_scale);
        for (GNode g : svgNode.getG()) {
            if (g.getName().equalsIgnoreCase(CARD_FRONT_ACTIVATED) && !card_status.equalsIgnoreCase(CARD_DEACTIVATED)) {
                if (g.getImage() != null) {
                    ImageNode image = g.getImage();
                    try {
                        Drawable backgroundDrawable = Drawable.createFromStream(assetManager.open(image.getXlink()), null);
                        front_card_container.setBackgroundDrawable(backgroundDrawable);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                for (TextNode text : g.getText()) {
                    TextView tv = new TextView(this);
                    int mx = (int) (Double.parseDouble(text.getX()) / x_scale - (Integer.parseInt(text.getFontSize()) / x_scale));
                    int my = (int) (Double.parseDouble(text.getY()) / y_scale - (Integer.parseInt(text.getFontSize()) / y_scale));
                    Log.i(TAG, "mx:" + mx + "/my:" + my);
                    RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    tvParams.setMargins(Utils.convertDpToPx(this, mx), Utils.convertDpToPx(this, my), 0, 0);
                    tv.setTextSize((float) (Integer.parseInt(text.getFontSize()) / x_scale));
                    tv.setTextColor(Color.parseColor(text.getFill()));
                    if (text.getFontFamily() != null)
                        tv.setTypeface(initializeTypefaces(text.getFontFamily()));
                    tv.setText(text.getCharacterData());
                    for (String tref : text.getTref()) {
                        try {
                            tv.append(card_data.getString(tref.split("#")[1].toLowerCase(Locale.ITALIAN)) + " ");
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            Log.i(TAG, card_data.names().toString());
                        }
                    }
                    front_card_container.addView(tv, tvParams);
                }
            } else if (g.getName().equalsIgnoreCase(CARD_FRONT_DEACTIVATED) && card_status.equalsIgnoreCase(CARD_DEACTIVATED)) {
                if (g.getImage() != null) {
                    ImageNode image = g.getImage();
                    try {
                        Drawable backgroundDrawable = Drawable.createFromStream(assetManager.open(image.getXlink()), null);
                        front_card_container.setBackgroundDrawable(backgroundDrawable);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            } else if (g.getName().equalsIgnoreCase(CARD_BACK)) {
                if (g.getImage() != null) {
                    ImageNode image = g.getImage();
                    try {
                        Drawable backgroundDrawable = Drawable.createFromStream(assetManager.open(image.getXlink()), null);
                        back_card_container.setBackgroundDrawable(backgroundDrawable);
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }
                for (TextNode text : g.getText()) {
                    TextView tv = new TextView(this);
                    int mx = (int) (Double.parseDouble(text.getX()) / x_scale - (Integer.parseInt(text.getFontSize()) / x_scale));
                    int my = (int) (Double.parseDouble(text.getY()) / y_scale - (Integer.parseInt(text.getFontSize()) / y_scale));
                    Log.i(TAG, "mx:" + mx + "/my:" + my);
                    RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    tvParams.setMargins(Utils.convertDpToPx(this, mx), Utils.convertDpToPx(this, my), 0, 0);
                    tv.setTextSize((float) (Integer.parseInt(text.getFontSize()) / x_scale));
                    tv.setTextColor(Color.parseColor(text.getFill()));
                    if (text.getFontFamily() != null)
                        tv.setTypeface(initializeTypefaces(text.getFontFamily()));
                    tv.setText(text.getCharacterData());
                    for (String tref : text.getTref()) {
                        try {
                            tv.append(card_data.getString(tref.split("#")[1].toLowerCase(Locale.ITALIAN)) + " ");
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            Log.i(TAG, card_data.names().toString());
                        }
                    }
                    back_card_container.addView(tv, tvParams);
                }
            }
        }

    }

    private void updateOtherInfo() {
        for (GNode g : svgNode.getG()) {
            if (g.getName().equalsIgnoreCase(CARD_OTHER) && !card_status.equalsIgnoreCase(CARD_DEACTIVATED)) {
                for (TextNode text : g.getText()) {
                    TextView tv = new TextView(this);
                    tv.setTextSize(Integer.parseInt(text.getFontSize()));
                    tv.setMovementMethod(new ScrollingMovementMethod());
                    tv.setTextColor(Color.parseColor(text.getFill()));
                    tv.setText(text.getCharacterData());
                    other_info_container.addView(tv, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
            } else if (g.getName().equalsIgnoreCase(CARD_ACTIONS) && !card_status.equalsIgnoreCase(CARD_DEACTIVATED)) {

                for (TextNode text : g.getText()) {
                    for (String tref : text.getTref()) {

                        try {
                            if (tref.contains("token"))
                                tokenToSend = card_data.getString(tref.split("#")[1].toLowerCase(Locale.ITALIAN));
                            if (tref.contains("nfc"))
                                tokenToSend = card_data.getString(tref.split("#")[1].toLowerCase(Locale.ITALIAN));
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                            Log.i(TAG, card_data.names().toString());
                        }
                    }
                }
                supportInvalidateOptionsMenu();
            } else if (g.getName().equalsIgnoreCase(CARD_BACK)) {
            }
        }
    }

    private JSONObject getDataFromJson(String url) {
        String jsonString = null;
        JSONObject jsonObject = null;
        InputStream is;
        try {
            is = assetManager.open(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "n");
            }
            is.close();
            jsonString = sb.toString();
            Log.i(TAG, jsonString);
        } catch (Exception e) {
            Log.i(TAG, "Error converting result " + e.toString());
            return jsonObject;
        }

        try {
            jsonObject = new JSONObject(jsonString);
            jsonObject = jsonObject.getJSONObject("customerProfile");
        } catch (JSONException e) {
            Log.i(TAG, "Error parsing data " + e.toString());
        }
        return jsonObject;
    }

    @SuppressLint("CommitTransaction")
    private void showQRCodeFragment(String text) {
        DialogFragment newFragment = QRCodeFragment.newInstance(text);
        newFragment.show(getSupportFragmentManager(), "qrcode");
    }

    private Typeface initializeTypefaces(String uri) {
        return Typeface.createFromAsset(getAssets(), uri);
    }

}
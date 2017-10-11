package mad9132.rgb_a;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import model.RGBAModel;

/**
 * The Controller for RGBAModel.
 *
 * As the Controller:
 *   a) event handler for the View
 *   b) observer of the Model (RGBAModel)
 *
 * Features the Update / React Strategy.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @version 1.0
 */
public class MainActivity extends Activity implements Observer, SeekBar.OnSeekBarChangeListener {
    // CLASS VARIABLES
    private static final String ABOUT_DIALOG_TAG = "About";
    private static final String LOG_TAG          = "RGBA";
    private static final String APPNAME          = "RGBA";
    private static int appState = 0;

    // INSTANCE VARIABLES
    // Pro-tip: different naming style; the 'm' means 'member'
    private AboutDialogFragment mAboutDialog;
    private TextView            mColorSwatch;
    private RGBAModel           mModel;
    private SeekBar             mRedSB;

    //TODO: declare private members for mGreenSB, mBlueSB, and mAlphaSB (DONE)
    private SeekBar             mGreenSB;
    private SeekBar             mAlphaSB;
    private SeekBar             mBlueSB;

    private TextView            mRedTV;
    private TextView            mGreenTV;
    private TextView            mBlueTV;
    private TextView            mAlphaTV;
    private SharedPreferences userDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a new AboutDialogFragment()
        // but do not show it (yet)
        mAboutDialog = new AboutDialogFragment();

        // Instantiate a new RGBA model
        // Initialize the model red (max), green (min), blue (min), and alpha (max)
        mModel = new RGBAModel();

        userDetails = getApplicationContext().getSharedPreferences(APPNAME, MODE_PRIVATE);
        if(appState != 0) {
            int rValue = userDetails.getInt("RED", RGBAModel.MAX_RGB);
            mModel.setRed(rValue);
            int gValue = userDetails.getInt("GREEN", RGBAModel.MIN_RGB);
            mModel.setGreen(gValue);
            int bValue = userDetails.getInt("BLUE", RGBAModel.MIN_RGB);
            mModel.setBlue(bValue);
            int aValue = userDetails.getInt("ALPAH", RGBAModel.MAX_ALPHA);
            mModel.setAlpha(aValue);
        } else {
            SharedPreferences.Editor edit = userDetails.edit();
            edit.clear();
            appState = 1;
            mModel.setRed( RGBAModel.MAX_RGB );
            mModel.setGreen( RGBAModel.MIN_RGB );
            mModel.setBlue( RGBAModel.MIN_RGB );
            mModel.setAlpha( RGBAModel.MAX_ALPHA );
        }

        // The Model is observing this Controller (class MainActivity implements Observer)
        mModel.addObserver( this );

        // reference each View
        mColorSwatch = (TextView) findViewById( R.id.colorSwatch );
        mRedSB = (SeekBar) findViewById( R.id.redSB );

        //TODO: reference the remaining <SeekBar>s: green, blue and alpha(DONE)
        mGreenSB = (SeekBar) findViewById( R.id.greenSB );
        mBlueSB = (SeekBar) findViewById( R.id.blueSB );
        mAlphaSB = (SeekBar) findViewById( R.id.alphaSB );

        mRedTV = (TextView) findViewById( R.id.red );
        mGreenTV = (TextView) findViewById( R.id.green );
        mBlueTV = (TextView) findViewById( R.id.blue );
        mAlphaTV = (TextView) findViewById( R.id.alpha );

        // set the domain (i.e. max) for each component
        mRedSB.setMax( RGBAModel.MAX_RGB );
        //TODO: setMax() for the remaining <SeekBar>s: green, blue and alpha(DONE)
        mGreenSB.setMax( RGBAModel.MAX_RGB );
        mBlueSB.setMax( RGBAModel.MAX_RGB );
        mAlphaSB.setMax( RGBAModel.MAX_ALPHA );
        // register the event handler for each <SeekBar>
        mRedSB.setOnSeekBarChangeListener( this );
        //TODO: register the remaining <SeekBar>s: green, blue and alpha(DONE)
        mGreenSB.setOnSeekBarChangeListener( this );
        mBlueSB.setOnSeekBarChangeListener( this );
        mAlphaSB.setOnSeekBarChangeListener( this );


        // initialize the View to the values of the Model
        this.updateView();
        //mModel.setAlpha( mAlphaSB.getProgress() );


        Log.d("Test","OnCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch ( item.getItemId() ) {

            case R.id.action_about:
                mAboutDialog.show( getFragmentManager(), ABOUT_DIALOG_TAG );
                return true;

            case R.id.action_red:
                mModel.asRed();
                return true;

            //TODO: handle the remaining menu items(DONE)
            case R.id.action_green:
                mModel.asGreen();
                return true;

            case R.id.action_blue:
                mModel.asBlue();
                return true;
            case R.id.action_black:
                mModel.asBlack();
                return true;
            case R.id.action_cyan:
                mModel.asCyan();
                return true;
            case R.id.action_magenta:
                mModel.asMagenta();
                return true;
            case R.id.action_white:
                mModel.asWhite();
                return true;
            case R.id.action_yellow:
                mModel.asYellow();
                return true;

            default:
                Toast.makeText(this, "MenuItem: " + item.getTitle(), Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Event handler for the <SeekBar>s: red, green, blue, and alpha.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        // Did the user cause this event?
        // YES > continue
        // NO  > leave this method
        if ( fromUser == false ) {
            return;
        }

        String keyString = "";
        Integer rgbValue = 0;
        // Determine which <SeekBark> caused the event (switch + case)
        // GET the SeekBar's progress, and SET the model to it's new value
        switch ( seekBar.getId() ) {
            case R.id.redSB:
                mModel.setRed( mRedSB.getProgress() );
                mRedTV.setText( getResources().getString(R.string.redProgress, progress).toUpperCase() );
                keyString = "RED";
                rgbValue = mModel.getRed();
                break;

            //TODO: case R.id.greenSB(DONE)
            case R.id.greenSB:
                mModel.setGreen( mGreenSB.getProgress() );
                mGreenTV.setText( getResources().getString(R.string.greeProgress, progress).toUpperCase() );
                keyString = "GREEN";
                rgbValue = mModel.getGreen();
                break;
            //TODO: case R.id.blueSB(DONE)
            case R.id.blueSB:
                mModel.setBlue( mBlueSB.getProgress() );
                mBlueTV.setText( getResources().getString(R.string.blueProgress, progress).toUpperCase() );
                keyString = "BLUE";
                rgbValue = mModel.getBlue();
                break;
            //TODO: case R.id.alphaSB(DONE)
            case R.id.alphaSB:
                mModel.setAlpha( mAlphaSB.getProgress() );
                mAlphaTV.setText( getResources().getString(R.string.alphaProgress, progress).toUpperCase() );
                keyString = "ALPHA";
                rgbValue = mModel.getAlpha();
                break;

        }

        userDetails = getApplicationContext().getSharedPreferences(APPNAME, MODE_PRIVATE);
        SharedPreferences.Editor edit = userDetails.edit();
        edit.putInt(keyString, rgbValue);
        edit.commit();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // No-Operation
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.redSB:
                mRedTV.setText( getResources().getString(R.string.red) );
                break;
            case R.id.greenSB:
                mGreenTV.setText( R.string.green );
                break;
            case R.id.blueSB:
                mBlueTV.setText(R.string.blue );
                break;
            case R.id.alphaSB:
                mAlphaTV.setText(R.string.alpha );
                break;
        }
    }

    // The Model has changed state!
    // Refresh the View to display the current values of the Model.
    @Override
    public void update(Observable observable, Object data) {
        Log.i( LOG_TAG, "The color (int) is: " + mModel.getColor() + "" );

        this.updateView();
    }

    private void updateBlueSB() {
        //TODO: set the blueSB's progress to the model's blue value(DONE)
        mBlueSB.setProgress(mModel.getBlue());
    }

    private void updateColorSwatch() {
        //GET the model's r,g,b,a values, and SET the background colour of the swatch <TextView>
        mColorSwatch.setBackgroundColor(Color.argb(mModel.getAlpha()
                , mModel.getRed()
                , mModel.getGreen()
                , mModel.getBlue()));
    }

    private void updateGreenSB() {
        //TODO: set the greenSB's progress to the model's green value(DONE)
        mGreenSB.setProgress(mModel.getGreen());
    }

    private void updateRedSB() {
        //GET the model's red value, and SET the red <SeekBar>
        mRedSB.setProgress( mModel.getRed() );
    }
    private void updateAlphaSB() {
        mAlphaSB.setProgress(mModel.getAlpha());
    }

    // synchronize each View component with the Model
    public void updateView() {
        this.updateColorSwatch();
        this.updateRedSB();
        this.updateGreenSB();
        this.updateBlueSB();
        this.updateAlphaSB();
    }
}   // end of class

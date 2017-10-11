package model;

import java.util.Observable;

import android.graphics.Color;

/**
 * The model holds the data.
 *
 * Model color.
 * Based on red, green, blue and alpha (transparency).
 *
 * RGB: integer values in the domain range of 0 to 255 (inclusive).
 * Alpha: integer value in the domain range of 0 to 255 (inclusive).
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @version 1.0
 */
public class RGBAModel extends Observable {

    // CLASS VARIABLES
    public static final Integer MAX_ALPHA = 255;
    public static final Integer MAX_RGB   = 255;
    public static final Integer MIN_ALPHA = 0;
    public static final Integer MIN_RGB   = 0;

    // INSTANCE VARIABLES
    private Integer alpha;
    private Integer blue;
    private Integer green;
    private Integer red;

    /**
     * No argument constructor.
     *
     * Instantiate a new instance of this class, and
     * initialize red, green, blue, and alpha to max values.
     */
    public RGBAModel() {
        this( MAX_RGB, MAX_RGB, MAX_RGB, MAX_ALPHA );
    }

    /**
     * Convenience constructor.
     *
     * @param red - starting red value
     * @param green - starting green value
     * @param blue - starting blue value
     * @param alpha - starting alpha value
     */
    public RGBAModel( Integer red, Integer green, Integer blue, Integer alpha ) {
        super();

        this.alpha = alpha;
        this.blue  = blue;
        this.green = green;
        this.red   = red;
    }

    public void asBlack()   { this.setRGB( MIN_RGB, MIN_RGB, MIN_RGB ); }
    public void asBlue()    { this.setRGB( MIN_RGB, MIN_RGB, MAX_RGB ); }
    public void asCyan()    { this.setRGB( MIN_RGB, MAX_RGB, MAX_RGB ); }
    public void asGreen()   { this.setRGB( MIN_RGB, MAX_RGB, MIN_RGB ); }
    public void asMagenta() { this.setRGB( MAX_RGB, MIN_RGB, MAX_RGB ); }
    public void asRed()     { this.setRGB( MAX_RGB, MIN_RGB, MIN_RGB ); }
    public void asWhite()   { this.setRGB( MAX_RGB, MAX_RGB, MAX_RGB ); }
    public void asYellow()  { this.setRGB( MAX_RGB, MAX_RGB, MIN_RGB ); }

    // GETTERS
    public Integer getAlpha() { return alpha; }
    public Integer getBlue()  { return blue; }
    public int     getColor() { return Color.argb( alpha, red, green, blue ); }
    public Integer getGreen() { return green; }
    public Integer getRed()   { return red; }

    // SETTERS
    public void setAlpha( Integer alpha ) {
        this.alpha = alpha;

        this.updateObservers();
    }

    public void setBlue(Integer blue) {
        this.blue = blue;

        this.updateObservers();
    }

    public void setGreen(Integer green) {
        this.green = green;

        this.updateObservers();
    }

    public void setRed(Integer red) {
        this.red = red;

        this.updateObservers();
    }

    // Convenient setter: set R, G, B
    public void setRGB( Integer red, Integer green, Integer blue ) {
        this.red   = red;
        this.green = green;
        this.blue  = blue;

        this.updateObservers();
    }

    // the model has changed!
    // broadcast the update method to all registered observers
    private void updateObservers() {
        this.setChanged();
        this.notifyObservers();
    }

    @Override
    public String toString() {
        return "RGB-A [r=" + red + " g=" + green + " b=" + blue + " alpha=" + alpha + "]";
    }

    // Proof that my model is independent of any view.
    public static void main( String[] args ) {
        RGBAModel model = new RGBAModel( 127, 127, 127, 255 );

        System.out.println( model );
    }
}
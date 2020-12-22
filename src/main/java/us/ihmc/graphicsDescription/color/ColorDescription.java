package us.ihmc.graphicsDescription.color;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import us.ihmc.commons.MathTools;
import us.ihmc.euclid.tools.EuclidCoreTools;

/**
 * This provides tools to create and manipulate colors.
 * <p>
 * The color is stored as (red, green, blue) and opacity alpha.
 * </p>
 */
public class ColorDescription
{
   static final Map<String, ColorDescription> namedColorLowerCaseMap;

   static
   {
      Map<String, ColorDescription> map = Stream.of(ColorDescription.class.getDeclaredMethods()).filter(ColorDescription::isNamedColorMethod)
                                                .collect(Collectors.toMap(m -> m.getName().toLowerCase(), ColorDescription::invokeMethod));
      namedColorLowerCaseMap = Collections.unmodifiableMap(map);
   }

   private double red, green, blue, alpha;

   /**
    * Creates a new color initialized as opaque black.
    */
   public ColorDescription()
   {
      this(0.0, 0.0, 0.0, 1.0);
   }

   /**
    * Copy constructor.
    * 
    * @param other the other color to copy. Not modified.
    */
   public ColorDescription(ColorDescription other)
   {
      this(other.red, other.green, other.blue, other.alpha);
   }

   /**
    * Creates a new color initialized from RGB components and opaque.
    * 
    * @param red   red component in range [0-255].
    * @param green green component in range [0-255].
    * @param blue  blue component in range [0-255].
    */
   public ColorDescription(int red, int green, int blue)
   {
      this(red, green, blue, 1.0);
   }

   /**
    * Creates a new color initialized from RGB components.
    * 
    * @param red   red component in range [0-255].
    * @param green green component in range [0-255].
    * @param blue  blue component in range [0-255].
    * @param alpha alpha component in range [0.0-1.0], 0.0 being fully transparent and 1.0 fully
    *              opaque.
    */
   public ColorDescription(int red, int green, int blue, double alpha)
   {
      setRed(red);
      setGreen(green);
      setBlue(blue);
      setAlpha(alpha);
   }

   /**
    * Creates a new color initialized from RGB components.
    * 
    * @param red   red component in range [0.0-255].
    * @param green green component in range [0-255].
    * @param blue  blue component in range [0-255].
    * @param alpha alpha component in range [0-255], 0 being fully transparent and 255 fully opaque.
    */
   public ColorDescription(int red, int green, int blue, int alpha)
   {
      setRed(red);
      setGreen(green);
      setBlue(blue);
      setAlpha(alpha);
   }

   /**
    * Creates a new color initialized from RGB components and opaque.
    * 
    * @param red   red component in range [0.0-1.0].
    * @param green green component in range [0.0-1.0].
    * @param blue  blue component in range [0.0-1.0].
    */
   public ColorDescription(double red, double green, double blue)
   {
      this(red, green, blue, 1.0);
   }

   /**
    * Creates a new color initialized from RGB components.
    * 
    * @param red   red component in range [0.0-1.0].
    * @param green green component in range [0.0-1.0].
    * @param blue  blue component in range [0.0-1.0].
    * @param alpha alpha component in range [0.0-1.0], 0.0 being fully transparent and 1.0 fully
    *              opaque.
    */
   public ColorDescription(double red, double green, double blue, double alpha)
   {
      setRed(red);
      setGreen(green);
      setBlue(blue);
      setAlpha(alpha);
   }

   /**
    * Sets the red component for this color.
    * 
    * @param red red component in range [0.0-255].
    */
   public void setRed(int red)
   {
      setRed(red / 255.0);
   }

   /**
    * Sets the red component for this color.
    * 
    * @param red red component in range [0.0-1.0].
    */
   public void setRed(double red)
   {
      this.red = MathTools.clamp(red, 0.0, 1.0);
   }

   /**
    * Sets the green component for this color.
    * 
    * @param green green component in range [0.0-255].
    */
   public void setGreen(int green)
   {
      setGreen(green / 255.0);
   }

   /**
    * Sets the green component for this color.
    * 
    * @param green green component in range [0.0-1.0].
    */
   public void setGreen(double green)
   {
      this.green = MathTools.clamp(green, 0.0, 1.0);
   }

   /**
    * Sets the blue component for this color.
    * 
    * @param blue blue component in range [0.0-255].
    */
   public void setBlue(int blue)
   {
      setBlue(blue / 255.0);
   }

   /**
    * Sets the blue component for this color.
    * 
    * @param blue blue component in range [0.0-1.0].
    */
   public void setBlue(double blue)
   {
      this.blue = MathTools.clamp(blue, 0.0, 1.0);
   }

   /**
    * Sets the alpha component for this color.
    * 
    * @param alpha alpha component in range [0-255], 0 being fully transparent and 255 fully opaque.
    */
   public void setAlpha(int alpha)
   {
      setAlpha(alpha / 255.0);
   }

   /**
    * Sets the alpha component for this color.
    * 
    * @param alpha alpha component in range [0.0-1.0], 0.0 being fully transparent and 1.0 fully
    *              opaque.
    */
   public void setAlpha(double alpha)
   {
      this.alpha = MathTools.clamp(alpha, 0.0, 1.0);
   }

   /**
    * Returns the value for the red component.
    * 
    * @return the value for the red component in range [0.0-1.0].
    */
   public double getRed()
   {
      return red;
   }

   /**
    * Returns the value for the red component.
    * 
    * @return the value for the red component in range [0-255].
    */
   public int getRedAsInteger()
   {
      return (int) (red * 255.0);
   }

   /**
    * Returns the value for the green component.
    * 
    * @return the value for the green component in range [0.0-1.0].
    */
   public double getGreen()
   {
      return green;
   }

   /**
    * Returns the value for the green component.
    * 
    * @return the value for the green component in range [0-255].
    */
   public int getGreenAsInteger()
   {
      return (int) (green * 255.0);
   }

   /**
    * Returns the value for the blue component.
    * 
    * @return the value for the blue component in range [0.0-1.0].
    */
   public double getBlue()
   {
      return blue;
   }

   /**
    * Returns the value for the blue component.
    * 
    * @return the value for the blue component in range [0-255].
    */
   public int getBlueAsInteger()
   {
      return (int) (blue * 255.0);
   }

   /**
    * Returns whether this color is completely opaque, i.e. {@code alpha = 1}, or transparent, i.e.
    * {@code alpha < 1}.
    * 
    * @return {@code true} if this color is opaque, {@code false} otherwise.
    */
   public boolean isOpaque()
   {
      return alpha == 1.0;
   }

   /**
    * Returns the value for the alpha component.
    * 
    * @return the value for the alpha component in range [0.0-1.0], 0.0 being fully transparent and 1.0
    *         fully opaque.
    */
   public double getAlpha()
   {
      return alpha;
   }

   /**
    * Returns the value for the alpha component.
    * 
    * @return the value for the alpha component in range [0-255], 0 being fully transparent and 255
    *         fully opaque.
    */
   public int getAlphaAsInteger()
   {
      return (int) (alpha * 255.0);
   }

   /**
    * Returns the hue component using the HSV/HSB representation for this color.
    * 
    * @return the hue component in range [0-360].
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public double getHue()
   {
      double brightness = getBrightness();
      double c = brightness - EuclidCoreTools.min(red, green, blue);

      double hue = 0.0;
      if (c == 0.0)
         hue = 0.0;
      else if (brightness == red)
         hue = 60.0 * (green - blue) / c;
      else if (brightness == green)
         hue = 60.0 * (2.0 + (blue - red) / c);
      else if (brightness == blue)
         hue = 60.0 * (4.0 + (red - green) / c);
      if (hue < 0.0)
         hue += 360.0;
      hue %= 360.0;
      return hue;
   }

   /**
    * Returns the saturation component using the HSV/HSB representation for this color.
    * 
    * @return the saturation component in range [0.0-1.0].
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public double getSaturation()
   {
      double brightness = getBrightness();
      if (brightness == 0.0)
         return 0.0;
      else
         return 1.0 - EuclidCoreTools.min(red, green, blue) / brightness;
   }

   /**
    * Returns the value/brightness component using the HSV/HSB representation for this color.
    * 
    * @return the value/brightness component in range [0.0-1.0].
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public double getBrightness()
   {
      return EuclidCoreTools.max(red, green, blue);
   }

   /**
    * Returns the RGB, "#00RRGGBB", value representing this color.
    * <p>
    * The components are stored as follows:
    * <ul>
    * <li>Bits [16-23] are used to store red,
    * <li>Bits [8-15] are used to store green,
    * <li>Bits [0-7] are used to store blue.
    * </ul>
    * </p>
    * 
    * @return the RGB value representing this color.
    */
   public int toRGB()
   {
      int argb = (getRedAsInteger() & 0xFF) << 16;
      argb |= (getGreenAsInteger() & 0xFF) << 8;
      argb |= (getBlueAsInteger() & 0xFF) << 0;
      return argb;
   }

   /**
    * Returns the ARGB, "#AARRGGBB", value representing this color.
    * <p>
    * The components are stored as follows:
    * <ul>
    * <li>Bits [24-31] are used to store alpha,
    * <li>Bits [16-23] are used to store red,
    * <li>Bits [8-15] are used to store green,
    * <li>Bits [0-7] are used to store blue.
    * </ul>
    * </p>
    * 
    * @return the ARGB value representing this color.
    */
   public int toARGB()
   {
      int argb = (getAlphaAsInteger() & 0xFF) << 24;
      argb |= (getRedAsInteger() & 0xFF) << 16;
      argb |= (getGreenAsInteger() & 0xFF) << 8;
      argb |= (getBlueAsInteger() & 0xFF) << 0;
      return argb;
   }

   /**
    * Returns the RGBA, "#RRGGBBAA", value representing this color.
    * <p>
    * The components are stored as follows:
    * <ul>
    * <li>Bits [24-31] are used to store red,
    * <li>Bits [16-23] are used to store green,
    * <li>Bits [8-15] are used to store blue,
    * <li>Bits [0-7] are used to store alpha.
    * </ul>
    * </p>
    * 
    * @return the RGBA value representing this color.
    */
   public int toRGBA()
   {
      int argb = (getRedAsInteger() & 0xFF) << 24;
      argb |= (getGreenAsInteger() & 0xFF) << 16;
      argb |= (getBlueAsInteger() & 0xFF) << 8;
      argb |= (getAlphaAsInteger() & 0xFF) << 0;
      return argb;
   }

   /**
    * Returns the three components red, green, and blue in order as an array.
    * <p>
    * Each component is expressed in the range [0.0-1.0].
    * </p>
    * 
    * @return the three components red, green, and blue in order as an array.
    */
   public double[] toRGBDoubleArray()
   {
      return new double[] {getRed(), getGreen(), getBlue()};
   }

   /**
    * Returns the three components red, green, and blue in order as an array.
    * <p>
    * Each component is expressed in the range [0-255].
    * </p>
    * 
    * @return the three components red, green, and blue in order as an array.
    */
   public int[] toRGBIntArray()
   {
      return new int[] {getRedAsInteger(), getGreenAsInteger(), getBlueAsInteger()};
   }

   /**
    * Returns the four components red, green, blue, and alpha in order as an array.
    * <p>
    * Each component is expressed in the range [0.0-1.0].
    * </p>
    * 
    * @return the three components red, green, blue, and alpha in order as an array.
    */
   public double[] toRGBADoubleArray()
   {
      return new double[] {getRed(), getGreen(), getBlue(), getAlpha()};
   }

   /**
    * Returns the four components red, green, blue, and alpha in order as an array.
    * <p>
    * Each component is expressed in the range [0-255].
    * </p>
    * 
    * @return the three components red, green, blue, and alpha in order as an array.
    */
   public int[] toRGBAIntArray()
   {
      return new int[] {getRedAsInteger(), getGreenAsInteger(), getBlueAsInteger(), getAlphaAsInteger()};
   }

   /**
    * Returns the three components, using the HSB/HSV representation of this color, hue, saturation,
    * and brightness in order as an array.
    * <p>
    * The hue is expressed in the range [0-360] while the saturation and brightness are expressed in
    * the range [0.0-1.0].
    * </p>
    * 
    * @return the three components hue, saturation, and brightness in order as an array.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public double[] toHSBDoubleArray()
   {
      return new double[] {getHue(), getSaturation(), getBrightness()};
   }

   /**
    * Returns the four components, using the HSB/HSV representation of this color, hue, saturation, and
    * brightness, alpha in order as an array.
    * <p>
    * The hue is expressed in the range [0-360] while the saturation, brightness, and alpha are
    * expressed in the range [0.0-1.0].
    * </p>
    * 
    * @return the four components hue, saturation, brightness, and alpha in order as an array.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public double[] toHSBADoubleArray()
   {
      return new double[] {getHue(), getSaturation(), getBrightness(), getAlpha()};
   }

   /**
    * Returns the three components, using the HSL representation of this color, hue, saturation, and
    * lightness in order as an array.
    * <p>
    * The hue is expressed in the range [0-360] while the saturation and lightness are expressed in the
    * range [0.0-1.0].
    * </p>
    * 
    * @return the three components hue, saturation, lightness in order as an array.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSL_color_solid_cylinder_saturation_gray.png">HSL
    *      representation</a>
    */
   public double[] toHSLDoubleArray()
   {
      return Arrays.copyOf(toHSLADoubleArray(), 3);
   }

   /**
    * Returns the four components, using the HSL representation of this color, hue, saturation,
    * lightness, and alpha in order as an array.
    * <p>
    * The hue is expressed in the range [0-360] while the saturation, lightness, and alpha are
    * expressed in the range [0.0-1.0].
    * </p>
    * 
    * @return the four components hue, saturation, lightness, and alpha in order as an array.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSL_color_solid_cylinder_saturation_gray.png">HSL
    *      representation</a>
    */
   public double[] toHSLADoubleArray()
   {
      double xmax = EuclidCoreTools.max(red, green, blue);
      double xmin = EuclidCoreTools.min(red, green, blue);

      double c = xmax - xmin;
      double lightness = 0.5 * (xmax + xmin);

      double hue = 0.0;
      if (c == 0.0)
         hue = 0.0;
      else if (xmax == red)
         hue = 60.0 * (green - blue) / c;
      else if (xmax == green)
         hue = 60.0 * (2.0 + (blue - red) / c);
      else if (xmax == blue)
         hue = 60.0 * (4.0 + (red - green) / c);
      if (hue < 0.0)
         hue += 360.0;
      hue %= 360.0;

      double saturation = 0.0;
      if (lightness > 0.0 && lightness < 1.0)
         saturation = (xmax - lightness) / Math.min(lightness, 1.0 - lightness);

      return new double[] {hue, saturation, lightness, alpha};
   }

   @Override
   public boolean equals(Object object)
   {
      if (object == this)
      {
         return true;
      }
      else if (object instanceof ColorDescription)
      {
         ColorDescription other = (ColorDescription) object;
         return red == other.red && green == other.green && blue == other.blue && alpha == other.alpha;
      }
      else
      {
         return false;
      }
   }

   @Override
   public int hashCode()
   {
      return toARGB();
   }

   @Override
   public String toString()
   {
      if (alpha == 1.0)
         return String.format("RGB(%d, %d, %d)", getRedAsInteger(), getGreenAsInteger(), getBlueAsInteger());
      else
         return String.format("RGBA(%d, %d, %d, %d)", getRedAsInteger(), getGreenAsInteger(), getBlueAsInteger(), getAlphaAsInteger());
   }

   /**
    * Creates a new opaque color from the combined RGB value.
    * <p>
    * The components are assumed to be stored as follows:
    * <ul>
    * <li>Bits [16-23] are used for red,
    * <li>Bits [8-15] are used for green,
    * <li>Bits [0-7] are used for blue.
    * </ul>
    * </p>
    * 
    * @param rgb the combined RGB value.
    * @return the new color.
    */
   public static ColorDescription rgb(int rgb)
   {
      return argb(0xff000000 | rgb);
   }

   /**
    * Creates a new color from the combined ARGB value.
    * <p>
    * The components are assumed to be stored as follows:
    * <ul>
    * <li>Bits [24-31] are used for alpha,
    * <li>Bits [16-23] are used for red,
    * <li>Bits [8-15] are used for green,
    * <li>Bits [0-7] are used for blue.
    * </ul>
    * </p>
    * 
    * @param argb the combined ARGB value.
    * @return the new color.
    */
   public static ColorDescription argb(int argb)
   {
      ColorDescription colorDescription = new ColorDescription();
      colorDescription.setAlpha((argb >> 24 & 0xFF) / 255.0);
      colorDescription.setRed((argb >> 16 & 0xFF) / 255.0);
      colorDescription.setGreen((argb >> 8 & 0xFF) / 255.0);
      colorDescription.setBlue((argb >> 0 & 0xFF) / 255.0);
      return colorDescription;
   }

   /**
    * Creates a new color from the combined RGBA value.
    * <p>
    * The components are assumed to be stored as follows:
    * <ul>
    * <li>Bits [24-31] are used for red,
    * <li>Bits [16-23] are used for green,
    * <li>Bits [8-15] are used for blue,
    * <li>Bits [0-7] are used for alpha.
    * </ul>
    * </p>
    * 
    * @param rgba the combined RGBA value.
    * @return the new color.
    */
   public static ColorDescription rgba(int rgba)
   {
      ColorDescription colorDescription = new ColorDescription();
      colorDescription.setRed((rgba >> 24 & 0xFF) / 255.0);
      colorDescription.setGreen((rgba >> 16 & 0xFF) / 255.0);
      colorDescription.setBlue((rgba >> 8 & 0xFF) / 255.0);
      colorDescription.setAlpha((rgba >> 0 & 0xFF) / 255.0);
      return colorDescription;
   }

   /**
    * Creates a new opaque color from the given RGB values.
    * <p>
    * The components are assumed to be ordered as red, green, and blue and expressed in the range
    * [0-255].
    * </p>
    * 
    * @param rgb the array containing the RGB components.
    * @return the new color.
    */
   public static ColorDescription rgb(int[] rgb)
   {
      ColorDescription colorDescription = new ColorDescription();
      colorDescription.setRed(rgb[0]);
      colorDescription.setGreen(rgb[1]);
      colorDescription.setBlue(rgb[2]);
      colorDescription.setAlpha(1.0);
      return colorDescription;
   }

   /**
    * Creates a new opaque color from the given RGB values.
    * <p>
    * The components are assumed to be ordered as red, green, and blue and expressed in the range
    * [0.0-1.0].
    * </p>
    * 
    * @param rgb the array containing the RGB components.
    * @return the new color.
    */
   public static ColorDescription rgb(double[] rgb)
   {
      ColorDescription colorDescription = new ColorDescription();
      colorDescription.setRed(rgb[0]);
      colorDescription.setGreen(rgb[1]);
      colorDescription.setBlue(rgb[2]);
      colorDescription.setAlpha(1.0);
      return colorDescription;
   }

   /**
    * Creates a new opaque color from the given RGBA values.
    * <p>
    * The components are assumed to be ordered as red, green, blue, and alpha and expressed in the
    * range [0-255].
    * </p>
    * 
    * @param rgba the array containing the RGBA components.
    * @return the new color.
    */
   public static ColorDescription rgba(int[] rgba)
   {
      ColorDescription colorDescription = new ColorDescription();
      colorDescription.setRed(rgba[0]);
      colorDescription.setGreen(rgba[1]);
      colorDescription.setBlue(rgba[2]);
      colorDescription.setAlpha(rgba[3]);
      return colorDescription;
   }

   /**
    * Creates a new opaque color from the given RGBA values.
    * <p>
    * The components are assumed to be ordered as red, green, blue, and alpha and expressed in the
    * range [0.0-1.0].
    * </p>
    * 
    * @param rgba the array containing the RGBA components.
    * @return the new color.
    */
   public static ColorDescription rgba(double[] rgba)
   {
      ColorDescription colorDescription = new ColorDescription();
      colorDescription.setRed(rgba[0]);
      colorDescription.setGreen(rgba[1]);
      colorDescription.setBlue(rgba[2]);
      colorDescription.setAlpha(rgba[3]);
      return colorDescription;
   }

   /**
    * Creates a new opaque color from the given HSB/HSV values.
    * <p>
    * The components are assumed to be ordered as hue [0-360], saturation [0.0-1.0], and
    * brightness/value [0.0-1.0].
    * </p>
    * 
    * @param hsb the array containing the HSB/HSV components.
    * @return the new color.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public static ColorDescription hsb(double[] hsb)
   {
      return hsb(hsb[0], hsb[1], hsb[2]);
   }

   /**
    * Creates a new opaque color from the given HSB/HSV values.
    * 
    * @param hue        the hue component in range [0-360].
    * @param saturation the saturation component in range [0.0-1.0].
    * @param brightness the brightness/value component in range [0.0-1.0].
    * @return the new color.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public static ColorDescription hsb(double hue, double saturation, double brightness)
   {
      return hsba(hue, saturation, brightness, 1.0);
   }

   /**
    * Creates a new color from the given HSBA/HSVA values.
    * <p>
    * The components are assumed to be ordered as hue [0-360], saturation [0.0-1.0], brightness/value
    * [0.0-1.0], and alpha [0.0-1.0].
    * </p>
    * 
    * @param hsba the array containing the HSBA/HSVA components.
    * @return the new color.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public static ColorDescription hsba(double[] hsba)
   {
      return hsba(hsba[0], hsba[1], hsba[2], hsba[3]);
   }

   /**
    * Creates a new opaque color from the given HSBA/HSVA values.
    * 
    * @param hue        the hue component in range [0-360].
    * @param saturation the saturation component in range [0.0-1.0].
    * @param brightness the brightness/value component in range [0.0-1.0].
    * @param alpha      the alpha component in range [0.0-1.0], 0 being fully transparent and 255 fully
    *                   opaque.
    * @return the new color.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public static ColorDescription hsba(double hue, double saturation, double brightness, double alpha)
   {
      hue %= 360.0;
      if (hue < 0.0)
         hue += 360.0;
      saturation = MathTools.clamp(saturation, 0.0, 1.0);
      brightness = MathTools.clamp(brightness, 0.0, 1.0);

      // https://en.wikipedia.org/wiki/HSL_and_HSV#HSV_to_RGB
      double c = brightness * saturation;
      double hh = hue / 60.0;
      double x = c * (1.0 - Math.abs(hh % 2 - 1.0));

      double m = brightness - c;

      if (hh <= 1.0)
         return new ColorDescription(c + m, x + m, 0 + m, alpha);
      else if (hh <= 2)
         return new ColorDescription(x + m, c + m, 0 + m, alpha);
      else if (hh <= 3)
         return new ColorDescription(0 + m, c + m, x + m, alpha);
      else if (hh <= 4)
         return new ColorDescription(0 + m, x + m, c + m, alpha);
      else if (hh <= 5)
         return new ColorDescription(x + m, 0 + m, c + m, alpha);
      else if (hh <= 6)
         return new ColorDescription(c + m, 0 + m, x + m, alpha);
      else
         return new ColorDescription(m, m, m, alpha);
   }

   /**
    * Creates a new opaque color from the given HSL values.
    * <p>
    * The components are assumed to be ordered as hue [0-360], saturation [0.0-1.0], and lightness
    * [0.0-1.0].
    * </p>
    * 
    * @param hsl the array containing the HSL components.
    * @return the new color.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSL_color_solid_cylinder_saturation_gray.png">HSL
    *      representation</a>
    */
   public static ColorDescription hsl(double[] hsl)
   {
      return hsl(hsl[0], hsl[1], hsl[2]);
   }

   /**
    * Creates a new opaque color from the given HSL values.
    * 
    * @param hue        the hue component in range [0-360].
    * @param saturation the saturation component in range [0.0-1.0].
    * @param lightness  the lightness component in range [0.0-1.0].
    * @return the new color.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSL_color_solid_cylinder_saturation_gray.png">HSL
    *      representation</a>
    */
   public static ColorDescription hsl(double hue, double saturation, double lightness)
   {
      return hsla(hue, saturation, lightness, 1.0);
   }

   /**
    * Creates a new color from the given HSLA values.
    * <p>
    * The components are assumed to be ordered as hue [0-360], saturation [0.0-1.0], lightness
    * [0.0-1.0], and alpha [0.0-1.0].
    * </p>
    * 
    * @param hsla the array containing the HSLA components.
    * @return the new color.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSL_color_solid_cylinder_saturation_gray.png">HSL
    *      representation</a>
    */
   public static ColorDescription hsla(double[] hsla)
   {
      return hsla(hsla[0], hsla[1], hsla[2], hsla[3]);
   }

   /**
    * Creates a new opaque color from the given HSLA values.
    * 
    * @param hue        the hue component in range [0-360].
    * @param saturation the saturation component in range [0.0-1.0].
    * @param lightness  the lightness component in range [0.0-1.0].
    * @param alpha      the alpha component in range [0.0-1.0], 0 being fully transparent and 255 fully
    *                   opaque.
    * @return the new color.
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSL_color_solid_cylinder_saturation_gray.png">HSL
    *      representation</a>
    */
   public static ColorDescription hsla(double hue, double saturation, double lightness, double alpha)
   {
      hue %= 360.0;
      if (hue < 0.0)
         hue += 360.0;
      saturation = MathTools.clamp(saturation, 0.0, 1.0);
      lightness = MathTools.clamp(lightness, 0.0, 1.0);

      // https://en.wikipedia.org/wiki/HSL_and_HSV#HSL_to_RGB
      double c = (1.0 - Math.abs(2.0 * lightness - 1.0)) * saturation;
      double hh = hue / 60.0;
      double x = c * (1.0 - Math.abs(hh % 2 - 1.0));

      double m = lightness - 0.5 * c;

      if (hh <= 1.0)
         return new ColorDescription(c + m, x + m, 0 + m, alpha);
      else if (hh <= 2)
         return new ColorDescription(x + m, c + m, 0 + m, alpha);
      else if (hh <= 3)
         return new ColorDescription(0 + m, c + m, x + m, alpha);
      else if (hh <= 4)
         return new ColorDescription(0 + m, x + m, c + m, alpha);
      else if (hh <= 5)
         return new ColorDescription(x + m, 0 + m, c + m, alpha);
      else if (hh <= 6)
         return new ColorDescription(c + m, 0 + m, x + m, alpha);
      else
         return new ColorDescription(m, m, m, alpha);
   }

   /**
    * Attempts to parse the given string to create a new color.
    * <p>
    * Accepted formats for parsing RGB colors:
    * <ul>
    * <li>rgb(255, 0, 0) => integer range 0 - 255
    * <li>rgb(100%, 0%, 0%) => float range 0.0% - 100.0%
    * <li>rgba(100%, 0%, 0%, 0.5) => 0.5 opacity, semi-transparent
    * <li>rgba(255, 0, 0, 0.5) => 0.5 opacity, semi-transparent
    * </ul>
    * </p>
    * <p>
    * Accepted formats for parsing HSV colors:
    * <ul>
    * <li>hsv(120, 50%, 50%) => hue in [0-360], saturation and value in [0.0%-100.0%]
    * <li>hsv(120, 0.5, 0.5) => hue in [0-360], saturation and value in [0.0-1.0]
    * <li>hsva(120, 100%, 50%, 0.5) => 0.5 opacity, semi-transparent
    * <li>hsva(120, 1.0, 0.5, 0.5) => 0.5 opacity, semi-transparent
    * </ul>
    * </p>
    * <p>
    * Accepted formats for parsing HSB colors:
    * <ul>
    * <li>hsb(120, 50%, 50%) => hue in [0-360], saturation and brightness in [0.0%-100.0%]
    * <li>hsb(120, 0.5, 0.5) => hue in [0-360], saturation and brightness in [0.0-1.0]
    * <li>hsba(120, 100%, 50%, 0.5) => 0.5 opacity, semi-transparent
    * <li>hsba(120, 1.0, 0.5, 0.5) => 0.5 opacity, semi-transparent
    * </ul>
    * </p>
    * <p>
    * Accepted formats for parsing HSL colors:
    * <ul>
    * <li>hsl(120, 50%, 50%) => hue in [0-360], saturation and lightness in [0.0%-100.0%]
    * <li>hsl(120, 0.5, 0.5) => hue in [0-360], saturation and lightness in [0.0-1.0]
    * <li>hsla(120, 100%, 50%, 0.5) => 0.5 opacity, semi-transparent
    * <li>hsla(120, 1.0, 0.5, 0.5) => 0.5 opacity, semi-transparent
    * </ul>
    * </p>
    * <p>
    * Accepted formats for Hex RGB colors:
    * <ul>
    * <li>#00F or 0x00F for opaque {@link #Blue()}.
    * <li>#F00F or 0xF00F for opaque {@link #Blue()}.
    * <li>#000F or 0x000F for transparent {@link #Blue()}.
    * <li>#00FF00 or 0xFF0000 for opaque {@link #Lime()}.
    * <li>#FF00FF00 or 0xFFFF0000 for opaque {@link #Red()}.
    * <li>#FF00FF00 or 0x0000FF00 for transparent {@link #Lime()}.
    * <li>
    * </ul>
    * </p>
    * <p>
    * Finally, the given string can also be the name of one of the 147 CSS named colors such as
    * "AliceBlue".
    * </p>
    * 
    * @param webColor the string representing the color to parse.
    * @return the new color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    * @see <a href= "https://en.wikipedia.org/wiki/Web_colors">Web colors</a>
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSL_color_solid_cylinder_saturation_gray.png">HSL
    *      representation</a>
    * @see <a href=
    *      "https://en.wikipedia.org/wiki/HSL_and_HSV#/media/File:HSV_color_solid_cylinder_saturation_gray.png">HSB/HSV
    *      representation</a>
    */
   public static ColorDescription parse(String webColor)
   {
      String color = webColor.trim().toLowerCase();

      ColorDescription namedColor = namedColorLowerCaseMap.get(color);

      if (namedColor != null)
      {
         return new ColorDescription(namedColor);
      }
      else if (color.startsWith("#") || color.startsWith("0x"))
      {
         String code;
         if (color.startsWith("#"))
            code = color.substring(1);
         else
            code = color.substring(2);

         try
         {
            if (code.length() == 3)
               return new ColorDescription(Integer.parseInt(code.substring(0, 1), 16) / 15.0,
                                           Integer.parseInt(code.substring(1, 2), 16) / 15.0,
                                           Integer.parseInt(code.substring(2, 3), 16) / 15.0);
            if (code.length() == 4)
               return new ColorDescription(Integer.parseInt(code.substring(0, 1), 16) / 15.0,
                                           Integer.parseInt(code.substring(1, 2), 16) / 15.0,
                                           Integer.parseInt(code.substring(2, 3), 16) / 15.0,
                                           Integer.parseInt(code.substring(2, 3), 16) / 15.0);
            if (code.length() == 6)
               return new ColorDescription(Integer.parseInt(code.substring(0, 2), 16),
                                           Integer.parseInt(code.substring(2, 4), 16),
                                           Integer.parseInt(code.substring(4, 6), 16));
            if (code.length() == 8)
               return new ColorDescription(Integer.parseInt(code.substring(0, 2), 16),
                                           Integer.parseInt(code.substring(2, 4), 16),
                                           Integer.parseInt(code.substring(4, 6), 16),
                                           Integer.parseInt(code.substring(6, 8), 16));
         }
         catch (Exception e)
         {
            throw new IllegalArgumentException("Unable to parse hex code: " + webColor);
         }

         throw new IllegalArgumentException("Unable to parse hex code: " + webColor);
      }
      else if (color.startsWith("rgb"))
      {
         // Formats accepted:
         // rgb(255, 0, 0)          /* integer range 0 - 255 */
         // rgb(100%, 0%, 0%)       /* float range 0.0% - 100.0% */
         // rgba(100%, 0%, 0%, 0.5) /* 0.5 opacity, semi-transparent */
         // rgba(255, 0, 0, 0.5)    /* 0.5 opacity, semi-transparent */

         boolean parseAlpha = color.startsWith("a(", 3);
         int numberOfComponents = parseAlpha ? 4 : 3;
         color = color.substring(color.indexOf("(") + 1, color.lastIndexOf(")"));
         String[] rgbaArray = color.split(",");

         try
         {
            for (int i = 0; i < numberOfComponents; i++)
               rgbaArray[i] = rgbaArray[i].trim();

            boolean parsePercent;
            if (rgbaArray[0].contains("%"))
            {
               if (!rgbaArray[1].contains("%") || !rgbaArray[2].contains("%"))
                  throw new IllegalArgumentException("Inconsistent color formatting: " + webColor);

               parsePercent = true;
               for (int i = 0; i < 3; i++)
                  rgbaArray[i] = rgbaArray[i].substring(0, rgbaArray[i].length() - 1);
            }
            else
            {
               if (rgbaArray[1].contains("%") || rgbaArray[2].contains("%"))
                  throw new IllegalArgumentException("Inconsistent color formatting: " + webColor);
               parsePercent = false;
            }

            double red = Integer.parseInt(rgbaArray[0]);
            double green = Integer.parseInt(rgbaArray[1]);
            double blue = Integer.parseInt(rgbaArray[2]);
            double alpha = parseAlpha ? Double.parseDouble(rgbaArray[3]) : 1.0;

            if (parsePercent)
            {
               red /= 100.0;
               green /= 100.0;
               blue /= 100.0;
            }
            else
            {
               red /= 255.0;
               green /= 255.0;
               blue /= 255.0;
            }

            return new ColorDescription(red, green, blue, alpha);
         }
         catch (Exception e)
         {
            throw new IllegalArgumentException("Unable to parse RGBA color: " + webColor, e);
         }
      }
      else if (color.startsWith("hsv") || color.startsWith("hsb") || color.startsWith("hsl"))
      {
         // Formats accepted:
         // hsv(120, 50%, 50%)        /* hue in [0,360], float range 0.0% - 100.0%  */
         // hsv(120, 0.5, 0.5)        /* hue in [0,360], float range 0.0 - 1.0  */
         // hsva(120, 100%, 50%, 0.5) /* 0.5 opacity, semi-transparent */
         // hsva(120, 1.0, 0.5, 0.5)  /* 0.5 opacity, semi-transparent */
         // hsb(120, 50%, 50%)        /* hue in [0,360], float range 0.0% - 100.0%  */
         // hsb(120, 0.5, 0.5)        /* hue in [0,360], float range 0.0 - 1.0  */
         // hsba(120, 100%, 50%, 0.5) /* 0.5 opacity, semi-transparent */
         // hsba(120, 1.0, 0.5, 0.5)  /* 0.5 opacity, semi-transparent */
         // hsl(120, 50%, 50%)        /* hue in [0,360], float range 0.0% - 100.0%  */
         // hsl(120, 0.5, 0.5)        /* hue in [0,360], float range 0.0 - 1.0  */
         // hsla(120, 100%, 50%, 0.5) /* 0.5 opacity, semi-transparent */
         // hsla(120, 1.0, 0.5, 0.5)  /* 0.5 opacity, semi-transparent */
         boolean parseHSL = color.charAt(2) == 'l';
         boolean parseAlpha = color.startsWith("a(", 3);
         int numberOfComponents = parseAlpha ? 4 : 3;
         color = color.substring(color.indexOf("(") + 1, color.lastIndexOf(")"));
         String[] hsbArray = color.split(",");
         for (int i = 0; i < numberOfComponents; i++)
            hsbArray[i] = hsbArray[i].trim();

         boolean parsePercent;
         if (hsbArray[1].contains("%"))
         {
            if (!hsbArray[2].contains("%"))
               throw new IllegalArgumentException("Inconsistent color formatting: " + webColor);

            parsePercent = true;
            hsbArray[1] = hsbArray[1].substring(0, hsbArray[1].length() - 1);
            hsbArray[2] = hsbArray[2].substring(0, hsbArray[2].length() - 1);
         }
         else
         {
            if (hsbArray[2].contains("%"))
               throw new IllegalArgumentException("Inconsistent color formatting: " + webColor);
            parsePercent = false;
         }

         try
         {
            double hue = Double.parseDouble(hsbArray[0].trim());
            double saturation = Double.parseDouble(hsbArray[1].trim());
            double brightness = Double.parseDouble(hsbArray[2].trim());
            double alpha = parseAlpha ? Double.parseDouble(hsbArray[3].trim()) : 1.0;

            if (parsePercent)
            {
               saturation /= 100.0;
               brightness /= 100.0;
            }

            if (parseHSL)
               return hsla(hue, saturation, brightness, alpha);
            else
               return hsba(hue, saturation, brightness, alpha);
         }
         catch (Exception e)
         {
            throw new IllegalArgumentException("Unable to parse HSV/HSB/HSL color: " + webColor, e);
         }
      }
      else
      {
         throw new IllegalArgumentException("Unknown color format: " + webColor);
      }
   }

   private static boolean isNamedColorMethod(Method method)
   {
      if (method.getReturnType() != ColorDescription.class)
         return false;
      if (!Modifier.isPublic(method.getModifiers()) || !Modifier.isStatic(method.getModifiers()))
         return false;
      if (method.getParameterCount() != 0)
         return false;
      return true;
   }

   private static final ColorDescription invokeMethod(Method method)
   {
      try
      {
         return (ColorDescription) method.invoke(null);
      }
      catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
      {
         throw new RuntimeException("Problem invoking color factory", e);
      }
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F0F8FF;float:left;margin: 0 10px 0 0"></div>Alice
    * blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription AliceBlue()
   {
      return rgb(0xF0F8FF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FAEBD7;float:left;margin: 0 10px 0
    * 0"></div>Antique white color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription AntiqueWhite()
   {
      return rgb(0xFAEBD7);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#00FFFF;float:left;margin: 0 10px 0 0"></div>Aqua
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Aqua()
   {
      return rgb(0x00FFFF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#7FFFD4;float:left;margin: 0 10px 0
    * 0"></div>Aquamarine color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Aquamarine()
   {
      return rgb(0x7FFFD4);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F0FFFF;float:left;margin: 0 10px 0 0"></div> Azure
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Azure()
   {
      return rgb(0xF0FFFF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F5F5DC;float:left;margin: 0 10px 0 0"></div> Beige
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Beige()
   {
      return rgb(0xF5F5DC);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFE4C4;float:left;margin: 0 10px 0 0"></div>
    * Bisque color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Bisque()
   {
      return rgb(0xFFE4C4);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#000000;float:left;margin: 0 10px 0 0"></div> Black
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Black()
   {
      return rgb(0x000000);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFEBCD;float:left;margin: 0 10px 0 0"></div>
    * Blanched almond color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription BlanchedAlmond()
   {
      return rgb(0xFFEBCD);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#0000FF;float:left;margin: 0 10px 0 0"></div> Blue
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Blue()
   {
      return rgb(0x0000FF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#8A2BE2;float:left;margin: 0 10px 0 0"></div> Blue
    * violet color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription BlueViolet()
   {
      return rgb(0x8A2BE2);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#A52A2A;float:left;margin: 0 10px 0 0"></div> Brown
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Brown()
   {
      return rgb(0xA52A2A);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#DEB887;float:left;margin: 0 10px 0 0"></div> Burly
    * wood color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription BurlyWood()
   {
      return rgb(0xDEB887);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#5F9EA0;float:left;margin: 0 10px 0 0"></div> Cadet
    * blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription CadetBlue()
   {
      return rgb(0x5F9EA0);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#7FFF00;float:left;margin: 0 10px 0 0"></div>
    * Chartreuse color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Chartreuse()
   {
      return rgb(0x7FFF00);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#D2691E;float:left;margin: 0 10px 0 0"></div>
    * Chocolate color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Chocolate()
   {
      return rgb(0xD2691E);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF7F50;float:left;margin: 0 10px 0 0"></div> Coral
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Coral()
   {
      return rgb(0xFF7F50);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#6495ED;float:left;margin: 0 10px 0 0"></div>
    * Cornflower blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription CornflowerBlue()
   {
      return rgb(0x6495ED);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFF8DC;float:left;margin: 0 10px 0 0"></div>
    * Cornsilk color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Cornsilk()
   {
      return rgb(0xFFF8DC);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#DC143C;float:left;margin: 0 10px 0 0"></div>
    * Crimson color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Crimson()
   {
      return rgb(0xDC143C);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#00FFFF;float:left;margin: 0 10px 0 0"></div> Cyan
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Cyan()
   {
      return rgb(0x00FFFF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#00008B;float:left;margin: 0 10px 0 0"></div> Dark
    * blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkBlue()
   {
      return rgb(0x00008B);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#008B8B;float:left;margin: 0 10px 0 0"></div> Dark
    * cyan color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkCyan()
   {
      return rgb(0x008B8B);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#B8860B;float:left;margin: 0 10px 0 0"></div> Dark
    * goldenrod color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkGoldenrod()
   {
      return rgb(0xB8860B);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#A9A9A9;float:left;margin: 0 10px 0 0"></div> Dark
    * gray color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkGray()
   {
      return rgb(0xA9A9A9);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#006400;float:left;margin: 0 10px 0 0"></div> Dark
    * green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkGreen()
   {
      return rgb(0x006400);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#A9A9A9;float:left;margin: 0 10px 0 0"></div> Dark
    * grey color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkGrey()
   {
      return DarkGray();
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#BDB76B;float:left;margin: 0 10px 0 0"></div> Dark
    * khaki color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkKhaki()
   {
      return rgb(0xBDB76B);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#8B008B;float:left;margin: 0 10px 0 0"></div> Dark
    * magenta color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkMagenta()
   {
      return rgb(0x8B008B);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#556B2F;float:left;margin: 0 10px 0 0"></div> Dark
    * olive green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkOliveGreen()
   {
      return rgb(0x556B2F);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF8C00;float:left;margin: 0 10px 0 0"></div> Dark
    * orange color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkOrange()
   {
      return rgb(0xFF8C00);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#9932CC;float:left;margin: 0 10px 0 0"></div> Dark
    * orchid color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkOrchid()
   {
      return rgb(0x9932CC);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#8B0000;float:left;margin: 0 10px 0 0"></div> Dark
    * red color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkRed()
   {
      return rgb(0x8B0000);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#E9967A;float:left;margin: 0 10px 0 0"></div> Dark
    * salmon color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkSalmon()
   {
      return rgb(0xE9967A);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#8FBC8F;float:left;margin: 0 10px 0 0"></div> Dark
    * sea green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkSeaGreen()
   {
      return rgb(0x8FBC8F);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#483D8B;float:left;margin: 0 10px 0 0"></div> Dark
    * slate blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkSlateBlue()
   {
      return rgb(0x483D8B);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#2F4F4F;float:left;margin: 0 10px 0 0"></div> Dark
    * slate gray color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkSlateGray()
   {
      return rgb(0x2F4F4F);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#2F4F4F;float:left;margin: 0 10px 0 0"></div> Dark
    * slate grey color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkSlateGrey()
   {
      return DarkSlateGray();
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#00CED1;float:left;margin: 0 10px 0 0"></div> Dark
    * turquoise color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkTurquoise()
   {
      return rgb(0x00CED1);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#9400D3;float:left;margin: 0 10px 0 0"></div> Dark
    * violet color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DarkViolet()
   {
      return rgb(0x9400D3);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF1493;float:left;margin: 0 10px 0 0"></div> Deep
    * pink color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DeepPink()
   {
      return rgb(0xFF1493);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#00BFFF;float:left;margin: 0 10px 0 0"></div> Deep
    * sky blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DeepSkyBlue()
   {
      return rgb(0x00BFFF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#696969;float:left;margin: 0 10px 0 0"></div> Dim
    * gray color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DimGray()
   {
      return rgb(0x696969);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#696969;float:left;margin: 0 10px 0 0"></div> Dim
    * grey color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DimGrey()
   {
      return DimGray();
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#1E90FF;float:left;margin: 0 10px 0 0"></div>
    * Dodger blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription DodgerBlue()
   {
      return rgb(0x1E90FF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#B22222;float:left;margin: 0 10px 0 0"></div> Fire
    * brick color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription FireBrick()
   {
      return rgb(0xB22222);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFFAF0;float:left;margin: 0 10px 0 0"></div>
    * Floral white color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription FloralWhite()
   {
      return rgb(0xFFFAF0);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#228B22;float:left;margin: 0 10px 0 0"></div>
    * Forest green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription ForestGreen()
   {
      return rgb(0x228B22);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF00FF;float:left;margin: 0 10px 0 0"></div>
    * Fuchsia color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Fuchsia()
   {
      return rgb(0xFF00FF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#DCDCDC;float:left;margin: 0 10px 0 0"></div>
    * Gainsboro color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Gainsboro()
   {
      return rgb(0xDCDCDC);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F8F8FF;float:left;margin: 0 10px 0 0"></div> Ghost
    * white color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription GhostWhite()
   {
      return rgb(0xF8F8FF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFD700;float:left;margin: 0 10px 0 0"></div> Gold
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Gold()
   {
      return rgb(0xFFD700);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#DAA520;float:left;margin: 0 10px 0 0"></div>
    * Goldenrod color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Goldenrod()
   {
      return rgb(0xDAA520);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#808080;float:left;margin: 0 10px 0 0"></div> Gray
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Gray()
   {
      return rgb(0x808080);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#008000;float:left;margin: 0 10px 0 0"></div> Green
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Green()
   {
      return rgb(0x008000);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#ADFF2F;float:left;margin: 0 10px 0 0"></div> Green
    * yellow color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription GreenYellow()
   {
      return rgb(0xADFF2F);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#808080;float:left;margin: 0 10px 0 0"></div> Grey
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Grey()
   {
      return Gray();
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F0FFF0;float:left;margin: 0 10px 0 0"></div>
    * Honeydew color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Honeydew()
   {
      return rgb(0xF0FFF0);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF69B4;float:left;margin: 0 10px 0 0"></div> Hot
    * pink color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription HotPink()
   {
      return rgb(0xFF69B4);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#CD5C5C;float:left;margin: 0 10px 0 0"></div>
    * Indian red color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription IndianRed()
   {
      return rgb(0xCD5C5C);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#4B0082;float:left;margin: 0 10px 0 0"></div>
    * Indigo color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Indigo()
   {
      return rgb(0x4B0082);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFFFF0;float:left;margin: 0 10px 0 0"></div> Ivory
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Ivory()
   {
      return rgb(0xFFFFF0);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F0E68C;float:left;margin: 0 10px 0 0"></div> Khaki
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Khaki()
   {
      return rgb(0xF0E68C);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#E6E6FA;float:left;margin: 0 10px 0 0"></div>
    * Lavender color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Lavender()
   {
      return rgb(0xE6E6FA);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFF0F5;float:left;margin: 0 10px 0 0"></div>
    * Lavender blush color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LavenderBlush()
   {
      return rgb(0xFFF0F5);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#7CFC00;float:left;margin: 0 10px 0 0"></div> Lawn
    * green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LawnGreen()
   {
      return rgb(0x7CFC00);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFFACD;float:left;margin: 0 10px 0 0"></div> Lemon
    * chiffon color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LemonChiffon()
   {
      return rgb(0xFFFACD);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#ADD8E6;float:left;margin: 0 10px 0 0"></div> Light
    * blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightBlue()
   {
      return rgb(0xADD8E6);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F08080;float:left;margin: 0 10px 0 0"></div> Light
    * coral color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightCoral()
   {
      return rgb(0xF08080);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#E0FFFF;float:left;margin: 0 10px 0 0"></div> Light
    * cyan color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightCyan()
   {
      return rgb(0xE0FFFF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FAFAD2;float:left;margin: 0 10px 0 0"></div> Light
    * goldenrod yellow color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightGoldenrodYellow()
   {
      return rgb(0xFAFAD2);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#D3D3D3;float:left;margin: 0 10px 0 0"></div> Light
    * gray color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightGray()
   {
      return rgb(0xD3D3D3);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#90EE90;float:left;margin: 0 10px 0 0"></div> Light
    * green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightGreen()
   {
      return rgb(0x90EE90);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#D3D3D3;float:left;margin: 0 10px 0 0"></div> Light
    * grey color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightGrey()
   {
      return LightGray();
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFB6C1;float:left;margin: 0 10px 0 0"></div> Light
    * pink color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightPink()
   {
      return rgb(0xFFB6C1);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFA07A;float:left;margin: 0 10px 0 0"></div> Light
    * salmon color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightSalmon()
   {
      return rgb(0xFFA07A);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#20B2AA;float:left;margin: 0 10px 0 0"></div> Light
    * sea green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightSeaGreen()
   {
      return rgb(0x20B2AA);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#87CEFA;float:left;margin: 0 10px 0 0"></div> Light
    * sky blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightSkyBlue()
   {
      return rgb(0x87CEFA);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#778899;float:left;margin: 0 10px 0 0"></div> Light
    * slate gray color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightSlateGray()
   {
      return rgb(0x778899);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#778899;float:left;margin: 0 10px 0 0"></div> Light
    * slate grey color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightSlateGrey()
   {
      return LightSlateGray();
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#B0C4DE;float:left;margin: 0 10px 0 0"></div> Light
    * steel blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightSteelBlue()
   {
      return rgb(0xB0C4DE);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFFFE0;float:left;margin: 0 10px 0 0"></div> Light
    * yellow color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LightYellow()
   {
      return rgb(0xFFFFE0);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#00FF00;float:left;margin: 0 10px 0 0"></div> Lime
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Lime()
   {
      return rgb(0x00FF00);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#32CD32;float:left;margin: 0 10px 0 0"></div> Lime
    * green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription LimeGreen()
   {
      return rgb(0x32CD32);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FAF0E6;float:left;margin: 0 10px 0 0"></div> Linen
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Linen()
   {
      return rgb(0xFAF0E6);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF00FF;float:left;margin: 0 10px 0 0"></div>
    * Magenta color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Magenta()
   {
      return rgb(0xFF00FF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#800000;float:left;margin: 0 10px 0 0"></div>
    * Maroon color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Maroon()
   {
      return rgb(0x800000);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#66CDAA;float:left;margin: 0 10px 0 0"></div>
    * Medium aquamarine color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumAquamarine()
   {
      return rgb(0x66CDAA);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#0000CD;float:left;margin: 0 10px 0 0"></div>
    * Medium blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumBlue()
   {
      return rgb(0x0000CD);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#BA55D3;float:left;margin: 0 10px 0 0"></div>
    * Medium orchid color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumOrchid()
   {
      return rgb(0xBA55D3);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#9370DB;float:left;margin: 0 10px 0 0"></div>
    * Medium purple color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumPurple()
   {
      return rgb(0x9370DB);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#3CB371;float:left;margin: 0 10px 0 0"></div>
    * Medium sea green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumSeaGreen()
   {
      return rgb(0x3CB371);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#7B68EE;float:left;margin: 0 10px 0 0"></div>
    * Medium slate blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumSlateBlue()
   {
      return rgb(0x7B68EE);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#00FA9A;float:left;margin: 0 10px 0 0"></div>
    * Medium spring green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumSpringGreen()
   {
      return rgb(0x00FA9A);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#48D1CC;float:left;margin: 0 10px 0 0"></div>
    * Medium turquoise color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumTurquoise()
   {
      return rgb(0x48D1CC);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#C71585;float:left;margin: 0 10px 0 0"></div>
    * Medium violet red color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MediumVioletRed()
   {
      return rgb(0xC71585);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#191970;float:left;margin: 0 10px 0 0"></div>
    * Midnight blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MidnightBlue()
   {
      return rgb(0x191970);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F5FFFA;float:left;margin: 0 10px 0 0"></div> Mint
    * cream color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MintCream()
   {
      return rgb(0xF5FFFA);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFE4E1;float:left;margin: 0 10px 0 0"></div> Misty
    * rose color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription MistyRose()
   {
      return rgb(0xFFE4E1);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFE4B5;float:left;margin: 0 10px 0 0"></div>
    * Moccasin color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Moccasin()
   {
      return rgb(0xFFE4B5);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFDEAD;float:left;margin: 0 10px 0 0"></div>
    * Navajo white color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription NavajoWhite()
   {
      return rgb(0xFFDEAD);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#000080;float:left;margin: 0 10px 0 0"></div> Navy
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Navy()
   {
      return rgb(0x000080);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FDF5E6;float:left;margin: 0 10px 0 0"></div> Old
    * lace color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription OldLace()
   {
      return rgb(0xFDF5E6);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#808000;float:left;margin: 0 10px 0 0"></div> Olive
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Olive()
   {
      return rgb(0x808000);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#6B8E23;float:left;margin: 0 10px 0 0"></div> Olive
    * drab color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription OliveDrab()
   {
      return rgb(0x6B8E23);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFA500;float:left;margin: 0 10px 0 0"></div>
    * Orange color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Orange()
   {
      return rgb(0xFFA500);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF4500;float:left;margin: 0 10px 0 0"></div>
    * Orange red color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription OrangeRed()
   {
      return rgb(0xFF4500);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#DA70D6;float:left;margin: 0 10px 0 0"></div>
    * Orchid color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Orchid()
   {
      return rgb(0xDA70D6);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#EEE8AA;float:left;margin: 0 10px 0 0"></div> Pale
    * goldenrod color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription PaleGoldenrod()
   {
      return rgb(0xEEE8AA);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#98FB98;float:left;margin: 0 10px 0 0"></div> Pale
    * green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription PaleGreen()
   {
      return rgb(0x98FB98);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#AFEEEE;float:left;margin: 0 10px 0 0"></div> Pale
    * turquoise color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription PaleTurquoise()
   {
      return rgb(0xAFEEEE);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#DB7093;float:left;margin: 0 10px 0 0"></div> Pale
    * violet red color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription PaleVioletRed()
   {
      return rgb(0xDB7093);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFEFD5;float:left;margin: 0 10px 0 0"></div>
    * Papaya whip color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription PapayaWhip()
   {
      return rgb(0xFFEFD5);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFDAB9;float:left;margin: 0 10px 0 0"></div> Peach
    * puff color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription PeachPuff()
   {
      return rgb(0xFFDAB9);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#CD853F;float:left;margin: 0 10px 0 0"></div> Peru
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Peru()
   {
      return rgb(0xCD853F);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFC0CB;float:left;margin: 0 10px 0 0"></div> Pink
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Pink()
   {
      return rgb(0xFFC0CB);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#DDA0DD;float:left;margin: 0 10px 0 0"></div> Plum
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Plum()
   {
      return rgb(0xDDA0DD);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#B0E0E6;float:left;margin: 0 10px 0 0"></div>
    * Powder blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription PowderBlue()
   {
      return rgb(0xB0E0E6);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#800080;float:left;margin: 0 10px 0 0"></div>
    * Purple color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Purple()
   {
      return rgb(0x800080);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF0000;float:left;margin: 0 10px 0 0"></div> Red
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Red()
   {
      return rgb(0xFF0000);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#BC8F8F;float:left;margin: 0 10px 0 0"></div> Rosy
    * brown color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription RosyBrown()
   {
      return rgb(0xBC8F8F);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#4169E1;float:left;margin: 0 10px 0 0"></div> Royal
    * blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription RoyalBlue()
   {
      return rgb(0x4169E1);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#8B4513;float:left;margin: 0 10px 0 0"></div>
    * Saddle brown color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SaddleBrown()
   {
      return rgb(0x8B4513);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FA8072;float:left;margin: 0 10px 0 0"></div>
    * Salmon color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Salmon()
   {
      return rgb(0xFA8072);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F4A460;float:left;margin: 0 10px 0 0"></div> Sandy
    * brown color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SandyBrown()
   {
      return rgb(0xF4A460);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#2E8B57;float:left;margin: 0 10px 0 0"></div> Sea
    * green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SeaGreen()
   {
      return rgb(0x2E8B57);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFF5EE;float:left;margin: 0 10px 0 0"></div> Sea
    * shell color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SeaShell()
   {
      return rgb(0xFFF5EE);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#A0522D;float:left;margin: 0 10px 0 0"></div>
    * Sienna color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Sienna()
   {
      return rgb(0xA0522D);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#C0C0C0;float:left;margin: 0 10px 0 0"></div>
    * Silver color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Silver()
   {
      return rgb(0xC0C0C0);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#87CEEB;float:left;margin: 0 10px 0 0"></div> Sky
    * blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SkyBlue()
   {
      return rgb(0x87CEEB);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#6A5ACD;float:left;margin: 0 10px 0 0"></div> Slate
    * blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SlateBlue()
   {
      return rgb(0x6A5ACD);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#708090;float:left;margin: 0 10px 0 0"></div> Slate
    * gray color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SlateGray()
   {
      return rgb(0x708090);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#708090;float:left;margin: 0 10px 0 0"></div> Slate
    * grey color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SlateGrey()
   {
      return SlateGray();
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFFAFA;float:left;margin: 0 10px 0 0"></div> Snow
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Snow()
   {
      return rgb(0xFFFAFA);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#00FF7F;float:left;margin: 0 10px 0 0"></div>
    * Spring green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SpringGreen()
   {
      return rgb(0x00FF7F);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#4682B4;float:left;margin: 0 10px 0 0"></div> Steel
    * blue color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription SteelBlue()
   {
      return rgb(0x4682B4);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#D2B48C;float:left;margin: 0 10px 0 0"></div> Tan
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Tan()
   {
      return rgb(0xD2B48C);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#008080;float:left;margin: 0 10px 0 0"></div> Teal
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Teal()
   {
      return rgb(0x008080);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#D8BFD8;float:left;margin: 0 10px 0 0"></div>
    * Thistle color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Thistle()
   {
      return rgb(0xD8BFD8);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FF6347;float:left;margin: 0 10px 0 0"></div>
    * Tomato color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Tomato()
   {
      return rgb(0xFF6347);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#40E0D0;float:left;margin: 0 10px 0 0"></div>
    * Turquoise color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Turquoise()
   {
      return rgb(0x40E0D0);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#EE82EE;float:left;margin: 0 10px 0 0"></div>
    * Violet color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Violet()
   {
      return rgb(0xEE82EE);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F5DEB3;float:left;margin: 0 10px 0 0"></div> Wheat
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Wheat()
   {
      return rgb(0xF5DEB3);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFFFFF;float:left;margin: 0 10px 0 0"></div> White
    * color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription White()
   {
      return rgb(0xFFFFFF);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#F5F5F5;float:left;margin: 0 10px 0 0"></div> White
    * smoke color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription WhiteSmoke()
   {
      return rgb(0xF5F5F5);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#FFFF00;float:left;margin: 0 10px 0 0"></div>
    * Yellow color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription Yellow()
   {
      return rgb(0xFFFF00);
   }

   /**
    * <p>
    * <div style="border:1px solid
    * black;width:40px;height:20px;background-color:#9ACD32;float:left;margin: 0 10px 0 0"></div>
    * Yellow green color.
    * </p>
    *
    * @return the RGB color.
    * @see <a href="http://www.colors.commutercreative.com/grid/">147 CSS Named Colors</a>
    */
   public static final ColorDescription YellowGreen()
   {
      return rgb(0x9ACD32);
   }

   /**
    * Creates and returns a new color that is completely transparent.
    * 
    * @return the transparent color.
    */
   public static final ColorDescription Transparent()
   {
      return new ColorDescription(0.0, 0.0, 0.0, 0.0);
   }
}
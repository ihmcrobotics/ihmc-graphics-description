package us.ihmc.graphicsDescription.color;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.tools.EuclidCoreTools;

public class ColorDescriptionTest
{
   private static final double EPSILON = 1.0e-9;
   private static final double LARGE_EPSILON = 1.0 / 255.0;
   private static final int ITERATIONS = 10000;

   public static void main(String[] args)
   {
      System.out.println(ColorDescription.parse("AliceBlue"));;
      System.out.println(ColorDescription.namedColorLowerCaseMap.size());
   }

   @Test
   public void testArrayConversion()
   {
      Random random = new Random(304985);

      for (int i = 0; i < ITERATIONS; i++)
      {
         int redInt = random.nextInt(256);
         int greenInt = random.nextInt(256);
         int blueInt = random.nextInt(256);
         int alphaInt = random.nextInt(256);
         double red = redInt / 255.0;
         double green = greenInt / 255.0;
         double blue = blueInt / 255.0;
         double alpha = alphaInt / 255.0;

         assertRGBAEquals(red, green, blue, 1, ColorDescription.rgb(new double[] {red, green, blue}), EPSILON);
         assertRGBAEquals(red, green, blue, alpha, ColorDescription.rgba(new double[] {red, green, blue, alpha}), EPSILON);

         assertRGBAEquals(red, green, blue, 1, ColorDescription.rgb(new int[] {redInt, greenInt, blueInt}), EPSILON);
         assertRGBAEquals(red, green, blue, alpha, ColorDescription.rgba(new int[] {redInt, greenInt, blueInt, alphaInt}), EPSILON);

         assertRGBEquals(red, green, blue, new ColorDescription(red, green, blue).toRGBDoubleArray(), EPSILON);
         assertRGBAEquals(red, green, blue, alpha, new ColorDescription(red, green, blue, alpha).toRGBADoubleArray(), EPSILON);

         assertRGBEquals(redInt, greenInt, blueInt, new ColorDescription(redInt, greenInt, blueInt).toRGBIntArray());
         assertRGBAEquals(redInt, greenInt, blueInt, alphaInt, new ColorDescription(redInt, greenInt, blueInt, alphaInt).toRGBAIntArray());
      }
   }

   @Test
   public void testHexConversion()
   {
      Random random = new Random(4752);

      for (int i = 0; i < 256; i++)
      {
         assertRGBAEquals(0, 0, i / 255.0, 1, ColorDescription.rgb(i), EPSILON);
         assertRGBAEquals(0, i / 255.0, 0, 1, ColorDescription.rgb(i << 8), EPSILON);
         assertRGBAEquals(i / 255.0, 0, 0, 1, ColorDescription.rgb(i << 16), EPSILON);

         assertRGBAEquals(0, 0, i / 255.0, 0, ColorDescription.argb(i), EPSILON);
         assertRGBAEquals(0, i / 255.0, 0, 0, ColorDescription.argb(i << 8), EPSILON);
         assertRGBAEquals(i / 255.0, 0, 0, 0, ColorDescription.argb(i << 16), EPSILON);
         assertRGBAEquals(0, 0, 0, i / 255.0, ColorDescription.argb(i << 24), EPSILON);

         assertRGBAEquals(0, 0, i / 255.0, 0, ColorDescription.rgba(i << 8), EPSILON);
         assertRGBAEquals(0, i / 255.0, 0, 0, ColorDescription.rgba(i << 16), EPSILON);
         assertRGBAEquals(i / 255.0, 0, 0, 0, ColorDescription.rgba(i << 24), EPSILON);
         assertRGBAEquals(0, 0, 0, i / 255.0, ColorDescription.rgba(i), EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test RGB -> Hex -> RGB
         double red = random.nextInt(256) / 255.0;
         double green = random.nextInt(256) / 255.0;
         double blue = random.nextInt(256) / 255.0;
         double alpha = 1.0;
         ColorDescription color = ColorDescription.rgb(new ColorDescription(red, green, blue).toRGB());
         assertRGBAEquals(red, green, blue, alpha, color, EPSILON);

         alpha = random.nextInt(256) / 255.0;
         color = ColorDescription.argb(new ColorDescription(red, green, blue, alpha).toARGB());
         assertRGBAEquals(red, green, blue, alpha, color, EPSILON);

         color = ColorDescription.rgba(new ColorDescription(red, green, blue, alpha).toRGBA());
         assertRGBAEquals(red, green, blue, alpha, color, EPSILON);
      }

      // RGB
      assertRGBAEquals(0, 0, 0, 1, ColorDescription.rgb(0x000000), EPSILON);
      assertRGBAEquals(1, 0, 0, 1, ColorDescription.rgb(0xff0000), EPSILON);
      assertRGBAEquals(0, 1, 0, 1, ColorDescription.rgb(0x00ff00), EPSILON);
      assertRGBAEquals(0, 0, 1, 1, ColorDescription.rgb(0x0000ff), EPSILON);
      assertRGBAEquals(1, 1, 1, 1, ColorDescription.rgb(0xffffff), EPSILON);
      assertRGBAEquals(1.0 / 255.0, 0, 0, 1, ColorDescription.rgb(0x010000), EPSILON);
      assertRGBAEquals(0, 1.0 / 255.0, 0, 1, ColorDescription.rgb(0x000100), EPSILON);
      assertRGBAEquals(0, 0, 1.0 / 255.0, 1, ColorDescription.rgb(0x000001), EPSILON);

      // ARGB
      assertRGBAEquals(0, 0, 0, 0, ColorDescription.argb(0x00000000), EPSILON);
      assertRGBAEquals(0, 0, 0, 1, ColorDescription.argb(0xff000000), EPSILON);
      assertRGBAEquals(1, 0, 0, 0, ColorDescription.argb(0x00ff0000), EPSILON);
      assertRGBAEquals(0, 1, 0, 0, ColorDescription.argb(0x0000ff00), EPSILON);
      assertRGBAEquals(0, 0, 1, 0, ColorDescription.argb(0x000000ff), EPSILON);
      assertRGBAEquals(1, 1, 1, 1, ColorDescription.argb(0xffffffff), EPSILON);
      assertRGBAEquals(1.0 / 255.0, 0, 0, 0, ColorDescription.argb(0x00010000), EPSILON);
      assertRGBAEquals(0, 1.0 / 255.0, 0, 0, ColorDescription.argb(0x00000100), EPSILON);
      assertRGBAEquals(0, 0, 1.0 / 255.0, 0, ColorDescription.argb(0x00000001), EPSILON);
      assertRGBAEquals(0, 0, 0, 1.0 / 255.0, ColorDescription.argb(0x01000000), EPSILON);

      // RGBA
      assertRGBAEquals(0, 0, 0, 0, ColorDescription.rgba(0x00000000), EPSILON);
      assertRGBAEquals(1, 0, 0, 0, ColorDescription.rgba(0xff000000), EPSILON);
      assertRGBAEquals(0, 1, 0, 0, ColorDescription.rgba(0x00ff0000), EPSILON);
      assertRGBAEquals(0, 0, 1, 0, ColorDescription.rgba(0x0000ff00), EPSILON);
      assertRGBAEquals(0, 0, 0, 1, ColorDescription.rgba(0x000000ff), EPSILON);
      assertRGBAEquals(1, 1, 1, 1, ColorDescription.rgba(0xffffffff), EPSILON);
      assertRGBAEquals(1.0 / 255.0, 0, 0, 0, ColorDescription.rgba(0x01000000), EPSILON);
      assertRGBAEquals(0, 1.0 / 255.0, 0, 0, ColorDescription.rgba(0x00010000), EPSILON);
      assertRGBAEquals(0, 0, 1.0 / 255.0, 0, ColorDescription.rgba(0x00000100), EPSILON);
      assertRGBAEquals(0, 0, 0, 1.0 / 255.0, ColorDescription.rgba(0x00000001), EPSILON);
   }

   @Test
   public void testHSBConversion()
   {
      Random random = new Random(23545);

      for (int i = 0; i < ITERATIONS; i++)
      { // Test HSB -> RGB -> HSB
         double hue = EuclidCoreRandomTools.nextDouble(random, 0.0, 360.0);
         double saturation = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double brightness = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);

         ColorDescription color = ColorDescription.hsb(hue, saturation, brightness);
         assertHSBAEquals(hue, saturation, brightness, 1.0, color, EPSILON);

         double[] hsb = color.toHSBDoubleArray();
         assertHSBEquals(hue, saturation, brightness, hsb, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test HSBA -> RGBA -> HSBA
         double hue = EuclidCoreRandomTools.nextDouble(random, 0.0, 360.0);
         double saturation = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double brightness = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double alpha = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);

         ColorDescription color = ColorDescription.hsba(hue, saturation, brightness, alpha);
         assertHSBAEquals(hue, saturation, brightness, alpha, color, EPSILON);

         double[] hsba = color.toHSBADoubleArray();
         assertHSBAEquals(hue, saturation, brightness, alpha, hsba, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test RGB -> HSB -> RGB
         double red = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double green = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double blue = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double alpha = 1.0;

         ColorDescription color = ColorDescription.hsb(new ColorDescription(red, green, blue).toHSBDoubleArray());
         assertRGBAEquals(red, green, blue, alpha, color, EPSILON);
         alpha = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         color = ColorDescription.hsba(new ColorDescription(red, green, blue, alpha).toHSBADoubleArray());
         assertRGBAEquals(red, green, blue, alpha, color, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test varying red only
         double red = i / (ITERATIONS - 1.0);
         double green = 0.0;
         double blue = 0.0;

         double expectedHue = 0.0;
         double expectedSaturation = red == 0.0 ? 0.0 : 1.0;
         double expectedBrightness = red;
         ColorDescription color = new ColorDescription(red, green, blue);
         assertHSBEquals(expectedHue, expectedSaturation, expectedBrightness, color, EPSILON);

         green = 1.0;
         blue = 1.0;

         expectedHue = red == 1.0 ? 0.0 : 180.0;
         expectedSaturation = 1.0 - red;
         expectedBrightness = 1.0;
         color = new ColorDescription(red, green, blue);
         assertHSBEquals(expectedHue, expectedSaturation, expectedBrightness, color, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test varying green only
         double red = 0.0;
         double green = i / (ITERATIONS - 1.0);
         double blue = 0.0;

         double expectedHue = green == 0.0 ? 0.0 : 120.0;
         double expectedSaturation = green == 0.0 ? 0.0 : 1.0;
         double expectedBrightness = green;
         ColorDescription color = new ColorDescription(red, green, blue);
         assertHSBEquals(expectedHue, expectedSaturation, expectedBrightness, color, EPSILON);

         red = 1.0;
         blue = 1.0;

         expectedHue = green == 1.0 ? 0.0 : 300.0;
         expectedSaturation = 1.0 - green;
         expectedBrightness = 1.0;
         color = new ColorDescription(red, green, blue);
         assertHSBEquals(expectedHue, expectedSaturation, expectedBrightness, color, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test varying blue only
         double red = 0.0;
         double green = 0.0;
         double blue = i / (ITERATIONS - 1.0);

         double expectedHue = blue == 0.0 ? 0.0 : 240.0;
         double expectedSaturation = blue == 0.0 ? 0.0 : 1.0;
         double expectedBrightness = blue;
         ColorDescription color = new ColorDescription(red, green, blue);
         assertHSBEquals(expectedHue, expectedSaturation, expectedBrightness, color, EPSILON);

         red = 1.0;
         green = 1.0;

         expectedHue = blue == 1.0 ? 0.0 : 60.0;
         expectedSaturation = 1.0 - blue;
         expectedBrightness = 1.0;
         color = new ColorDescription(red, green, blue);
         assertHSBEquals(expectedHue, expectedSaturation, expectedBrightness, color, EPSILON);
      }
   }

   @Test
   public void testHSLConversion()
   {
      Random random = new Random(23545);

      for (int i = 0; i < ITERATIONS; i++)
      { // Test HSL -> RGB -> HSL
         double hue = EuclidCoreRandomTools.nextDouble(random, 0.0, 360.0);
         double saturation = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double lightness = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);

         ColorDescription color = ColorDescription.hsl(hue, saturation, lightness);
         assertHSLAEquals(hue, saturation, lightness, 1.0, color, EPSILON);
         assertHSLEquals(hue, saturation, lightness, color, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test HSLA -> RGBA -> HSLA
         double hue = EuclidCoreRandomTools.nextDouble(random, 0.0, 360.0);
         double saturation = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double lightness = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double alpha = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);

         ColorDescription color = ColorDescription.hsla(hue, saturation, lightness, alpha);
         assertHSLAEquals(hue, saturation, lightness, alpha, color, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test RGB -> HSL -> RGB
         double red = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double green = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double blue = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         double alpha = 1.0;

         ColorDescription color = ColorDescription.hsl(new ColorDescription(red, green, blue).toHSLDoubleArray());
         assertRGBAEquals(red, green, blue, alpha, color, EPSILON);
         alpha = EuclidCoreRandomTools.nextDouble(random, 0.0, 1.0);
         color = ColorDescription.hsla(new ColorDescription(red, green, blue, alpha).toHSLADoubleArray());
         assertRGBAEquals(red, green, blue, alpha, color, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test varying red only
         double red = i / (ITERATIONS - 1.0);
         double green = 0.0;
         double blue = 0.0;

         double expectedHue = 0.0;
         double expectedSaturation = red == 0.0 ? 0.0 : 1.0;
         double expectedLightness = 0.5 * red;
         ColorDescription color = new ColorDescription(red, green, blue);
         assertHSLEquals(expectedHue, expectedSaturation, expectedLightness, color, EPSILON);

         green = 1.0;
         blue = 1.0;

         expectedHue = red == 1.0 ? 0.0 : 180.0;
         expectedSaturation = red == 1.0 ? 0.0 : 1.0;
         expectedLightness = 0.5 * (1.0 + red);
         color = new ColorDescription(red, green, blue);
         assertHSLEquals(expectedHue, expectedSaturation, expectedLightness, color, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test varying green only
         double red = 0.0;
         double green = i / (ITERATIONS - 1.0);
         double blue = 0.0;

         double expectedHue = green == 0.0 ? 0.0 : 120.0;
         double expectedSaturation = green == 0.0 ? 0.0 : 1.0;
         double expectedLightness = 0.5 * green;
         ColorDescription color = new ColorDescription(red, green, blue);
         assertHSLEquals(expectedHue, expectedSaturation, expectedLightness, color, EPSILON);

         red = 1.0;
         blue = 1.0;

         expectedHue = green == 1.0 ? 0.0 : 300.0;
         expectedSaturation = green == 1.0 ? 0.0 : 1.0;
         expectedLightness = 0.5 * (1.0 + green);
         color = new ColorDescription(red, green, blue);
         assertHSLEquals(expectedHue, expectedSaturation, expectedLightness, color, EPSILON);
      }

      for (int i = 0; i < ITERATIONS; i++)
      { // Test varying blue only
         double red = 0.0;
         double green = 0.0;
         double blue = i / (ITERATIONS - 1.0);

         double expectedHue = blue == 0.0 ? 0.0 : 240.0;
         double expectedSaturation = blue == 0.0 ? 0.0 : 1.0;
         double expectedLightness = 0.5 * blue;
         ColorDescription color = new ColorDescription(red, green, blue);
         assertHSLEquals(expectedHue, expectedSaturation, expectedLightness, color, EPSILON);

         red = 1.0;
         green = 1.0;

         expectedHue = blue == 1.0 ? 0.0 : 60.0;
         expectedSaturation = blue == 1.0 ? 0.0 : 1.0;
         expectedLightness = 0.5 * (1.0 + blue);
         color = new ColorDescription(red, green, blue);
         assertHSLEquals(expectedHue, expectedSaturation, expectedLightness, color, EPSILON);
      }
   }

   @Test
   public void testBasicColors()
   {
      // https://en.wikipedia.org/wiki/Web_colors#Basic_colors
      assertRGBEquals(1.0, 1.0, 1.0, ColorDescription.White(), LARGE_EPSILON);
      assertHSLEquals(0.0, 0.0, 1.0, ColorDescription.White(), LARGE_EPSILON);
      assertHSBEquals(0.0, 0.0, 1.0, ColorDescription.White(), LARGE_EPSILON);

      assertRGBEquals(0.75, 0.75, 0.75, ColorDescription.Silver(), LARGE_EPSILON);
      assertHSLEquals(0.00, 0.00, 0.75, ColorDescription.Silver(), LARGE_EPSILON);
      assertHSBEquals(0.00, 0.00, 0.75, ColorDescription.Silver(), LARGE_EPSILON);
      
      assertRGBEquals(0.50, 0.50, 0.50, ColorDescription.Gray(), LARGE_EPSILON);
      assertHSLEquals(0.00, 0.00, 0.50, ColorDescription.Gray(), LARGE_EPSILON);
      assertHSBEquals(0.00, 0.00, 0.50, ColorDescription.Gray(), LARGE_EPSILON);
      
      assertRGBEquals(0.00, 0.00, 0.00, ColorDescription.Black(), LARGE_EPSILON);
      assertHSLEquals(0.00, 0.00, 0.00, ColorDescription.Black(), LARGE_EPSILON);
      assertHSBEquals(0.00, 0.00, 0.00, ColorDescription.Black(), LARGE_EPSILON);
      
      assertRGBEquals(1.00, 0.00, 0.00, ColorDescription.Red(), LARGE_EPSILON);
      assertHSLEquals(0.00, 1.00, 0.50, ColorDescription.Red(), LARGE_EPSILON);
      assertHSBEquals(0.00, 1.00, 1.00, ColorDescription.Red(), LARGE_EPSILON);

      assertRGBEquals(0.5, 0.0, 0.00, ColorDescription.Maroon(), LARGE_EPSILON);
      assertHSLEquals(0.0, 1.0, 0.25, ColorDescription.Maroon(), LARGE_EPSILON);
      assertHSBEquals(0.0, 1.0, 0.50, ColorDescription.Maroon(), LARGE_EPSILON);

      assertRGBEquals(01.00, 1.00, 0.00, ColorDescription.Yellow(), LARGE_EPSILON);
      assertHSLEquals(60.00, 1.00, 0.50, ColorDescription.Yellow(), LARGE_EPSILON);
      assertHSBEquals(60.00, 1.00, 1.00, ColorDescription.Yellow(), LARGE_EPSILON);

      assertRGBEquals(00.5, 0.5, 0.00, ColorDescription.Olive(), LARGE_EPSILON);
      assertHSLEquals(60.0, 1.0, 0.25, ColorDescription.Olive(), LARGE_EPSILON);
      assertHSBEquals(60.0, 1.0, 0.50, ColorDescription.Olive(), LARGE_EPSILON);

      assertRGBEquals(000.0, 1.0, 0.0, ColorDescription.Lime(), LARGE_EPSILON);
      assertHSLEquals(120.0, 1.0, 0.5, ColorDescription.Lime(), LARGE_EPSILON);
      assertHSBEquals(120.0, 1.0, 1.0, ColorDescription.Lime(), LARGE_EPSILON);

      assertRGBEquals(000.0, 0.5, 0.00, ColorDescription.Green(), LARGE_EPSILON);
      assertHSLEquals(120.0, 1.0, 0.25, ColorDescription.Green(), LARGE_EPSILON);
      assertHSBEquals(120.0, 1.0, 0.50, ColorDescription.Green(), LARGE_EPSILON);
      
      assertRGBEquals(000.0, 1.0, 1.0, ColorDescription.Aqua(), LARGE_EPSILON);
      assertHSLEquals(180.0, 1.0, 0.5, ColorDescription.Aqua(), LARGE_EPSILON);
      assertHSBEquals(180.0, 1.0, 1.0, ColorDescription.Aqua(), LARGE_EPSILON);
      
      assertRGBEquals(000.0, 0.5, 0.50, ColorDescription.Teal(), LARGE_EPSILON);
      assertHSLEquals(180.0, 1.0, 0.25, ColorDescription.Teal(), LARGE_EPSILON);
      assertHSBEquals(180.0, 1.0, 0.50, ColorDescription.Teal(), LARGE_EPSILON);
      
      assertRGBEquals(000.0, 0.5, 0.50, ColorDescription.Teal(), LARGE_EPSILON);
      assertHSLEquals(180.0, 1.0, 0.25, ColorDescription.Teal(), LARGE_EPSILON);
      assertHSBEquals(180.0, 1.0, 0.50, ColorDescription.Teal(), LARGE_EPSILON);
      
      assertRGBEquals(000.0, 0.0, 1.0, ColorDescription.Blue(), LARGE_EPSILON);
      assertHSLEquals(240.0, 1.0, 0.5, ColorDescription.Blue(), LARGE_EPSILON);
      assertHSBEquals(240.0, 1.0, 1.0, ColorDescription.Blue(), LARGE_EPSILON);
      
      assertRGBEquals(000.0, 0.0, 0.50, ColorDescription.Navy(), LARGE_EPSILON);
      assertHSLEquals(240.0, 1.0, 0.25, ColorDescription.Navy(), LARGE_EPSILON);
      assertHSBEquals(240.0, 1.0, 0.50, ColorDescription.Navy(), LARGE_EPSILON);
      
      assertRGBEquals(001.0, 0.0, 1.0, ColorDescription.Fuchsia(), LARGE_EPSILON);
      assertHSLEquals(300.0, 1.0, 0.5, ColorDescription.Fuchsia(), LARGE_EPSILON);
      assertHSBEquals(300.0, 1.0, 1.0, ColorDescription.Fuchsia(), LARGE_EPSILON);
      
      assertRGBEquals(000.5, 0.0, 0.50, ColorDescription.Purple(), LARGE_EPSILON);
      assertHSLEquals(300.0, 1.0, 0.25, ColorDescription.Purple(), LARGE_EPSILON);
      assertHSBEquals(300.0, 1.0, 0.50, ColorDescription.Purple(), LARGE_EPSILON);
   }

   @Test
   public void testParse()
   {
      Random random = new Random(234);

      for (int i = 0; i < ITERATIONS; i++)
      { // Without alpha
         ColorDescription expected = ColorDescription.rgb(random.nextInt(1 << 24));
         int rInt = expected.getRedAsInteger();
         int gInt = expected.getGreenAsInteger();
         int bInt = expected.getBlueAsInteger();
         double rDouble = expected.getRed();
         double gDouble = expected.getGreen();
         double bDouble = expected.getBlue();
         int rPercent = (int) (100.0 * rDouble);
         int gPercent = (int) (100.0 * gDouble);
         int bPercent = (int) (100.0 * bDouble);
         double hDouble = expected.getHue();
         double svDouble = expected.getSaturation();
         double vDouble = expected.getBrightness();
         double svPercent = 100.0 * svDouble;
         double vPercent = 100.0 * vDouble;
         double slDouble = expected.toHSLDoubleArray()[1];
         double lDouble = expected.toHSLDoubleArray()[2];
         double slPercent = 100.0 * slDouble;
         double lPercent = 100.0 * lDouble;

         // RGB(0, 128, 255)
         String rgb = "RGB(" + rInt + ", " + gInt + ", " + bInt + ")";
         assertColorDescriptionEquals(expected, ColorDescription.parse(rgb.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(rgb.toLowerCase()), EPSILON);

         // RGB(0%, 50%, 100%)
         rgb = "RGB(" + rPercent + "%, " + gPercent + "%, " + bPercent + "%)";
         assertRGBAEquals(rPercent / 100.0, gPercent / 100.0, bPercent / 100.0, 1, ColorDescription.parse(rgb.toUpperCase()), EPSILON);
         assertRGBAEquals(rPercent / 100.0, gPercent / 100.0, bPercent / 100.0, 1, ColorDescription.parse(rgb.toLowerCase()), EPSILON);

         // HSV
         String hsv = "HSV(" + hDouble + ", " + svDouble + ", " + vDouble + ")";
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsv.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsv.toLowerCase()), EPSILON);
         hsv = "HSV(" + hDouble + ", " + svPercent + "%, " + vPercent + "%)";
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsv.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsv.toLowerCase()), EPSILON);
         // HSB
         String hsb = "HSB(" + hDouble + ", " + svDouble + ", " + vDouble + ")";
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsb.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsb.toLowerCase()), EPSILON);
         hsb = "HSB(" + hDouble + ", " + svPercent + "%, " + vPercent + "%)";
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsb.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsb.toLowerCase()), EPSILON);
         // HSL
         String hsl = "HSL(" + hDouble + ", " + slDouble + ", " + lDouble + ")";
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsl.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsl.toLowerCase()), EPSILON);
         hsl = "HSL(" + hDouble + ", " + slPercent + "%, " + lPercent + "%)";
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsl.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hsl.toLowerCase()), EPSILON);

         // Hex: 0x09FA3C
         String hex = String.format("0x%02x%02x%02x", rInt, gInt, bInt);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hex.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hex.toLowerCase()), EPSILON);

         // Hex: #09FA3C
         hex = String.format("#%02x%02x%02x", rInt, gInt, bInt);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hex.toUpperCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(hex.toLowerCase()), EPSILON);
      }

      assertColorDescriptionEquals(ColorDescription.AliceBlue(), ColorDescription.parse("AliceBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.AntiqueWhite(), ColorDescription.parse("AntiqueWhite"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Aqua(), ColorDescription.parse("Aqua"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Aquamarine(), ColorDescription.parse("Aquamarine"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Azure(), ColorDescription.parse("Azure"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Beige(), ColorDescription.parse("Beige"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Bisque(), ColorDescription.parse("Bisque"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Black(), ColorDescription.parse("Black"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.BlanchedAlmond(), ColorDescription.parse("BlanchedAlmond"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Blue(), ColorDescription.parse("Blue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.BlueViolet(), ColorDescription.parse("BlueViolet"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Brown(), ColorDescription.parse("Brown"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.BurlyWood(), ColorDescription.parse("BurlyWood"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.CadetBlue(), ColorDescription.parse("CadetBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Chartreuse(), ColorDescription.parse("Chartreuse"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Chocolate(), ColorDescription.parse("Chocolate"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Coral(), ColorDescription.parse("Coral"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.CornflowerBlue(), ColorDescription.parse("CornflowerBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Cornsilk(), ColorDescription.parse("Cornsilk"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Crimson(), ColorDescription.parse("Crimson"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Cyan(), ColorDescription.parse("Cyan"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkBlue(), ColorDescription.parse("DarkBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkCyan(), ColorDescription.parse("DarkCyan"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkGoldenrod(), ColorDescription.parse("DarkGoldenrod"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkGray(), ColorDescription.parse("DarkGray"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkGreen(), ColorDescription.parse("DarkGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkGrey(), ColorDescription.parse("DarkGrey"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkKhaki(), ColorDescription.parse("DarkKhaki"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkMagenta(), ColorDescription.parse("DarkMagenta"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkOliveGreen(), ColorDescription.parse("DarkOliveGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkOrange(), ColorDescription.parse("DarkOrange"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkOrchid(), ColorDescription.parse("DarkOrchid"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkRed(), ColorDescription.parse("DarkRed"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkSalmon(), ColorDescription.parse("DarkSalmon"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkSeaGreen(), ColorDescription.parse("DarkSeaGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkSlateBlue(), ColorDescription.parse("DarkSlateBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkSlateGray(), ColorDescription.parse("DarkSlateGray"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkSlateGrey(), ColorDescription.parse("DarkSlateGrey"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkTurquoise(), ColorDescription.parse("DarkTurquoise"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DarkViolet(), ColorDescription.parse("DarkViolet"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DeepPink(), ColorDescription.parse("DeepPink"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DeepSkyBlue(), ColorDescription.parse("DeepSkyBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DimGray(), ColorDescription.parse("DimGray"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DimGrey(), ColorDescription.parse("DimGrey"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.DodgerBlue(), ColorDescription.parse("DodgerBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.FireBrick(), ColorDescription.parse("FireBrick"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.FloralWhite(), ColorDescription.parse("FloralWhite"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.ForestGreen(), ColorDescription.parse("ForestGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Fuchsia(), ColorDescription.parse("Fuchsia"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Gainsboro(), ColorDescription.parse("Gainsboro"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.GhostWhite(), ColorDescription.parse("GhostWhite"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Gold(), ColorDescription.parse("Gold"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Goldenrod(), ColorDescription.parse("Goldenrod"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Gray(), ColorDescription.parse("Gray"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Green(), ColorDescription.parse("Green"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.GreenYellow(), ColorDescription.parse("GreenYellow"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Grey(), ColorDescription.parse("Grey"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Honeydew(), ColorDescription.parse("Honeydew"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.HotPink(), ColorDescription.parse("HotPink"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.IndianRed(), ColorDescription.parse("IndianRed"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Indigo(), ColorDescription.parse("Indigo"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Ivory(), ColorDescription.parse("Ivory"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Khaki(), ColorDescription.parse("Khaki"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Lavender(), ColorDescription.parse("Lavender"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LavenderBlush(), ColorDescription.parse("LavenderBlush"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LawnGreen(), ColorDescription.parse("LawnGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LemonChiffon(), ColorDescription.parse("LemonChiffon"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightBlue(), ColorDescription.parse("LightBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightCoral(), ColorDescription.parse("LightCoral"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightCyan(), ColorDescription.parse("LightCyan"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightGoldenrodYellow(), ColorDescription.parse("LightGoldenrodYellow"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightGray(), ColorDescription.parse("LightGray"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightGreen(), ColorDescription.parse("LightGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightGrey(), ColorDescription.parse("LightGrey"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightPink(), ColorDescription.parse("LightPink"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightSalmon(), ColorDescription.parse("LightSalmon"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightSeaGreen(), ColorDescription.parse("LightSeaGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightSkyBlue(), ColorDescription.parse("LightSkyBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightSlateGray(), ColorDescription.parse("LightSlateGray"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightSlateGrey(), ColorDescription.parse("LightSlateGrey"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightSteelBlue(), ColorDescription.parse("LightSteelBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LightYellow(), ColorDescription.parse("LightYellow"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Lime(), ColorDescription.parse("Lime"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.LimeGreen(), ColorDescription.parse("LimeGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Linen(), ColorDescription.parse("Linen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Magenta(), ColorDescription.parse("Magenta"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Maroon(), ColorDescription.parse("Maroon"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumAquamarine(), ColorDescription.parse("MediumAquamarine"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumBlue(), ColorDescription.parse("MediumBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumOrchid(), ColorDescription.parse("MediumOrchid"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumPurple(), ColorDescription.parse("MediumPurple"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumSeaGreen(), ColorDescription.parse("MediumSeaGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumSlateBlue(), ColorDescription.parse("MediumSlateBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumSpringGreen(), ColorDescription.parse("MediumSpringGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumTurquoise(), ColorDescription.parse("MediumTurquoise"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MediumVioletRed(), ColorDescription.parse("MediumVioletRed"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MidnightBlue(), ColorDescription.parse("MidnightBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MintCream(), ColorDescription.parse("MintCream"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.MistyRose(), ColorDescription.parse("MistyRose"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Moccasin(), ColorDescription.parse("Moccasin"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.NavajoWhite(), ColorDescription.parse("NavajoWhite"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Navy(), ColorDescription.parse("Navy"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.OldLace(), ColorDescription.parse("OldLace"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Olive(), ColorDescription.parse("Olive"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.OliveDrab(), ColorDescription.parse("OliveDrab"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Orange(), ColorDescription.parse("Orange"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.OrangeRed(), ColorDescription.parse("OrangeRed"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Orchid(), ColorDescription.parse("Orchid"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.PaleGoldenrod(), ColorDescription.parse("PaleGoldenrod"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.PaleGreen(), ColorDescription.parse("PaleGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.PaleTurquoise(), ColorDescription.parse("PaleTurquoise"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.PaleVioletRed(), ColorDescription.parse("PaleVioletRed"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.PapayaWhip(), ColorDescription.parse("PapayaWhip"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.PeachPuff(), ColorDescription.parse("PeachPuff"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Peru(), ColorDescription.parse("Peru"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Pink(), ColorDescription.parse("Pink"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Plum(), ColorDescription.parse("Plum"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.PowderBlue(), ColorDescription.parse("PowderBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Purple(), ColorDescription.parse("Purple"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Red(), ColorDescription.parse("Red"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.RosyBrown(), ColorDescription.parse("RosyBrown"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.RoyalBlue(), ColorDescription.parse("RoyalBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SaddleBrown(), ColorDescription.parse("SaddleBrown"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Salmon(), ColorDescription.parse("Salmon"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SandyBrown(), ColorDescription.parse("SandyBrown"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SeaGreen(), ColorDescription.parse("SeaGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SeaShell(), ColorDescription.parse("SeaShell"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Sienna(), ColorDescription.parse("Sienna"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Silver(), ColorDescription.parse("Silver"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SkyBlue(), ColorDescription.parse("SkyBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SlateBlue(), ColorDescription.parse("SlateBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SlateGray(), ColorDescription.parse("SlateGray"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SlateGrey(), ColorDescription.parse("SlateGrey"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Snow(), ColorDescription.parse("Snow"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SpringGreen(), ColorDescription.parse("SpringGreen"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.SteelBlue(), ColorDescription.parse("SteelBlue"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Tan(), ColorDescription.parse("Tan"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Teal(), ColorDescription.parse("Teal"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Thistle(), ColorDescription.parse("Thistle"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Tomato(), ColorDescription.parse("Tomato"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Turquoise(), ColorDescription.parse("Turquoise"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Violet(), ColorDescription.parse("Violet"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Wheat(), ColorDescription.parse("Wheat"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.White(), ColorDescription.parse("White"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.WhiteSmoke(), ColorDescription.parse("WhiteSmoke"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.Yellow(), ColorDescription.parse("Yellow"), EPSILON);
      assertColorDescriptionEquals(ColorDescription.YellowGreen(), ColorDescription.parse("YellowGreen"), EPSILON);

      for (String colorName : ColorDescription.namedColorLowerCaseMap.keySet())
      {
         ColorDescription expected = ColorDescription.namedColorLowerCaseMap.get(colorName);
         assertColorDescriptionEquals(expected, ColorDescription.parse(colorName.toLowerCase()), EPSILON);
         assertColorDescriptionEquals(expected, ColorDescription.parse(colorName.toLowerCase()), EPSILON);
      }
   }

   public static void assertColorDescriptionEquals(ColorDescription expectedColor, ColorDescription actualColor, double epsilon)
   {
      assertRGBAEquals(expectedColor.getRed(),
                       expectedColor.getGreen(),
                       expectedColor.getBlue(),
                       expectedColor.getAlpha(),
                       actualColor.getRed(),
                       actualColor.getGreen(),
                       actualColor.getBlue(),
                       actualColor.getAlpha(),
                       epsilon);
   }

   public static void assertHSBEquals(double expectedHue, double expectedSaturation, double expectedBrightness, double[] actualHSB, double epsilon)
   {
      assertHSBEquals(expectedHue, expectedSaturation, expectedBrightness, actualHSB[0], actualHSB[1], actualHSB[2], epsilon);
   }

   public static void assertHSBEquals(double expectedHue, double expectedSaturation, double expectedBrightness, ColorDescription actualColor, double epsilon)
   {
      assertHSBEquals(expectedHue,
                      expectedSaturation,
                      expectedBrightness,
                      actualColor.getHue(),
                      actualColor.getSaturation(),
                      actualColor.getBrightness(),
                      epsilon);
   }

   private static void assertHSBEquals(double expectedHue, double expectedSaturation, double expectedBrightness, double actualHue, double actualSaturation,
                                       double actualBrightness, double epsilon)
         throws AssertionFailedError
   {
      boolean equals = EuclidCoreTools.epsilonEquals(expectedHue, actualHue, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedSaturation, actualSaturation, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedBrightness, actualBrightness, epsilon);

      if (!equals)
      {
         String message = String.format("expected:\n\t<%s>\nwas:\n\t<%s>\ndifference:\n\t<%s>",
                                        hsbToString(expectedHue, expectedSaturation, expectedBrightness),
                                        hsbToString(actualHue, actualSaturation, actualBrightness),
                                        hsbToString(Math.abs(expectedHue - actualHue),
                                                    Math.abs(expectedSaturation - actualSaturation),
                                                    Math.abs(expectedBrightness - actualBrightness)));

         throw new AssertionFailedError(message);
      }
   }

   public static void assertHSBAEquals(double expectedHue, double expectedSaturation, double expectedBrightness, double expectedAlpha, double[] actualHSBA,
                                       double epsilon)
   {
      assertHSBAEquals(expectedHue, expectedSaturation, expectedBrightness, expectedAlpha, actualHSBA[0], actualHSBA[1], actualHSBA[2], actualHSBA[3], epsilon);
   }

   public static void assertHSBAEquals(double expectedHue, double expectedSaturation, double expectedBrightness, double expectedAlpha,
                                       ColorDescription actualColor, double epsilon)
   {
      assertHSBAEquals(expectedHue,
                       expectedSaturation,
                       expectedBrightness,
                       expectedAlpha,
                       actualColor.getHue(),
                       actualColor.getSaturation(),
                       actualColor.getBrightness(),
                       actualColor.getAlpha(),
                       epsilon);
   }

   private static void assertHSBAEquals(double expectedHue, double expectedSaturation, double expectedBrightness, double expectedAlpha, double actualHue,
                                        double actualSaturation, double actualBrightness, double actualAlpha, double epsilon)
         throws AssertionFailedError
   {
      boolean equals = EuclidCoreTools.epsilonEquals(expectedHue, actualHue, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedSaturation, actualSaturation, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedBrightness, actualBrightness, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedAlpha, actualAlpha, epsilon);

      if (!equals)
      {
         String message = String.format("expected:\n\t<%s>\nwas:\n\t<%s>\ndifference:\n\t<%s>",
                                        hsbaToString(expectedHue, expectedSaturation, expectedBrightness, expectedAlpha),
                                        hsbaToString(actualHue, actualSaturation, actualBrightness, actualAlpha),
                                        hsbaToString(Math.abs(expectedHue - actualHue),
                                                     Math.abs(expectedSaturation - actualSaturation),
                                                     Math.abs(expectedBrightness - actualBrightness),
                                                     Math.abs(expectedAlpha - actualAlpha)));

         throw new AssertionFailedError(message);
      }
   }

   public static void assertHSLEquals(double expectedHue, double expectedSaturation, double expectedLightness, double[] actualHSL, double epsilon)
   {
      assertHSLEquals(expectedHue, expectedSaturation, expectedLightness, actualHSL[0], actualHSL[1], actualHSL[2], epsilon);
   }

   public static void assertHSLEquals(double expectedHue, double expectedSaturation, double expectedLightness, ColorDescription actualColor, double epsilon)
   {
      assertHSLEquals(expectedHue, expectedSaturation, expectedLightness, actualColor.toHSLDoubleArray(), epsilon);
   }

   private static void assertHSLEquals(double expectedHue, double expectedSaturation, double expectedLightness, double actualHue, double actualSaturation,
                                       double actualLightness, double epsilon)
         throws AssertionFailedError
   {
      boolean equals = EuclidCoreTools.epsilonEquals(expectedHue, actualHue, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedSaturation, actualSaturation, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedLightness, actualLightness, epsilon);

      if (!equals)
      {
         String message = String.format("expected:\n\t<%s>\nwas:\n\t<%s>\ndifference:\n\t<%s>",
                                        hslToString(expectedHue, expectedSaturation, expectedLightness),
                                        hslToString(actualHue, actualSaturation, actualLightness),
                                        hslToString(Math.abs(expectedHue - actualHue),
                                                    Math.abs(expectedSaturation - actualSaturation),
                                                    Math.abs(expectedLightness - actualLightness)));

         throw new AssertionFailedError(message);
      }
   }

   public static void assertHSLAEquals(double expectedHue, double expectedSaturation, double expectedLightness, double expectedAlpha, double[] actualHSLA,
                                       double epsilon)
   {
      assertHSLAEquals(expectedHue, expectedSaturation, expectedLightness, expectedAlpha, actualHSLA[0], actualHSLA[1], actualHSLA[2], actualHSLA[3], epsilon);
   }

   public static void assertHSLAEquals(double expectedHue, double expectedSaturation, double expectedLightness, double expectedAlpha,
                                       ColorDescription actualColor, double epsilon)
   {
      assertHSLAEquals(expectedHue, expectedSaturation, expectedLightness, expectedAlpha, actualColor.toHSLADoubleArray(), epsilon);
   }

   private static void assertHSLAEquals(double expectedHue, double expectedSaturation, double expectedLightness, double expectedAlpha, double actualHue,
                                        double actualSaturation, double actualLightness, double actualAlpha, double epsilon)
         throws AssertionFailedError
   {
      boolean equals = EuclidCoreTools.epsilonEquals(expectedHue, actualHue, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedSaturation, actualSaturation, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedLightness, actualLightness, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedAlpha, actualAlpha, epsilon);

      if (!equals)
      {
         String message = String.format("expected:\n\t<%s>\nwas:\n\t<%s>\ndifference:\n\t<%s>",
                                        hslaToString(expectedHue, expectedSaturation, expectedLightness, expectedAlpha),
                                        hslaToString(actualHue, actualSaturation, actualLightness, actualAlpha),
                                        hslaToString(Math.abs(expectedHue - actualHue),
                                                     Math.abs(expectedSaturation - actualSaturation),
                                                     Math.abs(expectedLightness - actualLightness),
                                                     Math.abs(expectedAlpha - actualAlpha)));

         throw new AssertionFailedError(message);
      }
   }

   public static void assertRGBEquals(double expectedRed, double expectedGreen, double expectedBlue, double[] actualRGB, double epsilon)
   {
      assertRGBEquals(expectedRed, expectedGreen, expectedBlue, actualRGB[0], actualRGB[1], actualRGB[2], epsilon);
   }

   public static void assertRGBEquals(double expectedRed, double expectedGreen, double expectedBlue, ColorDescription actualColor, double epsilon)
   {
      assertRGBEquals(expectedRed, expectedGreen, expectedBlue, actualColor.getRed(), actualColor.getGreen(), actualColor.getBlue(), epsilon);
   }

   public static void assertRGBEquals(double expectedRed, double expectedGreen, double expectedBlue, double actualRed, double actualGreen, double actualBlue,
                                      double epsilon)
         throws AssertionFailedError
   {
      boolean equals = EuclidCoreTools.epsilonEquals(expectedRed, actualRed, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedGreen, actualGreen, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedBlue, actualBlue, epsilon);

      if (!equals)
      {
         String message = String.format("expected:\n\t<%s>\nwas:\n\t<%s>\ndifference:\n\t<%s>",
                                        rgbToString(expectedRed, expectedGreen, expectedBlue),
                                        rgbToString(actualRed, actualGreen, actualBlue),
                                        rgbToString(Math.abs(expectedRed - actualRed),
                                                    Math.abs(expectedGreen - actualGreen),
                                                    Math.abs(expectedBlue - actualBlue)));

         throw new AssertionFailedError(message);
      }
   }

   public static void assertRGBAEquals(double expectedRed, double expectedGreen, double expectedBlue, double expectedAlpha, double[] actualRGBA, double epsilon)
   {
      assertRGBAEquals(expectedRed, expectedGreen, expectedBlue, expectedAlpha, actualRGBA[0], actualRGBA[1], actualRGBA[2], actualRGBA[3], epsilon);
   }

   public static void assertRGBAEquals(double expectedRed, double expectedGreen, double expectedBlue, double expectedAlpha, ColorDescription actualColor,
                                       double epsilon)
   {
      assertRGBAEquals(expectedRed,
                       expectedGreen,
                       expectedBlue,
                       expectedAlpha,
                       actualColor.getRed(),
                       actualColor.getGreen(),
                       actualColor.getBlue(),
                       actualColor.getAlpha(),
                       epsilon);
   }

   public static void assertRGBAEquals(double expectedRed, double expectedGreen, double expectedBlue, double expectedAlpha, double actualRed,
                                       double actualGreen, double actualBlue, double actualAlpha, double epsilon)
         throws AssertionFailedError
   {
      boolean equals = EuclidCoreTools.epsilonEquals(expectedRed, actualRed, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedGreen, actualGreen, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedBlue, actualBlue, epsilon);
      equals &= EuclidCoreTools.epsilonEquals(expectedAlpha, actualAlpha, epsilon);

      if (!equals)
      {
         String message = String.format("expected:\n\t<%s>\nwas:\n\t<%s>\ndifference:\n\t<%s>",
                                        rgbaToString(expectedRed, expectedGreen, expectedBlue, expectedAlpha),
                                        rgbaToString(actualRed, actualGreen, actualBlue, actualAlpha),
                                        rgbaToString(Math.abs(expectedRed - actualRed),
                                                     Math.abs(expectedGreen - actualGreen),
                                                     Math.abs(expectedBlue - actualBlue),
                                                     Math.abs(expectedAlpha - actualAlpha)));

         throw new AssertionFailedError(message);
      }
   }

   public static void assertRGBEquals(int expectedRed, int expectedGreen, int expectedBlue, int[] actualRGB)
   {
      assertRGBEquals(expectedRed, expectedGreen, expectedBlue, actualRGB[0], actualRGB[1], actualRGB[2]);
   }

   public static void assertRGBEquals(int expectedRed, int expectedGreen, int expectedBlue, ColorDescription actualColor)
   {
      assertRGBEquals(expectedRed, expectedGreen, expectedBlue, actualColor.getRedAsInteger(), actualColor.getGreenAsInteger(), actualColor.getBlueAsInteger());
   }

   public static void assertRGBEquals(int expectedRed, int expectedGreen, int expectedBlue, int actualRed, int actualGreen, int actualBlue)
         throws AssertionFailedError
   {
      boolean equals = expectedRed == actualRed;
      equals &= expectedGreen == actualGreen;
      equals &= expectedBlue == actualBlue;

      if (!equals)
      {
         String message = String.format("expected:\n\t<%s>\nwas:\n\t<%s>\ndifference:\n\t<%s>",
                                        rgbToString(expectedRed, expectedGreen, expectedBlue),
                                        rgbToString(actualRed, actualGreen, actualBlue),
                                        rgbToString(Math.abs(expectedRed - actualRed),
                                                    Math.abs(expectedGreen - actualGreen),
                                                    Math.abs(expectedBlue - actualBlue)));

         throw new AssertionFailedError(message);
      }
   }

   public static void assertRGBAEquals(int expectedRed, int expectedGreen, int expectedBlue, int expectedAlpha, int[] actualRGBA)
   {
      assertRGBAEquals(expectedRed, expectedGreen, expectedBlue, expectedAlpha, actualRGBA[0], actualRGBA[1], actualRGBA[2], actualRGBA[3]);
   }

   public static void assertRGBAEquals(int expectedRed, int expectedGreen, int expectedBlue, int expectedAlpha, ColorDescription actualColor)
   {
      assertRGBAEquals(expectedRed,
                       expectedGreen,
                       expectedBlue,
                       expectedAlpha,
                       actualColor.getRedAsInteger(),
                       actualColor.getGreenAsInteger(),
                       actualColor.getBlueAsInteger(),
                       actualColor.getAlphaAsInteger());
   }

   public static void assertRGBAEquals(int expectedRed, int expectedGreen, int expectedBlue, int expectedAlpha, int actualRed, int actualGreen, int actualBlue,
                                       int actualAlpha)
         throws AssertionFailedError
   {
      boolean equals = expectedRed == actualRed;
      equals &= expectedGreen == actualGreen;
      equals &= expectedBlue == actualBlue;
      equals &= expectedAlpha == actualAlpha;

      if (!equals)
      {
         String message = String.format("expected:\n\t<%s>\nwas:\n\t<%s>\ndifference:\n\t<%s>",
                                        rgbaToString(expectedRed, expectedGreen, expectedBlue, expectedAlpha),
                                        rgbaToString(actualRed, actualGreen, actualBlue, actualAlpha),
                                        rgbaToString(Math.abs(expectedRed - actualRed),
                                                     Math.abs(expectedGreen - actualGreen),
                                                     Math.abs(expectedBlue - actualBlue),
                                                     Math.abs(expectedAlpha - actualAlpha)));

         throw new AssertionFailedError(message);
      }
   }

   private static String hsbToString(double hue, double saturation, double brightness)
   {
      return "H=" + hue + ", S=" + saturation + ", B=" + brightness;
   }

   private static String hsbaToString(double hue, double saturation, double brightness, double alpha)
   {
      return hsbToString(hue, saturation, brightness) + ", A=" + alpha;
   }

   private static String hslToString(double hue, double saturation, double lightness)
   {
      return "H=" + hue + ", S=" + saturation + ", L=" + lightness;
   }

   private static String hslaToString(double hue, double saturation, double lightness, double alpha)
   {
      return hslToString(hue, saturation, lightness) + ", A=" + alpha;
   }

   private static String rgbToString(double red, double green, double blue)
   {
      return "R=" + red + ", G=" + green + ", B=" + blue;
   }

   private static String rgbaToString(double red, double green, double blue, double alpha)
   {
      return rgbToString(red, green, blue) + ", A=" + alpha;
   }
}

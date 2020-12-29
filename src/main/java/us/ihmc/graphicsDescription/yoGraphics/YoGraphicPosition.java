package us.ihmc.graphicsDescription.yoGraphics;

import java.awt.Color;
import java.util.ArrayList;

import us.ihmc.euclid.Axis3D;
import us.ihmc.euclid.referenceFrame.FramePoint3D;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.referenceFrame.interfaces.FramePoint3DReadOnly;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DReadOnly;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.VisualDescription;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.appearance.YoAppearanceRGBColor;
import us.ihmc.graphicsDescription.yoGraphics.plotting.YoArtifactPosition;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePoint2D;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePoint3D;
import us.ihmc.yoVariables.registry.YoRegistry;
import us.ihmc.yoVariables.variable.YoDouble;
import us.ihmc.yoVariables.variable.YoVariable;

public class YoGraphicPosition extends YoGraphic implements RemoteYoGraphic
{
   public enum GraphicType
   {
      BALL, SOLID_BALL, CROSS, BALL_WITH_CROSS, ROTATED_CROSS, BALL_WITH_ROTATED_CROSS, DIAMOND, DIAMOND_WITH_CROSS, SQUARE, SQUARE_WITH_CROSS, ELLIPSOID
   }

   protected final YoDouble x, y, z;
   private final double scale;

   private final GraphicType type;
   private final AppearanceDefinition appearance;

   private final ArrayList<VisualDescription> visualDescriptions = new ArrayList<>();

   public YoGraphicPosition(String namePrefix, String nameSuffix, YoRegistry registry, double scale, AppearanceDefinition appearance)
   {
      this(namePrefix + nameSuffix, new YoFramePoint3D(namePrefix, nameSuffix, ReferenceFrame.getWorldFrame(), registry), scale, appearance);
   }

   public YoGraphicPosition(String namePrefix, String nameSuffix, YoRegistry registry, double scale, AppearanceDefinition appearance, GraphicType type)
   {
      this(namePrefix + nameSuffix, new YoFramePoint3D(namePrefix, nameSuffix, ReferenceFrame.getWorldFrame(), registry), scale, appearance, type);
   }

   public YoGraphicPosition(String name, YoDouble x, YoDouble y, YoDouble z, double scale, AppearanceDefinition appearance)
   {
      this(name, x, y, z, scale, appearance, GraphicType.BALL);
   }

   public YoGraphicPosition(String name, YoDouble x, YoDouble y, double scale, AppearanceDefinition appearance, GraphicType type)
   {
      this(name, x, y, null, scale, appearance, type);
   }

   public YoGraphicPosition(String name, YoDouble x, YoDouble y, YoDouble z, double scale, AppearanceDefinition appearance, GraphicType type)
   {
      super(name);

      this.x = x;
      this.y = y;
      this.z = z;

      this.scale = scale;
      this.type = type;
      this.appearance = appearance;
   }

   public YoGraphicPosition(String name, YoFramePoint3D framePoint, double scale, AppearanceDefinition appearance)
   {
      this(name, framePoint, scale, appearance, GraphicType.BALL);
   }

   public YoGraphicPosition(String name, YoFramePoint3D framePoint, double scale, AppearanceDefinition appearance, GraphicType type)
   {
      super(name);

      framePoint.checkReferenceFrameMatch(ReferenceFrame.getWorldFrame());

      x = framePoint.getYoX();
      y = framePoint.getYoY();
      z = framePoint.getYoZ();

      this.scale = scale;

      this.type = type;
      this.appearance = appearance;
   }

   public YoGraphicPosition(String name, YoFramePoint2D framePoint, double scale, AppearanceDefinition appearance)
   {
      this(name, framePoint, scale, appearance, GraphicType.BALL);
   }

   public YoGraphicPosition(String name, YoFramePoint2D framePoint, double scale, AppearanceDefinition appearance, GraphicType type)
   {
      super(name);

      framePoint.checkReferenceFrameMatch(ReferenceFrame.getWorldFrame());

      x = framePoint.getYoX();
      y = framePoint.getYoY();
      z = null;

      this.scale = scale;

      this.type = type;
      this.appearance = appearance;
   }

   public void setPositionToNaN()
   {
      setPosition(Double.NaN, Double.NaN, Double.NaN);
   }

   public void setPosition(FramePoint3DReadOnly position)
   {
      position.checkReferenceFrameMatch(ReferenceFrame.getWorldFrame());
      setPosition((Tuple3DReadOnly) position);
   }

   public void setPosition(Tuple3DReadOnly position)
   {
      setPosition(position.getX(), position.getY(), position.getZ());
   }

   public void getPosition(FramePoint3D positionToPack)
   {
      positionToPack.setToZero(ReferenceFrame.getWorldFrame());
      getPosition((Tuple3DBasics) positionToPack);
   }

   public void setPosition(double x, double y, double z)
   {
      this.x.set(x);
      this.y.set(y);
      if (this.z != null)
         this.z.set(z);
   }

   public void getPosition(Tuple3DBasics positionToPack)
   {
      positionToPack.setX(getX());
      positionToPack.setY(getY());
      if (z != null)
         positionToPack.setZ(getZ());
      else
         positionToPack.setZ(0.0);
   }

   public double getX()
   {
      return x.getDoubleValue();
   }

   public double getY()
   {
      return y.getDoubleValue();
   }

   public double getZ()
   {
      return z.getDoubleValue();
   }

   public void setAppearance(AppearanceDefinition appearance)
   {
      for (int i = 0; i < visualDescriptions.size(); i++)
      {
         visualDescriptions.get(i).getMaterial().setDiffuseColor(appearance.getColor());
      }
   }

   public GraphicType getType()
   {
      return type;
   }

   public double getScale()
   {
      return scale;
   }

   public Color getColor()
   {
      if (appearance instanceof YoAppearanceRGBColor)
      {
         YoAppearanceRGBColor yoAppearanceRGBColor = (YoAppearanceRGBColor) appearance;
         Color color = new Color((float) yoAppearanceRGBColor.getRed(), (float) yoAppearanceRGBColor.getGreen(), (float) yoAppearanceRGBColor.getBlue());
         return color;
      }
      else
      {
         throw new RuntimeException("No color defined");
      }
   }

   @Override
   public Graphics3DObject getLinkGraphics()
   {
      Graphics3DObject linkGraphics = new Graphics3DObject();
      linkGraphics.setChangeable(true);
      switch (type)
      {
         case BALL:
         case SOLID_BALL:
         case BALL_WITH_CROSS:
         case BALL_WITH_ROTATED_CROSS:
         {
            double radius = 1.0;
            visualDescriptions.add(linkGraphics.addSphere(radius, appearance));
            break;
         }
         case ELLIPSOID:
         {
            visualDescriptions.add(linkGraphics.addEllipsoid(0.50, 1.0, 0.20, appearance));
            break;
         }
         case CROSS:
         {
            double R = 1.0, r = 0.16;

            visualDescriptions.add(linkGraphics.addArcTorus(0.0, 2.0 * Math.PI, R, r, appearance));

            linkGraphics.appendRotation(Math.PI / 2.0, Axis3D.X);
            linkGraphics.appendTranslation(0.0, 0.0, -R);
            visualDescriptions.add(linkGraphics.addCylinder(2.0 * R, r, appearance));

            linkGraphics.identity();
            linkGraphics.appendRotation(Math.PI / 2.0, Axis3D.Y);
            linkGraphics.appendTranslation(0.0, 0.0, -R);
            visualDescriptions.add(linkGraphics.addCylinder(2.0 * R, r, appearance));
            break;
         }

         case ROTATED_CROSS:
         {

            double R = 1.0, r = 0.16;

            visualDescriptions.add(linkGraphics.addArcTorus(0.0, 2.0 * Math.PI, R, r, appearance));

            linkGraphics.appendRotation(Math.PI / 4.0, Axis3D.Z);
            linkGraphics.appendRotation(Math.PI / 2.0, Axis3D.X);
            linkGraphics.appendTranslation(0.0, 0.0, -R);
            visualDescriptions.add(linkGraphics.addCylinder(2.0 * R, r, appearance));

            linkGraphics.identity();
            linkGraphics.appendRotation(Math.PI / 4.0, Axis3D.Z);
            linkGraphics.appendRotation(Math.PI / 2.0, Axis3D.Y);
            linkGraphics.appendTranslation(0.0, 0.0, -R);
            visualDescriptions.add(linkGraphics.addCylinder(2.0 * R, r, appearance));
            break;
         }
         default:
            throw new RuntimeException("Shouldn't get here. Seems you set an invalid YoGraphicPosition type! type = " + type);
      }

      return linkGraphics;
   }

   private Vector3D translationVector = new Vector3D();

   @Override
   protected void computeRotationTranslation(AffineTransform transform3D)
   {
      transform3D.setIdentity();

      if (Double.isNaN(x.getDoubleValue()) || Double.isNaN(y.getDoubleValue()) || z != null && Double.isNaN(z.getDoubleValue()))
      {
         translationVector.set(-1000.0, -1000.0, -1000.0);
         transform3D.setTranslation(translationVector);
      }
      else
      {
         if (z != null)
            translationVector.set(x.getDoubleValue(), y.getDoubleValue(), z.getDoubleValue());
         else
            translationVector.set(x.getDoubleValue(), y.getDoubleValue(), 0.0);

         double globalScale = 1.0;
         if (globalScaleProvider != null)
         {
            globalScale = globalScaleProvider.getValue();
         }

         transform3D.appendScale(scale * globalScale);
         transform3D.setTranslation(translationVector);
      }
   }

   @Override
   public YoArtifactPosition createArtifact()
   {
      return new YoArtifactPosition(getName(), x, y, type, getColor(), scale);
   }

   @Override
   protected boolean containsNaN()
   {
      if (x.isNaN())
         return true;
      if (y.isNaN())
         return true;
      if (z != null && z.isNaN())
         return true;

      return false;
   }

   @Override
   public YoVariable[] getVariables()
   {
      if (z != null)
      {
         return new YoDouble[] {x, y, z};
      }
      else
      {
         return new YoDouble[] {x, y};
      }
   }

   @Override
   public double[] getConstants()
   {
      return new double[] {scale, type.ordinal()};
   }

   @Override
   public AppearanceDefinition getAppearance()
   {
      return appearance;
   }

   public YoVariable getYoX()
   {
      return x;
   }

   public YoVariable getYoY()
   {
      return y;
   }

   @Override
   public YoGraphicPosition duplicate(YoRegistry newRegistry)
   {
      YoDouble x = (YoDouble) newRegistry.findVariable(this.x.getFullNameString());
      YoDouble y = (YoDouble) newRegistry.findVariable(this.y.getFullNameString());
      YoDouble z = (YoDouble) newRegistry.findVariable(this.z.getFullNameString());
      return new YoGraphicPosition(getName(), x, y, z, scale, appearance, type);
   }
}

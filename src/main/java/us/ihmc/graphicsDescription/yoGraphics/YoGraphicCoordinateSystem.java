package us.ihmc.graphicsDescription.yoGraphics;

import java.awt.Color;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.referenceFrame.interfaces.FramePoint3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FramePose3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameQuaternionReadOnly;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DReadOnly;
import us.ihmc.euclid.tuple4D.interfaces.QuaternionBasics;
import us.ihmc.euclid.tuple4D.interfaces.QuaternionReadOnly;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.appearance.YoAppearance;
import us.ihmc.graphicsDescription.appearance.YoAppearanceRGBColor;
import us.ihmc.graphicsDescription.plotting.artifact.Artifact;
import us.ihmc.yoVariables.euclid.referenceFrame.*;
import us.ihmc.yoVariables.registry.YoRegistry;
import us.ihmc.yoVariables.variable.YoDouble;
import us.ihmc.yoVariables.variable.YoVariable;

public class YoGraphicCoordinateSystem extends YoGraphic implements RemoteYoGraphic
{
   protected static final AppearanceDefinition DEFAULT_APPEARANCE = YoAppearance.Gray();
   private static final ReferenceFrame worldFrame = ReferenceFrame.getWorldFrame();

   protected final YoFramePoint3D position;
   protected final YoFrameYawPitchRoll yawPitchRoll;
   protected final YoFrameQuaternion quaternion;
   protected final double scale;
   protected AppearanceDefinition arrowColor = DEFAULT_APPEARANCE;
   private double colorRGB32BitInt = arrowColor.getAwtColor().getRGB();
   private double transparency = arrowColor.getTransparency();

   public YoGraphicCoordinateSystem(String namePrefix, String nameSuffix, YoRegistry registry, boolean useYawPitchRoll, double scale)
   {
      this(namePrefix, nameSuffix, registry, useYawPitchRoll, scale, DEFAULT_APPEARANCE);
   }

   public YoGraphicCoordinateSystem(String namePrefix, String nameSuffix, YoRegistry registry, boolean useYawPitchRoll, double scale,
                                    AppearanceDefinition arrowAppearance)
   {
      this(namePrefix + nameSuffix, new YoFramePoint3D(namePrefix, nameSuffix, worldFrame, registry),
           useYawPitchRoll ? new YoFrameYawPitchRoll(namePrefix, nameSuffix, worldFrame, registry) : null,
           useYawPitchRoll ? null : new YoFrameQuaternion(namePrefix, nameSuffix, worldFrame, registry), scale, arrowAppearance);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePose3D yoFramePose, double scale)
   {
      this(name, yoFramePose, scale, DEFAULT_APPEARANCE);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePose3D yoFramePose, double scale, AppearanceDefinition arrowAppearance)
   {
      this(name, yoFramePose.getPosition(), yoFramePose.getOrientation(), scale, arrowAppearance);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoint3D framePoint, YoFrameQuaternion orientation, double scale)
   {
      this(name, framePoint, orientation, scale, DEFAULT_APPEARANCE);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoint3D framePoint, YoFrameQuaternion orientation, double scale, AppearanceDefinition arrowAppearance)
   {
      this(name, framePoint, null, orientation, scale, arrowAppearance);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoseUsingYawPitchRoll yoFramePose, double scale)
   {
      this(name, yoFramePose, scale, DEFAULT_APPEARANCE);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoseUsingYawPitchRoll yoFramePose, double scale, AppearanceDefinition arrowAppearance)
   {
      this(name, yoFramePose.getPosition(), yoFramePose.getYawPitchRoll(), scale, arrowAppearance);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoint3D framePoint, YoFrameYawPitchRoll orientation, double scale)
   {
      this(name, framePoint, orientation, scale, DEFAULT_APPEARANCE);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoint3D framePoint, YoFrameYawPitchRoll orientation, double scale, AppearanceDefinition arrowAppearance)
   {
      this(name, framePoint, orientation, null, scale, arrowAppearance);
   }

   protected YoGraphicCoordinateSystem(String name, YoFramePoint3D position, YoFrameYawPitchRoll yawPitchRoll, YoFrameQuaternion quaternion, double scale,
                                       AppearanceDefinition arrowAppearance)
   {
      super(name);

      worldFrame.checkReferenceFrameMatch(position);
      if (yawPitchRoll == null && quaternion == null || yawPitchRoll != null && quaternion != null)
         throw new IllegalArgumentException("Can only describe the orientation of this shape with either yaw-pitch-roll or quaternion.");

      if (yawPitchRoll != null)
         worldFrame.checkReferenceFrameMatch(yawPitchRoll);
      if (quaternion != null)
         worldFrame.checkReferenceFrameMatch(quaternion);

      this.position = position;
      this.yawPitchRoll = yawPitchRoll;
      this.quaternion = quaternion;

      this.scale = scale;
      setArrowColor(arrowAppearance);
   }

   static YoGraphicCoordinateSystem createAsRemoteYoGraphic(String name, YoVariable[] yoVariables, double[] constants)
   {
      return new YoGraphicCoordinateSystem(name, yoVariables, constants);
   }

   /**
    * This constructor for creating a remote {@code YoGraphic} from deserialized {@code YoVariable}s.
    * The expected number of {@code YoVariable}s is either 6 or 7 depending on whether the original
    * {@code YoGraphic} was created using {@code YoFrameYawPitchRoll} or {@code YoFrameQuaternion}
    * respectively.
    *
    * @param name        the name of the {@code YoGraphic}.
    * @param yoVariables the deserialized variables.
    * @param scale       the scale to apply on the graphics.
    */
   protected YoGraphicCoordinateSystem(String name, YoVariable[] yoVariables, double[] constants)
   {
      super(name);

      int yoIndex = 0;
      YoDouble x = (YoDouble) yoVariables[yoIndex++];
      YoDouble y = (YoDouble) yoVariables[yoIndex++];
      YoDouble z = (YoDouble) yoVariables[yoIndex++];
      position = new YoFramePoint3D(x, y, z, worldFrame);

      if (yoVariables.length == 6)
      {
         YoDouble yaw = (YoDouble) yoVariables[yoIndex++];
         YoDouble pitch = (YoDouble) yoVariables[yoIndex++];
         YoDouble roll = (YoDouble) yoVariables[yoIndex++];
         yawPitchRoll = new YoFrameYawPitchRoll(yaw, pitch, roll, worldFrame);
         quaternion = null;
      }
      else if (yoVariables.length == 7)
      {
         YoDouble qx = (YoDouble) yoVariables[yoIndex++];
         YoDouble qy = (YoDouble) yoVariables[yoIndex++];
         YoDouble qz = (YoDouble) yoVariables[yoIndex++];
         YoDouble qs = (YoDouble) yoVariables[yoIndex++];
         yawPitchRoll = null;
         quaternion = new YoFrameQuaternion(qx, qy, qz, qs, worldFrame);
      }
      else
      {
         throw new RuntimeException("Unexpected number of YoVariables. Expected either 6 or 7 but had: " + yoVariables.length);
      }

      scale = constants[0];
      // Ensuring backward compatibility
      if (constants.length == 3)
         setArrowColor(new YoAppearanceRGBColor(new Color((int) constants[1]), constants[2]));
   }

   private final RigidBodyTransform transformToWorld = new RigidBodyTransform();

   public void setToReferenceFrame(ReferenceFrame referenceFrame)
   {
      if (referenceFrame == null)
      {
         throw new RuntimeException("referenceFrame == null");
      }

      referenceFrame.getTransformToDesiredFrame(transformToWorld, worldFrame);
      setTransformToWorld(transformToWorld);
   }

   public void setTransformToWorld(RigidBodyTransform transformToWorld)
   {
      position.set(transformToWorld.getTranslation());
      if (isUsingYawPitchRoll())
         yawPitchRoll.set(transformToWorld.getRotation());
      else
         quaternion.set(transformToWorld.getRotation());
   }

   public double getScale()
   {
      return scale;
   }

   public void getPosition(Tuple3DBasics position)
   {
      position.set(this.position);
   }

   public void getOrientation(QuaternionBasics quaternionToPack)
   {
      if (isUsingYawPitchRoll())
         quaternionToPack.set(yawPitchRoll);
      else
         quaternionToPack.set(quaternion);
   }

   public void setPosition(double x, double y, double z)
   {
      position.set(x, y, z);
   }

   public void setPosition(Tuple3DReadOnly position)
   {
      this.position.set(position);
   }

   public void setPosition(FramePoint3DReadOnly position)
   {
      this.position.set(position);
   }

   public void setOrientation(QuaternionReadOnly orientation)
   {
      if (isUsingYawPitchRoll())
         yawPitchRoll.set(orientation);
      else
         quaternion.set(orientation);
   }

   public void setOrientation(FrameQuaternionReadOnly orientation)
   {
      if (isUsingYawPitchRoll())
         yawPitchRoll.set(orientation);
      else
         quaternion.set(orientation);
   }

   public void setYawPitchRoll(double yaw, double pitch, double roll)
   {
      if (isUsingYawPitchRoll())
         yawPitchRoll.setYawPitchRoll(yaw, pitch, roll);
      else
         quaternion.setYawPitchRoll(yaw, pitch, roll);
   }

   public void setPose(FramePose3DReadOnly pose)
   {
      setPosition(pose.getPosition());
      setOrientation(pose.getOrientation());
   }

   public void setArrowColor(AppearanceDefinition arrowColor)
   {
      this.arrowColor = arrowColor;
      colorRGB32BitInt = arrowColor.getAwtColor().getRGB();
      transparency = arrowColor.getTransparency();
   }

   public void hide()
   {
      position.setToNaN();
      if (isUsingYawPitchRoll())
         yawPitchRoll.setToNaN();
      else
         quaternion.setToNaN();
   }

   @Override
   public Graphics3DObject getLinkGraphics()
   {
      Graphics3DObject linkGraphics = new Graphics3DObject();

      linkGraphics.addCoordinateSystem(1.0, arrowColor);

      return linkGraphics;
   }

   @Override
   protected void computeRotationTranslation(AffineTransform transform3D)
   {
      transform3D.setIdentity();

      double globalScale = 1.0;
      if (globalScaleProvider != null)
      {
         globalScale = globalScaleProvider.getValue();
      }

      if (isUsingYawPitchRoll())
         transform3D.setLinearTransform(yawPitchRoll);
      else
         transform3D.setLinearTransform(quaternion);
      transform3D.appendScale(scale * globalScale);
      transform3D.setTranslation(position);
   }

   @Override
   public boolean containsNaN()
   {
      if (position.containsNaN())
         return true;

      if (isUsingYawPitchRoll())
         return yawPitchRoll.containsNaN();
      else
         return quaternion.containsNaN();
   }

   @Override
   public Artifact createArtifact()
   {
      throw new RuntimeException("Implement Me!");
   }

   @Override
   public YoDouble[] getVariables()
   {
      YoDouble[] vars = new YoDouble[isUsingYawPitchRoll() ? 6 : 7];
      int i = 0;

      vars[i++] = position.getYoX();
      vars[i++] = position.getYoY();
      vars[i++] = position.getYoZ();

      if (isUsingYawPitchRoll())
      {
         vars[i++] = yawPitchRoll.getYoYaw();
         vars[i++] = yawPitchRoll.getYoPitch();
         vars[i++] = yawPitchRoll.getYoRoll();
      }
      else
      {
         vars[i++] = quaternion.getYoQx();
         vars[i++] = quaternion.getYoQy();
         vars[i++] = quaternion.getYoQz();
         vars[i++] = quaternion.getYoQs();
      }

      return vars;
   }

   @Override
   public double[] getConstants()
   {
      return new double[] {scale, colorRGB32BitInt, transparency};
   }

   @Override
   public YoGraphicCoordinateSystem duplicate(YoRegistry newRegistry)
   {
      if (isUsingYawPitchRoll())
         return new YoGraphicCoordinateSystem(getName(), position.duplicate(newRegistry), yawPitchRoll.duplicate(newRegistry), scale, arrowColor);
      else
         return new YoGraphicCoordinateSystem(getName(), position.duplicate(newRegistry), quaternion.duplicate(newRegistry), scale, arrowColor);
   }

   @Override
   public AppearanceDefinition getAppearance()
   {
      return arrowColor;
   }

   public boolean isUsingYawPitchRoll()
   {
      return yawPitchRoll != null;
   }
}

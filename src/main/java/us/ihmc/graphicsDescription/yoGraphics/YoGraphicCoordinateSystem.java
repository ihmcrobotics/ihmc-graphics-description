package us.ihmc.graphicsDescription.yoGraphics;

import java.awt.Color;

import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.referenceFrame.interfaces.FramePoint3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FramePose3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameQuaternionReadOnly;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DBasics;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.appearance.AppearanceDefinition;
import us.ihmc.graphicsDescription.appearance.YoAppearance;
import us.ihmc.graphicsDescription.appearance.YoAppearanceRGBColor;
import us.ihmc.graphicsDescription.plotting.artifact.Artifact;
import us.ihmc.yoVariables.registry.YoVariableRegistry;
import us.ihmc.yoVariables.variable.YoDouble;
import us.ihmc.yoVariables.variable.YoFramePoint3D;
import us.ihmc.yoVariables.variable.YoFramePoseUsingYawPitchRoll;
import us.ihmc.yoVariables.variable.YoFrameYawPitchRoll;
import us.ihmc.yoVariables.variable.YoVariable;

public class YoGraphicCoordinateSystem extends YoGraphic implements RemoteYoGraphic
{
   private static final ReferenceFrame worldFrame = ReferenceFrame.getWorldFrame();
   protected final YoFramePoint3D position;
   protected final YoFrameYawPitchRoll yawPitchRoll;
   protected final double scale;
   protected AppearanceDefinition arrowColor = YoAppearance.Gray();
   private double colorRGB32BitInt = arrowColor.getAwtColor().getRGB();
   private double transparency = arrowColor.getTransparency();

   public YoGraphicCoordinateSystem(String name, YoFramePoint3D framePoint, YoFrameYawPitchRoll orientation, double scale)
   {
      super(name);

      worldFrame.checkReferenceFrameMatch(framePoint);
      framePoint.checkReferenceFrameMatch(orientation.getReferenceFrame());

      position = framePoint;
      yawPitchRoll = orientation;

      this.scale = scale;
   }

   public YoGraphicCoordinateSystem(String namePrefix, String nameSuffix, YoVariableRegistry registry, double scale)
   {
      this(namePrefix, nameSuffix, registry, scale, YoAppearance.Gray());
   }

   public YoGraphicCoordinateSystem(String namePrefix, String nameSuffix, YoVariableRegistry registry, double scale, AppearanceDefinition arrowColor)
   {
      this(namePrefix + nameSuffix, new YoFramePoint3D(namePrefix, nameSuffix, worldFrame, registry),
           new YoFrameYawPitchRoll(namePrefix, nameSuffix, worldFrame, registry), scale);
      setArrowColor(arrowColor);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoint3D framePoint, YoFrameYawPitchRoll orientation, double scale, AppearanceDefinition arrowColor)
   {
      this(name, framePoint, orientation, scale);
      setArrowColor(arrowColor);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoseUsingYawPitchRoll yoFramePose, double scale)
   {
      this(name, yoFramePose.getPosition(), yoFramePose.getOrientation(), scale);
   }

   public YoGraphicCoordinateSystem(String name, YoFramePoseUsingYawPitchRoll yoFramePose, double scale, AppearanceDefinition arrowColor)
   {
      this(name, yoFramePose.getPosition(), yoFramePose.getOrientation(), scale, arrowColor);
   }

   static YoGraphicCoordinateSystem createAsRemoteYoGraphic(String name, YoVariable<?>[] yoVariables, double[] constants)
   {
      return new YoGraphicCoordinateSystem(name, yoVariables, constants);
   }

   protected YoGraphicCoordinateSystem(String name, YoVariable<?>[] yoVariables, double[] constants)
   {
      super(name);

      int yoIndex = 0;
      YoDouble x = (YoDouble) yoVariables[yoIndex++];
      YoDouble y = (YoDouble) yoVariables[yoIndex++];
      YoDouble z = (YoDouble) yoVariables[yoIndex++];
      YoDouble yaw = (YoDouble) yoVariables[yoIndex++];
      YoDouble pitch = (YoDouble) yoVariables[yoIndex++];
      YoDouble roll = (YoDouble) yoVariables[yoIndex++];
      position = new YoFramePoint3D(x, y, z, worldFrame);
      yawPitchRoll = new YoFrameYawPitchRoll(yaw, pitch, roll, worldFrame);

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
      position.set(transformToWorld.getTranslationVector());
      yawPitchRoll.set(transformToWorld.getRotationMatrix());
   }

   public double getScale()
   {
      return scale;
   }

   public void getPosition(Tuple3DBasics position)
   {
      position.set(this.position);
   }

   public void getYawPitchRoll(double[] yawPitchRollToPack)
   {
      yawPitchRoll.getYawPitchRoll(yawPitchRollToPack);
   }

   public void setPosition(double x, double y, double z)
   {
      position.set(x, y, z);
   }

   public void setPosition(FramePoint3DReadOnly position)
   {
      this.position.set(position);
   }

   public void setOrientation(FrameQuaternionReadOnly orientation)
   {
      yawPitchRoll.set(orientation);
   }

   public void setYawPitchRoll(double[] yawPitchRoll)
   {
      this.yawPitchRoll.setYawPitchRoll(yawPitchRoll);
   }

   public void setYawPitchRoll(double yaw, double pitch, double roll)
   {
      yawPitchRoll.setYawPitchRoll(yaw, pitch, roll);
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
      yawPitchRoll.setToNaN();
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

      transform3D.setScale(scale * globalScale);
      transform3D.setRotationYawPitchRoll(yawPitchRoll.getYaw().getValue(), yawPitchRoll.getPitch().getValue(), yawPitchRoll.getRoll().getValue());
      transform3D.setTranslation(position);
   }

   @Override
   public boolean containsNaN()
   {
      return position.containsNaN() && yawPitchRoll.containsNaN();
   }

   @Override
   public Artifact createArtifact()
   {
      throw new RuntimeException("Implement Me!");
   }

   @Override
   public YoDouble[] getVariables()
   {
      YoDouble[] vars = new YoDouble[6];
      int i = 0;

      vars[i++] = position.getYoX();
      vars[i++] = position.getYoY();
      vars[i++] = position.getYoZ();

      vars[i++] = yawPitchRoll.getYaw();
      vars[i++] = yawPitchRoll.getPitch();
      vars[i++] = yawPitchRoll.getRoll();

      return vars;
   }

   @Override
   public double[] getConstants()
   {
      return new double[] {scale, colorRGB32BitInt, transparency};
   }

   @Override
   public YoGraphicCoordinateSystem duplicate(YoVariableRegistry newRegistry)
   {
      return new YoGraphicCoordinateSystem(getName(), position.duplicate(newRegistry), yawPitchRoll.duplicate(newRegistry), scale, arrowColor);
   }

   @Override
   public AppearanceDefinition getAppearance()
   {
      return arrowColor;
   }
}

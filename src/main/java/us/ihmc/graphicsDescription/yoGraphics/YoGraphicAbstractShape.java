package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.referenceFrame.FramePose3D;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.referenceFrame.interfaces.FramePoint3DBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FramePoint3DReadOnly;
import us.ihmc.euclid.referenceFrame.interfaces.FrameQuaternionBasics;
import us.ihmc.euclid.referenceFrame.interfaces.FrameQuaternionReadOnly;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.transform.interfaces.RigidBodyTransformReadOnly;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.graphicsDescription.plotting.artifact.Artifact;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFramePoint3D;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFrameQuaternion;
import us.ihmc.yoVariables.euclid.referenceFrame.YoFrameYawPitchRoll;
import us.ihmc.yoVariables.variable.YoDouble;
import us.ihmc.yoVariables.variable.YoVariable;

public abstract class YoGraphicAbstractShape extends YoGraphic
{
   private static final ReferenceFrame worldFrame = ReferenceFrame.getWorldFrame();
   protected final YoFramePoint3D yoFramePoint;
   protected final YoFrameYawPitchRoll yoFrameYawPitchRoll;
   protected final YoFrameQuaternion yoFrameQuaternion;
   protected final double scale;

   protected YoGraphicAbstractShape(String name, YoFramePoint3D framePoint, YoFrameQuaternion frameOrientation, double scale)
   {
      this(name, framePoint, null, frameOrientation, scale);
   }

   protected YoGraphicAbstractShape(String name, YoFramePoint3D framePoint, YoFrameYawPitchRoll frameOrientation, double scale)
   {
      this(name, framePoint, frameOrientation, null, scale);
   }

   protected YoGraphicAbstractShape(String name, YoFramePoint3D framePoint, YoFrameYawPitchRoll frameYawPitchRoll, YoFrameQuaternion frameQuaternion,
                                    double scale)
   {
      super(name);
      framePoint.checkReferenceFrameMatch(worldFrame);

      if (frameYawPitchRoll == null && frameQuaternion == null || frameYawPitchRoll != null && frameQuaternion != null)
         throw new IllegalArgumentException("Can only describe the orientation of this shape with either yaw-pitch-roll or quaternion.");

      if (frameYawPitchRoll != null)
         frameYawPitchRoll.checkReferenceFrameMatch(worldFrame);
      if (frameQuaternion != null)
         frameQuaternion.checkReferenceFrameMatch(worldFrame);

      yoFramePoint = framePoint;
      yoFrameYawPitchRoll = frameYawPitchRoll;
      yoFrameQuaternion = frameQuaternion;

      this.scale = scale;
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
   protected YoGraphicAbstractShape(String name, YoVariable[] yoVariables, double scale)
   {
      super(name);

      int yoIndex = 0;
      YoDouble x = (YoDouble) yoVariables[yoIndex++];
      YoDouble y = (YoDouble) yoVariables[yoIndex++];
      YoDouble z = (YoDouble) yoVariables[yoIndex++];
      yoFramePoint = new YoFramePoint3D(x, y, z, worldFrame);

      if (yoVariables.length == 6)
      {
         YoDouble yaw = (YoDouble) yoVariables[yoIndex++];
         YoDouble pitch = (YoDouble) yoVariables[yoIndex++];
         YoDouble roll = (YoDouble) yoVariables[yoIndex++];
         yoFrameYawPitchRoll = new YoFrameYawPitchRoll(yaw, pitch, roll, worldFrame);
         yoFrameQuaternion = null;
      }
      else if (yoVariables.length == 7)
      {
         YoDouble qx = (YoDouble) yoVariables[yoIndex++];
         YoDouble qy = (YoDouble) yoVariables[yoIndex++];
         YoDouble qz = (YoDouble) yoVariables[yoIndex++];
         YoDouble qs = (YoDouble) yoVariables[yoIndex++];
         yoFrameYawPitchRoll = null;
         yoFrameQuaternion = new YoFrameQuaternion(qx, qy, qz, qs, worldFrame);
      }
      else
      {
         throw new RuntimeException("Unexpected number of YoVariables. Expected either 6 or 7 but had: " + yoVariables.length);
      }

      this.scale = scale;
   }

   public void setPose(FramePose3D framePose)
   {
      yoFramePoint.checkReferenceFrameMatch(framePose.getReferenceFrame());
      yoFramePoint.set(framePose.getPosition());
      if (isUsingYawPitchRoll())
         yoFrameYawPitchRoll.set(framePose.getOrientation());
      else
         yoFrameQuaternion.set(framePose.getOrientation());
   }

   public void setPosition(double x, double y, double z)
   {
      yoFramePoint.set(x, y, z);
   }

   public void getPosition(FramePoint3DBasics framePointToPack)
   {
      framePointToPack.setIncludingFrame(yoFramePoint);
   }

   public void setPosition(FramePoint3DReadOnly position)
   {
      yoFramePoint.set(position);
   }

   public void getOrientation(FrameQuaternionBasics orientationToPack)
   {
      if (isUsingYawPitchRoll())
         orientationToPack.setIncludingFrame(yoFrameYawPitchRoll);
      else
         orientationToPack.setIncludingFrame(yoFrameQuaternion);
   }

   public void setOrientation(FrameQuaternionReadOnly orientation)
   {
      if (isUsingYawPitchRoll())
         yoFrameYawPitchRoll.set(orientation);
      else
         yoFrameQuaternion.set(orientation);
   }

   public void setYawPitchRoll(double yaw, double pitch, double roll)
   {
      if (isUsingYawPitchRoll())
         yoFrameYawPitchRoll.setYawPitchRoll(yaw, pitch, roll);
      else
         yoFrameQuaternion.setYawPitchRoll(yaw, pitch, roll);
   }

   public void setTransformToWorld(RigidBodyTransformReadOnly transformToWorld)
   {
      yoFramePoint.set(transformToWorld.getTranslation());
      yoFrameYawPitchRoll.set(transformToWorld.getRotation());
   }

   public void setToReferenceFrame(ReferenceFrame referenceFrame)
   {
      if (referenceFrame == null)
         throw new RuntimeException("referenceFrame == null");

      RigidBodyTransform transformToWorld = new RigidBodyTransform();
      ReferenceFrame ancestorFrame = referenceFrame;

      // March up the parents until you get to the world:
      while (!ancestorFrame.isWorldFrame())
      {
         RigidBodyTransform transformToAncestor = ancestorFrame.getTransformToParent();

         RigidBodyTransform tempTransform3D = new RigidBodyTransform(transformToAncestor);
         tempTransform3D.multiply(transformToWorld);

         transformToWorld = tempTransform3D;

         ReferenceFrame newAncestorFrame = ancestorFrame.getParent();

         if (newAncestorFrame == null)
            throw new RuntimeException("No ancestor path to world. referenceFrame = " + referenceFrame + ", most ancient = " + ancestorFrame);

         ancestorFrame = newAncestorFrame;
      }

      setTransformToWorld(transformToWorld);
   }

   private Vector3D rotationEulerVector = new Vector3D();

   @Override
   protected void computeRotationTranslation(AffineTransform transform3D)
   {
      transform3D.setIdentity();
      if (isUsingYawPitchRoll())
      {
         yoFrameYawPitchRoll.getEuler(rotationEulerVector);
         transform3D.getLinearTransform().setEuler(rotationEulerVector);
      }
      else
      {
         transform3D.setLinearTransform(yoFrameQuaternion);
      }
      transform3D.setTranslation(yoFramePoint);
      transform3D.appendScale(scale);
   }

   @Override
   public Artifact createArtifact()
   {
      throw new RuntimeException("Implement Me!");
   }

   public void setPoseToNaN()
   {
      yoFramePoint.setToNaN();
      if (isUsingYawPitchRoll())
         yoFrameYawPitchRoll.setToNaN();
      else
         yoFrameQuaternion.setToNaN();
   }

   @Override
   protected boolean containsNaN()
   {
      if (yoFramePoint.containsNaN())
         return true;
      if (isUsingYawPitchRoll() ? yoFrameYawPitchRoll.containsNaN() : yoFrameQuaternion.containsNaN())
         return true;

      return false;
   }

   public YoVariable[] getVariables()
   {
      YoVariable[] vars = new YoVariable[isUsingYawPitchRoll() ? 6 : 7];
      int i = 0;

      vars[i++] = yoFramePoint.getYoX();
      vars[i++] = yoFramePoint.getYoY();
      vars[i++] = yoFramePoint.getYoZ();

      if (isUsingYawPitchRoll())
      {
         vars[i++] = yoFrameYawPitchRoll.getYoYaw();
         vars[i++] = yoFrameYawPitchRoll.getYoPitch();
         vars[i++] = yoFrameYawPitchRoll.getYoRoll();
      }
      else
      {
         vars[i++] = yoFrameQuaternion.getYoQx();
         vars[i++] = yoFrameQuaternion.getYoQy();
         vars[i++] = yoFrameQuaternion.getYoQz();
         vars[i++] = yoFrameQuaternion.getYoQs();
      }

      return vars;
   }

   public boolean isUsingYawPitchRoll()
   {
      return yoFrameYawPitchRoll != null;
   }
}
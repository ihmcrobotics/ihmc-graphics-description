package us.ihmc.graphicsDescription.yoGraphics;

import us.ihmc.euclid.referenceFrame.FramePoint3D;
import us.ihmc.euclid.referenceFrame.FramePose3D;
import us.ihmc.euclid.referenceFrame.FrameQuaternion;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.graphicsDescription.plotting.artifact.Artifact;
import us.ihmc.yoVariables.variable.YoFramePoint3D;
import us.ihmc.yoVariables.variable.YoFrameYawPitchRoll;

public abstract class YoGraphicAbstractShape extends YoGraphic
{
   protected final YoFramePoint3D yoFramePoint;
   protected final YoFrameYawPitchRoll yoFrameOrientation;
   protected final double scale;
   private final Vector3D translationVector = new Vector3D();

   protected YoGraphicAbstractShape(String name, YoFramePoint3D framePoint, YoFrameYawPitchRoll frameOrientation, double scale)
   {
      super(name);
      framePoint.checkReferenceFrameMatch(ReferenceFrame.getWorldFrame());

      this.yoFramePoint = framePoint;
      this.yoFrameOrientation = frameOrientation;

      this.scale = scale;
   }

   public void setPose(FramePose3D framePose)
   {
      yoFramePoint.checkReferenceFrameMatch(framePose.getReferenceFrame());
      yoFramePoint.set(framePose.getPosition());
      yoFrameOrientation.set(framePose.getOrientation());
   }

   public void setPosition(double x, double y, double z)
   {
      yoFramePoint.set(x, y, z);
   }

   public void getPosition(FramePoint3D framePointToPack)
   {
      framePointToPack.set(yoFramePoint);
   }

   public void setPosition(FramePoint3D position)
   {
      yoFramePoint.set(position);
   }

   public void getOrientation(FrameQuaternion orientationToPack)
   {
      this.yoFrameOrientation.getFrameOrientationIncludingFrame(orientationToPack);
   }

   public void setOrientation(FrameQuaternion orientation)
   {
      this.yoFrameOrientation.set(orientation);
   }

   public void setYawPitchRoll(double yaw, double pitch, double roll)
   {
      this.yoFrameOrientation.setYawPitchRoll(yaw, pitch, roll);
   }

   public void setTransformToWorld(RigidBodyTransform transformToWorld)
   {
      Vector3D translationToWorld = new Vector3D();

      transformToWorld.getTranslation(translationToWorld);

      this.yoFramePoint.set(translationToWorld);
      FrameQuaternion orientation = new FrameQuaternion(ReferenceFrame.getWorldFrame(), transformToWorld.getRotationMatrix());

      double[] yawPitchRoll = new double[3];
      orientation.getYawPitchRoll(yawPitchRoll);
      yoFrameOrientation.setYawPitchRoll(yawPitchRoll[0], yawPitchRoll[1], yawPitchRoll[2]);
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
      translationVector.set(yoFramePoint.getX(), yoFramePoint.getY(), yoFramePoint.getZ());
      yoFrameOrientation.getEulerAngles(rotationEulerVector);

      transform3D.setRotationEuler(rotationEulerVector);
      transform3D.setTranslation(translationVector);
      transform3D.setScale(scale);
   }

   @Override
   public Artifact createArtifact()
   {
      throw new RuntimeException("Implement Me!");
   }

   public void setPoseToNaN()
   {
      yoFramePoint.setToNaN();
      yoFrameOrientation.setToNaN();
   }

   @Override
   protected boolean containsNaN()
   {
      if (yoFramePoint.containsNaN())
         return true;
      if (yoFrameOrientation.containsNaN())
         return true;

      return false;
   }
}
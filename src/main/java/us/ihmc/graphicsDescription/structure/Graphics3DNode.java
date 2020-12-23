package us.ihmc.graphicsDescription.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import us.ihmc.euclid.Axis3D;
import us.ihmc.euclid.transform.AffineTransform;
import us.ihmc.euclid.transform.interfaces.RigidBodyTransformReadOnly;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Tuple3DReadOnly;
import us.ihmc.euclid.tuple4D.interfaces.QuaternionReadOnly;
import us.ihmc.graphicsDescription.Graphics3DObject;
import us.ihmc.graphicsDescription.input.SelectedListener;
import us.ihmc.tools.inputDevices.keyboard.ModifierKeyInterface;

public class Graphics3DNode
{
   private final String name;
   private final AffineTransform transform = new AffineTransform();

   private Graphics3DObject graphicsObject;
   private boolean hasGraphicsObjectChanged = false;

   private final ArrayList<Graphics3DNode> children = new ArrayList<>();
   private final ArrayList<SelectedListener> selectedListeners = new ArrayList<>();

   public Graphics3DNode(String name)
   {
      this(name, null);
   }

   public Graphics3DNode(String name, Graphics3DObject graphicsObject)
   {
      this.name = name;

      if (graphicsObject != null)
         setGraphicsObject(graphicsObject);
   }

   public synchronized AffineTransform getTransform()
   {
      return transform;
   }

   public synchronized void setTransform(RigidBodyTransformReadOnly transform)
   {
      this.transform.set(transform);
   }

   public synchronized void setTransform(AffineTransform transform)
   {
      this.transform.set(transform);
   }

   public void translate(double distance, Axis3D axis)
   {
      if (axis == Axis3D.X)
      {
         translate(distance, 0, 0);
      }
      else if (axis == Axis3D.Y)
      {
         translate(0, distance, 0);
      }
      else if (axis == Axis3D.Z)
      {
         translate(0, 0, distance);
      }
   }

   public void translate(double x, double y, double z)
   {
      transform.appendTranslation(x, y, z);
   }

   public void translateTo(double x, double y, double z)
   {
      translateTo(new Vector3D(x, y, z));
   }

   public void translateTo(Tuple3DReadOnly translation)
   {
      transform.setIdentity();
      transform.setTranslation(translation);
   }

   public void rotate(double angle, Axis3D axis)
   {
      switch (axis)
      {
         case X:
            transform.appendRollRotation(angle);
            break;
         case Y:
            transform.appendPitchRotation(angle);
            break;
         case Z:
            transform.appendYawRotation(angle);
            break;
         default:
            throw new RuntimeException("Unhandled value of Axis: " + axis);
      }
   }

   public Vector3D getTranslation()
   {
      return new Vector3D(getTransform().getTranslation());
   }

   public void addChild(Graphics3DNode child)
   {
      synchronized (children)
      {
         children.add(child);
      }
   }

   public List<Graphics3DNode> getChildrenNodes()
   {
      synchronized (children)
      {
         return Collections.unmodifiableList(children);
      }
   }

   public Graphics3DObject getGraphics3DObject()
   {
      return graphicsObject;
   }

   public synchronized Graphics3DObject getGraphicsObjectAndResetHasGraphicsObjectChanged()
   {
      setHasGraphicsObjectChanged(false);
      return graphicsObject;
   }

   public synchronized void setHasGraphicsObjectChanged(boolean hasGraphicsObjectChanged)
   {
      this.hasGraphicsObjectChanged = hasGraphicsObjectChanged;
   }

   public boolean getHasGraphicsObjectChanged()
   {
      return hasGraphicsObjectChanged;
   }

   public void setGraphicsObject(Graphics3DObject graphicsObject)
   {
      this.graphicsObject = graphicsObject;
      setHasGraphicsObjectChanged(true);
   }

   public String getName()
   {
      return name;
   }

   public void notifySelectedListeners(ModifierKeyInterface modifierKeys, Point3DReadOnly location, Point3DReadOnly cameraPosition,
                                       QuaternionReadOnly cameraRotation)
   {
      for (SelectedListener selectedListener : selectedListeners)
      {
         selectedListener.selected(this, modifierKeys, location, cameraPosition, cameraRotation);
      }
   }

   public void addSelectedListener(SelectedListener selectedListener)
   {
      selectedListeners.add(selectedListener);
   }
}

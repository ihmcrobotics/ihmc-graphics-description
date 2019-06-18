package us.ihmc.graphicsDescription.exceptions;

import us.ihmc.euclid.shape.primitives.interfaces.Shape3DReadOnly;

@SuppressWarnings("serial")
public class ShapeNotSupportedException extends Exception
{
   public ShapeNotSupportedException(Shape3DReadOnly shape)
   {
      super(shape.getClass().getName());
   }
}

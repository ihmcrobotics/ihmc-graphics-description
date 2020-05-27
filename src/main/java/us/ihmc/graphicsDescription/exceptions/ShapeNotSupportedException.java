package us.ihmc.graphicsDescription.exceptions;

import us.ihmc.euclid.shape.primitives.interfaces.Shape3DReadOnly;

@SuppressWarnings("serial")
public class ShapeNotSupportedException extends RuntimeException
{
   public ShapeNotSupportedException(Shape3DReadOnly shape)
   {
      super(shape.getClass().getName());
   }
}

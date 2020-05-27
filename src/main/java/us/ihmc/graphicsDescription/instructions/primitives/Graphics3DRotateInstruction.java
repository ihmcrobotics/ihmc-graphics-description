package us.ihmc.graphicsDescription.instructions.primitives;

import us.ihmc.euclid.matrix.interfaces.RotationMatrixReadOnly;
import us.ihmc.graphicsDescription.instructions.Graphics3DPrimitiveInstruction;

public class Graphics3DRotateInstruction implements Graphics3DPrimitiveInstruction
{
   private RotationMatrixReadOnly rot;

   public Graphics3DRotateInstruction(RotationMatrixReadOnly rot)
   {
      this.rot = rot;
   }

   public RotationMatrixReadOnly getRotationMatrix()
   {
      return rot;
   }

   @Override
   public String toString()
   {
      String matrix = rot.toString().replaceAll("\n", ", ");
      return "\t\t\t<RotateMatrix>" + matrix.substring(0, matrix.length() - 2) + "</RotateMatrix>\n";
   }

   public boolean hasChangedSinceLastCalled()
   {
      return false;
   }
}

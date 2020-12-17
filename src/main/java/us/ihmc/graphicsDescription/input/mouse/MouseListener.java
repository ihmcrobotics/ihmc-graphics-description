package us.ihmc.graphicsDescription.input.mouse;

public interface MouseListener
{
   public void mouseDragged(MouseButton mouseButton, double dx, double dy);

   public default void scrolled(double amount)
   {

   }
}

package us.ihmc.graphicsDescription.appearance;

import java.util.ArrayList;
import java.util.List;

import us.ihmc.graphicsDescription.color.ColorDescription;

public class MaterialDescription
{
   private String name;
   private ColorDescription ambientColor;
   private ColorDescription diffuseColor;
   private ColorDescription specularColor;
   private ColorDescription emissiveColor;

   private double shininess = Double.NaN;

   private TextureDescription diffuseMap;
   private TextureDescription normalMap;
   private TextureDescription specularMap;
   private TextureDescription emissiveMap;

   private List<MaterialChangedListener> listeners;

   public MaterialDescription()
   {
   }

   public MaterialDescription(TextureDescription diffuseMap)
   {
      this.diffuseMap = diffuseMap;
   }

   public MaterialDescription(ColorDescription diffuseColor)
   {
      this.diffuseColor = diffuseColor;
   }

   public void setName(String name)
   {
      this.name = name;
      notifyListeners(true, false, false, false, false, false, false, false, false, false);
   }

   public void setAmbientColor(ColorDescription ambientColor)
   {
      this.ambientColor = ambientColor;
      notifyListeners(false, true, false, false, false, false, false, false, false, false);
   }

   public void setDiffuseColor(ColorDescription diffuseColor)
   {
      this.diffuseColor = diffuseColor;
      notifyListeners(false, false, true, false, false, false, false, false, false, false);
   }

   public void setSpecularColor(ColorDescription specularColor)
   {
      this.specularColor = specularColor;
      notifyListeners(false, false, false, true, false, false, false, false, false, false);
   }

   public void setEmissiveColor(ColorDescription emissiveColor)
   {
      this.emissiveColor = emissiveColor;
      notifyListeners(false, false, false, false, true, false, false, false, false, false);
   }

   public void setShininess(double shininess)
   {
      this.shininess = shininess;
      notifyListeners(false, false, false, false, false, true, false, false, false, false);
   }

   public void setDiffuseMap(TextureDescription diffuseMap)
   {
      this.diffuseMap = diffuseMap;
      notifyListeners(false, false, false, false, false, false, true, false, false, false);
   }

   public void setNormalMap(TextureDescription normalMap)
   {
      this.normalMap = normalMap;
      notifyListeners(false, false, false, false, false, false, false, true, false, false);
   }

   public void setSpecularMap(TextureDescription specularMap)
   {
      this.specularMap = specularMap;
      notifyListeners(false, false, false, false, false, false, false, false, true, false);
   }

   public void setEmissiveMap(TextureDescription emissiveMap)
   {
      this.emissiveMap = emissiveMap;
      notifyListeners(false, false, false, false, false, false, false, false, false, true);
   }

   private void notifyListeners(boolean wasNameChanged, boolean wasAmbientColorChanged, boolean wasDiffuseColorChanged, boolean wasSpecularColorChanged,
                                boolean wasEmissiveColorChanged, boolean wasShininessChanged, boolean wasDiffuseMapChanged, boolean wasNormalMapChanged,
                                boolean wasSpecularMapChanged, boolean wasEmissiveMapChanged)
   {
      if (listeners == null || listeners.isEmpty())
         return;

      Change change = new ChangeImpl(wasNameChanged,
                                     wasAmbientColorChanged,
                                     wasDiffuseColorChanged,
                                     wasSpecularColorChanged,
                                     wasEmissiveColorChanged,
                                     wasShininessChanged,
                                     wasDiffuseMapChanged,
                                     wasNormalMapChanged,
                                     wasSpecularMapChanged,
                                     wasEmissiveMapChanged,
                                     this);
      listeners.forEach(listener -> listener.onChange(change));
   }

   public void addListener(MaterialChangedListener listener)
   {
      if (listeners == null)
         listeners = new ArrayList<>();
      listeners.add(listener);
   }

   public boolean removeListener(MaterialChangedListener listener)
   {
      if (listeners == null)
         return false;
      else
         return listeners.remove(listener);
   }

   public void removeListeners()
   {
      listeners = null;
   }

   public String getName()
   {
      return name;
   }

   public ColorDescription getAmbientColor()
   {
      return ambientColor;
   }

   public ColorDescription getDiffuseColor()
   {
      return diffuseColor;
   }

   public ColorDescription getSpecularColor()
   {
      return specularColor;
   }

   public ColorDescription getEmissiveColor()
   {
      return emissiveColor;
   }

   public double getShininess()
   {
      return shininess;
   }

   public TextureDescription getDiffuseMap()
   {
      return diffuseMap;
   }

   public TextureDescription getNormalMap()
   {
      return normalMap;
   }

   public TextureDescription getSpecularMap()
   {
      return specularMap;
   }

   public TextureDescription getEmissiveMap()
   {
      return emissiveMap;
   }

   public static interface MaterialChangedListener
   {
      void onChange(Change change);
   }

   public static interface Change
   {
      boolean wasNameChanged();

      boolean wasAmbientColorChanged();

      boolean wasDiffuseColorChanged();

      boolean wasSpecularColorChanged();

      boolean wasEmissiveColorChanged();

      boolean wasShininessChanged();

      boolean wasDiffuseMapChanged();

      boolean wasNormalMapChanged();

      boolean wasSpecularMapChanged();

      boolean wasEmissiveMapChanged();

      MaterialDescription getMaterialDescription();
   }

   private static class ChangeImpl implements Change
   {
      private final boolean wasNameChanged;
      private final boolean wasAmbientColorChanged;
      private final boolean wasDiffuseColorChanged;
      private final boolean wasSpecularColorChanged;
      private final boolean wasEmissiveColorChanged;
      private final boolean wasShininessChanged;
      private final boolean wasDiffuseMapChanged;
      private final boolean wasNormalMapChanged;
      private final boolean wasSpecularMapChanged;
      private final boolean wasEmissiveMapChanged;
      private final MaterialDescription materialDescription;

      private ChangeImpl(boolean wasNameChanged, boolean wasAmbientColorChanged, boolean wasDiffuseColorChanged, boolean wasSpecularColorChanged,
                         boolean wasEmissiveColorChanged, boolean wasShininessChanged, boolean wasDiffuseMapChanged, boolean wasNormalMapChanged,
                         boolean wasSpecularMapChanged, boolean wasEmissiveMapChanged, MaterialDescription materialDescription)
      {
         this.wasNameChanged = wasNameChanged;
         this.wasAmbientColorChanged = wasAmbientColorChanged;
         this.wasDiffuseColorChanged = wasDiffuseColorChanged;
         this.wasSpecularColorChanged = wasSpecularColorChanged;
         this.wasEmissiveColorChanged = wasEmissiveColorChanged;
         this.wasShininessChanged = wasShininessChanged;
         this.wasDiffuseMapChanged = wasDiffuseMapChanged;
         this.wasNormalMapChanged = wasNormalMapChanged;
         this.wasSpecularMapChanged = wasSpecularMapChanged;
         this.wasEmissiveMapChanged = wasEmissiveMapChanged;
         this.materialDescription = materialDescription;
      }

      @Override
      public boolean wasNameChanged()
      {
         return wasNameChanged;
      }

      @Override
      public boolean wasAmbientColorChanged()
      {
         return wasAmbientColorChanged;
      }

      @Override
      public boolean wasDiffuseColorChanged()
      {
         return wasDiffuseColorChanged;
      }

      @Override
      public boolean wasSpecularColorChanged()
      {
         return wasSpecularColorChanged;
      }

      @Override
      public boolean wasEmissiveColorChanged()
      {
         return wasEmissiveColorChanged;
      }

      @Override
      public boolean wasShininessChanged()
      {
         return wasShininessChanged;
      }

      @Override
      public boolean wasDiffuseMapChanged()
      {
         return wasDiffuseMapChanged;
      }

      @Override
      public boolean wasNormalMapChanged()
      {
         return wasNormalMapChanged;
      }

      @Override
      public boolean wasSpecularMapChanged()
      {
         return wasSpecularMapChanged;
      }

      @Override
      public boolean wasEmissiveMapChanged()
      {
         return wasEmissiveMapChanged;
      }

      @Override
      public MaterialDescription getMaterialDescription()
      {
         return materialDescription;
      }
   }
}

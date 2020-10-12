package us.ihmc.graphicsDescription.appearance;

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
   }

   public void setAmbientColor(ColorDescription ambientColor)
   {
      this.ambientColor = ambientColor;
   }

   public void setDiffuseColor(ColorDescription diffuseColor)
   {
      this.diffuseColor = diffuseColor;
   }

   public void setSpecularColor(ColorDescription specularColor)
   {
      this.specularColor = specularColor;
   }

   public void setEmissiveColor(ColorDescription emissiveColor)
   {
      this.emissiveColor = emissiveColor;
   }

   public void setShininess(double shininess)
   {
      this.shininess = shininess;
   }

   public void setDiffuseMap(TextureDescription diffuseMap)
   {
      this.diffuseMap = diffuseMap;
   }

   public void setNormalMap(TextureDescription normalMap)
   {
      this.normalMap = normalMap;
   }

   public void setSpecularMap(TextureDescription specularMap)
   {
      this.specularMap = specularMap;
   }

   public void setEmissiveMap(TextureDescription emissiveMap)
   {
      this.emissiveMap = emissiveMap;
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
}

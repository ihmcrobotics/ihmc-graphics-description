plugins {
   id("us.ihmc.ihmc-build")
   id("us.ihmc.log-tools-plugin") version "0.6.4"
}

ihmc {
   group = "us.ihmc"
   version = "0.26.2"
   vcsUrl = "https://github.com/ihmcrobotics/ihmc-graphics-description"
   openSource = true

   configureDependencyResolution()
   configurePublications()
}

mainDependencies {
   api("net.sf.trove4j:trove4j:3.0.3")

   api("us.ihmc:euclid-frame:0.22.3")
   api("us.ihmc:euclid-shape:0.22.3")
   api("us.ihmc:ihmc-commons:0.35.1")
   api("us.ihmc:ihmc-yovariables:0.13.6")
   api("us.ihmc:scs2-definition:17-0.29.0")
}

testDependencies {
   api("us.ihmc:ihmc-commons-testing:0.35.1")
}

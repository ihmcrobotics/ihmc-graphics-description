plugins {
   id("us.ihmc.ihmc-build")
   id("us.ihmc.log-tools-plugin") version "0.6.3"
   id("us.ihmc.ihmc-ci") version "8.3"
   id("us.ihmc.ihmc-cd") version "1.26"
}

ihmc {
   group = "us.ihmc"
   version = "0.20.6"
   vcsUrl = "https://github.com/ihmcrobotics/ihmc-graphics-description"
   openSource = true

   configureDependencyResolution()
   configurePublications()
}

mainDependencies {
   api("net.sf.trove4j:trove4j:3.0.3")

   api("us.ihmc:euclid-frame:0.20.0")
   api("us.ihmc:euclid-shape:0.20.0")
   api("us.ihmc:ihmc-commons:0.32.0")
   api("us.ihmc:ihmc-yovariables:0.10.0")
   api("us.ihmc:scs2-definition:17-0.17.0")
}

testDependencies {
   api("us.ihmc:ihmc-commons-testing:0.32.0")
}

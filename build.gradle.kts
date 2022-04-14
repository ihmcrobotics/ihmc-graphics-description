plugins {
   id("us.ihmc.ihmc-build")
   id("us.ihmc.log-tools-plugin") version "0.6.3"
   id("us.ihmc.ihmc-ci") version "7.6"
   id("us.ihmc.ihmc-cd") version "1.23"
}

ihmc {
   group = "us.ihmc"
   version = "0.19.4"
   vcsUrl = "https://github.com/ihmcrobotics/ihmc-graphics-description"
   openSource = true

   configureDependencyResolution()
   configurePublications()
}

mainDependencies {
   api("net.sf.trove4j:trove4j:3.0.3")

   api("us.ihmc:euclid-frame:0.17.2")
   api("us.ihmc:euclid-shape:0.17.2")
   api("us.ihmc:ihmc-commons:0.30.5")
   api("us.ihmc:ihmc-yovariables:0.9.12")
}

testDependencies {
   api("us.ihmc:ihmc-commons-testing:0.30.5")
}

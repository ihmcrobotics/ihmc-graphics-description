plugins {
   id("us.ihmc.ihmc-build") version "0.20.1"
   id("us.ihmc.log-tools") version "0.4.2"
   id("us.ihmc.ihmc-ci") version "5.3"
   id("us.ihmc.ihmc-cd") version "1.8"
}

ihmc {
   group = "us.ihmc"
   version = "0.15.0"
   vcsUrl = "https://github.com/ihmcrobotics/ihmc-graphics-description"
   openSource = true

   configureDependencyResolution()
   configurePublications()
}

mainDependencies {
   api("net.sf.trove4j:trove4j:3.0.3")
   api("org.apache.commons:commons-lang3:3.9")

   api("us.ihmc:euclid-frame:0.14.0")
   api("us.ihmc:euclid-shape:0.14.0")
   api("us.ihmc:ihmc-commons:0.28.2")
   api("us.ihmc:ihmc-yovariables:0.6.0")
}

testDependencies {
   api("us.ihmc:ihmc-commons-testing:0.28.2")
}

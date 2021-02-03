FROM openjdk:8

EXPOSE 8080

ADD target/simple-spring-boot.jar simple-spring-boot.jar

# Use the CMD when you want the user of the image to have the flexibility 
# to run any executable they want to start the container.
# Use ENTRYPOINT when you want the container to behave exclusively
# as if it were the executable it's wrapping.
ENTRYPOINT ["java", "-jar", "simple-spring-boot.jar"]
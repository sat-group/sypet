# Development environment with ant and java 8.
FROM frekele/ant:1.10.3-jdk8 AS dev
WORKDIR /app
CMD ["/bin/bash"]

# Build SyPet in the development environment.
FROM dev AS build
WORKDIR /app
COPY . .
RUN ant build

# Run SyPet as a docker app.
FROM frekele/java:jdk8u172 AS app
WORKDIR /app
COPY --from=build /app/target ./target
COPY --from=build /app/lib ./lib
ENTRYPOINT ["java", "-cp", "target:lib/sat4j-pb.jar:lib/apt.jar:lib/commons-lang3-3.4.jar:lib/gson-2.8.5.jar:lib/point.jar:lib/soot-trunk.jar:lib/systring.jar", "cmu.edu.ui.SyPet"]

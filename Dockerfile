FROM  openjdk:17.0.2-oracle
COPY  MegaMarket-0.0.1-SNAPSHOT.jar /MegaMarket/MegaMarket-0.0.1-SNAPSHOT.jar
WORKDIR /MegaMarket/
ENTRYPOINT ["java","-jar","MegaMarket-0.0.1-SNAPSHOT.jar"]

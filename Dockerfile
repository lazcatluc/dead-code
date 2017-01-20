FROM ubuntu:14.04

# Install Java 8
RUN apt-get update
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:webupd8team/java -y
RUN apt-get update
RUN echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections
RUN apt-get install oracle-java8-installer -y --force-yes
RUN apt-get install oracle-java8-set-default

#Install aws cli
RUN apt-get install awscli -y

#Install SciTools
RUN apt-get install libxm4 -y
RUN apt-get install libxtst6 -y
RUN apt-get install libxi6 -y
RUN wget http://builds.scitools.com/all_builds/b844/Understand/Understand-4.0.844-Linux-64bit.tgz
RUN tar -xvf Understand-4.0.844-Linux-64bit.tgz
RUN touch /scitools/conf/license/locallicense.dat
RUN echo "Server: scitools-license.devfactory.com 00000000 9000" > /scitools/conf/license/locallicense.dat
#Check license
ENV PATH /usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/scitools/bin/linux64
RUN und version

# Configure  env
RUN mkdir projects
RUN mkdir logs
RUN mkdir app
COPY build/libs/dead-code.jar /app/dead-code.jar

EXPOSE 80 8080
ENTRYPOINT [ "sh", "-c", "java -Dspring.profiles.active=docker -jar /app/dead-code.jar >/logs/dead-code.log" ] 
# 基础镜像
FROM openjdk:8-jre-slim

# 时区设置
RUN ln -sf /usr/share/zoneinfo/PRC /etc/localtime && echo "PRC" > /etc/timezone

# 添加应用
COPY target/highmyopia-2.4.2.jar /highmyopia.jar

# 启动应用的命令
ENTRYPOINT ["java", "-jar", "/highmyopia.jar"]

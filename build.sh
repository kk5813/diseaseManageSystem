docker build --no-cache -t highmyopia-app:1.0 -f ./Dockerfile .
# 本机部署
# docker run -d -p 8082:8082 --restart=always -v /opt/resources/images:/opt/resources/images --name app crpi-zm140fqn1cizrn23.cn-chengdu.personal.cr.aliyuncs.com/kk0708/swjtu:hosApp
# docker run -d -p 8082:8082 --restart=always -v /opt/resources/images:/opt/resources/images --name app crpi-zm140fqn1cizrn23.cn-chengdu.personal.cr.aliyuncs.com/kk0708/swjtu:hosApp
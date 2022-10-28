
mvn clean install
aws ecr describe-repositories

docker build -t ms-generator-service:v01 .
docker tag ms-generator-service:v01 376368920129.dkr.ecr.ap-south-1.amazonaws.com/ms-generator-service:v01

aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 376368920129.dkr.ecr.ap-south-1.amazonaws.com
docker push 376368920129.dkr.ecr.ap-south-1.amazonaws.com/ms-generator-service:v01

----

aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 376368920129.dkr.ecr.ap-south-1.amazonaws.com
docker build -t ms-generator-service .
docker tag ms-generator-service:latest 376368920129.dkr.ecr.ap-south-1.amazonaws.com/ms-generator-service:latest
docker push 376368920129.dkr.ecr.ap-south-1.amazonaws.com/ms-generator-service:latest
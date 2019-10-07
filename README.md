# Kafka streams example



 

## Kafka start 
`docker-compose up -d`  

## Create topics
`./create-topics.sh`

## Run application
`mvn spring-boot:run`

## Send payments to topic
`./send.sh`
```
{"clientId":"1","parentRrn":"RRN-1","currentRrn":"RRN-1","operation":"PAYMENT","amount":"24"}
{"clientId":"1","parentRrn":"RRN-1","currentRrn":"RRN-2","operation":"CONFIRM","amount":"24"}
{"clientId":"1","parentRrn":"RRN-1","currentRrn":"RRN-3","operation":"CASH","amount":"12"}
{"clientId":"1","parentRrn":"RRN-1","currentRrn":"RRN-4","operation":"CANCEL","amount":"0"}
```
## Show topics
```
./read-topic.sh
```
## Show tables
```
 http://localhost:8080/showOperations
 http://localhost:8080/showPurchases
``` 

## Stop
```
^C
docker-compose down
```
docker exec -it broker kafka-console-consumer --topic bms-transactions --bootstrap-server broker:9092 --from-beginning
docker exec -it broker kafka-console-consumer --topic bms-transactions2 --bootstrap-server broker:9092 --from-beginning
docker exec -it broker kafka-console-consumer --topic bms-transactions3 --bootstrap-server broker:9092 --from-beginning
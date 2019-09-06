docker exec broker kafka-topics --create --topic bms-transactions --replication-factor 1 --partitions 1 --zookeeper zookeeper:2181
docker exec broker kafka-topics --create --topic bms-transactions2 --replication-factor 1 --partitions 1 --zookeeper zookeeper:2181
docker exec broker kafka-topics --create --topic bms-transactions3 --replication-factor 1 --partitions 1 --zookeeper zookeeper:2181
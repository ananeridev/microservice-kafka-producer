# Configurando o Kafka na sua máquina localmente

<details><summary>Se você utiliza o Mac use estas configs</summary>
<p>

- Verifique se esta no diretório bin.

## Start Zookeeper e Kafka Broker

-   Subindo o  Zookeeper.

```
./zookeeper-server-start.sh ../config/zookeeper.properties
```

- Adicione as configurações abaixo no  server.properties

```
listeners=PLAINTEXT://localhost:9092
auto.create.topics.enable=false
```

-   Subindo Kafka Broker

```
./kafka-server-start.sh ../config/server.properties
```

## Para criar um tópico via linha de comando

```
./kafka-topics.sh --create --topic topico-teste -zookeeper localhost:2181 --replication-factor 1 --partitions 4
```

</p>

</details>

<details><summary>Caso você utilize Windows</summary>
<p>

- Verifique se está no diretório bin **bin/windows**.

## Configurando o Zookeper e Kafka bRoker

-   Subindo Zookeeper.

```
zookeeper-server-start.bat ..\..\config\zookeeper.properties
```

-   Subindo o Kafka Broker.

```
kafka-server-start.bat ..\..\config\server.properties
```

</details>

## Configurando multiplos Kafka Brokers

- O primeiro passo é adicionar um novo **server.properties**.

```
broker.id=<unique-broker-d>
listeners=PLAINTEXT://localhost:<unique-port>
log.dirs=/tmp/<unique-kafka-folder>
auto.create.topics.enable=false
```

```
broker.id=1
listeners=PLAINTEXT://localhost:9093
log.dirs=/tmp/kafka-logs-1
auto.create.topics.enable=false
```

### Starting up the new Broker

- Provide the new **server.properties** thats added.

```
./kafka-server-start.sh ../config/server-1.properties
```

```
./kafka-server-start.sh ../config/server-2.properties
```

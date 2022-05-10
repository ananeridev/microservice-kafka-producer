
## A Kafka producer using TDD 


### The requirements of these systems are:

* Send a notification every time a book store book is exchanged

* Notifications are produced with Kafka default (key, value)

* The sending of this message must be triggered and for the time being it is consumed in a local kafka cluster

### Sending a message

> Tip: import these commands on Postman make your life easy

```
curl -i \
-d '{"bookStoreEventId":null,"book":{"bookId":456,"bookName":"Effective Java","bookAuthor":"Joshua Bloch"}}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/v1/bookstore-event
```

```
curl -i \
-d '{"bookStoreEventId":1,"book":{"bookId":456,"bookName":"Effective Java","bookAuthor":"Joshua Bloch"}}' \
-H "Content-Type: application/json" \
-X POST http://localhost:8080/v1/bookstore-event
```


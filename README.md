# Timekeeper Application

### Instructions:

There are two main ways to run this application.

The first one is running the application directly from the IDE, with just the required
docker services being instanced with access from the host machine.

* To run the application this way, just run it in the same way you would run any Spring application with the "dev" profile.


* If you would like to run the application in its own container, you can just run

    ```bash
  docker compose -f compose-prod.yaml up --build
  ```

  This will build the application and deploy the docker container with the exposed port 8080 on the host machine.
    
  Keep in mind however, that this deployment will skip tests.

To run tests you can simply run

``mvn test``

Both deployments will expose a [Kafka UI](http://localhost:8090) container  on port 8090, this way you can interact with the topics in a friendlier way.

The application uses mostly default ports, so there shouldn't be any issues. There also shouldn't be any need to set any environment variables as there are defaults set.

However, if you wish to change any variables here they are

| Variable Name                            | Example Value | Description                                    |
|------------------------------------------|---------------|------------------------------------------------|
| `SPRING_KAFKA_BOOTSTRAP_SERVERS_HOST`     | `localhost`   | Kafka Server Host                              |
| `SPRING_KAFKA_BOOTSTRAP_SERVERS_PORT`     | `9092`        | Kafka Server Port                              |
| `KAFKA_TOPIC_NAME`                        | `batidas`     | The topic the application should send messages to |
| `REDIS_HOST`                              | `localhost`   | The host for Redis service                     |
| `REDIS_PORT`                              | `6379`        | Port for Redis Service                         |


Set these as default environment values on the IDE or Host Machine

The [compose-prod.yaml](./compose-prod.yaml) will use the internal network, so this shouldn't be an issue


The application follows the specifications defined in the [contract](./controle-de-ponto_api.yml) provided by the TA tam

I have also included a Postman collection for testing


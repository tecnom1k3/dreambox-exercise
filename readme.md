#Dreambox spell checker application

### By Miguel Ibarra <miguel.ibarra.r@gmail.com>

---

This is a springboot application, requires maven and jdk 8

in order to execute, in a shell prompt run the build.sh file

```shell
./build.sh
```

you should be able to see a line like `com.miguel.dreambox.DreamboxApplication  : Started DreamboxApplication in 2.267 seconds (JVM running for 2.647)
` indicating that the application is ready to accept requests.

```shell
curl --location --request GET 'http://localhost:8080/spelling/kads'
```

after this, you should be able to see the following response

```json
{
    "correct": false,
    "suggestions": [
        "aikidos",
        "kids"
    ]
}
```
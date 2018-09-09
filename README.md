# TomEE Java EE CRUD Rest Arquillian Project

To run the application enter the following command in the project directory

# Run Tests
$ mvn clean test

# Run the application
$ mvn package tomee:run

# Tutorial
https://www.kodnito.com/posts/integration-tests-with-arquillian-using-tomee/

# Endpoints

POST

curl -d '{"task":"task1", "description":"description"}' -H "Content-Type: application/json" -X POST http://localhost:8080/restapi/api/todos

GET
curl http://localhost:8080/restapi/api/todos

PUT
curl -d '{"task":"task2", "description":"description2"}' -H "Content-Type: application/json" -X PUT http://localhost:8080/restapi/api/todos/{id}

DELETE
curl -X "DELETE" http://localhost:8080/restapi/api/todos/{id}

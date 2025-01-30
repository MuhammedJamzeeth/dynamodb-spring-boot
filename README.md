Spring Boot Application Startup Guide
=====================================

Step 1: Download Maven Dependencies
-----------------------------------
```bash
  mvn clean install
```

Step 2: Configure DynamoDB
---------------------------
Before you can access DynamoDB programmatically or through the AWS Command Line Interface (AWS CLI), you must configure your credentials to enable authorization for your applications. Downloadable DynamoDB requires any credentials to work, as shown in the following example.
Ensure that DynamoDB is running on port 8000. If DynamoDB is not running or is running on a different port, the application will throw an error.
if you use docker to run dynamodb, you can use the following command to start dynamodb

```bash
  aws configure
  AWS Access Key ID: "fakeMyKeyId" 
  AWS Secret Access Key: "fakeSecretAccessKey"
  Default Region Name: "fakeRegion"
 ```

```bash
  docker run -d -p 8000:8000 amazon/dynamodb-local
```

Step 3: Update application.yml
-------------------------------
Update the application.yml file with your DynamoDB credentials and configuration. Here is an example configuration:
```yaml
com:
  dynamodb:
    main:
      open-api:
        enabled: true
        api-version: 1.0.0
        title: Main
        description: User Traits API
      dynamodb:
        endpoint: http://localhost:8000
        region: us-west-2
        access-key: YOUR_ACCESS_KEY
        secret-key: YOUR_SECRET_KEY
        table-prefix: test
```
Step 4: Start the Application
------------------------------
```bash
  mvn spring-boot:run
```

Step 5: Verify the Application
-------------------------------
Open a browser and navigate to http://localhost:8080/swagger-ui.html. You should see the Swagger UI page with the available APIs.


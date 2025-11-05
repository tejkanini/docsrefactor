---
sidebar_label: 'AI Server Configuration Parameters'
sidebar_position: 3
---


## High-Availability Grade Deployment

### Deployment.yaml

The **deployment.yaml** file is used to define and configure a Kubernetes Deployment. A Deployment is responsible for managing a set of identical Pods and ensuring their availability and scalability.
* apiVersion: specifies the Kubernetes API version.
* kind: Defines the resource type.
* metadata: Provides information about the resource.
* labels : Key-value pairs for identification.
* replicas: Specifies the number of identical Pods.
* volumes: In the specsection of a Kubernetes Deployment or Pod configuration, the volumes field defines the storage volumes that can be accessed by containers running within the Pod. Each volume can have various types depending on the storage requirement.
    * One type of volume is a secret volume, which is used to securely store sensitive data such as passwords, tokens, or certificates. When defining a secret volume, you need to specify the secretName field which refers to the name of the Kubernetes Secret that holds the sensitive data.
    * Here's an example of how to define a secret volume with a secretName and defaultMode:
````
volumes:
    - name: secret
        secret:
            secretName: secret
            defaultMode: 256
            optional: false
````
* Within a Pod's spec section, the containers field is an array that defines one or more containers to be run within the Pod. Each container within the array has its own configuration, including the Docker image to use, command-line arguments (args), and network connectivity configuration (ports).
    * The args field within a container definition allows you to specify the command-line arguments to be passed to the container's entrypoint. These arguments are typically used to customize the behavior of the containerized application. The args field is an array of strings, where each element of the array represents an individual argument.
    * Here's an example of how to define args within a container:
````
containers:
- name: my-container
image: my-image:latest
args:
  - --arg1=value1
  - --arg2=value2
````

* The ports field within a container definition is used to define the network connectivity configuration for the container. It specifies which port(s) the container will listen on and how those ports should be exposed externally.
    * In the above example, the container named my-container is using the my-image:latest` Docker image. It exposes port 8080 within the container and specifies that it uses the TCP protocol.

* The env field is an array of key-value pairs, where each pair consists of a name and value field. The name field represents the name of the environment variable, and the value field holds the corresponding value. In the deployment that we have provided we have defined 5 different databases and we have provided one example below:

````
env:
- name: CUSTOM_SECURITY_DRIVER
    value: com.microsoft.sqlserver.jdbc.SQLServerDriver
- name: CUSTOM_SECURITY_RDBMS_TYPE
    value: SQL_SERVER
- name: CUSTOM_SECURITY_USERNAME
    value: username
- name: CUSTOM_SECURITY_PASSWORD
    value: passwd
- name: CUSTOM_SECURITY_SCHEMA
    value: dbo
- name: CUSTOM_SECURITY_DATABASE
    value: cfg_ai_sec
- name: CUSTOM_SECURITY_CONNECTION_URL
    value: >-
    jdbc:sqlserver://10.10.10.10:443;databaseName=cfg_ai_sec;encrypt=true;trustServerCertificate=true;
- name: MS_AUTHORITY
    value: >-
    https://login.microsoftonline.com/36da45f1-dd2c-4d1f-af13-5abe46b99921/
- name: MS_CLIENT_ID
    value: client_id
- name: MS_REDIRECT
    value: >-
    https://<my-dns>/cfg-ai-demo/Monolith/api/auth/login/ms
- name: MS_SECRET_KEY
    value: secret_key
- name: MS_TENANT
    value: deloitte.onmicrosoft.com
````
      
* If you have a different endpoint for SSO, you would need to modify the value of the SSO_ENDPOINT environment variable accordingly. Additionally, if the authentication credentials for this different endpoint are distinct from the previous configuration, you would need to provide the appropriate credentials to authenticate with that endpoint.

* Resources: In Kubernetes, CPU, ephemeral-storage, memory, and GPU are resources used to allocate and manage compute resources for containers in a cluster.

* The volumeMounts field is part of a container definition within a Pod specification. It is an array of objects where each object represents a volume mount. Each volume mount object consists of the following fields:
      * name: The name of the volume to mount. This should match the name field of the corresponding volume defined in the Pod's volumes field.
     * mountPath: The directory or file path within the container's file system where the volume should be mounted.
````
  volumeMounts:
  - name: bucketsecret
    mountPath: /mnt/key
````

* In the above example, It mounts a volume named bucketsecret at the path /mnt/key within the container's file system. This allows the container to read from and write to files stored in the data-volume volume.

* Recreate indicates that when an update is required, the existing pods will be terminated and new pods will be created from the updated deployment.
````
strategy:
    type: Recreate
revisionHistoryLimit: 10
progressDeadlineSeconds: 600
````

* evisionHistoryLimit: 10 specifies that a maximum of 10 previous revisions of the deployment will be stored in the revision history.
* progressDeadlineSeconds: 600 sets the maximum amount of time (in seconds) that the deployment can take to progress during an update. If the progress exceeds this deadline, the deployment will be considered as failed.
* This configuration ensures that the deployment uses the "Recreate" strategy, keeps a revision history of up to 10 revisions, and has a progress deadline of 600 seconds.


## Environment Variables

### Database Configurations

1. **CUSTOM_SECURITY_DRIVER**:
   * Description: Specifies the JDBC driver class for the RDBMS being used.
   * Example Values:
      *    For SQL Server: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
      *    For PostgreSQL: `org.postgresql.Driver`
   * *Note*: Choose the appropriate driver class based on your RDBMS type.
     
2. **CUSTOM_SECURITY_DATABASE**:
   * Description: Specifies the name of the security database.
   * Example Values: `cfg_ai_sec`

3. **CUSTOM_SECURITY_RDBMS_TYPE**:
   * Descriptio: Specifies the type of relational database management system (RDBMS) being used.
   * Example Values: `SQL_SERVER`
  
4. **CUSTOM_SECURITY_SCHEMA**:
   * Description: Specifies the SQL schema used in the database.
   * Example Values: `dbo`
  
5. **CUSTOM_SECURITY_CONNECTION_URL**
   * Description: Connection URL for Security Configuration CUSTOM_SECURITY_CONNECTION_URL.
   * Example Values: `jdbc:sqlserver://10.10.10.10:443;databaseName=cfg_ai_sec;encrypt=true;trustServerCertificate=true;`
  
6. **CUSTOM_LM_DRIVER**
   * Description: Specifies the JDBC driver class for the RDBMS being used.
   * Example Values:
      *    For SQL Server: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
      *    For PostgreSQL: `org.postgresql.Driver`
   * Note: Choose the appropriate driver class based on your RDBMS type.
  
7. **CUSTOM_LM_DATABASE**
   * Description: Specifies the name of the LM database.
   * Example Values: `cfg_ai_lm`
  
8. **CUSTOM_LM_RDBMS_TYPE**
   * *Description*: Specifies the type of relational database management system (RDBMS) being used.
   * *Example Values*: `SQL_SERVER`
  
9. **CUSTOM_LM_SCHEMA**
   * Description: Specifies the SQL schema used in the database.
   * Example Values: `dbo`
  
10. **CUSTOM_LM_CONNECTION_URL**
   * Description: Connection URL for Security Configuration CUSTOM_LM_CONNECTION_URL.
   * Example Values: `jdbc:sqlserver://10.10.10.10:443;databaseName=cfg_ai_lm;encrypt=true;trustServerCertificate=true;`

11. **CUSTOM_SCHEDULER_DRIVER:**
   * Description: Specifies the JDBC driver class for the RDBMS being used.
   * Example Values:
      * For SQL Server: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
      * For PostgreSQL: `org.postgresql.Driver`
   * Note: Choose the appropriate driver class based on your RDBMS type.

12. **CUSTOM_SCHEDULER_DATABASE:**
   * Description: Specifies the name of the scheduler database.
   * Example Value: `cfg_ai_sched`

13. **CUSTOM_SCHEDULER_RDBMS_TYPE:**
   * Description: Specifies the type of relational database management system (RDBMS) being used.
   * Example Value: `SQL_SERVER`

14. **CUSTOM_SCHEDULER_SCHEMA:**
   * Description: Specifies the SQL schema used in the database.
   * Value: `dbo`

15. **CUSTOM_SCHEDULER_CONNECTION_URL:**
   * Description: Connection URL for Security Configuration CUSTOM_SCHEDULER_CONNECTION_URL.
   * Value: `jdbc:sqlserver://10.10.10.10:443;databaseName=cfg_ai_sched;encrypt=true;trustServerCertificate=true;`

16. **CUSTOM_USER_TRACKING_DRIVER:**
   * Description: Specifies the JDBC driver class for the RDBMS being used.
   * Example Values:
      * For SQL Server: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
      * For PostgreSQL: `org.postgresql.Driver`
   * Note: Choose the appropriate driver class based on your RDBMS type.

17. **CUSTOM_USER_TRACKING_DATABASE:**
   * Description: Specifies the name of the user tracking database.
   * Example Value: `cfg_ai_user`

18. **CUSTOM_USER_TRACKING_RDBMS_TYPE:**
   * Description: Specifies the type of relational database management system (RDBMS) being used.
   * Example Value: `SQL_SERVER`

19. **CUSTOM_USER_TRACKING_SCHEMA:**
   * Description: Specifies the SQL schema used in the database.
   * Value: `dbo`

20. **CUSTOM_USER_TRACKING_CONNECTION_URL:**
   * Description: Connection URL for Security Configuration CUSTOM_USER_TRACKING_CONNECTION_URL.
   * Value: `jdbc:sqlserver://10.10.10.10:443;databaseName=cfg_ai_user;encrypt=true;trustServerCertificate=true;`

21. **CUSTOM_MODEL_INFERENCE_LOGS_DRIVER:**
   * Description: Specifies the JDBC driver class for the RDBMS being used.
   * Example Values:
      * For SQL Server: `com.microsoft.sqlserver.jdbc.SQLServerDriver`
      * For PostgreSQL: `org.postgresql.Driver`
   * Note: Choose the appropriate driver class based on your RDBMS type.

21. **CUSTOM_MODEL_INFERENCE_LOGS_DATABASE:**
   * Description: Specifies the name of the model inference logs database.
   * Example Value: `cfg_ai_model_logs`

22. **CUSTOM_MODEL_INFERENCE_LOGS_RDBMS_TYPE:**
   * Description: Specifies the type of relational database management system (RDBMS) being used.
   * Example Value: `SQL_SERVER`

23. **CUSTOM_MODEL_INFERENCE_LOGS_SCHEMA:**
   * Description: Specifies the SQL schema used in the database.
   * Value: `dbo`

> **Notes:**
> The SQL schema configurations determine the default schema used in the database for various scenarios.
> Although the names of the configurations are different, they all share the same value (`dbo`).
> Adjust the schema names or configurations based on your specific database structure and requirements.

24. **CUSTOM_MODEL_INFERENCE_LOGS_CONNECTION_URL:**
   * Description: Connection URL for Security Configuration CUSTOM_MODEL_INFERENCE_LOGS_CONNECTION_URL.
   * Value: `jdbc:sqlserver://10.10.10.10:443;databaseName=cfg_ai_model_logs;encrypt=true;trustServerCertificate=true;`

> **Notes:**
> Ensure that the specified connection URLs are correctly configured based on your database settings and security requirements.
> Adjust the databaseName parameter in each URL according to the specific database names for your configurations.

25. **CONFIGURATION_DATABASE_USERNAME:**
   * Description: Specifies the username for accessing the security-related features in the application.
   * Example Value: username

26. **CONFIGURATION_DATABASE_PASSWORD:**
   * Description: Specifies the password for the security user account.
   * Example Value: password

27. **MODEL_INFERENCE_LOGS_ENABLED:**
   * Description: Use Logs to Monitor an model
   * Value: True or False

> **Notes:**
> Adjust the database name based on your specific requirements and ensure that it aligns with the database structure and content.

### SSO Configuration:

1. **ENABLE_NATIVE:**
   * Description: Enable Native Authentication
   * value: true

2. **ENABLE_NATIVE_ACCESS_KEY_ALLOWED:**
   * Description: Enable Native Authentication access Key
   * value: false

3. **SETSOCIAL:**
   * Description: specify if you wish to set the social properties
   * value: true

4. **ENABLE_API_USER:**
   * Description: Allows API calls to use username and password
   * value: True or False

5. **API_USER_DYNAMIC:**
   * Description: To enable API calls, a dynamic username and password authentication mechanism is implemented. This allows flexibility in configuring credentials for different users or scenarios.
   * value: True or False

6. **ENABLE_MS:**
   * Description: Enable Microsoft login
   * value: True or False

7. **MS_ACCESS_KEY_ALLOWED:**
   * Description:
   * value: True or False

8. **ENABLE_NATIVE_REGISTRATION:**
   * Description:
   * value: True or False

9. **REDIRECT:**
   * Description: Specify the redirect url
   * Example Value: https://workshop.cfg.deloitte.com/cfg-ai-demo/SemossWeb/

10. **MS_AUTHORITY:**
   * Description: Provide the microsoft authority
   * Example Value: https://login.microsoftonline.com/36da45f1-dd2c-4d1f-af13-5abe46b99921/

11. **MS_CLIENT_ID:**
   * Description: Provide the microsoft Client ID
   * value: client_id

12. **MS_REDIRECT:**
   * Description: Provide the microsoft redirect
   * Example Value: https://workshop.cfg.deloitte.com/cfg-ai-demo/Monolith/api/auth/login/ms

13. **MS_SECRET_KEY:**
   * Description: Provide the Microsoft secret key
   * value: secret_key

14. **MS_TENANT:**
   * Description: Provide the microsoft tenant
   * value: deloitte.onmicrosoft.com

### SEMOSS Configuration (R/Py):

1. **R_ON:**
   * Description: Enable R
   * value: True or False

2. **R_CONNECTION_TYPE:**
   * Description: Specifies the type of R connection.
   * Value: JRI

3. **USE_TCP_PY:**
   * Description: Determines whether to use TCP for Python.
   * Value: false

4. **USE_PY_FILE:**
   * Description: Specifies whether to use a Python file.
   * Value: false

5. **USE_FILE_PY:**
   * Description: Specifies whether to use a file for Python.
   * Value: false

6. **NATIVE_PY_SERVER:**
   * Description: Controls the usage of the native Python server.
   * Value: true

7. **FILE_TRANSFER_LIMIT:**
   * Description: Sets the limit for file transfer.
   * Value: 500

8. **FILE_UPLOAD_LIMIT:**
   * Description: Sets the limit for file uploads.
   * Value: 500

9. **TCP_WORKER:**
   * Description: Specifies the TCP worker class for socket server.
   * Value: prerna.tcp.SocketServer

10. **TCP_CLIENT:**
    * Description: Specifies the TCP client class for Native Python Socket Client.
    * Value: prerna.tcp.client.NativePySocketClient

11. **NETTY_R:**
   * Description: Enables or disables the Netty R functionality.
   * Value: true

12. **NETTY_PYTHON:**
   * Description: Enables or disables the Netty Python functionality.
   * Value: true

13. **MONOLITH_COOKIE:**
   * Description: The MONOLITH_COOKIE is used to maintain your session with the application. You can choose any desired name for your cookie.
   * Value: cfg-ai-demo

14. **MONOLITH_ROUTE:**
   * Description: This is an optional environment variable that depends on the existence of a subpath called cfg-ai-demo/monolith. If you don't have this subpath, you can safely ignore this variable.
   * Value: cfg-ai-demo

15. **FE_ROUTE:**
   * Description: This will come into existence if a subpath called cfg-ai-demo/monolith exists, and it will serve as your monolith endpoint.
   * Value: /cfg-ai-demo/Monolith

16. **SEMOSS_IS_CLUSTER:**
   * Description: Indicates whether the application is part of a cluster.
   * Value: true

17. **OPTIONAL_COOKIES:**
   * Description: This environment variable is to allow for tracking the cookies
   * Value: false

### Storage Configuration
In the course of creating projects, applications, or establishing connections to databases and models, the resulting data from these processes is systematically stored within a dedicated storage bucket.

#### AWS Instructions:

1. **SEMOSS_STORAGE_PROVIDER:**
   * Description: Specifies the storage type used by the application.
   * Value: s3

2. **S3_REGION:**
   * Description: Specifies the AWS region for storage.
   * Value: us-east-1

3. **S3_BUCKET:**
   * Description: Specifies the AWS storage bucket.
   * Value: mybucket

4. **S3_ACCESS_KEY:**
   * Description: Specify the AWS Access key
   * Value: AWS S3 Access Key Value

5. **S3_SECRET_KEY:**
   * Description: Specify the AWS secret key
   * Value: AWS S3 Secret Key Value


#### Azure Instructions:

1. **SEMOSS_STORAGE_PROVIDER:**
   * Description: Specifies the storage type used by the application.
   * Value: AZURE

2. **AZURE_ACCT_KEY:**
   * Description: Specify the Azure access key
   * Value: aaaaaaaaabbbbbbbbbbccccccc

3. **AZURE_ACCT_NAME:**
   * Description: Specify the Azure account name
   * Value: myacct

#### GCP Instructions:

1. **GCP_REGION:**
   * Description: Specifies the GCP region for storage.
   * Value: us-east4

2. **GCP_REGION:**
   * Description: Specifies the GCP region for storage.
   * Value: us-east4

3. **GCP_BUCKET:**
   * Description: Specifies the GCP storage bucket.
   * Value: cfg-ai

4. **GCP_SERVICE_ACCOUNT_FILE:**
   * Description: Specifies the service account filename.
   * Value: /mnt/key/key.json
  
### Service.yaml
The service.yaml file is used to define and configure a Kubernetes Service. A Service provides stable network access to a set of Pods, allowing other resources within or outside the cluster to communicate with them.

````
spec:
  ports:
    - protocol: TCP
      port: 8080
      targetPort: 8080
  selector:
    app.kubernetes.io/instance: cfg-ai
    app.kubernetes.io/name: cfg-ai
  type: ClusterIP
  sessionAffinity: None
  ipFamilies:
    - IPv4
  ipFamilyPolicy: SingleStack
  internalTrafficPolicy: Cluster
````

In the given YAML configuration for a Kubernetes Service, the spec field defines the desired state of the Service.
   * ports section specifies the port configuration for the Service. In this example, it defines a single port with TCP protocol, where both the port and targetPort are set to 8080.
   * selector field is used to select the Pods that the Service will route traffic to. In this case, the Service selects Pods with the labels app.kubernetes.io/instance: cfg-ai and app.kubernetes.io/name: cfg-ai.
   * type: ClusterIP indicates that the Service is of type ClusterIP, which means it is only accessible within the cluster.
   * sessionAffinity: None specifies that the Service does not perform session affinity (client IP stickiness).
   * ipFamilies section specifies the IP families supported by the Service. In this case, it supports only IPv4.
   * ipFamilyPolicy: SingleStack indicates that the Service only supports a single IP family (IPv4).
   * internalTrafficPolicy: Cluster specifies that the Service only receives traffic from within the cluster.
   * This configuration sets up a Kubernetes Service with a single TCP port 8080, routing traffic to Pods with specific labels. The Service is of type ClusterIP, supports only IPv4, does not perform session affinity, and receives traffic only from within the cluster.


### Ingress.yaml
The ingress.yaml file is used to configure an Ingress resource in Kubernetes. Ingress provides external access to services within a cluster by routing incoming traffic to the appropriate service based on rules defined in the Ingress resource.

**Example 1:** Ingress with Base Path \
This configuration sets up an ingress rule for the base path / directing traffic to the specified service.
````
spec:
  ingressClassName: nginx
  tls:
    - hosts:
        - workshop.cfg.deloitte.com
      secretName: workshop-tls
  rules:
    - host: workshop.cfg.deloitte.com
      http:
        paths:
          - path: /
            pathType: ImplementationSpecific
            backend:
              service:
                name: cfg-ai
                port:
                  number: 8080
````

**Example 2:** Ingress with sub-Path /cfg-ai/(.*)

This configuration establishes an ingress rule for the sub-path /cfg-ai/(.*) directing traffic to a different service with a specific port. Adjust the service names, ports, and domain according to your specific setup. It also includes a rewrite annotation for handling the sub-path.
````
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: ingress-with-subpath
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /$1
spec:
  ingressClassName: nginx
  tls:
    - hosts:
        - workshop.cfg.deloitte.com
      secretName: workshop-tls
  rules:
    - host: workshop.cfg.deloitte.com
      http:
        paths:
          - path: /cfg-ai/(.*)
            pathType: ImplementationSpecific
            backend:
              service:
                name: cfg-ai
                port:
                  number: 8080
````

In the given YAML configuration for an Ingress resource in Kubernetes, the spec field defines the desired state of the Ingress.
   * ingressClassName: nginx specifies the class name of the Ingress controller that should handle this Ingress resource.
   * tls section defines the TLS configuration for the Ingress. In this case, it specifies that the host workshop.cfg.deloitte.com should be secured using the TLS certificate stored in the Secret named workshop-tls.
   * rules section defines the routing rules for the Ingress. In this example, there is a single rule for the host workshop.cfg.deloitte.com. Requests matching the specified path /cfg-ai/(.*) will be forwarded to the backend service named cfg-ai on port 8080.
   * pathType: ImplementationSpecific indicates that the path matching behavior is specific to the Ingress controller implementation.
   * This configuration sets up an Ingress resource that uses the nginx Ingress controller, secures the workshop.cfg.deloitte.com host with TLS using the workshop-tls Secret, and forwards requests to the cfg-ai service on port 8080 for the specified path.

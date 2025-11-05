# Hosting SEMOSS Privately

> **Note**
> For most use cases, you will not need to host your own instance. Instead, it is sufficient to connect to the public SEMOSS server. Please view the [Getting Started guide](GettingStarted.md) to learn how to access the public SEMOSS server.
> 
> However, if your use case involves the following factors/situations listed below, then you can host SEMOSS on a **private server** (ex. on-premises), or procure your own hosts from a cloud provider and deploy SEMOSS onto it.
> * Data residency requirements
> * Confidential or Classified information
> * **TBD / needs review**

## Prerequisites
For this guide, you will need a server with:
-  Debian-based Linux distribution
- `bash` command line access
- `sudo` privileges 

## Instructions
### Request Access to the Docker Setup Scripts
Please contact your administrator to request for a copy of the **AI_Scripts** folder and for the **link to the SEMOSS Docker Repository** before proceeding with the guide.

### Install Docker on Server
- In the bash terminal, run `cd ~ && mkdir AI` to create an `AI/` folder inside the root directory.
- Transfer the (unzipped) `AI_Scripts` folder to the `~/AI/` folder on your server.
- Navigate into the AI_Scripts directory using `cd ~/AI/AI_Scripts`
-  Run the the following commands from the bash terminal:
    - `chmod +x init.sh`
    - `sudo apt update`
    - `sudo apt upgrade`
    - `sudo apt install docker.io`

### Pull Docker Image and Build Container
- Run the the following commands from the bash terminal:
  - `sudo ~/AI/AI_Scripts/init.sh`
      - **Note**: This command may take a while to run as it needs to pull down the Docker image
  - `currDir=/opt/ai-server`
  - `sudo groupadd docker`
  - `sudo usermod -aG docker ${your-USER}`
      - This will allow you to run docker without sudo. *You may need to restart server for these permissions to take effect.* 
  - `sudo docker run -itd --name semosstmp DOCKER_REPOSITORY_URL/genai/genai-server:latest bash`
  - `docker container ps`
      - The output should look like:
  ![Docker Container Ps Output](/images/PrivateServerSetup/DockerContainerPsOutput.png)
  - `docker kill semosstmp`
  - `docker rm semosstmp`

### Start the Docker Container
- Run the following command from the bash terminal:
```
docker run -itd --name ai-server \
-v /opt/api-server/docker/ai-server/tomcat-logs:/opt/apache-tomcat-9.0.73/logs \
-v /opt/api-server/docker/ai-server/semosshome:/opt/semosshome \
-p 8080:8080 DOCKER_REPOSITORY_URL/genai/genai-server:4.3.0-SNAPSHOT-dev-2023-10-06 bash
```

>  **Note:** Please replace `DOCKER_REPOSITORY_URL` in the above command with the SEMOSS Docker Repository link that you received from your administrator in the [first section](#request-access-to-the-docker-setup-scripts).
- View the running `ai-server` container by running `docker container ps`
![AI Server Container](/images/PrivateServerSetup/AIServerContainer.png)
- Execute the following command:
```
docker exec -itd  ai /bin/bash -c "sed -i "s#MonolithDev#Monolith#g" $TOMCAT_HOME/webapps/SemossWeb/packages/legacy/app.constants.js; pip3 install protobuf accelerate;
```
- Finally, start the webserver by running:
  - `docker exec -itd ai-server start.sh`
- The Tomcat web application server is now running on container port 8080, and can also be accessed at `localhost:8080` of the host machine.


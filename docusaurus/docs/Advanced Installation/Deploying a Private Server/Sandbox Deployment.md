---
sidebar_label: 'Sandbox Deployment'
sidebar_position: 1
---

## Infrastructure Requirements

The minimum requirements include 32 GB of RAM, 8 CPU cores, and a 200 GB disk size. Additionally, GPU support is needed with 2 nodes, each having a 200 GB disk.
We aim to have both CPU and GPUs available for each of the nodes.

## Deploy AI server using a Docker container (Non-Prod)  

This talks about how you can test the exiting docker image in the sandbox. We would need couple of scripts in order to run the docker image in a VM and perform testing.

### Prerequisites  

#### Docker Login

Before you begin, ensure that you have Docker installed on your machine. If not, you can download and install Docker from the official website: [Docker - Get Started](https://docs.docker.com/get-started/).  

* Obtain Docker Registry Credentials
* Login into https://docker.cfg.deloitte.com/
* Copy the CLI secret from User Profile
* Log in to the Docker Registry and obtain CLI secret. Use the following command to log in to your Docker registry `docker login docker.cfg.deloitte.com`

###  Docker Deployment Scripts

You can find the [deployment scripts](https://repo.semoss.org/gen-ai/cfgai-deployment/-/tree/master/dev-grade-deployment) here.

1. Create a configuration file (e.g., config.env) that defines the common variables. It might look like this: 

**config.env**
````
currDir="/path/to/your/directory"
dockerImage=docker.cfg.deloitte.com/genai/genai-server:4.3.0-SNAPSHOT-dev-latest
````
* Replace "/path/to/your/directory" with the actual path you want to use

2. Run the init.sh script: This script will pull the Docker image, create a container, copy its contents to the specified local directory, and  perform cleanup.

**init.sh**  
````
 #!/bin/bash
 source /path/to/config.env
 mkdir -p $currDir
 docker pull $dockerImage
 docker run -itd --name semosstmp $dockerImage bash
 docker cp semosstmp:/opt/semosshome $currDir
 docker kill semosstmp
 docker rm semosstmp
````
* **/opt/semosshome:** This directory serves as the central hub for storing various components, including databases, model connections,  and projects. It acts as the primary location for organizing and managing your system's key elements.
* Replace "/path/to/config.env" with the actual path to your config.env file.

3. Automates the setup of an AI server by sourcing configuration, running a Docker container with volume mapping and port forwarding:

**startSEMOSS.sh**
````
#!/bin/bash
 source /path/to/config.env
 docker run -itd --name ai-server \
 -v $currDir/semosshome:/opt/semosshome \
 -p 8080:8080 $dockerImage
````
* Replace "/path/to/config.env" with the actual path to your config.env file.
* If you intend to run the server in a Docker container with GPU support, use the --gpus flag to specify GPU devices. You can either provide the GPU IDs or use 'all' to utilize all available GPUs. Example:`docker run --gpus all -d your_image_name`

4. Script to stop and remove a Docker container named "ai-server".

**stopSEMOSS.sh**
````
#!/bin/bash
 docker kill ai-server
 docker rm ai-server
````

5. Execute the below script to perform a restart.

**restartSEMOSS.sh**
````
 #!/bin/bash
 /path/to/your/directory/stopSEMOSS.sh
 /path/to/your/directory/startSEMOSS.sh
````
* Replace "/path/to/your/directory" with the actual path.

#### Optional  
Systemd is entirely optional element to start AI server.  

6. Systemd service configuration for an AI server.

**ai.service**
````
 [Unit]
 Description=Health check script
 After=network.target

 [Service]
 User=root
 WorkingDirectory=/path/to/your/directory
 ExecStart=/usr/bin/python3 /path/to/your/directory/startHealthCheck.py

 [Install]
 WantedBy=multi-user.target
````
* Copy the startHealthCheck.py from cfg-ai-server-scripts folder.
* Replace WorkingDirectory "/path/to/your/directory" with the actual path.
* Provide absolute path to the startHealthCheck.py script.
* As a psuedo user, copy the contents of this file to the following location: "/lib/systemd/system/service".

* To manage the AI server using systemd, use the following systemctl commands:

``` bash 
 # Reload the AI server service after making changes to the systemd service file
 sudo systemctl daemon-reload 

 # Enable the AI server service to start on boot
 sudo systemctl enable ai.service 

 # Start the AI server service
 sudo systemctl start ai.service 

 # Stop the AI server service
 sudo systemctl stop ai.service 

 # Check the status of the AI server service
 sudo systemctl status ai.service 
 ```

7. Locate the script where the *run_restart_script()* function is defined.

8. Look for the run_restart_script function in your script. It should resemble the following:

````
def run_restart_script():
    # replace this with the path to your restart script
    subprocess.call(["/path/to/your/directory/restartSEMOSS.sh"])
````

9. Update the path in the subprocess.call line to point to the correct location of your restartSEMOSS script.

---
sidebar_label: 'High Availability Deployment'
sidebar_position: 2
---

## Infrastructure Requirements

* The minimum requirements include 32 GB of RAM, 8 CPU cores, and a 200 GB disk size. Additionally, GPU support is needed with 2 nodes, each having a 200 GB disk.
* We aim to have both CPU and GPUs available for each of the nodes.
* Specifically, we prefer T4 GPUs or newer models such as Nvidia T4, Nvidia A10, and Nvidia A100.

## Terraform Scripts (optional)  

This [repository](https://repo.semoss.org/gen-ai/cloud-terraform-infra) contains Terraform configurations for managing infrastructure on both AWS and GCP cloud platforms. The directory structure is organized as follows:

* environments/aws/semoss-nonprod/main.tf: AWS-specific Terraform configurations for the environment.
* environments/gcp/prod/main.tf: GCP-specific Terraform configurations for the environment.
* modules/aws/eks-cluster: Terraform module for provisioning an EKS cluster on AWS.
* modules/aws/mysql-private: Terraform module for deploying a private MySQL instance on AWS.
* modules/gcp/eks-cluster: Terraform module for provisioning a GKE cluster on GCP.
* modules/gcp/mysql-private: Terraform module for deploying a private MySQL instance on GCP.

#### Executing Terraform Commands for AWS

To execute Terraform commands for AWS, navigate to the environments/aws/semoss-nonprod/ directory using the following command: `cd environments/aws/semoss-nonprod/`

Once inside the AWS environment directory, you can run standard Terraform commands such as terraform init, terraform plan, and terraform apply to manage your AWS infrastructure.

#### Executing Terraform Commands for GCP

To execute Terraform commands for GCP, navigate to the environments/gcp/prod/ directory using the following command: `cd environments/gcp/prod/`

Once inside the GCP environment directory, you can run standard Terraform commands such as terraform init, terraform plan, and terraform apply to manage your GCP infrastructure.

#### AWS EKS Cluster and MySQL Private

Terraform modules in modules/aws/eks-cluster and modules/aws/mysql-private facilitate the creation of an Amazon EKS cluster and a private MySQL instance on AWS, respectively.

#### GCP EKS Cluster and MySQL Private

Terraform modules in modules/gcp/eks-cluster and modules/gcp/mysql-private enable the provisioning of a Google Kubernetes Engine (GKE) cluster and a private MySQL instance on GCP, respectively.

## Kubernetes Yaml

You can find the [scripts](https://repo.semoss.org/gen-ai/semoss_helm/-/tree/cfgai-deployment/cfg-ai-deployment) here.

## Helm Scripts

You can find the [scripts](https://repo.semoss.org/gen-ai/semoss_helm/-/tree/cfgai-deployment/cfg-ai-deployment) here.

# GitHub Actions Setup (CI/CD Pipelines)

- Every push to the repo will trigger the Unit Test Pipeline defined in the ".github/workflows/unit-tests.yml" file.

- Release creation will triger a Deployment of the SpringBoot application into the AWS ECS cluster.
The Deployment Pipeline is defined in the ".github/workflows/aws.yml" file.

  

# Deploy Pipeline Flowchart (.github/workflows/aws.yml)


  

# AWS Elastic Container Service (ECS) Fargate Setup

#### Create the ECR repository
```
aws ecr create-repository --repository-name igorya5v/spring-boot-app --region us-east-1
```
Ensure that you have an ecsTaskExecutionRole IAM role in your account. You can follow the [Amazon ECS Task Execution IAM Role guide](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/task_execution_IAM_role.html) to create the role.

##### Register a task definition

```
aws ecs register-task-definition --region us-east-1 --cli-input-json file://simple-spring-boot/ecs-task-definition.json
```

##### Create an ECS cluster

Create the ECS cluster in the default VPC with this command:

```
aws ecs create-cluster --region us-east-1 --cluster-name fargate
```

##### Create a security group

For the Fargate service we need a security group.

```
aws ec2 create-security-group --group-name fargate-sg --description "Group for fargate ECS Cluster"
{
    "GroupId": "sg-043ab03ec99b60369"
}
```

For our application we have to open port 8080. Use the security GroupId returned from the previous command.

```
aws ec2 authorize-security-group-ingress --group-id sg-043ab03ec99b60369 --protocol tcp --port 8080 --cidr 0.0.0.0/0
```

##### Create the Fargate service

This command creates a Fargate service using the task definition which we registered before. Use the security GroupId from above. Also use your subnet id’s from your default VPC in the command below.

```
aws ecs create-service --region us-east-1 --service-name sb-app-service --task-definition sb-app-task --desired-count 1 --launch-type "FARGATE" --network-configuration "awsvpcConfiguration={subnets=[subnet-fcc5779b], securityGroups=[sg-043ab03ec99b60369],assignPublicIp=ENABLED}" --cluster fargate
```

##### Configure the Github secrets

Configure the two secrets below with the credentials for an IAM user:

```
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
```

#####  After Successful Deployment test the simple-spring-boot app

We need to get the public IP adress. First list the tasks in the fargate cluster:

```
aws ecs list-tasks  --cluster fargate
```

Next get information about the running task:

```
aws ecs describe-tasks --task "arn:aws:ecs:us-east-1:601912882130:task/fargate/e9551bfe073e435e803e22268f6b21d1" --cluster fargate
```

In the output of the above command you will find the “networkInterfaceId”.

```
   {
        "name": "networkInterfaceId",
        "value": "eni-0a9e3072cf3299729"
    }
```

With the above networkInterfaceId you can find the public IP:

```
aws ec2 describe-network-interfaces --network-interface-ids eni-0a9e3072cf3299729
```

In the output of the above command you will find the public IP adress:

```
"PublicDnsName": "ec2-34-247-74-194.us-east-1.compute.amazonaws.com",
"PublicIp": "34.247.74.194"
```

Now we can test the simple-spring-boot app with curl.

```
curl 34.247.74.194:8080/message
Simple Spring Boot Demo...
```

##### Cleaning Up

The AWS resources are not free. To clean them up, run the following:

```
aws ecs delete-service --service sb-app-service --cluster fargate --force
```

# TODO
Define infrustructure as a code using tools like AWS Cloud Formation, Teraform, etc.

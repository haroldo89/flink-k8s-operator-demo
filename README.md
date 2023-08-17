# Flink Kubernetes Operator #
The Flink Kubernetes Operator extends the Kubernetes API with the ability to manage and operate Flink Deployments. The operator features the following amongst others:

- Deploy and monitor Flink Application and Session deployments
- Upgrade, suspend and delete deployments
- Full logging and metrics integration
- Flexible deployments and native integration with Kubernetes tooling

# Quickstart

This document provides a quick introduction to using the Flink Kubernetes Operator. Readers of this document will be able to deploy the Flink operator itself and an example Flink job to a local Kubernetes installation.

# Prerequisites
- docker
- kubernetes
- helm

# Deploying the operator

Install the certificate manager on your Kubernetes cluster to enable adding the webhook component (only needed once per Kubernetes cluster):

```
kubectl create -f https://github.com/jetstack/cert-manager/releases/download/v1.8.2/cert-manager.yaml
```

Now you can deploy the selected stable Flink Kubernetes Operator version using the included Helm chart:

```
helm repo add flink-operator-repo https://downloads.apache.org/flink/flink-kubernetes-operator-1.5.0/
helm install flink-kubernetes-operator flink-operator-repo/flink-kubernetes-operator
```
To find the list of stable versions please visit https://flink.apache.org/downloads.html

# Submitting a Flink job
Once the operator is running as seen in the previous step you are ready to submit a Flink job:


Create a docker image with the Flink Application

```
docker build -t flinkdemo .
```

Create a file called `flink-deployment.yaml` with FlinkDeployment definition

```
apiVersion: flink.apache.org/v1beta1
kind: FlinkDeployment
metadata:
  name: kafka-example
spec:
  image: flinkdemo
  flinkVersion: v1_13
  flinkConfiguration:
    taskmanager.numberOfTaskSlots: "1"
  serviceAccount: flink
  jobManager:
    resource:
      memory: "2048m"
      cpu: 1
  taskManager:
    resource:
      memory: "2048m"
      cpu: 1
  job:
    jarURI: local:///opt/flink/demo/streaming/flinkdemo.jar
    parallelism: 1
    upgradeMode: stateless
```

submit the Flink job

```
kubectl create -f ./flink-deployment.yaml
```
You may follow the logs of your job, after a successful startup (which can take on the order of a minute in a fresh environment, seconds afterwards) you can:

```
kubectl logs -f deploy/flinkdemo
```

To expose the Flink Dashboard you may add a port-forward rule:

```
kubectl port-forward svc/flinkdemo-rest 8081
```

Now the Flink Dashboard is accessible at localhost:8081.

In order to stop your job and delete your FlinkDeployment you can:

```
kubectl delete flinkdeployment/flinkdemo
```
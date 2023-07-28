#!/bin/bash

# Nombre del archivo de salida
archivo_salida="validation.txt"

# Obtener todos los namespaces
namespaces=$(kubectl get namespaces -o json | jq -r '.items[].metadata.name')

# Bucle a través de todos los namespaces
for namespace in $namespaces; do
  echo "Namespace: $namespace" >> "$archivo_salida"

  # Verificar si hay DeploymentConfigs en el namespace actual
  deployment_configs=$(kubectl get dc -n "$namespace" -o json | jq -r '.items[].metadata.name')

  # Si no hay DeploymentConfigs en el namespace, pasa al siguiente
  if [ -z "$deployment_configs" ]; then
    echo "No hay DeploymentConfigs en este namespace." + "$namespace" >> "$archivo_salida"
    continue
  fi

  # Bucle a través de todos los DeploymentConfigs
  for dc in $deployment_configs; do
    # Verificar si el DeploymentConfig tiene una etiqueta específica (por ejemplo, 'no-borrar: true')
    label=$(kubectl get dc "$dc" -n "$namespace" -o json | jq -r '.metadata.annotations["inditex.com/template"]')
    if [[ "$label" == "com.inditex.sentinel:senttapp-flink"* ]]; then
      echo "El DeploymentConfig '$dc' en el namespace '$namespace' no se puede borrar." >> "$archivo_salida"
    fi
  done
done


# # Verificar si hay DeploymentConfigs en el namespace actual
#   deployment_configs=$(kubectl get deploy -n "$namespace" -o json | jq -r '.items[].metadata.name')

#   # Si no hay DeploymentConfigs en el namespace, pasa al siguiente
#   if [ -z "$deployment_configs" ]; then
#     echo "No hay DeploymentConfigs en este namespace." + "$namespace"
#     continue
#   fi

#   # Bucle a través de todos los DeploymentConfigs
#   for dc in $deployment_configs; do
#     # Verificar si el DeploymentConfig tiene una etiqueta específica (por ejemplo, 'no-borrar: true')
#     label=$(kubectl get deploy "$dc" -n "$namespace" -o json | jq -r '.metadata.annotations["inditex.com/template"]')
#     if [[ "$label" == "com.inditex.sentinel:senttapp-flink"* ]]; then
#       echo "El DeploymentConfig '$dc' en el namespace '$namespace' no se puede borrar."
#     fi
#   done
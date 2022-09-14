#!/usr/bin/env bash
# Params.
PASS=$1

# Settings.
CONTAINER_NAME="mpdc-postgres"

# Check if container exists.
CONTAINER=$(docker ps -al | grep $CONTAINER_NAME)

if [ -z "$CONTAINER" ]; then
  # Variable empty, run new container.
  docker run -it --name $CONTAINER_NAME -p 5432:5432 -e POSTGRES_PASSWORD="$PASS" postgres:latest
else
  # Container exists.
  if [[ $CONTAINER == *Exited* ]]; then
    # Stopped, restart.
    docker start $CONTAINER_NAME
  else
    # Already running.
    docker container ls
  fi
fi

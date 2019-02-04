#!/bin/bash

echo "START_1 Start db container"
docker run --network host --name mongodb isu/resource-management-db

echo "START_2 Start backend container"
docker run --network host --name backend isu/resource-management-backend

echo "START_3 Start frontend container"
docker run --network host --name frontend isu/resource-management-frontend
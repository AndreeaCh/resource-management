#!/bin/bash

echo "START_1 Start db container"
docker run --network host --name mongodb isu/easy-manage-db

echo "START_2 Start backend container"
docker run --network host --name backend isu/easy-manage-backend

echo "START_3 Start frontend container"
docker run --network host --name frontend isu/easy-manage-frontend

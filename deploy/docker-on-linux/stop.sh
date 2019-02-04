#!/usr/bin/env bash

echo "STOP_1 Stopping frontend"
docker stop frontend

echo "STOP_2 Stopping backend"
docker stop backend

echo "STOP_3 Stopping database"
docker stop mongodb
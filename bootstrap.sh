#!/usr/bin/zsh

TOKEN=$(vault token lookup --format=json | jq .data.id -r)

echo "server.port=9090
vault.root.address=http://localhost:8200
vault.root.token=$TOKEN" > src/main/resources/application.properties
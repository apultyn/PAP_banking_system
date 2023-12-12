#!/bin/bash

if ! command -v java &> /dev/null || ! java --version 2>&1 | grep -q "17\." ; then
    echo "Java JDK 17 nie jest zainstalowana. Instalowanie..."

    sudo apt update
    sudo apt install -y openjdk-17-jdk
fi

if ! command -v mvn &> /dev/null; then
    echo "Maven nie jest zainstalowany. Instalowanie..."

    sudo apt install -y maven
fi

mvn clean install

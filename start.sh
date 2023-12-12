#!/bin/bash

# Sprawdź, czy Java JDK 17 jest zainstalowana
if ! command -v java &> /dev/null || ! java --version 2>&1 | grep -q "17\." ; then
    echo "Java JDK 17 nie jest zainstalowana. Instalowanie..."

    # Zainstaluj Java JDK 17 przy użyciu apt (Ubuntu)
    sudo apt update
    sudo apt install -y openjdk-17-jdk
fi

# Sprawdź, czy Maven jest zainstalowany
if ! command -v mvn &> /dev/null; then
    echo "Maven nie jest zainstalowany. Instalowanie..."

    # Zainstaluj Maven przy użyciu apt
    sudo apt install -y maven
fi

# Wywołaj polecenie Maven
mvn clean install
#!/bin/bash

mvn -Dmaven.test.skip=true clean package
cp -f target/dict.jar dict.jar
rm data/*

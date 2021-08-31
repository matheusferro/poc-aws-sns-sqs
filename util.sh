#!/bin/sh

LogSuccess(){
  if [ $# -eq 2 ]
  then
    echo -e "\e[32m\n$1\nMessage:\n$2\e[39m"
  else
    echo -e "\e[32m\n$1\n\e[39m"
  fi
}

LogError(){
  if [ $# -eq 2 ]
  then
    echo -e "\e[31m\n$1\nERROR:\n$2\e[39m"
  else
    echo -e "\e[31m\n$1\n\e[39m"
  fi
  exit
}

LogInfo(){
  echo -e "\n$1\n"
}

if ! aws --version COMMAND &> /dev/null
then
  LogSuccess "[INFO] AWS CLI is necessary to use this script."
  exit
fi
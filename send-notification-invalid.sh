#!/bin/sh
. ./util.sh

if ! aws --version COMMAND &> /dev/null
then
  LogSuccess "[INFO] AWS CLI is necessary to use this script."
  exit
fi

#This message is invalid and will be send to DLQ
aws --endpoint-url http://localhost:4566 \
      sqs send-message --queue-url http://localhost:4566/000000000000/send-notification \
      --message-body "{\"message\":\"Information updated !\", \"subject\":\"Sbuject\", \"send\":\"someEmail@email.com\"}"

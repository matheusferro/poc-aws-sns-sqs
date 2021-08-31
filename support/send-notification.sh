#!/bin/sh
. ./util.sh

if ! aws --version COMMAND &> /dev/null
then
  LogSuccess "[INFO] AWS CLI is necessary to use this script."
  exit
fi

#This command is executed to include a request notification
#simulating the action of one of the cluster of the image on README.md
aws --endpoint-url http://localhost:4566 \
      sqs send-message --queue-url http://localhost:4566/000000000000/send-notification \
      --message-body "{\"message_body\":\"Information updated !\", \"subject\":\"Update Profile\", \"send_to\":\"someEmail@gmail.com\"}"

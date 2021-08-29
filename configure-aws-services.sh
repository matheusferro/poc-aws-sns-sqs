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

LogInfo "Creating SNS topic 'subscription-topic'..."
if output=$(aws --endpoint-url http://localhost:4566 sns create-topic --name subscription-topic 2>&1);
then
  LogSuccess "'subscription-topic' created:" "$output"
else
  LogError "error on create 'subscription-topic'" "$output"
fi

LogInfo "Creating SNS topic 'notify-topic'..."
if output=$(aws --endpoint-url http://localhost:4566 sns create-topic --name notify-topic 2>&1);
then
  LogSuccess "'notify-topic' created:" "$output"
else
  LogError "error on create 'notify-topic'" "$output"
fi

LogInfo "Creating SQS Queue 'notification01'..."
if output=$(aws --endpoint-url http://localhost:4566 sqs create-queue --queue-name notification01 2>&1);
then
  LogSuccess "'notification01' created:" "$output"
else
  LogError "error on create 'notification01'" "$output"
fi

LogInfo "Creating SQS Queue 'notification02'..."
if output=$(aws --endpoint-url http://localhost:4566 sqs create-queue --queue-name notification02 2>&1);
then
  LogSuccess "'notification02' created:" "$output"
else
  LogError "error on create 'notification02'" "$output"
fi

LogInfo "Subscritpion SNS topic 'subscription-topic' on queue 'notification01'"
if output=$(aws --endpoint-url http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:subscription-topic --protocol sqs --notification-endpoint http://localhost:4566/000000000000/notification01 2>&1);
then
  LogSuccess "Subscribed" "$output"
else
  LogError "error on create subscriptions" "$output"
fi

LogInfo "Subscritpion SNS topic 'subscription-topic' on queue 'notification02'"
if output=$(aws --endpoint-url http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:subscription-topic --protocol sqs --notification-endpoint http://localhost:4566/000000000000/notification01 2>&1);
then
  LogSuccess "Subscribed" "$output"
else
  LogError "error on create subscriptions" "$output"
fi
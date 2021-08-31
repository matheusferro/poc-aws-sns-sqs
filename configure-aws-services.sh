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

LogInfo "Creating SQS Queue 'notify-tech'..."
if output=$(aws --endpoint-url http://localhost:4566 sqs create-queue --queue-name notify-tech 2>&1);
then
  LogSuccess "'notify-tech' created:" "$output"
else
  LogError "error on create 'notify-tech'" "$output"
fi

LogInfo "Creating SQS Queue 'notify-sports'..."
if output=$(aws --endpoint-url http://localhost:4566 sqs create-queue --queue-name notify-sports 2>&1);
then
  LogSuccess "'notify-sports' created:" "$output"
else
  LogError "error on create 'notify-sports'" "$output"
fi

LogInfo "Subscritpion SNS topic 'subscription-topic' on queue 'notify-tech' and set FilterPolicy"
if output=$(aws --endpoint-url http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:subscription-topic --protocol sqs --notification-endpoint http://localhost:4566/000000000000/notify-tech 2>&1);
then
  arn_subscription=$(echo $output | awk '{print $3}')
  arn_subscription=${arn_subscription: 1:-1}
  aws --endpoint-url http://localhost:4566 sns set-subscription-attributes --subscription-arn "$arn_subscription" --attribute-name FilterPolicy --attribute-value "{\"notify_option\":[\"TECH\"]}"
  LogSuccess "Subscribed" "$output"
else
  LogError "error on create subscriptions" "$output"
fi

LogInfo "Subscritpion SNS topic 'subscription-topic' on queue 'notify-sports'"
if output=$(aws --endpoint-url http://localhost:4566 sns subscribe --topic-arn arn:aws:sns:us-east-1:000000000000:subscription-topic --protocol sqs --notification-endpoint http://localhost:4566/000000000000/notify-sports 2>&1);
then
  arn_subscription=$(echo $output | awk '{print $3}')
  arn_subscription=${arn_subscription: 1:-1}
  aws --endpoint-url http://localhost:4566 sns set-subscription-attributes --subscription-arn "$arn_subscription" --attribute-name FilterPolicy --attribute-value "{\"notify_option\":[\"SPORTS\"]}"
  LogSuccess "Subscribed" "$output"
else
  LogError "error on create subscriptions" "$output"
fi

LogInfo "Creating SQS Queue 'send-notification'..."
if output=$(aws --endpoint-url http://localhost:4566 sqs create-queue --queue-name send-notification 2>&1);
then
  LogSuccess "'send-notification' created:" "$output"
else
  LogError "error on create 'send-notification'" "$output"
fi

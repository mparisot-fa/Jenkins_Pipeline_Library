#! /bin/bash

user_access_key_id=""
user_secret_access_key=""
user_session_token=""

aws-start-session() {
    aws sts get-session-token --duration-seconds 129600 >credentials.tmp
    if [[ $? == 0 ]]; then
        aws-set-session-variables
        echo "Awesome!!! Token susccessfully activated until "$(cat credentials.tmp | jq -r '.Credentials.Expiration')
        rm -f credentials.tmp
    else
        echo "Uh...oh...Session initiation failed. Please rectify & try again."
    fi
}

aws-switch-to-build-role() {

    # Role & AWS account id will be referred from environment
    if [[ -n "${JENKINS_HOME:-}" ]]; then
        echo "Building from Jenkins, switching to build role"
    else
        echo "Building outside jenkins, keeping current role"
        return
    fi
    role_arn="arn:aws:iam::$aws_actid:role/$aws_role"
    echo "Switching to "$aws_role" role... please wait..."
    aws sts assume-role --role-arn $role_arn --role-session-name $aws_role >credentials.tmp
    if [[ $? == 0 ]]; then
        user_access_key_id=$AWS_ACCESS_KEY_ID
        user_secret_access_key=$AWS_SECRET_ACCESS_KEY
        user_session_token=$AWS_SESSION_TOKEN
        aws-set-session-variables
        rm -f credentials.tmp
        echo -e "\nSuccessfully switched to "$aws_role
    else
        echo -e "\nFailed to switch role, check if you have access to "$aws_role" role on AWS console or contact admin\n"
        exit
    fi
}

aws-set-session-variables() {
    export AWS_ACCESS_KEY_ID=$(cat credentials.tmp | jq -r .Credentials.AccessKeyId)
    export AWS_SECRET_ACCESS_KEY=$(cat credentials.tmp | jq -r .Credentials.SecretAccessKey)
    export AWS_SESSION_TOKEN=$(cat credentials.tmp | jq -r .Credentials.SessionToken)
}

aws-switch-back-to-user() {
    if [ ! -z "$AWS_SESSION_TOKEN" -a "$AWS_SESSION_TOKEN" != " " ]; then
        export AWS_ACCESS_KEY_ID=$user_access_key_id
        export AWS_SECRET_ACCESS_KEY=$user_secret_access_key
        export AWS_SESSION_TOKEN=$user_session_token
    fi
}

aws-stop-session() {
    unset AWS_ACCESS_KEY_ID
    unset AWS_SECRET_ACCESS_KEY
    unset AWS_SESSION_TOKEN
}

aws-create-deployment() {
    # This function will accept 5 mandatory arguments as described below
    # Arg1 - project name
    # Arg2 - environment name
    # Arg3 - Service name
    # Arg4 - Build number
    # Arg5 - Artifacts bucket

    app_name="${1}-${2}-${3}"
    echo "Preparing deployment for ${app_name}"
    deployment_id=$(aws deploy create-deployment \
        --application-name "${app_name}" \
        --deployment-config-name CodeDeployDefault.AllAtOnce \
        --deployment-group-name "${app_name}-group" \
        --ignore-application-stop-failures \
        --s3-location bucket=${5},bundleType=zip,key="${3}/${3}-${4}.zip" | jq -r ".deploymentId")

    echo "Scheduled deployment for ${app_name}"
    status=$(aws deploy get-deployment --deployment-id ${deployment_id} | jq -r ".deploymentInfo.status")

    while [ "$status" == "InProgress" -o "$status" == "Created" -o "$status" == "Queued" -o "$status" == "Ready" ]; do
        echo "${3} deployment is in progress... Please wait..."
        sleep 30s
        status=$(aws deploy get-deployment --deployment-id ${deployment_id} | jq -r ".deploymentInfo.status")
    done

    if [ "$status" == "Succeeded" ]; then
        echo "${3} deployment Completed Successfully!!!"
    else
        echo "${3} deployment failed with status - ${status}. Please check code deploy logs for this id $deployment_id"
        exit 1
    fi
}

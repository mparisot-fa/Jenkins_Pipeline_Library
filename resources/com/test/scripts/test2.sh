#! /usr/bin/env bash

echo "Here are the parameters:"
echo -- -----
echo "$@"
echo -- -----
echo
echo "Here is the env:"
echo -- -----
env
echo -- -----
echo
echo "Here are the declared functions"
echo -- -----
declare -f
echo -- -----

echo "Sourcing ..."
source functions
echo "Using myecho:"
myecho "Youpi"

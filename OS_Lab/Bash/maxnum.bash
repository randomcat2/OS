#!/bin/bash

# Script to find the largest of two numbers
echo "Enter the first number:"
read num1
echo "Enter the second number:"
read num2

if [ $num1 -gt $num2 ]; then
    echo "The largest number is $num1"
else
    echo "The largest number is $num2"
fi

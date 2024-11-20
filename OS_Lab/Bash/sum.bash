#!/bin/bash

# Script to calculate the sum of numbers from 1 to 100
sum=0

for ((i=1; i<=100; i++)); do
    sum=$((sum + i))
done

echo "The sum of numbers from 1 to 100 is: $sum"


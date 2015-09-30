#!/bin/bash
#===============================================================================
#
#          FILE:  UI2.0Regression.sh
# 
#         USAGE:  ./UI2.0Regression.sh 
# 
#   DESCRIPTION:  
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.2
#       CREATED:  07/21/2015  8:59:19 AM EDT
#      REVISION:  Added functions 
#===============================================================================

JMETERHOME=/cygdrive/c/Users/Manish/Documents/apache-jmeter-2.12/bin
scriptdirHOME=/cygdrive/c/Users/Manish/Desktop/WorkSpace/Scripts
MAVENHOME=/cygdrive/c/Users/Manish/workspace/apache-maven-3.3.3/bin/
NOW=$(date +"%m-%d-%H-%M.%S")

################################################ Function Scripts ##################################################

cd /cygdrive/c/Users/Manish/Desktop/WorkSpace/RegressionTesting/UI2.0/WebDriver/
echo
echo
echo "Starting....  Please wait"
echo
echo

java -jar ResetDeviceTargeting.jar

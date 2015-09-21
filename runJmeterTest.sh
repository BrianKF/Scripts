#!/bin/bash
#===============================================================================
#
#          FILE:  runJmeterTest.sh
# 
#         USAGE:  ./runJmeterTest.sh 
# 
#   DESCRIPTION:  
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.0
#       CREATED:  04/ 8/2015  2:10:52 PM EDT
#      REVISION:  ---
#===============================================================================
echo

JMETERHOME=/cygdrive/e/Jmeter/apache-jmeter-2.13/apache-jmeter-2.13/bin
scriptdirHOME=/cygdrive/c/Users/Manish/Desktop/WorkSpace/Scripts

echo
echo "Please note this script is set to hit bidder 02 (IP: 10.0.0.223).  If this is not your intention, modify the script."
echo
read -p "Enter bidder ip address: " bidder
echo
echo "Setting FilterLog to ON ..."

#curl http://$bidder:9999/?filterlog=on

mv $scriptdirHOME/Jmeter/files/*.json $scriptdirHOME/Jmeter/files/backup/

cd Jmeter/files
rm jmeter.log

echo
echo
echo "Starting Jmeter Script.  Please wait..."
echo
echo


$JMETERHOME/jmeter.bat -n -t "E:\Jmeter\testFiles\jmeterTestPlans\SanityAllAdExchanges.jmx"

find . -maxdepth 1 -size 0c -exec rm {} \;


echo
echo
read -p "Continue to testing responses? (y/n) " response

if [ "$response" == "y" ]; then

echo
echo
echo "Starting the fire all responses script"
$scriptdirHOME/fireAllImpWinLoss.sh

else
echo
echo
echo "Done.  Thank you for your participation."

fi

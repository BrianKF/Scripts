#!/bin/bash
#===============================================================================
#
#          FILE:  getResponseFiles.sh
# 
#         USAGE:  ./getResponseFiles.sh
# 
#   DESCRIPTION:  Gets files created by Response JMeter and sorts them for testing.
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.0
#       CREATED:  04/ 3/2015 11:11:19 AM EDT
#      REVISION:  ---
#===============================================================================

cd Responses
mv /cygdrive/c/Users/Manish/Documents/apache-jmeter-2.12/bin/*.json .
find . -maxdepth 1 -size 0c -exec rm {} \;
cd ..
mv /cygdrive/c/Users/Manish/Desktop/WorkSpace/Scripts/Responses/San* .

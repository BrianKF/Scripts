#!/bin/bash
#===============================================================================
#
#          FILE:  cleanJmeterLogs.sh
# 
#         USAGE:  ./cleanJmeterLogs.sh 
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
#       CREATED:  07/10/2015  3:28:38 PM EDT
#      REVISION:  ---
#===============================================================================

./getResponseFiles.sh
cd Responses
ls -ltr
rm *

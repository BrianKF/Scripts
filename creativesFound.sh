#!/bin/bash
#===============================================================================
#
#          FILE:  creativesFound.sh
# 
#         USAGE:  ./creativesFound.sh 
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
#       CREATED:  11/18/2015  3:30:58 PM EST
#      REVISION:  ---
#===============================================================================

#ResponsesFound=`ls -ltr San* |awk '{print $9}'`
ls -ltr San* | awk '{ print $9 }' > ResponsesFound
echo $ResponsesFound
crid=`cat $ResponsesFound |sed s'/^.*&adExchange=//' |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
echo $crid
rm ResponsesFound
rm crid

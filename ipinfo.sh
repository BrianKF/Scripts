#!/bin/bash
#===============================================================================
#
#          FILE:  ipinfo.sh
# 
#         USAGE:  ./ipinfo.sh 
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
#       CREATED:  10/29/2015  8:58:43 AM EDT
#      REVISION:  ---
#===============================================================================

data=$1

echo
curl ipinfo.io/$data
echo


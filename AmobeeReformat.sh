#!/bin/bash
#===============================================================================
#
#          FILE:  AmobeeReformat.sh
# 
#         USAGE:  ./AmobeeReformat.sh 
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
#       CREATED:  08/26/2015 10:15:23 AM EDT
#      REVISION:  ---
#===============================================================================

echo
file=$1

grep VAST $file
if  [ $? -eq 0 ]
    then
echo
echo
echo "This is a Amobee VAST file.  Using AmobeeVAST Formant Script"
./AmobeeVASTreformat.sh $file

else

grep function $file
if  [ $? -eq 0 ]
    then
echo
echo
echo "This is a Amobee Rich Media file.  Using AmobeeVAST Formant Script"
./AmobeeRichMediaReformat.sh $file

else

echo "This is a Amobee banner.  Using the AmobeeBanner reformat Script."
./AmobeeBannerReformat.sh $file

fi
fi

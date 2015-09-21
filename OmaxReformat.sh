#!/bin/bash
#===============================================================================
#
#          FILE:  bannerReformat.sh
# 
#         USAGE:  ./bannerReformat.sh file
# 
#   DESCRIPTION:  Reformats the banner response file
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.0
#       CREATED:  04/ 6/2015  2:21:52 PM EDT
#      REVISION:  ---
#===============================================================================

echo
file=$1

grep VAST $file
if  [ $? -eq 0 ]
    then
echo
echo
echo "This is a VAST file.  Using VAST Formant Script"
./OmaxVASTreformat.sh $file

else

echo
echo "Using the banner Response file:"
echo
echo

./OmaxBannerReformat.sh $file



fi

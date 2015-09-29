#!/bin/bash
#===============================================================================
#
#          FILE:  formatResponse.sh
# 
#         USAGE:  ./formatResponse.sh file
# 
#   DESCRIPTION:  Checks for Exchange and runs appropriate script
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.0
#       CREATED:  04/ 6/2015 11:00:50 AM EDT
#      REVISION:  ---
#===============================================================================
file=$1

TripleLift=$(grep TripleLift $file)
if [ $? -eq 0 ]
    then
	echo "Found TripleLift Response.  Using TripleLift Reformat script."
	./TripleLiftReformat.sh $file
else

LiveRail=$(grep LiveRail $file)
if [ $? -eq 0 ]
    then
	echo "Found LiveRail Response.  Using LiveRail Reformat script."
	./VASTreformatLiveRail.sh $file
else

AerServ=$(grep AerServ $file)
if [ $? -eq 0 ]
    then
	echo "Found AerServ Response.  Using AerServ Reformat script."
	./AerServReformat.sh $file
else

Amobee=$(grep Amobee $file)
if [ $? -eq 0 ]
    then
	echo "Found Amobee Response.  Using Banner Reformat script."
	./AmobeeReformat.sh $file
else

Nexage=$(grep Nexage $file)
if [ $? -eq 0 ]
then
        echo "Found Nexage Response.  Using banner Reformat script."
	./nexageReformat.sh $file
else

Pubmatic=$(grep Pubmatic $file)
if [ $? -eq 0 ]
then
        echo "Found Pubmatic VAST Response.  Using VAST Reformat script."
	./PubmaticVASTreformat.sh $file
else

Smaato=$(grep Smaato $file)
if [ $? -eq 0 ]
then
        echo
	echo "Found Smaato Response.  Using Smaato Reformat script."
	./SmaatoReformat.sh $file
else

Adaptv=$(grep Adaptv $file)
if [ $? -eq 0 ]
then
        echo "Found Adaptv VAST Response.  Using Adaptv VAST Reformat script."
	./AdaptvVASTreformat.sh $file
else

SpotX=$(grep SpotX $file)
if [ $? -eq 0 ]
then
        echo "Found SpotX VAST Response.  Using SpotX VAST Reformat script."
	./SpotXVASTReformat.sh $file
else


MoPub=$(grep MoPub $file)
if [ $? -eq 0 ]
then
        echo "Found MoPub Response.  Using Banner Reformat script."
	./bannerReformat.sh $file
else

OMAX=$(grep OMAX $file)
if [ $? -eq 0 ]
then
        echo "Found OMAX Response.  Using Banner Reformat script."
	./OmaxReformat.sh $file
else

Rubicon=$(grep Rubicon $file)
if [ $? -eq 0 ]
then
        echo "Found Rubicon Response.  Using Rubicon Reformat script."
	./RubiconRichMediaReformat.sh $file
else

OpenX=$(grep OpenX $file)
if [ $? -eq 0 ]
then
        echo "Found OpenX Response.  Using OpenX Reformat script."
	./OpenXReformat.sh $file
else

Vdopia=$(grep Vdopia $file)
if [ $? -eq 0 ]
then
echo "Found Vdopia Response.  Using Vdopia Reformat script."
	./VdopiaReformat.sh $file

fi
fi
fi
fi
fi
fi
fi
fi
fi
fi
fi
fi
fi
fi

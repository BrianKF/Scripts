#!/bin/bash
#===============================================================================
#
#          FILE:  fireAllImpWinLoss.sh
# 
#         USAGE:  ./fireAllImpWinLoss.sh 
# 
#   DESCRIPTION:  Fires impressions for all Response in the MoPub directory 
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.0
#       CREATED:  04/ 3/2015 11:16:15 AM EDT
#      REVISION:  ---
#===============================================================================

#cd /cygdrive/c/Users/Manish/Desktop/WorkSpace/Scripts/Jmeter/test/


#JmeterScriptdir="/cygdrive/c/Documents and Settings/Manish/Desktop/WorkSpace/Scripts"

#cd Jmeter/files
#count=`ls -ltr San* | awk '{ print $9 }'`
ls -ltr San* | awk '{ print $9 }' > count
echo
total=`cat count|grep Sanity |wc |awk '{print $1}'`

#total=`cat count|grep *.json |wc`
echo
read -p "I found $total responses and will launch all impressions.  Are you sure???  This may take a WHILE! Press Enter to continue, CNTRL+C to cancel: " enter


#responses=`ls -ltr Sanit* | awk '{ print $9 }'`
ls -ltr Sanit* | awk '{ print $9 }' > newresponses

while read line
do

TripleLift=$(grep TripleLift $line)
if [ $? -eq 0 ]
    then
        echo "Found TripleLift Response.  Using TripleLift Reformat script."
        ./TripleLiftReformat.sh $line
else

LiveRail=$(grep LiveRail $line)
if [ $? -eq 0 ]
    then
        echo "Found LiveRail Response.  Using LiveRail Reformat script."
        ./VASTreformatLiveRail.sh $line
else

AerServ=$(grep AerServ $line)
if [ $? -eq 0 ]
    then
        echo "Found AerServ Response.  Using AerServ Reformat script."
        ./AerServReformat.sh $line
else

Amobee=$(grep Amobee $line)
if [ $? -eq 0 ]
    then
        echo "Found Amobee Response.  Using Banner Reformat script."
        ./AmobeeReformat.sh $line
else

Nexage=$(grep Nexage $line)
if [ $? -eq 0 ]
then
        echo "Found Nexage Response.  Using banner Reformat script."
        ./nexageReformat.sh $line
else

PubMatic=$(grep PubMatic $line)
if [ $? -eq 0 ]
then
        echo "Found Pubmatic VAST Response.  Using VAST Reformat script."
        ./PubMaticReformat.sh $line
else

Smaato=$(grep Smaato $line)
if [ $? -eq 0 ]
then
        echo
        echo "Found Smaato Response.  Using Smaato Reformat script."
        ./SmaatoReformat.sh $line
else

Adaptv=$(grep Adaptv $line)
if [ $? -eq 0 ]
then
        echo "Found Adaptv VAST Response.  Using Adaptv VAST Reformat script."
        ./AdaptvVASTreformat.sh $line
else

SpotX=$(grep SpotX $line)
if [ $? -eq 0 ]
then
        echo "Found SpotX VAST Response.  Using SpotX VAST Reformat script."
        ./SpotXVASTReformat.sh $line
else


MoPub=$(grep MoPub $line)
if [ $? -eq 0 ]
then
        echo "Found MoPub Response.  Using Banner Reformat script."
        ./bannerReformat.sh $line
else

OMAX=$(grep OMAX $line)
if [ $? -eq 0 ]
then
        echo "Found OMAX Response.  Using Banner Reformat script."
        ./OmaxReformat.sh $line
else

Rubicon=$(grep Rubicon $line)
if [ $? -eq 0 ]
then
        echo "Found Rubicon Response.  Using Rubicon Reformat script."
        ./RubiconRichMediaReformat.sh $line
else

OpenX=$(grep OpenX $line)
if [ $? -eq 0 ]
then
        echo "Found OpenX Response.  Using OpenX Reformat script."
        ./OpenXReformat.sh $line
else

Vdopia=$(grep Vdopia $line)
if [ $? -eq 0 ]
then
echo "Found Vdopia Response.  Using Vdopia Reformat script."
        ./VdopiaReformat.sh $line

else

LKQD=$(grep LKQD $line)
if [ $? -eq 0 ]
then
echo "Found LKQD Response.  Using Vdopia Reformat script."
        ./LKQDReformat.sh $line

else

AdX=$(grep AdX $line)
if [ $? -eq 0 ]
then
        ./AdXReformat.sh $line

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
fi
fi


done < newresponses
rm count newresponses


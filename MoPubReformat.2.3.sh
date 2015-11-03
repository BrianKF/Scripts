#!/bin/bash
#===============================================================================
#
#          FILE:  MoPubReformat.sh
# 
#         USAGE:  ./MoPubReformat.sh file
# 
#   DESCRIPTION:  Reformats the response file
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

echo
echo "Using the Native 2.3 script"
echo

#WebPage for comparing Banner
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &
rm url

#Strip WinLoss URL
adid=`cat $file |sed s'/.*adid//'|cut -d , -f1|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
end=`cat $file|sed s'/.*&lId//'|sed s'/"adm".*//'|cut -d , -f-1|sed s'/.$//'`

echo "https://uatwins.adtheorent.com/Wins?price=1&impId=1&adId=" $adid "&lId" $end |awk '{ print $1 $2 $3 $4 $5 $6}' > WinLossURL

#echo "https://uatwins" $head $imp $adid $crid $end |awk '{print $1 $2 $3 $4 $5 $6}' > WinLossURL
echo
cat WinLossURL
curl `cat WinLossURL`

#Strip Impression URL
sleep 2
body=`cat $file | sed 's/^.*uatimps//'|cut -d , -f1|sed s'/.$//'`
echo "https://uatimps" $body |awk '{print $1 $2}' > ImpressionURL
echo
echo
cat ImpressionURL
curl `cat ImpressionURL`
rm WinLossURL ImpressionURL

#Strip ClickTracker
clickend=`cat $file |sed s'/.*uatclicks//'|cut -d , -f1|sed s'/.$//'|sed s'/.$//'`
echo "http://uatclicks" $clickend |awk '{print $1 $2}' > ClickTrakerURL
echo
echo
cat ClickTrakerURL
curl `cat ClickTrakerURL`
sleep 2
rm ClickTrakerURL


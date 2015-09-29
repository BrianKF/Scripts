#!/bin/bash
#===============================================================================
#
#          FILE:  TripleLiftReformat.sh
# 
#         USAGE:  ./TripleLiftReformat.sh 
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
#       CREATED:  07/ 6/2015  8:27:16 AM EDT
#      REVISION:  ---
#===============================================================================

file=$1

echo "TripleLift response.  Using the Response file:"
echo
echo
cat $file
echo
echo

#WebPage for comparing Banner
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&adThdId.*//'`
CreativeID=`cat $file |sed 's/.*crId//'|sed s'/&adExchange.*//'|sed s'/^.//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
#crid=`cat $file|sed 's/.*crId//'|sed 's/&adExchange.*//'|sed s'/^.//'`
#sleep 2
echo
echo
cat url
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &


#Extract Impression URL
cat $file |sed 's/^.*uatimps//' |cut -d , -f1|sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
sleep 2
echo
cat impressionURL
echo
curl `cat impressionURL`
sleep 2

#Code to launch WinLoss URL
body=`cat $file|sed 's/^.*{AUCTION_BID_ID}//'|cut -d , -f1|sed s'/.$//'`
adid=`cat $file |sed 's/.*adId//'|sed 's/lId.*//'|sed s'/^.//'|sed s'/.$//'`
echo "http://uatwins.adtheorent.com/Wins?price=1&impId=1&adId=" $adid $body |awk '{ print $1 $2 $3}' > WinLossURL
sleep 2
cat WinLossURL
curl `cat WinLossURL`
sleep 2

#Extract ClickUrl

cat $file|sed 's/^.*uatclicks//'|cut -d , -f1 |sed s'/.$//'|awk '{print "https://uatclicks" $1}' > ClickURL
echo
cat ClickURL
curl `cat ClickURL`
rm WinLossURL url impressionURL ClickURL
sleep 2


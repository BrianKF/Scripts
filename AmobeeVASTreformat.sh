#!/bin/bash
#===============================================================================
#
#          FILE:  VASTreformt.sh
# 
#         USAGE:  ./VASTreformt.sh 
# 
#   DESCRIPTION:  Reformats the Request into a testable VAST format and launches browser
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.0
#       CREATED:  04/ 3/2015 11:07:20 AM EDT
#      REVISION:  ---
#===============================================================================

file=$1
echo "Using the Response file:"
echo
echo
cat $file

echo
echo
cat $file |sed 's/.*"adm"//'|sed 's/"iurl".*//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g" > Results/$file.VAST
cat $file |sed 's/.*"adm"//'|sed 's/"iurl".*//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"

#WebPage for comparing VAST
echo
echo
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&action.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/lId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/cId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/adExchange.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
cat $file |sed 's/^.*uatimps//' | sed 's/Impression.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
echo
echo
cat impressionURL
curl `cat impressionURL`
sleep 2


#Code to launch WinLoss URL
body=`cat $file |sed 's/^.*AUCTION_AD_ID}//'|cut -d , -f1 |sed 's/adm.*//'|sed s'/.$//'`
adid=`cat $file |sed 's/.*adid//' |cut -d , -f1|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
insert="&impId=1&adId="
echo
echo
echo "https://uatwins.adtheorent.com/Wins?price=1" $insert $adid $body |awk '{ print $1 $2 $3 $4}' > WinLossURL
cat WinLossURL
curl `cat WinLossURL`
sleep 2

#Catch click URL
body2=`cat $file |sed 's/^.*uatclicks//'|cut -d ">" -f1|sed s'/.$//'|sed s'/.$//'` 

echo
echo
echo "https://uatclicks" $body2 |awk '{print $1 $2}' > ClickURL

cat ClickURL
curl `cat ClickURL`
sleep 2

rm WinLossURL impressionURL url ClickURL

#!/bin/bash
#===============================================================================
#
#          FILE:  VASTreformtLiveRail.sh
# 
#         USAGE:  ./VASTreformtLiveRail.sh filename
# 
#   DESCRIPTION:  Reformats file into VAST format for LiveRail testing
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.0
#       CREATED:  04/ 3/2015 10:58:52 AM EDT
#      REVISION:  ---
#===============================================================================

echo
file=$1
echo "Using the Response file:"
echo
echo
cat $file

#read -p "Enter VAST filename: " file
echo
echo
cat $file |sed 's/.*"adm"//'|sed 's/"adomain".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'| sed "s/\\\\\"/\"/g" > Results/$file.VAST
cat $file |sed 's/.*"adm"//'|sed 's/"adomain".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'| sed "s/\\\\\"/\"/g" 
echo
echo

#WebPage for comparing VAST
#crid=`cat $file |sed 's/.*crid//'|awk '{print $1}'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
sleep 2
#echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=LiveRail" |awk '{print $1 $2 $3}' > url
#echo
#echo
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url

echo
cat url

"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
cat $file |sed 's/^.*uatimps//' | sed 's/Impression.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
sleep 2
echo
echo
cat impressionURL
curl `cat impressionURL`
sleep 2

#Code to launch WinLoss URL
cat $file|sed 's/.*$WINNING_PRICE//'|cut -d ] -f1|awk '{print "https://uatwins.adtheorent.com/Wins?ratio=1" $1}' > WinLossURL
sleep 2
echo
echo
cat WinLossURL
curl `cat WinLossURL`
sleep 2
#Extract CliclURL
cat $file|sed 's/.*uatclicks//'|cut -d ] -f1|awk '{print "https://uatclicks" $1}' > ClickURL
echo
echo
cat ClickURL
curl `cat ClickURL`

rm WinLossURL impressionURL url ClickURL


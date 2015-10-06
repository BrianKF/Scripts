#!/bin/bash
#===============================================================================
#
#          FILE:  PubmaticVASTreformt.sh
# 
#         USAGE:  ./PubmaticVASTreformt.sh file
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

#read -p "Enter VAST filename: " file
echo
echo
cat $file |cut -d : -f11- |cut -d "<" -f2-|awk '{ print "<" $0 }' | sed "s/\\\\\"/\"/g"|cut -d , -f1| sed s'/.$//' > Results/$file.VAST
cat $file |cut -d : -f11- |cut -d "<" -f2-|awk '{ print "<" $0 }' | sed "s/\\\\\"/\"/g"|cut -d , -f1| sed s'/.$//'

#WebPage for comparing VAST
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo
echo
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url

"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &
cat url

#Code to launch Impression URL
cat $file |sed 's/^.*uatimps//' | sed 's/Impression.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
sleep 2
echo
cat impressionURL
curl `cat impressionURL`
sleep 2


#Code to launch WinLoss URL
body=`cat $file|sed 's/^.*{AUCTION_ID}//'|cut -d ] -f1`
echo
echo "https://uatwins.adtheorent.com/Wins?price=1&impId=1" $body |awk '{ print $1 $2 $3 }' > WinLossURL &
sleep 2
cat WinLossURL
curl  `cat WinLossURL`


#Code to launch Click URL
cat $file |sed 's/^.*uatclicks//'|cut -d ] -f1 |awk '{print "https://uatclicks" $0 }' > ClickURL
sleep 2
echo
cat ClickURL
curl `cat ClickURL`
rm WinLossURL url impressionURL ClickURL


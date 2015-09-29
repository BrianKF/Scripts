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
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" "https://developers.google.com/interactive-media-ads/docs/vastinspector_dual?hl=it" &
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" "https://developers.google.com/interactive-media-ads/docs/vastinspector_dual?hl=it" &

#WebPage for comparing VAST
#crid=`cat $file |sed 's/.*crid//'|cut -d , -f1|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
#site="&SiteApp=SITE"
#sleep 2
#echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE" |awk '{print $1 $2 $3}' > url
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &
echo
echo
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
cat $file |sed 's/^.*uatimps//' | sed 's/Impression.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
echo
echo
cat impressionURL
#curl `cat impressionURL`
sleep 2
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat impressionURL`
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat impressionURL` &


#Code to launch WinLoss URL
body=`cat $file |sed 's/^.*AUCTION_AD_ID}//'|cut -d , -f1 |sed 's/adm.*//'|sed s'/.$//'`
adid=`cat $file |sed 's/.*adid//' |cut -d , -f1|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
insert="&impId=1&adId="
echo
echo
echo "https://uatwins.adtheorent.com/Wins?price=1" $insert $adid $body |awk '{ print $1 $2 $3 $4}' > WinLossURL
cat WinLossURL
#curl `cat WinLossURL`
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat WinLossURL`
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat WinLossURL`
sleep 2

#Catch click URL
body2=`cat $file |sed 's/^.*uatclicks//'|cut -d ">" -f1|sed s'/.$//'|sed s'/.$//'` 

echo
echo
echo "https://uatclicks" $body2 |awk '{print $1 $2}' > ClickURL

cat ClickURL
#curl `cat ClickURL`
sleep 2

rm WinLossURL impressionURL url ClickURL

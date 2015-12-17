#!/bin/bash
#===============================================================================
#
#          FILE:  PubMaticReformat.sh
# 
#         USAGE:  ./PubMaticReformat.sh 
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
#       CREATED:  10/ 2/2015  9:16:48 AM EDT
#      REVISION:  ---
#===============================================================================

echo
file=$1

cat $file

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*"adm"//'|sed s'/^.//'|sed s'/^.//'|sed s'/"adomain".*//'|sed s'/.$//'|sed s'/.$//'| sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.banner.html
cat $file |sed 's/.*"adm"//'|sed s'/^.//'|sed s'/^.//'|sed s'/"adomain".*//'|sed s'/.$//'|sed s'/.$//'| sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
echo
echo
cat launch.html
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &

#WebPage for comparing Banner
AdExchange=`cat $file |sed s'/^.*adExchange=//' |sed s'/impId.*//'|sed s'/.$//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*sId//'|sed 's/cId.*//'|sed s'/.$//'`
LineItemID=`cat $file |sed 's/^.*lId//'|sed 's/sId.*//'|sed s'/.$//'`
CampaignID=`cat $file |sed 's/^.*cId//'|sed 's/crId.*//'|sed s'/.$//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "&StrategyID" $StrategyID "&LineItemID" $LineItemID "&CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
sleep 2
#echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4}' > url
echo
echo
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &

#Code to launch Impression URL
sleep 2
cat $file| sed 's/^.*uatimps//' |awk '{print $1}'|sed s'/.$//' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
echo
cat impressionURL
curl `cat impressionURL`

sleep 2
#Code to launch WinLoss URL
body=` cat $file |sed s'/^.*AUCTION_ID}//'|cut -d " " -f1|sed s'/.$//'`
adid=`cat $file|sed 's/.*"adid//'|cut -d , -f1|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
insert="&impId=1&adId="
echo "https://uatwins.adtheorent.com/Wins?price=1" $insert $adid $body |awk '{ print $1 $2 $3 $4 }' > WinLossURL
echo
echo
cat WinLossURL
curl `cat WinLossURL`

sleep 2

# extract ClickURL
cat $file|sed 's/.*uatclicks//'|sed 's/.*uatclicks//'|cut -d , -f1|sed s'/.$//'|sed s'/.$//'|cut -d " " -f1|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|awk '{print "https://uatclicks" $1}' > clickURL
echo
cat clickURL
curl `cat clickURL`

rm WinLossURL launch.html url impressionURL clickURL


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

grep VAST $file
if  [ $? -eq 0 ]
    then
echo

echo "Using the Response file:"
echo
echo
cat $file

echo
echo
cat $file |sed 's/.*"adm"//'|sed 's/"cid".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'| sed "s/\\\\\"/\"/g" > Results/$file.VAST
cat $file |sed 's/.*"adm"//'|sed 's/"cid".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'| sed "s/\\\\\"/\"/g"
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" "https://developers.google.com/interactive-media-ads/docs/vastinspector_dual?hl=it" &

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
cat url
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
cat $file |sed 's/^.*uatimps//' | sed 's/Impression.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
echo
echo
cat impressionURL
curl `cat impressionURL`
sleep 2


#Code to launch WinLoss URL
body=`cat $file |sed s'/.*AUCTION_AD_ID}//'|sed s'/adm.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'`
adid=`cat $file |sed 's/.*adid//'|sed s'/price.*//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'`
insert="&impId=1&adId="
echo
echo "https://uatwins.adtheorent.com/Wins?price=1" $insert $adid $body |awk '{ print $1 $2 $3 $4 }' > WinLossURL
cat WinLossURL
curl `cat WinLossURL`
sleep 2

#Extract Click URL
cat $file |sed s'/^.*uatclicks//'|cut -d ] -f1|awk '{print "https://uatclicks" $1}' > clickURL
echo
cat clickURL
curl `cat clickURL`
echo
sleep 2

rm WinLossURL impressionURL url clickURL

else

echo
echo
cat $file
echo
echo

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*"adm"//'|sed 's/"cid".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.banner.html
cat $file |sed 's/.*"adm"//'|sed 's/"cid".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
echo
echo
cat launch.html
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &
sleep 2

#WebPage for comparing Banner
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
echo
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
sleep 2
cat $file| sed 's/^.*uatimps//' |awk '{print $1}'| sed s'/.$//'|sed s'/.$//' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
cat impressionURL
curl `cat impressionURL`

sleep 2
#Code to launch WinLoss URL
end=`cat $file|sed 's/^.*AUCTION_AD_ID}//'|sed 's/adm.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'`

echo "https://uatwins.adtheorent.com/Wins?price=1" $end |awk '{print $1 $2}' > WinLossURL
echo
cat WinLossURL
curl `cat WinLossURL`

#Extract Click URL
cat $file |sed s'/^.*uatclicks//'|cut -d ] -f1|awk '{print "https://uatclicks" $1}' |sed s'/.$//'> clickURL
echo
cat clickURL
curl `cat clickURL`
echo

rm launch.html url impressionURL WinLossURL clickURL
fi


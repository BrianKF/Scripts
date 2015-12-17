#!/bin/bash
#===============================================================================
#
#          FILE:  OpenXReformat.sh
# 
#         USAGE:  ./OpenXReformat.sh 
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
#       CREATED:  05/ 6/2015  3:27:31 PM EDT
#      REVISION:  ---
#===============================================================================

echo
file=$1

grep VAST $file
if  [ $? -eq 0 ]
    then

echo "Using the Response file:"
echo
echo
cat $file

echo
echo
cat $file |cut -d : -f15- |cut -d "<" -f2-|awk '{ print "<" $0 }' | sed "s/\\\\\"/\"/g"|cut -d , -f1| sed s'/.$//' > Results/$file.VAST.xml


#WebPage for comparing VAST
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
echo
echo
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
cat $file |sed 's/^.*uatimps//' |awk '{print $1}' |sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
sleep 4
cat impressionURL
curl `cat impressionURL`
sleep 2


#Code to launch WinLoss URL
body=`cat $file|sed s'/.*winning_price//'|cut -d " " -f1|sed s'/.$//'|sed s'/^.//'`
echo
echo
echo "https://uatwins.adtheorent.com/WinLoss/Channels/AdExchanges/OpenX?price=AAABRm1HY3gQULKaLb4ibNY1V5bnHvy7BtEArA" $body |awk '{print $1 $2}' > WinLossURL &
sleep 5
cat WinLossURL
curl `cat WinLossURL`
rm WinLossURL


else

echo "Using the Response file:"
echo
echo
cat $file
echo
#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*adm//'|sed 's/adomain.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.banner.html
cat $file |sed 's/.*adm//'|sed 's/adomain.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" ./launch.html &
echo
echo
cat launch.html
#WebPage for comparing Banner
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
echo
echo
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &


#Code to launch Impression URL
sleep 2
cat $file| sed 's/^.*uatimps//' |awk '{print $1}'| sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
echo
echo
cat impressionURL
curl `cat impressionURL`

sleep 2
#Code to launch WinLoss URL



grep AUCTION_ID} $file
if  [ $? -eq 0 ]
    then

body=`cat $file|sed s'/.*AUCTION_ID}//'|cut -d " " -f1|sed s'/.$//'`
echo
echo
echo "https://uatwins.adtheorent.com/Wins?price=AAABRm1HY3gQULKaLb4ibNY1V5bnHvy7BtEArA&impId=1&" $body |awk '{ print $1 $2 $3}' > WinLossURL
cat WinLossURL
curl `cat WinLossURL`

else 



body=`cat $file|sed s'/.*winning_price}//'|cut -d " " -f1|sed s'/.$//'`
echo
echo
echo "https://uatwins.adtheorent.com/Wins?price=AAABRm1HY3gQULKaLb4ibNY1V5bnHvy7BtEArA" $body |awk '{ print $1 $2 $3}' > WinLossURL
cat WinLossURL
curl `cat WinLossURL`

#Extract ClickURL
cat $file |sed 's/^.*uatclicks//'|cut -d } -f1|sed s'/.$//'|sed s'/.$//' |awk '{print "https://uatclicks" $1}'|sed s'/.$//'  > ClickURL
echo
cat ClickURL
curl `cat ClickURL`
sleep 2
rm launch.html WinLossURL url impressionURL ClickURL


fi
fi

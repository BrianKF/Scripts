#!/bin/bash
#===============================================================================
#
#          FILE:  RubiconRichMediaReformat.sh
# 
#         USAGE:  ./RubiconRichMediaReformat.sh file
# 
#   DESCRIPTION:  Formats RichMedia file and launches Imppression link
# 
#       OPTIONS:  ---
#  REQUIREMENTS:  ---
#          BUGS:  ---
#         NOTES:  ---
#        AUTHOR:  Brian Frazier (), Brian.Frazier@AdTheorent.com
#       COMPANY:  AdTheorent
#       VERSION:  1.0
#       CREATED:  04/ 3/2015 10:56:25 AM EDT
#      REVISION:  ---
#===============================================================================

echo
file=$1
echo "Using the Response file:"
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*adm//'|sed 's/adomain.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g" > Results/$file.banner.html
cat $file |sed 's/.*adm//'|sed 's/adomain.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g" |awk '{print "<html><body>" $0 "</body></html>"}'> launch.html

echo
echo
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" ./launch.html &
cat launch.html

#WebPage for comparing Banner
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
echo
echo
cat url
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &


#Code to launch Impression URL
sleep 2
impCheck=$(grep uatimps.adtheorent.com $file)
if [ $? -eq 0 ]
    then

cat $file |sed 's/^.*uatimps//'|awk '{print $1}'|sed s'/.$//' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
cat impressionURL
curl `cat impressionURL`
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat impressionURL` &
#Code to launch WinLoss URL
body=`cat $file|sed 's/.*{AUCTION_ID}//'|cut -d " " -f1|sed s'/.$//'|sed s'/.$//'`
insert="&impId=1"
echo
echo "https://uatwins.adtheorent.com/Wins?price=7B41A0380FCBC50F" $insert $body |awk '{ print $1 $2 $3 }' > WinLossURL
cat WinLossURL
echo
echo
#curl `cat WinLossURL`
sleep 2
#Extract ClickURL
cat $file| sed 's/.*uatclicks//'|cut -d , -f1|sed s'/.$//'|awk '{print "https://uatclicks" $1}' > ClickURL
cat ClickURL
curl `cat ClickURL`
sleep 2
rm launch.html WinLossURL url impressionURL ClickURL

sleep 2

else
#Code to launch WinLoss URL
body=`cat $file|sed 's/.*{AUCTION_ID}//'|cut -d " " -f1|sed s'/.$//'|sed s'/.$//'`
insert="&impId=1"
echo
echo "https://uatwins.adtheorent.com/Wins?price=7B41A0380FCBC50F" $insert $body |awk '{ print $1 $2 $3 }' > WinLossURL
cat WinLossURL
#curl `cat WinLossURL`
echo
echo

sleep 2
#Extract ClickURL
#cat $file| sed 's/.*uatclicks//'|cut -d " " -f1|sed s'/.$//'|sed s'/.$//'|awk '{print "https://uatclicks" $1}' > ClickURL
#cat ClickURL
#sleep 2
rm launch.html WinLossURL url

fi

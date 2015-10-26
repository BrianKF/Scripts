#!/bin/bash
#===============================================================================
#
#          FILE:  bannerReformat.sh
# 
#         USAGE:  ./bannerReformat.sh file
# 
#   DESCRIPTION:  Reformats the banner response file
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

grep native $file
if  [ $? -eq 0 ]
    then
echo
echo
./MoPubReformat.2.3.sh $file

else

grep VAST $file
if  [ $? -eq 0 ]
    then
echo
echo
echo "This is a VAST file.  Using VAST Formant Script"
./VASTreformat.2.3.sh $file

else

echo
echo "Using the banner Response file:"
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*"adm"//'|sed s'/^.//'|sed s'/^.//'|sed s'/"adomain".*//'|sed s'/.$//'|sed s'/.$//'| sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.banner.html
cat $file |sed 's/.*"adm"//'|sed s'/^.//'|sed s'/^.//'|sed s'/"adomain".*//'|sed s'/.$//'|sed s'/.$//'| sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
echo
echo
cat launch.html
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" ./launch.html &

#WebPage for comparing Banner
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
sleep 2
#echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4}' > url
echo
echo
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &

#Code to launch Impression URL
sleep 2
cat $file|sed 's/^.*uatimps//' |cut -d ] -f1|sed s'/.$//'|awk '{print $1}' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
echo
cat impressionURL
curl `cat impressionURL`

sleep 2
#Code to launch WinLoss URL
body=` cat $file |sed s'/^.*{AUCTION_AD_ID}//'|cut -d , -f1|sed s'/.$//'`
adid=`cat $file|sed 's/.*"adid//'|cut -d , -f1|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
insert="&impId=1&adId="
echo "https://uatwins.adtheorent.com/Wins?price=1" $insert $adid $body |awk '{ print $1 $2 $3 $4 }' > WinLossURL
echo
echo
cat WinLossURL
curl `cat WinLossURL`

sleep 2

#extract ClickURL
cat $file|sed 's/.*uatclicks//'|cut -d , -f1|sed s'/.$//'|sed s'/.$//'|awk '{print "https://uatclicks" $1}'|sed s'/.$//' > clickURL
echo
cat clickURL
curl `cat clickURL`

rm WinLossURL launch.html url impressionURL clickURL

fi
fi

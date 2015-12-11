#!/bin/bash
#===============================================================================
#
#          FILE:  nexageReformat.sh
# 
#         USAGE:  ./nexageReformat.sh 
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
#       CREATED:  05/18/2015 12:19:33 PM EDT
#      REVISION:  11/30/2015
#===============================================================================


echo
file=$1

grep VAST $file
if  [ $? -eq 0 ]
    then
echo
echo
echo "This is a VAST file.  Using VAST Formant Script"
./VASTreformatNexage.sh $file

else

echo
echo "Using the banner Response file:"
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*"adm"//'|sed s'/^.//'|sed s'/^.//'|sed s'/"adomain".*//'| sed s'/.$//'| sed s'/.$//'| sed s'/.$//'| sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.banner.html
cat $file |sed 's/.*"adm"//'|sed s'/^.//'|sed s'/^.//'|sed s'/"adomain".*//'| sed s'/.$//'| sed s'/.$//'| sed s'/.$//'| sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
echo
echo
cat launch.html
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &

#WebPage for comparing Banner
AdExchange=`cat $file |sed 's/^.*adExchange=//' |sed s'/engine.*//'|sed s'/.$//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*sId//'|sed 's/cId.*//'|sed s'/.$//'`
LineItemID=`cat $file |sed 's/^.*lId//'|sed 's/sId.*//'|sed s'/.$//'`
CampaignID=`cat $file |sed 's/^.*cId//'|sed 's/crId.*//'|sed s'/.$//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "&StrategyID" $StrategyID "&LineItemID" $LineItemID "&CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
echo
echo
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
sleep 2
cat $file |sed 's/.*uatimps//'|awk '{print $1}'|sed s'/.$//' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
echo
cat impressionURL
curl `cat impressionURL`


sleep 2
#Code to launch WinLoss URL
body=`cat $file |sed s'/.*AUCTION_AD_ID}//'|cut -d " " -f1|sed s'/.$//'`
adid=`cat $file |sed 's/.*adid//'|cut -d , -f1|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
echo
echo "https://uatwins.adtheorent.com/Wins?price=1&impId=1&adId=" $adid $body |awk '{print $1 $2 $3}' > WinLossURL

echo
echo
cat WinLossURL
curl `cat WinLossURL`

sleep 5

#Extract ClickURL

cat $file |sed 's/.*uatclicks//'|cut -d , -f1|sed s'/.$//'|awk '{print "http://uatclicks" $1}'|sed s'/.$//' > clickURL
echo
echo
cat clickURL
curl `cat clickURL`
sleep 2
rm WinLossURL launch.html url impressionURL clickURL

fi

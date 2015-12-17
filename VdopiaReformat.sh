#!/bin/bash
#===============================================================================
#
#          FILE:  VdopiaReformat.sh
# 
#         USAGE:  ./VdopiaReformat.sh 
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
#       CREATED:  04/22/2015 10:55:23 AM EDT
#      REVISION:  ---
#===============================================================================
file=$1

grep VAST $file
if  [ $? -eq 0 ]
    then


#Code for VAST Reformat
echo "Using the Response file:"
echo
echo
cat $file

echo
echo
echo "Capture this text, and paste in the VAST viewer."
echo
cat $file |sed 's/.*"adm"//'|sed 's/"adomain".*//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g" > Results/$file.VAST
cat $file |sed 's/.*"adm"//'|sed 's/"adomain".*//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g" 
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" "https://developers.google.com/interactive-media-ads/docs/vastinspector_dual?hl=it" &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" "https://developers.google.com/interactive-media-ads/docs/vastinspector_dual?hl=it" &

#WebPage for comparing VAST
# crid=`cat $file |sed s'/.*crid//'|cut -d , -f1|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
# sleep 2
# echo
# echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=Vdopia" |awk '{print $1 $2 $3}' > url
# echo
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&action.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/cId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/&adExchange.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "&StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
echo
echo
cat url
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
cat $file |sed 's/^.*uatimps//'|cut -d ] -f1|awk '{ print "https://uatimps" $0 }' > impressionURL
sleep 2
echo
cat impressionURL
#curl `cat impressionURL`
sleep 2


#Code to launch WinLoss URL
echo
cat $file|sed 's/^.*{AUCTION_PRICE}//'|cut -d ] -f1 |awk '{print "https://uatwins.adtheorent.com/Wins?ratio=100&price=10" $1}' > WinLossURL
cat WinLossURL
#curl `cat WinLossURL`

sleep 2

cat $file|sed 's/.*uatclicks//'|cut -d ] -f1|awk '{print "https://uatclicks" $1}' > ClickURL
echo
cat ClickURL
#curl `cat ClickURL`
sleep 2

rm WinLossURL url impressionURL ClickURL


#--------------------------------------------------------------------------------------------------------------------------------------------------
#--------------------------------------------------------------------------------------------------------------------------------------------------
#--------------------------------------------------------------------------------------------------------------------------------------------------


else
grep javascript $file
if  [ $? -eq 0 ]
    then

#--------------------------------------------------------------------------------------------------------------------------------------------------
#--------------------------------------------------------------------------------------------------------------------------------------------------
#--------------------------------------------------------------------------------------------------------------------------------------------------

echo "Using the Response file:"
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*"adm"//'|sed 's/"adomain".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.banner.html
cat $file |sed 's/.*"adm"//'|sed 's/"adomain".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" ./launch.html &
echo
echo
cat launch.html
#WebPage for comparing Banner
#crid=`cat $file |sed 's/.*crid//'|awk '{print $1}'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
#sleep 2
#echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=Vdopia" |awk '{print $1 $2 $3}' > url
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &
#echo
echo
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url

cat url

#Code to launch Impression URL
sleep 2
cat $file| sed 's/^.*uatimps//' |awk '{print $1}'| sed s'/.$//' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
cat impressionURL
curl `cat impressionURL`

sleep 2
#Code to launch WinLoss URL
adid=`cat $file| sed 's/.*adid//'|cut -d " " -f1|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
body=`cat $file|sed 's/.*{AUCTION_AD_ID}//'|cut -d , -f1|sed s'/.$//'`
echo
echo "https://uatwins.adtheorent.com/Wins?price=1&impid=1&adId=" $adid $body |awk '{ print $1 $2 $3}' > WinLossURL
cat WinLossURL
curl `cat WinLossURL`

sleep 2

cat $file|sed 's/.*uatclicks//'|cut -d , -f1|sed s'/.$//'|sed s'/.$//'|awk '{print "https://uatclicks" $1}' > ClickURL
echo
echo
cat ClickURL
curl `cat ClickURL`
sleep 2
rm WinLossURL launch.html url impressionURL ClickURL



#--------------------------------------------------------------------------------------------------------------------------------------------------
#--------------------------------------------------------------------------------------------------------------------------------------------------
#--------------------------------------------------------------------------------------------------------------------------------------------------

else

#Code for Banner Reformat
echo "Using the Response file:"
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*adm//'|sed 's/adomain.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.banner.html
cat $file |sed 's/.*adm//'|sed 's/adomain.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" ./launch.html &

#WebPage for comparing Banner
crid=`cat $file |sed 's/.*crid//'|awk '{print $1}'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=Vdopia" |awk '{print $1 $2 $3}' > url
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &
echo
echo
cat url

#Code to launch Impression URL
sleep 2
cat $file| sed 's/^.*uatimps//' |sed 's/height.*//'| sed s'/.$//'|sed s'/.$//' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
cat impressionURL
#curl `cat impressionURL`

sleep 2
#Code to launch WinLoss URL
adid=`cat $file|sed 's/.*adid//'|cut -d " " -f1|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
body=`cat $file|sed 's/.*{AUCTION_AD_ID}//'|cut -d , -f1|sed s'/.$//'`
echo
echo "https://uatwins.adtheorent.com/Wins?price=" $adid $body |awk '{ print $1 $2 $3}' > WinLossURL
cat WinLossURL
#curl `cat WinLossURL`

sleep 2

cat $file|sed 's/.*uatclicks//'|cut -d " " -f1|sed s'/.$//'|sed s'/.$//'|awk '{print "https://uatclicks" $1}' > ClickURL
echo
echo
cat ClickURL
curl `cat ClickURL`
sleep 2

rm WinLossURL launch.html url impressionURL ClickURL

fi
fi

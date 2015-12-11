#!/bin/bash
#===============================================================================
#
#          FILE:  AmobeeBannerReformat.sh
# 
#         USAGE:  ./AmobeeBannerReformat.sh file
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

#grep VAST $file
#if  [ $? -eq 0 ]
#    then
#echo
#echo
#echo "This is a Amobee VAST file.  Using AmobeeVAST Formant Script"
#./AmobeeVASTreformat.sh $file
#
#else
#
#grep function $file
#if  [ $? -eq 0 ]
#    then
#echo
#echo
#echo "This is a Amobee Rich Media file.  Using AmobeeVAST Formant Script"
#./AmobeeRichMediaReformat.sh $file

#else

#echo "This is a Amobee banner.  Using the AmobeeBanner reformat Script."
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*"adm"//'|sed 's/"iurl".*//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.AmobeeBanner.html
cat $file |sed 's/.*"adm"//'|sed 's/"iurl".*//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &
echo
echo
cat ./launch.html
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" ./launch.html &

#WebPage for comparing Banner
#crid=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
#sleep 2
#echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=Amobee" |awk '{print $1 $2 $3}' > url
AdExchange=`cat $file |sed s'/^.*adExchange=//' |sed s'/engine.*//'|sed s'/.$//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*sId//'|sed 's/cId.*//'|sed s'/.$//'`
LineItemID=`cat $file |sed 's/^.*lId//'|sed 's/sId.*//'|sed s'/.$//'`
CampaignID=`cat $file |sed 's/^.*cId//'|sed 's/crId.*//'|sed s'/.$//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "&StrategyID" $StrategyID "&LineItemID" $LineItemID "&CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
echo
echo
cat url
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &

#Code to launch Impression URL
sleep 2
cat $file| sed 's/^.*uatimps.adtheorent.com//' |awk '{print $1}'|sed s'/.$//' |awk '{ print "https://uatimps.adtheorent.com" $0 }' > impressionURL &
echo
echo
cat impressionURL
curl `cat impressionURL`

#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat impressionURL` &

sleep 2
#Code to launch WinLoss URL
body=`cat $file |sed 's/.*${AUCTION_AD_ID}//'|cut -d , -f1|sed s'/.$//'`
adid=`cat $file|sed 's/.*"adid//'|cut -d , -f1|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
echo "http://uatwins.adtheorent.com/Wins?price=1&impId=1&adId=" $adid $body |awk '{ print $1 $2 $3}' > WinLossURL
echo
cat WinLossURL
curl `cat WinLossURL`
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat WinLossURL` &

sleep 2

#Extract ClickURL
cat $file|sed 's/.*uatclicks//'|cut -d " " -f1 |sed s'/.$//'|sed s'/.$//'|awk '{print "https://uatclicks" $1}' > ClickURL
echo
cat ClickURL
curl `cat ClickURL`
sleep 2
rm WinLossURL launch.html url impressionURL ClickURL

#fi
#fi

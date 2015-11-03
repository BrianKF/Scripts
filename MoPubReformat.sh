#!/bin/bash
#===============================================================================
#
#          FILE:  MoPubReformat.sh
# 
#         USAGE:  ./MoPubReformat.sh file
# 
#   DESCRIPTION:  Reformats the response file
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
echo "Using the Native Response file:"
echo
echo

#WebPage for comparing Banner
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
rm url

#Strip WinLoss URL
head=`cat $file |sed s'/.*uatwins//'|sed s'/{AU.*//'|sed s'/.$//'`
imp="1&impId=1&adId"
adid=`cat $file |sed s'/.*adId//'|sed s'/crId.*//'|sed s'/.$//'`
crid="&crId"
end=`cat $file|sed s'/.*crId//'|sed s'/"adm".*//'|cut -d , -f-1|sed s'/.$//'`

echo "https://uatwins" $head $imp $adid $crid $end |awk '{print $1 $2 $3 $4 $5 $6}' > WinLossURL
cat WinLossURL
curl `cat WinLossURL`

#Strip Impression URL
sleep 2
body=`cat $file | sed 's/^.*uatimps//'|cut -d , -f1|sed s'/.$//'`
echo "https://uatimps" $body |awk '{print $1 $2}' > ImpressionURL
echo
echo
cat ImpressionURL
curl `cat ImpressionURL`
rm WinLossURL ImpressionURL

#Strip ClickTracker
clickend=`cat $file |sed s'/.*uatclicks//'|cut -d , -f1|sed s'/.$//'`
echo "http://uatclicks" $clickend |awk '{print $1 $2}' > ClickTrakerURL
echo
echo
cat ClickTrakerURL
curl `cat ClickTrakerURL`
sleep 2
rm ClickTrakerURL







else

echo "Using the Response file:"
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
AdExchange=`cat $file |sed s'/^.*&adExchange=//' |sed s'/&engine.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&adExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
rm url
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &

#Code to launch Impression URL
sleep 4
cat $file| sed 's/^.*uatimps//' |awk '{print $1}'|sed s'/.$//' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
cat impressionURL
#curl `cat impressionURL`


sleep 2
#Code to launch WinLoss URL
body=`cut -d ":" -f8,9-11 $file |awk '{ print $2 }'|sed s'/.$//'|sed s'/.$//'|sed 's/.*c_id//'`
price=`cut -d ":" -f7 $file |awk '{ print $1 }'|sed s'/.$//'`
adid=`cut -d ":" -f8,9-11 $file |awk '{ print $1 }'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'`
insert="&imp_id=1&ad_id="
cid="&c_id"
echo "https://uatwins.adtheorent.com/WinLoss/Channels/AdExchanges/Generic?price=" $price $insert $adid $cid $body |awk '{ print $1 $2 $3 $4 $5 $6}' > WinLossURL
echo
cat WinLossURL
#curl `cat WinLossURL`

rm WinLossURL url impressionURL
fi

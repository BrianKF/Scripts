#!/bin/bash
#===============================================================================
#
#          FILE:  SmaatoReformat.sh
# 
#         USAGE:  ./SmaatoReformat.sh $file
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
#       CREATED:  04/23/2015 12:22:54 PM EDT
#      REVISION:  ---
#===============================================================================

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
cat $file |sed 's/.*adm//'|sed 's/adomain.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g" > Results/$file.VAST
cat $file |sed 's/.*adm//'|sed 's/adomain.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"

#WebPage for comparing VAST
AdExchange=`cat $file |sed s'/^.*adExchange=//' |sed s'/&action.*//'`
CreativeID=`cat $file |sed 's/.*crid//'|cut -d , -f1 |sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
StrategyID=`cat $file |sed 's/^.*&sId//'|sed 's/cId.*//'`
LineItemID=`cat $file |sed 's/^.*&lId//'|sed 's/sId.*//'`
CampaignID=`cat $file |sed 's/^.*&cId//'|sed 's/crId.*//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CampaignID" $CampaignID "StrategyID" $StrategyID "LineItemID" $LineItemID "CreativeID=" $CreativeID "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10}' > url
echo
echo
cat url
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
impCheck=$(grep uatimps.adtheorent.com $file)
if [ $? -eq 0 ]
    then

echo
echo "Found an impression file and rendering it..."
cat $file |sed 's/^.*uatimps//' | sed 's/Impression.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
sleep 2
cat impressionURL
echo
#curl `cat impressionURL`
sleep 2

#Code to launch WinLoss URL
echo "Found an WinLoss file and rendering it..."
body=`cat $file|sed 's/^.*{AUCTION_AD_ID}//'|cut -d , -f1|sed s'/.$//'`
insert="&impId=1&adId="
adid=`cat $file |sed 's/.*adid//'|cut -d , -f1|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
echo "http://uatwins.adtheorent.com/Wins?price=1" $insert $adid $body |awk '{ print $1 $2 $3 $4 $5}' > WinLossURL
sleep 2
cat WinLossURL
#curl `cat WinLossURL`
sleep 2

#Extract ClickUrl
cat $file|sed 's/^.*uatclicks//'|cut -d ] -f1 |awk '{print "https://uatclicks" $1}' > ClickURL
echo
echo
cat ClickURL
#curl `cat ClickURL`
rm WinLossURL url impressionURL ClickURL
sleep 2

else

#Code to launch WinLoss URL
body=`cat $file|sed 's/^.*&cId//'|cut -d ] -f1`
adid=`cat $file |sed 's/.*adid//'|cut -d , -f1|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'`
echo "http://uatwins.adtheorent.com/Wins?price=1" $adid $body |awk '{ print $1 $2 $3 $4}' > WinLossURL
sleep 2
cat WinLossURL
#curl `cat WinLossURL`
sleep 2

#Extract ClickUrl

cat $file |sed 's/^.*uatclicks//'|cut -d ] -f1 |awk '{print "https://uatclicks" $1}' > ClickURL
echo
echo
cat ClickURL
#curl `cat ClickURL`
sleep 2
rm WinLossURL url impressionURL ClickURL

fi

else 


echo
file=$1
echo "Using the Banner reformat script:"
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
cat $file |cut -d "<" -f2- | awk '{ print "<" $0 }'|sed -e 's/script.*//'|awk '{ print "<html><body>" $0 "/script></body></html>" }' > Results/$file.banner.html
cat $file |cut -d "<" -f2- | awk '{ print "<" $0 }'|sed -e 's/script.*//'|awk '{ print "<html><body>" $0 "/script></body></html>" }' > launch.html

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
impCheck=$(grep uatimps.adtheorent.com $file)
if [ $? -eq 0 ]
    then
sleep 2
imp=`cat $file |sed 's/^.*uatimps//' |sed 's/beacon.*//'| sed s'/.$//'|sed s'/.$//'|cut -d ";" -f1|sed s'/amp//'`
adid=`cat $file |sed 's/.*adId=//'|sed 's/&amp.*//'`
cid=`cat $file |sed s'/.*lId//'|sed 's/&amp.*//'|sed s'/^.//'`
crid=`cat $file |sed s'/.*crId//'|sed 's/&amp.*//'|sed s'/^.//'`
adthid=`cat $file |sed 's/.*adThdId=//'|sed 's/&amp.*//'`
ad_exchange=`cat $file | sed 's/.*adExchange//'|sed 's/&amp.*//'|sed s'/^.//'`
engine_id=`cat $file |sed 's/.*engineId=//'|cut -d "<" -f1`
lId=`cat $file|sed s'/.*lId//'|sed 's/&amp.*//'|sed s'/^.//'`
sId=`cat $file|sed s'/.*sId//'|sed 's/&amp.*//'|sed s'/^.//'`

echo "https://uatimps.adtheorent.com/Imps?impId=1&adId=" $adid "&lId=" $lId "&sId=" $sId "&cId=" $cid "&crId=" $crid "&adThdId=" $adthid "&adExchange=" $ad_exchange "&engineId=" $engine_id |awk '{print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10 $11 $12 $13 $14 $15 $16}'> impressionURL
echo
echo
cat impressionURL
#curl `cat impressionURL`
sleep 2

#Code to launch WinLoss URL
body=`cat $file|sed 's/.*{AUCTION_AD_ID}//'|cut -d " " -f1|sed s'/.$//'|sed s'/.$//'`
adid=`cat $file|sed 's/.*"adid"//'|cut -d , -f1|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'`
insert="&impId=1&adId="
echo "http://uatwins.adtheorent.com/Wins?price=1" $insert $adid $body |awk '{ print $1 $2 $3 $4 }' > WinLossURL
echo
echo
cat WinLossURL
curl `cat WinLossURL`
sleep 2

#Extrack ClickURL
echo "https://uatclicks.adtheorent.com/Clicks?adExchange=Smaato&action=click&adId=" $adid "&lId" $LineItemID "&sId" $StrategyID "cId=" $cid "&crId=" $crid "&adThdId=" $adthid "&redir=" |awk '{ print $1 $2 $3 $4 $5 $6 $7 $8 $9 $10 $11 $12 $13 }' > ClickURL
echo
cat ClickURL
curl `cat ClickURL`
sleep 2

rm WinLossURL launch.html url impressionURL ClickURL

else

sleep 2
#Code to launch WinLoss URL
body=`cat $file|sed 's/.*&cId//'|cut -d , -f1|sed s'/.$//'`
adid=`cat $file|sed 's/.*adid//'|cut -d , -f1|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'`
cid="&cId"
echo "https://uatwins.adtheorent.com/Wins?price=1&impId=1&adId=" $adid $cid $body |awk '{ print $1 $2 $3 $4 $5 }' > WinLossURL
echo
echo
cat WinLossURL
curl `cat WinLossURL`
sleep 2
rm WinLossURL launch.html url
fi
fi

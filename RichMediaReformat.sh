#!/bin/bash
#===============================================================================
#
#          FILE:  PubBannerReformat.sh
# 
#         USAGE:  ./PubBannerReformat.sh file
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
echo "Using the Response file:"
echo
echo
cat $file

#Code to launch Banner and Marup for comparison
cat $file |sed 's/.*"adm"//'|sed 's/"adomain".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > Results/$file.banner.html
cat $file |sed 's/.*"adm"//'|sed 's/"adomain".*//'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'|sed s'/^.//'|sed s'/.$//'| sed "s/\\\\\"/\"/g"|awk '{ print "<html><body>" $0 "</body></html>" }' > launch.html
echo
echo
cat launch.html
"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" ./launch.html &
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" ./launch.html &
sleep 2
#WebPage for comparing Banner
crid=`cat $file |sed 's/.*crid//'|awk '{print $1}'|sed s'/^.//'|sed s'/^.//'|sed s'/^.//'| sed s'/.$//'| sed s'/.$//'`
AdExchange=`cat $file |sed s'/^.*ad_exchange=//' |sed s'/&engine.*//'`
echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=" $AdExchange |awk '{print $1 $2 $3 $4}' > url
echo
echo
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &

#Code to launch Impression URL
sleep 2
cat $file| sed 's/^.*uatimps//' |awk '{print $1}'| sed s'/.$//'|sed s'/.$//' |awk '{ print "https://uatimps" $0 }' > impressionURL
echo
cat impressionURL
# curl `cat impressionURL`

#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat impressionURL` &
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat impressionURL` &

sleep 2
#Code to launch WinLoss URL
body=`cut -d ":" -f8-8,9-11 $file |awk '{ print $2 }'|sed s'/.$//'|sed s'/.$//'|sed 's/.*c_id//'`
price=`cut -d ":" -f7-7 $file |awk '{ print $1 }'|sed s'/.$//'`
adid=`cut -d ":" -f8-8,9-11 $file |awk '{ print $1 }'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'`
insert="&imp_id=1&ad_id="
cid="&c_id"
echo "https://uatwins.adtheorent.com/WinLoss/Channels/AdExchanges/Generic?price=" $price $insert $adid $cid $body |awk '{ print $1 $2 $3 $4 $5 $6}' > WinLossURL
echo
cat WinLossURL
# curl `cat WinLossURL`
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat WinLossURL` &
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat WinLossURL` &

rm WinLossURL url impressionURL

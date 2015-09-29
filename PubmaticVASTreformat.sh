#!/bin/bash
#===============================================================================
#
#          FILE:  PubmaticVASTreformt.sh
# 
#         USAGE:  ./PubmaticVASTreformt.sh file
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
echo "Using the Response file:"
echo
echo
cat $file

#read -p "Enter VAST filename: " file
echo
echo
cat $file |cut -d : -f15- |cut -d "<" -f2-|awk '{ print "<" $0 }' | sed "s/\\\\\"/\"/g"|cut -d , -f1| sed s'/.$//' > Results/$file.VAST
#cat $file |cut -d : -f15- |cut -d "<" -f2-|awk '{ print "<" $0 }' | sed "s/\\\\\"/\"/g"|cut -d , -f1| sed s'/.$//'
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" "https://developers.google.com/interactive-media-ads/docs/vastinspector_dual?hl=it" &
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" "https://developers.google.com/interactive-media-ads/docs/vastinspector_dual?hl=it" &

#WebPage for comparing VAST
crid=`cut -d ":" -f58 $file|cut -d " " -f1|sed s'/^.//'| sed s'/.$//'|sed s'/.$//'`
sleep 2
echo "http://uatrtb.adtheorent.com:7070/?CreativeID=" $crid "&SiteApp=SITE&AdExchange=Pubmatic" |awk '{print $1 $2 $3}' > url
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat url` &
echo
cat url
"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat url` &

#Code to launch Impression URL
cat $file |sed 's/^.*uatimps//' | sed 's/Impression.*//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|awk '{ print "https://uatimps" $0 }' > impressionURL
sleep 2
echo
cat impressionURL
#curl `cat impressionURL`
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat impressionURL`
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat impressionURL` &
sleep 2


#Code to launch WinLoss URL
body=`cut -d ":" -f18 $file |awk '{ print $1 }'|sed s'/.$//'|sed s'/.$//'|sed s'/Impression.*//'| sed 's/.*c_id//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'|sed s'/.$//'`
price=`cut -d ":" -f7-7 $file |awk '{ print $1 }'|sed s'/.$//'`
adid=`cut -d ":" -f13 $file |awk '{ print $1 }'|sed s'/.$//'|sed s'/.$//'|sed s'/^.//'`
insert="&imp_id=1&ad_id="
cid="&c_id"
echo
echo "https://uatwins.adtheorent.com/WinLoss/Channels/AdExchanges/Generic?price=" $price $insert $adid $cid $body |awk '{ print $1 $2 $3 $4 $5 $6}' > WinLossURL &
sleep 2
cat WinLossURL
#curl  `cat WinLossURL`
#"/cygdrive/c/Program Files (x86)/Google/Chrome/Application/chrome.exe" `cat WinLossURL`
#"/cygdrive/c/Program Files (x86)/Mozilla Firefox/firefox.exe" `cat WinLossURL`
rm WinLossURL url impressionURL

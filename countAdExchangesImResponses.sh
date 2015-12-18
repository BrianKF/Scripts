#!/bin/bash
#===============================================================================
#
#          FILE:  countAdExchangesImResponses.sh
# 
#         USAGE:  ./countAdExchangesImResponses.sh 
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
#       CREATED:  10/15/2015  2:57:58 PM EDT
#      REVISION:  ---
#===============================================================================

Pub=`grep PubMatic SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
MoP=`grep MoPub SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Nex=`grep Nexage SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Live=`grep Live SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Adap=`grep Adap SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Aer=`grep AerS SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Amo=`grep Amob SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Oma=`grep OMAX SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Rub=`grep Rubic SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Sma=`grep Smaa SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Spot=`grep Spot SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Vdop=`grep Vdop SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Trip=`grep Trip SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
Ope=`grep Ope SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`
LKQ=`grep LKQ SanityTestin* |awk '{print $1}'|cut -d : -f1|wc|awk '{print $1}'`

echo
echo
echo "1. PubMatic" $Pub
echo "2. MoPub" $MoP
echo "3. Nexage" $Nex
echo "4. LiveRail" $Live
echo "5. AdapTV" $Adap
echo "6. AerServ" $Aer
echo "7. Amobee" $Amo
echo "8. Omax" $Oma
echo "9. Rubicon" $Rub
echo "10. Smaato" $Sma
echo "12. SpotX" $Spot
echo "13. Vdopia" $Vdop
echo "14. TripleLift" $Trip
echo "15. OpenX" $Ope
echo "16. LKQD" $LKQ


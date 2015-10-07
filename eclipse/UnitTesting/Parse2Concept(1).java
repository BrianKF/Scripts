package com.adtheorent.bidder.requestprocess;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adtheorent.bidder.concept.AdThRequest;
import com.adtheorent.bidder.concept.BidContext;
import com.adtheorent.bidder.requestprocess.ext.request.AerServRequestExt;
import com.adtheorent.bidder.requestprocess.ext.request.AerServRequestExt.PlatformType;
import com.adtheorent.bidder.requestprocess.ext.request.AerServRequestImpBannerExt;
import com.adtheorent.bidder.requestprocess.ext.request.TripleLiftRequestImpBannerExtTripleLift;
import com.adtheorent.concept.AdExchange;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO consider consistent usage of JSON node .get() and .path() methods. Currently we're using both for similar things.
// TODO consider jsonNode.isMissing() to detect existence of certain nodes when using .path(), as it will never return null

/**
 * Processes and converts an incoming bid request into our custom {@link AdThRequest} bid request object. This is done
 * by traversing the entire JSON bid request object and assigning each value.
 *
 * @since 1.0.0
 */
public class Parse2Concept {

	private static final Logger logger = Logger.getLogger( Parse2Concept.class.getName() );

	public static ObjectMapper m;
	public static String exception = null;

	public static void OpenRTB2( final String Payload, final AdThRequest _req, final BidContext bidContext )
			throws IOException {

		String MediaType = "ALL", impType = "Banner", AdThdid = null, AdThdid_md5 = null, AdThdid_sha1 = null, AdThdid_uid =
				null;
		String Adthdid_final = null;
		String device_ua = "", device_ip = "", code = "", factual = "", bluekai = "";
		int coppa = 0, device_dnt = 0;
		String Reqwseat = null, Reqcur = null, Reqbcat = null, Reqbadv = null, impiFrameBuster = null, impBtypeIntArrayAsString =
				null, impBattrIntArrayAsString = null, Impmimes = null, Impapi = null, Impexpdir = null, Impplaybackmethod =
				null, Impdelivery = null, Mediacat = null, Mediasectioncat = null, Mediapagecat = null, Publishercat = null, Publisherkeywords =
				null, Contentcat = null, Contentkeywords = null, Userkeywords = null;
		String extString = null;
		String uidh = null, protocol = null, protocols = null;
		exception = null;
		if (m == null) {
			m = new ObjectMapper();
		}

		final JsonNode rootNode = m.readValue( Payload, JsonNode.class );

		if (rootNode == null) {
			throw new IllegalStateException( "Error: null root node encountered while attempting to parse bid request" );
		}

		_req.BidReq_id = rootNode.path( "id" ).textValue(); // 8-4-14 GP
		_req.BidReq_at = rootNode.path( "at" ).asInt(); // 8-4-14 GP
		_req.BidReq_tmax = rootNode.path( "tmax" ).asInt(); // 8-4-14 GP
		_req.BidReq_allimps = rootNode.path( "allimps" ).asInt(); // 8-4-14 GP

		// array of string
		final JsonNode bidreqwseatNode = rootNode.path( "wseat" );
		if (bidreqwseatNode.isArray()) {
			Reqwseat = bidreqwseatNode.toString();
			Reqwseat = fixArrayedString( Reqwseat, false ); // RSJ - 5 AUG 14
		}
		_req.BidReq_wseatString = Reqwseat;

		final JsonNode bidreqcurNode = rootNode.path( "cur" );
		if (bidreqcurNode.isArray()) {
			Reqcur = bidreqcurNode.toString();
			Reqcur = fixArrayedString( Reqcur, false ); // RSJ - 5 AUG 14
		}
		_req.BidReq_curString = Reqcur;

		final JsonNode bidreqbcatNode = rootNode.path( "bcat" );
		if (bidreqbcatNode.isArray()) {
			Reqbcat = bidreqbcatNode.toString();
			Reqbcat = fixArrayedString( Reqbcat, false ); // RSJ - 5 AUG 14
		}
		_req.BidReq_bcatString = Reqbcat;

		final JsonNode bidreqbadvNode = rootNode.path( "badv" );
		if (bidreqbadvNode.isArray()) {
			Reqbadv = bidreqbadvNode.toString();
			Reqbadv = fixArrayedString( Reqbadv, false ); // RSJ - 5 AUG 14
		}
		_req.BidReq_badvString = Reqbadv;

		// OpenRTB2 - 3.3.2 - Impression Object
		final JsonNode impObjectArrayNode = rootNode.path( "imp" );
		if (impObjectArrayNode != null && impObjectArrayNode.size() > 0) {
			// Even though the RTB spec supports multiple impressions per Bid Request, we are only processing 1st.
			final JsonNode impObjectNode = impObjectArrayNode.get( 0 );
			_req.Imp_id = impObjectNode.path( "id" ).textValue();
			_req.Imp_displaymanager = impObjectNode.path( "displaymanager" ).textValue();
			_req.Imp_displaymanagerver = impObjectNode.path( "displaymanagerver" ).textValue();
			_req.Imp_instl = impObjectNode.path( "instl" ).asInt();
			_req.Imp_tagid = impObjectNode.path( "tagid" ).textValue();
			_req.Imp_bidfloor = impObjectNode.path( "bidfloor" ).asDouble();
			_req.Imp_bidfloorcur = impObjectNode.path( "bidfloorcur" ).textValue();

			final JsonNode impiFrameBusterNode = impObjectNode.path( "iframebuster" );
			if (impiFrameBusterNode.isArray()) {
				impiFrameBuster = impiFrameBusterNode.toString();
				impiFrameBuster = fixArrayedString( impiFrameBuster, false ); // RSJ - 5 AUG 14
			}
			_req.Imp_iframebusterString = impiFrameBuster;

			final JsonNode impExtObjectNode = impObjectArrayNode.path( "ext" );
			if (impExtObjectNode != null && impExtObjectNode.size() > 0) {
				_req.ImpExt_strictbannersize = impExtObjectNode.path( "strictbannersize" ).asInt();
			}

			// OpenRTB - Section 3.3.3 - Banner Object
			// BidRequest : imp : banner
			final JsonNode impBannerObjectNode = impObjectNode.path( "banner" ); // nested banner object
			if (impBannerObjectNode != null && impBannerObjectNode.size() > 0) {
				impType = "Banner";
				_req.Imp_bannerId = impBannerObjectNode.path( "id" ).textValue();

				// Check implemented for Vdopia, as they send the height and width as hmin & wmin instead of h & w
				int impWidth = impBannerObjectNode.path( "w" ).asInt();
				if (impWidth == 0) {
					impWidth = impBannerObjectNode.path( "wmin" ).asInt();
				}

				int impHeight = impBannerObjectNode.path( "h" ).asInt();
				if (impHeight == 0) {
					impHeight = impBannerObjectNode.path( "hmin" ).asInt();
				}

				_req.Imp_w = impWidth;
				_req.Imp_h = impHeight;
				_req.Imp_pos = impBannerObjectNode.path( "pos" ).asInt();
				_req.Imp_topframe = impBannerObjectNode.path( "topframe" ).asInt();

				final JsonNode impBannerBtypeIntArrayNode = impBannerObjectNode.path( "btype" );
				if (impBannerBtypeIntArrayNode.isArray()) {
					final List<Integer> intValues = new ArrayList<>();
					for ( final JsonNode innerNode : impBannerBtypeIntArrayNode ) {
						final int intValue = innerNode.asInt();

						int insert = Collections.binarySearch( intValues, intValue );

						if (insert < 0) {
							insert = Math.abs( insert + 1 );
							intValues.add( insert, intValue );
						}
					}
					_req.splitImp_btypeString = intValues.toArray( new Integer[ intValues.size() ] );
					impBtypeIntArrayAsString = impBannerBtypeIntArrayNode.toString();
					impBtypeIntArrayAsString = fixArrayedString( impBtypeIntArrayAsString, false ); // RSJ - 5 AUG 14
				}
				_req.Imp_btypeString = impBtypeIntArrayAsString;

				final JsonNode impBannerBattrIntArrayNode = impBannerObjectNode.path( "battr" );
				if (impBannerBattrIntArrayNode.isArray()) {
					impBattrIntArrayAsString = impBannerBattrIntArrayNode.toString();
					impBattrIntArrayAsString = fixArrayedString( impBattrIntArrayAsString, false ); // RSJ - 5 AUG 14
				}
				_req.Imp_battrString = impBattrIntArrayAsString;

				final JsonNode ImpBannerObjmimes = impBannerObjectNode.path( "mimes" );
				if (ImpBannerObjmimes.isArray()) {
					Impmimes = ImpBannerObjmimes.toString();
					Impmimes = fixArrayedString( Impmimes, false ); // RSJ - 5 AUG 14
				}
				_req.Imp_mimesString = Impmimes;

				final JsonNode impBannerApiNode = impBannerObjectNode.path( "api" );
				if (impBannerApiNode.isArray()) {
					Impapi = impBannerApiNode.toString();
					Impapi = fixArrayedString( Impapi, true ); // RSJ - 5 AUG 14
				}
				_req.Imp_apiString = Impapi;

				final JsonNode ImpBannerObjexpdir = impBannerObjectNode.path( "expdir" );
				if (ImpBannerObjexpdir.isArray()) {
					Impexpdir = ImpBannerObjexpdir.toString();
					Impexpdir = fixArrayedString( Impexpdir, true ); // RSJ - 5 AUG 14
				}
				_req.Imp_expdirString = Impexpdir;

				// bidRequest.imp.banner.ext
				final JsonNode impBannerExtObjectNode = impBannerObjectNode.path( "ext" );
				if (impBannerExtObjectNode != null && impBannerExtObjectNode.size() > 0) {
					// bidRequest.imp.banner.ext.video
					final JsonNode impBannerExtVideoObjectNode = impBannerExtObjectNode.path( "video" );
					if (impBannerExtVideoObjectNode != null && impBannerExtVideoObjectNode.size() > 0) {
						impType = "Video";
						_req.Imp_deliveryString = "2";
						_req.Imp_minduration = impBannerExtVideoObjectNode.path( "minduration" ).asInt();
						_req.Imp_maxduration = impBannerExtVideoObjectNode.path( "maxduration" ).asInt();
						_req.Imp_linearity = impBannerExtVideoObjectNode.path( "linearity" ).asInt();

						final JsonNode protocolNode = impBannerExtVideoObjectNode.path( "type" );
						if (protocolNode.isArray()) {
							protocol = protocolNode.toString();
							protocol = fixArrayedString( protocol, false ); // RSJ - 5 AUG 14
							_req.Imp_protocolString = protocol;
						}
					}

					// Attempt to parse TripleLift custom bidRequest.imp.banner.ext object
					if (bidContext.AdExchangeID == AdExchange.TRIPLELIFT) {
						parseTripleLiftImpBannerExtTripleLiftNode( _req, impBannerExtObjectNode );
						impType = "Native";
					}

					// Attempt to parse AerServ custom bidRequest.imp.banner.ext object
					if (bidContext.AdExchangeID == AdExchange.AERSERV) {
						parseAerServImpBannerExtNode( _req, impBannerExtObjectNode );
					}

					// bidRequest.imp.banner.ext.mraid
					final JsonNode impBannerExtMraidNode = impBannerExtObjectNode.path( "mraid" );
					if (impBannerExtMraidNode != null && impBannerExtMraidNode.size() > 0) {
						final JsonNode Mraid = impBannerExtMraidNode.get( 0 );
						try {
							_req.Imp_mraidversion = Double.parseDouble( Mraid.path( "version" ).textValue() );
						} catch ( final Exception ex ) {
							_req.Imp_mraidversion = 0.0;
						}

						final JsonNode MraidFunctionsNode = Mraid.path( "functions" );
						if (MraidFunctionsNode.isArray()) {
							String Mraidfunction = MraidFunctionsNode.toString();
							Mraidfunction = fixArrayedString( Mraidfunction, false ); // RSJ - 5 AUG 14
							_req.Imp_mraidfunctions = Mraidfunction;
						}
					} else {
						final Integer nex_mraid = impBannerExtObjectNode.path( "nex_mraid" ).asInt();
						_req.Imp_mraidversion = nex_mraid;
					}
					_req.Imp_nativebrowserclick = impBannerExtObjectNode.path( "nativebrowserclick" ).asInt();

					// Banner - Ext - BCont
					final JsonNode bContNode = impBannerExtObjectNode.path( "bcont" );
					if (bContNode.isArray()) {
						String bCont = bContNode.toString();
						bCont = fixArrayedString( bCont, false ); // RSJ - 5 AUG 14
						_req.Imp_bcont = bCont;
					}

					// Banner - Ext - MatchingAdID
					final JsonNode matchingAdIdobjNode = impBannerExtObjectNode.path( "matching_ad_id" ).get( 0 );
					if (matchingAdIdobjNode != null && matchingAdIdobjNode.size() > 0) {
						_req.Imp_matching_ad_height = matchingAdIdobjNode.path( "ad_height" ).asInt();
						_req.Imp_matching_ad_width = matchingAdIdobjNode.path( "ad_width" ).asInt();
						_req.Imp_matching_ad_cid = matchingAdIdobjNode.path( "campaign_id" ).asInt();
						_req.Imp_matching_ad_crid = matchingAdIdobjNode.path( "creative_id" ).asInt();
						_req.Imp_matching_ad_placementid = matchingAdIdobjNode.path( "placement_id" ).asInt();
					}

					// Banner - Ext - Native (MoPub)
					final JsonNode impNativeNode = impBannerExtObjectNode.path( "native" );
					if (impNativeNode != null && impNativeNode.size() > 0) {
						impType = "Native";
						_req.ImpNative_ver = impNativeNode.path( "ver" ).asText();
						final JsonNode ImpNativeAdmSupport = impNativeNode.path( "admsupport" );
						if (ImpNativeAdmSupport.isArray()) {
							String admSupport = ImpNativeAdmSupport.toString();
							admSupport = fixArrayedString( admSupport, false ); // RSJ - 6 AUG 14
							_req.ImpNative_admSupport = admSupport;
						}

						_req.ImpNative_Seq = impNativeNode.path( "seq" ).asInt();
						_req.ImpNative_Ctatextlen = impNativeNode.path( "ctatextlen" ).asInt();
						_req.ImpNative_Titlelen = impNativeNode.path( "titlelen" ).asInt();
						_req.ImpNative_Textlen = impNativeNode.path( "textlen" ).asInt();
					}
				}
			} // close Banner Node

			// OpenRTB - Section 3.3.4 - Video Object
			final JsonNode impVideoObjectNode = impObjectNode.path( "video" ); // nested video object
			if (impVideoObjectNode != null && impVideoObjectNode.size() > 0) {
				impType = "Video";
				_req.Imp_linearity = impVideoObjectNode.path( "linearity" ).asInt();
				_req.Imp_minduration = impVideoObjectNode.path( "minduration" ).asInt();
				_req.Imp_maxduration = impVideoObjectNode.path( "maxduration" ).asInt();
				_req.Imp_protocol = impVideoObjectNode.path( "protocol" ).asText();
				protocol = impVideoObjectNode.path( "protocol" ).asInt() + "";
				if (protocol.isEmpty()) {
					_req.Imp_protocol = impVideoObjectNode.path( "protocols" ).asText();
					protocol = impVideoObjectNode.path( "protocols" ).asInt() + "";
				}
				_req.Imp_w = impVideoObjectNode.path( "w" ).asInt();
				_req.Imp_h = impVideoObjectNode.path( "h" ).asInt();
				_req.Imp_startdelay = impVideoObjectNode.path( "startdelay" ).asInt();
				_req.Imp_sequence = impVideoObjectNode.path( "sequence" ).asInt();
				_req.Imp_maxextended = impVideoObjectNode.path( "maxextended" ).asInt();
				_req.Imp_minbitrate = impVideoObjectNode.path( "minbitrate" ).asInt();
				_req.Imp_maxbitrate = impVideoObjectNode.path( "maxbitrate" ).asInt();
				_req.Imp_boxingallowed = impVideoObjectNode.path( "boxingallowed" ).asInt();
				_req.Imp_pos = impVideoObjectNode.path( "pos" ).asInt();

				// Ask about protocol node - SpotX - Protocol node is array, while Imp_protocol is int
				final JsonNode protocolNode = impVideoObjectNode.path( "protocol" );
				if (protocolNode.isArray()) {
					protocol = protocolNode.toString();
					protocol = fixArrayedString( protocol, false ); // RSJ - 5 AUG 14
					_req.Imp_protocol = protocol;
					protocol = assignProtocolString( protocol );
					_req.Imp_protocolString = protocol;
				} else if (!protocolNode.isArray() && !protocol.isEmpty()) {
					protocol = protocolNode.toString();
					protocol = assignProtocolString( protocol );
					_req.Imp_protocolString = protocol;
				}

				// LiveRail Change GP - Sept 11 2014; protocol tag will be deprecated in the future
				final JsonNode protocolsNode = impVideoObjectNode.path( "protocols" );
				if (protocolsNode.isArray()) {
					protocols = protocolsNode.toString();
					protocols = fixArrayedString( protocols, false );
					_req.Imp_protocol = protocols;
					protocols = assignProtocolString( protocols );
					_req.Imp_protocolString = protocols;
				} else {
					// TODO testing required - removed redundant "protocols" null check
					final int protocolsInt = protocolsNode.asInt();
					if (protocolsInt > 0) {
						protocols = protocolsNode.toString();
						protocols = assignProtocolString( protocols ); // GP - Sept 22 2014
						_req.Imp_protocolString = protocols;
					}
				}

				final JsonNode companionadobjNode = impVideoObjectNode.path( "companionad" ).get( 0 );
				if (companionadobjNode != null && companionadobjNode.size() > 0) {
					_req.Imp_companionad_height = companionadobjNode.path( "h" ).asInt();
					_req.Imp_companionad_width = companionadobjNode.path( "w" ).asInt();
					_req.Imp_companionad_id = companionadobjNode.path( "id" ).asInt();
				}

				final JsonNode companionTypeNode = impVideoObjectNode.path( "companiontype" );
				if (companionTypeNode.isArray()) {
					String companiontype = companionTypeNode.toString();
					companiontype = fixArrayedString( companiontype, false ); // RSJ - 5 AUG 14
					_req.Imp_companiontypeString = companiontype;
				}

				final JsonNode apiNode = impVideoObjectNode.path( "api" );
				if (apiNode.isArray()) {
					String videoAPI = apiNode.toString();
					videoAPI = fixArrayedString( videoAPI, false ); // RSJ - 5 AUG 14
					_req.Imp_VideoAPI = videoAPI;
				}

				final JsonNode extVideoNode = impVideoObjectNode.path( "ext" );
				_req.Imp_initiationtype = extVideoNode.path( "initiationtype" ).asInt();
				_req.Imp_spxplayersize = extVideoNode.path( "spxplayersize" ).asInt();

				final JsonNode ImpVideoObjmimes = impVideoObjectNode.path( "mimes" );
				if (ImpVideoObjmimes.isArray()) {
					Impmimes = ImpVideoObjmimes.toString();
					Impmimes = fixArrayedString( Impmimes, false ); // RSJ - 5 AUG 14
				}
				_req.Imp_mimesString = Impmimes;

				final JsonNode ImpVideoObjbtype = impVideoObjectNode.path( "btype" );
				if (ImpVideoObjbtype.isArray()) {
					final List<Integer> intValues = new ArrayList<>();
					for ( final JsonNode innerNode : ImpVideoObjbtype ) {
						final int intValue = innerNode.asInt();

						int insert = Collections.binarySearch( intValues, intValue );

						if (insert < 0) {
							insert = Math.abs( insert + 1 );
							intValues.add( insert, intValue );
						}
					}
					_req.splitImp_btypeString = intValues.toArray( new Integer[ intValues.size() ] );
					impBtypeIntArrayAsString = ImpVideoObjbtype.toString();
					impBtypeIntArrayAsString = fixArrayedString( impBtypeIntArrayAsString, true ); // RSJ - 5 AUG 14
				}
				_req.Imp_btypeString = impBtypeIntArrayAsString;

				// TODO consider deserializing into Enum
				final JsonNode ImpVideoObjbattr = impVideoObjectNode.path( "battr" );
				if (ImpVideoObjbattr.isArray()) {
					impBattrIntArrayAsString = ImpVideoObjbattr.toString();
					impBattrIntArrayAsString = fixArrayedString( impBattrIntArrayAsString, false ); // RSJ - 5 AUG 14
				}
				_req.Imp_battrString = impBattrIntArrayAsString;

				final JsonNode ImpVideoObjplaybackmethod = impVideoObjectNode.path( "playbackmethod" );
				if (ImpVideoObjplaybackmethod.isArray()) {
					Impplaybackmethod = ImpVideoObjplaybackmethod.toString();
					Impplaybackmethod = fixArrayedString( Impplaybackmethod, false ); // RSJ - 5 AUG 14
				}
				_req.Imp_playbackmethodString = Impplaybackmethod;

				final JsonNode ImpVideoObjdelivery = impVideoObjectNode.path( "delivery" );
				if (ImpVideoObjdelivery.isArray()) {
					Impdelivery = ImpVideoObjdelivery.toString();
					Impdelivery = fixArrayedString( Impdelivery, false ); // RSJ - 5 AUG 14
				}
				_req.Imp_deliveryString = Impdelivery;

			} // Close Video Node
			_req.Imp_Type = impType;
		} // Close Imp Node

		// *****************************SITE/APP OBJECT**************************************

		final JsonNode reqSiteNode = rootNode.path( "site" ); // Site Object
		if (reqSiteNode != null && reqSiteNode.size() > 0) {
			MediaType = "SITE";
			_req.Media_id = reqSiteNode.path( "id" ).textValue();
			_req.Media_name = reqSiteNode.path( "name" ).textValue();
			_req.Media_domain = reqSiteNode.path( "domain" ).textValue();
			_req.Media_page = reqSiteNode.path( "page" ).textValue();
			_req.Media_privacypolicy = reqSiteNode.path( "privacypolicy" ).asInt();
			_req.Media_ref = reqSiteNode.path( "ref" ).textValue();
			_req.Media_search = reqSiteNode.path( "search" ).textValue();

			final JsonNode siteExtobjNode = reqSiteNode.path( "ext" ); // Site ext Object
			if (siteExtobjNode != null && siteExtobjNode.size() > 0) {
				_req.MediaExt_mopt = siteExtobjNode.path( "mopt" ).asInt();
				_req.MediaExt_pageid = siteExtobjNode.path( "page_id" ).textValue();
				_req.MediaExt_IsMobileSite = siteExtobjNode.path( "is_mobile_site" ).asBoolean();
				_req.MediaExt_SSLEnabled = siteExtobjNode.path( "ssl_enabled" ).asBoolean();
				_req.MediaExt_ChannelID = siteExtobjNode.path( "channelid" ).textValue();
				coppa = siteExtobjNode.path( "nex_coppa" ).asInt();
				_req.Media_Coppa = coppa;
			}

			final JsonNode sitepublisherobjNode = reqSiteNode.path( "publisher" ); // Site Publisher Object
			if (sitepublisherobjNode != null && sitepublisherobjNode.size() > 0) {
				_req.Publisher_id = sitepublisherobjNode.path( "id" ).textValue();
				_req.Publisher_name = sitepublisherobjNode.path( "name" ).textValue();
				_req.Publisher_domain = sitepublisherobjNode.path( "domain" ).textValue();

				final JsonNode SitePublisherObjcat = sitepublisherobjNode.path( "cat" );
				if (SitePublisherObjcat.isArray()) {
					Publishercat = SitePublisherObjcat.toString();
					Publishercat = fixArrayedString( Publishercat, false ); // RSJ - 5 AUG 14
				}
				_req.Publisher_catString = Publishercat;

				final JsonNode SitePublisherObjkeywords = sitepublisherobjNode.path( "keywords" );
				if (SitePublisherObjkeywords.isArray()) {
					Publisherkeywords = SitePublisherObjkeywords.toString();
					Publisherkeywords = fixArrayedString( Publisherkeywords, false ); // RSJ - 5 AUG 14
				}
				_req.Publisher_keywordsString = Publisherkeywords;
			}

			final JsonNode sitecontentobjNode = reqSiteNode.path( "content" ); // Site Content Object
			if (sitecontentobjNode != null && sitecontentobjNode.size() > 0) {
				_req.Content_id = sitecontentobjNode.path( "id" ).textValue();
				_req.Content_episode = sitecontentobjNode.path( "episode" ).asInt();
				_req.Content_title = sitecontentobjNode.path( "title" ).textValue();
				_req.Content_series = sitecontentobjNode.path( "series" ).textValue();
				_req.Content_season = sitecontentobjNode.path( "season" ).textValue();
				_req.Content_url = sitecontentobjNode.path( "url" ).textValue();
				_req.Content_videoquality = sitecontentobjNode.path( "videoquality" ).asInt();
				_req.Content_contentrating = sitecontentobjNode.path( "contentrating" ).textValue();
				_req.Content_userrating = sitecontentobjNode.path( "userrating" ).textValue();
				_req.Content_context = sitecontentobjNode.path( "context" ).textValue();
				_req.Content_livestream = sitecontentobjNode.path( "livestream" ).asInt();
				_req.Content_sourcerelationship = sitecontentobjNode.path( "sourcerelationship" ).asInt();
				_req.Content_len = sitecontentobjNode.path( "len" ).asInt();

				final JsonNode SiteContentObjcat = sitecontentobjNode.path( "cat" );
				if (SiteContentObjcat.isArray()) {
					Contentcat = SiteContentObjcat.toString();
					Contentcat = fixArrayedString( Contentcat, false ); // RSJ - 5 AUG 14
				}
				_req.Content_catString = Contentcat;

				final JsonNode SiteContentObjkeywords = sitecontentobjNode.path( "keywords" );
				if (SiteContentObjkeywords.isArray()) {
					Contentkeywords = SiteContentObjkeywords.toString();
					Contentkeywords = fixArrayedString( Contentkeywords, false ); // RSJ - 5 AUG 14
				}
				_req.Content_keywordsString = Contentkeywords;
			}

			final JsonNode SiteObjcat = reqSiteNode.path( "cat" );
			if (SiteObjcat.isArray()) {
				Mediacat = SiteObjcat.toString();
				Mediacat = fixArrayedString( Mediacat, false ); // RSJ - 5 AUG 14
			}
			_req.Media_catString = Mediacat;

			final JsonNode SiteObjsectioncat = reqSiteNode.path( "sectioncat" );
			if (SiteObjsectioncat.isArray()) {
				Mediasectioncat = SiteObjsectioncat.toString();
				Mediasectioncat = fixArrayedString( Mediasectioncat, false ); // RSJ - 5 AUG 14
			}
			_req.Media_sectioncatString = Mediasectioncat;

			final JsonNode SiteObjpagecat = reqSiteNode.path( "pagecat" );
			if (SiteObjpagecat.isArray()) {
				Mediapagecat = SiteObjpagecat.toString();
				Mediapagecat = fixArrayedString( Mediapagecat, false ); // RSJ - 5 AUG 14
			}
			_req.Media_pagecatString = Mediapagecat;
			if (Mediacat == null && Mediapagecat != null) {
				_req.Media_catString = Mediapagecat;
			}
		}

		// **************************APP Object******************************
		final JsonNode bidreqappNode = rootNode.path( "app" ); // App Object
		if (bidreqappNode != null && bidreqappNode.size() > 0) {
			MediaType = "APP";
			_req.Media_id = bidreqappNode.path( "id" ).textValue();
			_req.Media_name = bidreqappNode.path( "name" ).textValue();
			_req.Media_domain = bidreqappNode.path( "domain" ).textValue();
			_req.Media_privacypolicy = bidreqappNode.path( "privacypolicy" ).asInt();
			_req.Media_ver = bidreqappNode.path( "ver" ).textValue();
			_req.Media_bundle = bidreqappNode.path( "bundle" ).textValue();
			_req.Media_paid = bidreqappNode.path( "paid" ).asInt();
			_req.Media_storeurl = bidreqappNode.path( "storeurl" ).textValue();

			final JsonNode appExtobjNode = bidreqappNode.path( "ext" );
			if (appExtobjNode != null && appExtobjNode.size() > 0) {
				_req.MediaExt_appstoreid = appExtobjNode.path( "appstoreid" ).textValue();
				_req.MediaExt_storerating = appExtobjNode.path( "storerating" ).asDouble();
				_req.MediaExt_IsMobileSite = appExtobjNode.path( "is_mobile_site" ).asBoolean();
				_req.MediaExt_SSLEnabled = appExtobjNode.path( "ssl_enabled" ).asBoolean();
				_req.MediaExt_ChannelID = appExtobjNode.path( "channelid" ).textValue();

				coppa = appExtobjNode.path( "nex_coppa" ).asInt();
				_req.Media_Coppa = coppa;
			}

			final JsonNode apppublisherobjNode = bidreqappNode.path( "publisher" );// App Publisher Object
			if (apppublisherobjNode != null && apppublisherobjNode.size() > 0) {

				_req.Publisher_id = apppublisherobjNode.path( "id" ).textValue();
				_req.Publisher_name = apppublisherobjNode.path( "name" ).textValue();
				_req.Publisher_domain = apppublisherobjNode.path( "domain" ).textValue();

				final JsonNode AppPublisherObjcat = apppublisherobjNode.path( "cat" );
				if (AppPublisherObjcat.isArray()) {
					Publishercat = AppPublisherObjcat.toString();
					Publishercat = fixArrayedString( Publishercat, false ); // RSJ - 5 AUG 14
				}
				_req.Publisher_catString = Publishercat;

				final JsonNode AppPublisherObjkeywords = apppublisherobjNode.path( "keywords" );
				if (AppPublisherObjkeywords.isArray()) {
					Publisherkeywords = AppPublisherObjkeywords.toString();
					Publisherkeywords = fixArrayedString( Publisherkeywords, false ); // RSJ - 5 AUG 14
				}
				_req.Publisher_keywordsString = Publisherkeywords;

			}

			final JsonNode appcontentobjNode = bidreqappNode.path( "content" ); // App Content object
			if (appcontentobjNode != null && appcontentobjNode.size() > 0) {

				_req.Content_id = appcontentobjNode.path( "id" ).textValue();
				_req.Content_episode = appcontentobjNode.path( "episode" ).asInt();
				_req.Content_title = appcontentobjNode.path( "title" ).textValue();
				_req.Content_series = appcontentobjNode.path( "series" ).textValue();
				_req.Content_season = appcontentobjNode.path( "season" ).textValue();
				_req.Content_url = appcontentobjNode.path( "url" ).textValue();
				_req.Content_videoquality = appcontentobjNode.path( "videoquality" ).asInt();
				_req.Content_contentrating = appcontentobjNode.path( "contentrating" ).textValue();
				_req.Content_userrating = appcontentobjNode.path( "userrating" ).textValue();
				_req.Content_context = appcontentobjNode.path( "context" ).textValue();
				_req.Content_livestream = appcontentobjNode.path( "livestream" ).asInt();
				_req.Content_sourcerelationship = appcontentobjNode.path( "sourcerelationship" ).asInt();
				_req.Content_len = appcontentobjNode.path( "len" ).asInt();

				final JsonNode AppContentObjcat = appcontentobjNode.path( "cat" );
				if (AppContentObjcat.isArray()) {
					Contentcat = AppContentObjcat.toString();
					Contentcat = fixArrayedString( Contentcat, false ); // RSJ - 5 AUG 14
				}
				_req.Content_catString = Contentcat;

				final JsonNode AppContentObjkeywords = appcontentobjNode.path( "keywords" );
				if (AppContentObjkeywords.isArray()) {
					Contentkeywords = AppContentObjkeywords.toString();
					Contentkeywords = fixArrayedString( Contentkeywords, false ); // RSJ - 5 AUG 14
				}
				_req.Content_keywordsString = Contentkeywords;
			}

			final JsonNode AppObjcat = bidreqappNode.path( "cat" );
			if (AppObjcat.isArray()) {
				Mediacat = AppObjcat.toString();
				Mediacat = fixArrayedString( Mediacat, false ); // RSJ - 5 AUG 14
			}
			_req.Media_catString = Mediacat;

			final JsonNode AppObjsectioncat = bidreqappNode.path( "sectioncat" );
			if (AppObjsectioncat.isArray()) {
				Mediasectioncat = AppObjsectioncat.toString();
				Mediasectioncat = fixArrayedString( Mediasectioncat, false ); // RSJ - 5 AUG 14
			}
			_req.Media_sectioncatString = Mediasectioncat;

			final JsonNode AppObjpagecat = bidreqappNode.path( "pagecat" );
			if (AppObjpagecat.isArray()) {
				Mediapagecat = AppObjpagecat.toString();
				Mediapagecat = fixArrayedString( Mediapagecat, false ); // RSJ - 5 AUG 14
			}
			_req.Media_pagecatString = Mediapagecat;
		}
		_req.Site_App = MediaType;

		// ************************DEVICE Object*******************************
		final JsonNode bidreqdeviceNode = rootNode.path( "device" ); // Device Object

		String DeviceObjdpidmd5 = null, DeviceObjos = null;

		if (bidreqdeviceNode != null && bidreqdeviceNode.size() > 0) {
			// The below 3 are used somewhere else in the code, so keeping them as is.

			device_dnt = bidreqdeviceNode.path( "dnt" ).asInt();
			_req.Device_dnt = device_dnt;

			device_ua = bidreqdeviceNode.path( "ua" ).textValue();
			_req.Device_ua = device_ua;

			device_ip = bidreqdeviceNode.path( "ip" ).textValue();
			_req.Device_ip = device_ip == null ? "" : device_ip;

			final String didshaStr = bidreqdeviceNode.path( "didsha" ).textValue();
			if (didshaStr != null) {
				_req.Device_didsha1 = didshaStr;
			} else {
				_req.Device_didsha1 = bidreqdeviceNode.path( "didsha1" ).textValue();
			}

			_req.Device_didmd5 = bidreqdeviceNode.path( "didmd5" ).textValue();

			final String DeviceObjdpidsha1 = bidreqdeviceNode.path( "dpidsha1" ).textValue();
			_req.Device_dpidsha1 = DeviceObjdpidsha1;

			DeviceObjdpidmd5 = bidreqdeviceNode.path( "dpidmd5" ).textValue();
			_req.Device_dpidmd5 = DeviceObjdpidmd5;

			_req.Device_ipv6 = bidreqdeviceNode.path( "ipv6" ).textValue();

			_req.Device_language = bidreqdeviceNode.path( "language" ).textValue();
			_req.Device_make = bidreqdeviceNode.path( "make" ).textValue();
			_req.Device_model = bidreqdeviceNode.path( "model" ).textValue();

			DeviceObjos = bidreqdeviceNode.path( "os" ).textValue();
			_req.Device_os = DeviceObjos;

			_req.Device_osv = bidreqdeviceNode.path( "osv" ).textValue();
			_req.Device_js = bidreqdeviceNode.path( "js" ).intValue();
			_req.Device_devicetype = bidreqdeviceNode.path( "devicetype" ).asInt();
			_req.Device_flashver = bidreqdeviceNode.path( "flashver" ).textValue();

			// Rubicon - Priority 1 - device-ifa
			// Added in OpenRTB 2.2 - 3.3.10 Device Object
			AdThdid = bidreqdeviceNode.path( "ifa" ).textValue();
			if (AdThdid != null) {
				code = "idfa";
			}

			final JsonNode deviceExtObjNode = bidreqdeviceNode.path( "ext" ); // Device Ext Obj
			if (deviceExtObjNode != null && deviceExtObjNode.size() > 0) {
				_req.DeviceExt_storerating = deviceExtObjNode.path( "storerating" ).asInt();
				_req.DeviceExt_appstoreid = deviceExtObjNode.path( "appstoreid" ).textValue();
				_req.DeviceExt_res = deviceExtObjNode.path( "res" ).textValue();
				uidh = deviceExtObjNode.path( "uidh" ).textValue();
				_req.DeviceExt_uidh = uidh;

				// MoPub - Priority 1 - device-ext-idfa
				if (AdThdid == null) {
					AdThdid = deviceExtObjNode.path( "idfa" ).textValue(); // idfa
					if (AdThdid != null) {
						code = "idfa";
					}
				}

				// Nexage - Priority 1 - device-ext-nex_ifa
				if (AdThdid == null) {
					AdThdid = deviceExtObjNode.path( "nex_ifa" ).textValue();
					if (AdThdid != null) {
						code = "idfa"; // nex_ifa
					}
				}

				// OpenX - Priority 1 - device-ext-idforad
				final Boolean deviceExtObjIdForAdEnabled = deviceExtObjNode.path( "idforad_enabled" ).asBoolean();
				_req.Device_idforadenabled = deviceExtObjIdForAdEnabled;

				if (deviceExtObjIdForAdEnabled == true) {
					AdThdid = deviceExtObjNode.path( "idforad" ).textValue();
					_req.Device_idfaclr = AdThdid;
					if (AdThdid != null) {
						code = "idfa";
					}
				}

				// AerServ - Priority 1 - device-ext-ifa
				if (AdThdid == null) {
					AdThdid = deviceExtObjNode.path( "ifa" ).textValue();
					code = "idfa";
				}

				_req.DeviceExt_adtruthid = deviceExtObjNode.path( "adtruth_id" ).textValue();
				_req.DeviceExt_browser = deviceExtObjNode.path( "browser" ).textValue();
				_req.DeviceExt_browserversion = deviceExtObjNode.path( "browser_version" ).textValue();
				_req.DeviceExt_macaddressmd5 = deviceExtObjNode.path( "macaddress_md5" ).textValue();
				_req.DeviceExt_macaddresssha1 = deviceExtObjNode.path( "macaddress_sha1" ).textValue();
				_req.DeviceExt_odin1 = deviceExtObjNode.path( "odin1" ).textValue();
			}

			// MoPub - Priority 2 in order: device-dpidmd5, device-dpidsha1, user-id
			if (AdThdid == null) {
				AdThdid_md5 = DeviceObjdpidmd5;// md5
				if (AdThdid_md5 != null) {
					code = "deviceid_md5";
				}

				AdThdid_sha1 = DeviceObjdpidsha1; // sha1
				if (AdThdid_sha1 != null) {
					code = "deviceid_sha1";
				}
			}

			// Nexage - Priority 2 in order: device-dpidmd5, device-ext-nex_dmacmd5, device-dpidsha1,
			// device-ext-nex_dmac, user-id
			if (AdThdid == null) {
				AdThdid_md5 = DeviceObjdpidmd5;// md5 - Not required as checking above, will remove post testing
				AdThdid_sha1 = DeviceObjdpidsha1;// sha1 - Not required as checking above, will remove post testing
				if (deviceExtObjNode != null) {
					if (AdThdid_md5 == null) {
						AdThdid_md5 = deviceExtObjNode.path( "nex_dmacmd5" ).textValue(); // nexage version of md5
						if (AdThdid_md5 != null) {
							code = "deviceid_md5";
						}
					}
					if (AdThdid_sha1 == null) {
						AdThdid_sha1 = deviceExtObjNode.path( "nex_dmac" ).textValue(); // nexage version of sha1
						if (AdThdid_sha1 != null) {
							code = "deviceid_sha1";
						}
					}
				}
			}

			final JsonNode devicegeoobjNode = bidreqdeviceNode.path( "geo" ); // Device geo Obj
			if (devicegeoobjNode != null && devicegeoobjNode.size() > 0) {

				_req.DeviceGeo_lat = devicegeoobjNode.path( "lat" ).asDouble();
				_req.DeviceGeo_lon = devicegeoobjNode.path( "lon" ).asDouble();
				_req.DeviceGeo_country = devicegeoobjNode.path( "country" ).textValue();
				_req.DeviceGeo_regionfips104 = devicegeoobjNode.path( "regionflips104" ).textValue();
				_req.DeviceGeo_city = devicegeoobjNode.path( "city" ).textValue();
				_req.DeviceGeo_zip = devicegeoobjNode.path( "zip" ).textValue();
				_req.DeviceGeo_zip = _req.DeviceGeo_zip == null ? "" : _req.DeviceGeo_zip;
				_req.DeviceGeo_type = devicegeoobjNode.path( "type" ).asInt();

				final String DeviceExtObjmetro = devicegeoobjNode.path( "metro" ).textValue();
				_req.DeviceGeo_metro = DeviceExtObjmetro;

				final String DeviceGeoObjregion = devicegeoobjNode.path( "region" ).textValue();
				if (DeviceGeoObjregion != null) {

					if (DeviceGeoObjregion.length() > 2) {
						_req.DeviceGeo_region = DeviceGeoObjregion;
					} else {
						_req.DeviceGeo_state = DeviceGeoObjregion;
					}
				}

				final JsonNode devicegeoobjExtNode = devicegeoobjNode.path( "ext" );
				final String DeviceExtObjstate = devicegeoobjExtNode.path( "state" ).textValue();

				if (DeviceExtObjstate != null && DeviceGeoObjregion == null) {
					if (DeviceExtObjstate.length() > 2) {
						_req.DeviceGeo_region = DeviceExtObjstate;
					} else {
						_req.DeviceGeo_state = DeviceExtObjstate;
					}
				}

				if (DeviceExtObjmetro == null) {
					_req.DeviceGeo_metro = devicegeoobjExtNode.path( "dma" ).asText();
				}

			}

		}

		// **************************REGULATIONS Object************************************
		final JsonNode bidreqregsNode = rootNode.path( "regs" );
		if (bidreqregsNode != null && bidreqregsNode.size() > 0) {
			_req.Media_Coppa = bidreqregsNode.path( "coppa" ).intValue();
			final JsonNode bidreqregsextNode = bidreqregsNode.path( "ext" ); // ext Object
			if (bidreqregsextNode != null && bidreqregsextNode.size() > 0) {
				final int s22580 = bidreqregsextNode.path( "s22580" ).intValue();
				if (s22580 == 1) {
					_req.Media_Coppa = 1;
				}
			}
		}

		// **************************USER Object************************************
		final JsonNode bidrequserNode = rootNode.path( "user" );
		if (bidrequserNode != null && bidrequserNode.size() > 0) {
			final String UserObjbuyerid = bidrequserNode.path( "buyerid" ).textValue();
			final String UserObjbuyeruid = bidrequserNode.path( "buyeruid" ).textValue();
			final String Userid = bidrequserNode.path( "id" ).textValue();
			_req.User_id = Userid;
			AdThdid_uid = Userid;

			String keywords = bidrequserNode.path( "keywords" ).textValue();
			if (keywords != null) {
				keywords = fixArrayedString( keywords, false ); // RSJ - 5 AUG 14
			}
			_req.User_keywordsString = keywords;

			if (UserObjbuyerid != null) {
				_req.User_buyeruid = UserObjbuyerid;
			} else {
				_req.User_buyeruid = UserObjbuyeruid;
			}
			_req.User_tz = bidrequserNode.path( "tz" ).asDouble();
			_req.User_sessiondepth = bidrequserNode.path( "sessiondepth" ).asInt();
			_req.User_yob = bidrequserNode.path( "yob" ).asInt();
			_req.User_gender = bidrequserNode.path( "gender" ).textValue();

			if (_req.User_gender != null && _req.User_gender.toLowerCase().startsWith( "m" )) {
				_req.User_gender = "m";
			} else if (_req.User_gender != null && _req.User_gender.toLowerCase().startsWith( "f" )) {
				_req.User_gender = "f";
			} else {
				_req.User_gender = "";
			}

			_req.User_customdata = bidrequserNode.path( "customdata" ).textValue();

			final JsonNode usergeoobjNode = bidrequserNode.path( "geo" ); // User geo Object
			if (usergeoobjNode != null && usergeoobjNode.size() > 0) {
				_req.UserGeo_lat = usergeoobjNode.path( "lat" ).asDouble();
				_req.UserGeo_lon = usergeoobjNode.path( "lon" ).asDouble();
				_req.UserGeo_country = usergeoobjNode.path( "country" ).textValue();
				_req.UserGeo_region = usergeoobjNode.path( "region" ).textValue();
				_req.UserGeo_regionfips104 = usergeoobjNode.path( "regionflips104" ).textValue();
				_req.UserGeo_metro = usergeoobjNode.path( "metro" ).textValue();
				_req.UserGeo_city = usergeoobjNode.path( "city" ).textValue();
				_req.UserGeo_zip = usergeoobjNode.path( "zip" ).textValue();
				_req.UserGeo_type = usergeoobjNode.path( "type" ).asInt();
			}

			// MoPub sends as id, Smaato sends as name
			final Boolean datanode = bidrequserNode.has( "data" );
			try {
				if (datanode != false) {
					final JsonNode userdataobjNode = bidrequserNode.path( "data" );
					for ( int i = 0; i < userdataobjNode.size(); i++ ) {
						final JsonNode dataNode = userdataobjNode.get( i );
						String name = dataNode.path( "name" ).textValue();
						if (name == null) {
							name = dataNode.path( "id" ).textValue();
						}

						if (name.equals( "factual" )) {
							final Boolean segmentnode = dataNode.has( "segment" );
							if (segmentnode != false) {
								final JsonNode userdataobjsegmentNode = dataNode.path( "segment" );
								final StringBuilder sbFactual = new StringBuilder( factual ); // RSJ - 5 AUG 14

								for ( int j = 0; j < userdataobjsegmentNode.size(); j++ ) {
									final JsonNode segmentNode = userdataobjsegmentNode.get( j );
									String segmentName = segmentNode.path( "name" ).textValue();
									if (segmentName == null) {
										segmentName = segmentNode.path( "id" ).textValue();
									}
									final String segmentValue = segmentNode.path( "value" ).textValue();

									sbFactual.append( segmentName ); // RSJ - 5 AUG 14
									sbFactual.append( ":" ); // RSJ - 5 AUG 14
									sbFactual.append( segmentValue ); // RSJ - 5 AUG 14
									if (j != userdataobjsegmentNode.size() - 1) {
										sbFactual.append( "|" ); // RSJ - 5 AUG 14
									}
								}

								factual = sbFactual.toString(); // RSJ - 5 AUG 14

								_req.Factual = factual;
							}
						}// end of factual check

						if (name.equals( "bluekai" )) {
							final Boolean segmentnode = dataNode.has( "segment" );
							if (segmentnode != false) {
								final JsonNode userdataobjsegmentNode = dataNode.path( "segment" );
								final StringBuilder sbBluekai = new StringBuilder( bluekai ); // RSJ - 5 AUG 14

								for ( int j = 0; j < userdataobjsegmentNode.size(); j++ ) {
									final JsonNode segmentNode = userdataobjsegmentNode.get( j );
									String segmentName = segmentNode.path( "name" ).textValue();
									if (segmentName == null) {
										segmentName = segmentNode.path( "id" ).textValue();
									}
									final String segmentValue = segmentNode.path( "value" ).textValue();

									sbBluekai.append( segmentName ); // RSJ - 5 AUG 14
									sbBluekai.append( ":" ); // RSJ - 5 AUG 14
									sbBluekai.append( segmentValue ); // RSJ - 5 AUG 14
									if (j != userdataobjsegmentNode.size() - 1) {
										sbBluekai.append( "|" ); // RSJ - 5 AUG 14
									}
								}

								bluekai = sbBluekai.toString(); // RSJ - 5 AUG 14

								_req.BlueKai = factual;
							}
						}// end of bluekai check
					}// end of data loop
				}

			} catch ( final Exception ex ) {
				exception = "Exception - Factual/BlueKai";
				_req.Exception = exception;
				logger.log( Level.SEVERE, ex.getMessage(), ex );
			}

			// Keywords - MoPub
			if (keywords == null) {

				// Ask about Keywords - MoPub
				final JsonNode UserObjkeywords = bidrequserNode.path( "keywords" );
				if (UserObjkeywords.isArray()) {
					Userkeywords = UserObjkeywords.toString();
					Userkeywords = fixArrayedString( Userkeywords, false ); // RSJ - 5 AUG 14
				}
				_req.User_keywordsString = Userkeywords;
			}
			final JsonNode userextNode = bidrequserNode.path( "ext" );
			_req.User_age = userextNode.path( "age" ).asInt();
			_req.User_ethnicity = userextNode.path( "ethnicity" ).asInt();
			_req.User_income = userextNode.path( "income" ).asInt();
			_req.User_marital = userextNode.path( "marital" ).textValue();
			if (uidh == null) {
				_req.DeviceExt_uidh = userextNode.path( "nex_vzwuidh" ).textValue();
				uidh = userextNode.path( "nex_vzwuidh" ).textValue(); // GP - Sept 16 2014
			}
		}

		// ********************EXT Object************************************
		// OpenRTB - 3.3.1 - Bid Request Object
		final JsonNode rootExtObjectNode = rootNode.path( "ext" );

		// Attempt to parse AerServ custom bidRequest.ext object
		if (bidContext.AdExchangeID == AdExchange.AERSERV) {
			parseAerServExtNode( _req, rootExtObjectNode );
		}

		if (rootExtObjectNode != null && rootExtObjectNode.size() > 0) {
			if (uidh == null) { // GP - Sept 16 2014
				_req.DeviceExt_uidh = rootExtObjectNode.path( "x_uidh" ).textValue();
				uidh = rootExtObjectNode.path( "x_uidh" ).textValue();
			}

			if (uidh == null) { // GP - Sept 16 2014
				_req.DeviceExt_uidh = rootExtObjectNode.path( "uidh" ).textValue();
				uidh = rootExtObjectNode.path( "uidh" ).textValue();
			}

			if (coppa == 0) {
				_req.Media_Coppa = rootExtObjectNode.path( "coppa" ).asInt();
			}

			final JsonNode rootExtUdiObjectNode = rootExtObjectNode.path( "udi" );
			if (rootExtUdiObjectNode != null && rootExtUdiObjectNode.size() > 0) {
				_req.DeviceExt_imei = rootExtUdiObjectNode.path( "imei" ).textValue();
				_req.DeviceExt_idfatracking = rootExtUdiObjectNode.path( "idfatracking" ).asInt();
				_req.DeviceExt_macmd5 = rootExtUdiObjectNode.path( "macmd5" ).textValue();
				_req.DeviceExt_odin = rootExtUdiObjectNode.path( "odin" ).textValue();
				_req.DeviceExt_openudid = rootExtUdiObjectNode.path( "openudid" ).textValue();
				_req.DeviceExt_udidsha1 = rootExtUdiObjectNode.path( "udidsha1" ).textValue();

				final String Android = "Android";
				if (AdThdid == null) {
					if (DeviceObjos != null) {
						if (Android.length() == DeviceObjos.length()) {

							// Priority 1 - ext-udi-androidid (Smaato)
							AdThdid = rootExtUdiObjectNode.path( "googleadid" ).textValue();
							if (AdThdid != null) {
								code = "deviceid";
							}

							// Priority 2 - ext-udi-androididmd5, ext-udi-androididsha1
							if (AdThdid == null) {
								AdThdid = rootExtUdiObjectNode.path( "androididmd5" ).textValue();
								if (AdThdid == null) {
									AdThdid = rootExtUdiObjectNode.path( "androididsha1" ).textValue();
									if (AdThdid != null) {
										code = "deviceid_sha1";
									}
								} else {
									code = "deviceid_md5";
								}
							}

						} else {
							// Smaato: Others [IOS] or anything not android
							// Priority 1 - ext-udi-idfa
							AdThdid = rootExtUdiObjectNode.path( "idfa" ).textValue();
							if (AdThdid != null) {
								code = "idfa";
							}
							// Priority 2 - ext-udi-udididmd5, ext-udi-udidsha1
							if (AdThdid == null) {
								AdThdid = rootExtUdiObjectNode.path( "udidmd5" ).textValue();
								if (AdThdid == null) {
									AdThdid = rootExtUdiObjectNode.path( "udidsha1" ).textValue();
									if (AdThdid != null) {
										code = "deviceid_sha1";
									}
								} else {
									code = "deviceid_md5";
								}
							}
						}
					}
				}
			}

			if (rootExtObjectNode.isArray()) {
				extString = rootExtObjectNode.toString();
				extString = fixArrayedString( extString, false ); // RSJ - 5 AUG 14
			}
			_req.extString = extString;
		}

		// Adthdid: If still, at this point adthdid is null, then take value from md5, sha1, uid in that order
		if (AdThdid == null) {
			AdThdid = AdThdid_md5;
			if (AdThdid == null) {
				AdThdid = AdThdid_sha1;
				if (AdThdid == null) {
					AdThdid = AdThdid_uid;
					if (AdThdid == null) {
						// AdThdid = createMD5(Adthdid_final);
						Adthdid_final = device_ip + "|" + device_ua + "|" + device_dnt; // RSJ - 5 AUG 14
						final String check = createMD5( Adthdid_final );

						if (check.equalsIgnoreCase( "MD5Exception" )) {
							exception = "MD5Exception";
						} else {
							AdThdid = check;
							code = "web_md5";
						}
					} else {
						code = "userid";
					}
				}
			}
		}

		// Fix for the adthdid having ",", replacing it with "|"
		AdThdid = fixArrayedString( AdThdid, true );
		if (AdThdid.contains( "%" )) {
			AdThdid = AdThdid.replace( "%", "" );
		}
		_req.Device_AdThdid = AdThdid;
		_req.Device_AdThdid_Code = code;
		_req.Exception = exception;

		final JsonNode bidreqpmpNode = rootNode.path( "pmp" );
		if (bidreqpmpNode != null && bidreqpmpNode.size() > 0) {
			final JsonNode pmpobjdeals = bidreqpmpNode.path( "deals" );
			final JsonNode pmpdealsobjNode = pmpobjdeals.get( 0 );
			if (pmpdealsobjNode != null && pmpdealsobjNode.size() > 0) {
				_req.PmpDeals_id = pmpdealsobjNode.path( "id" ).textValue();
				_req.PmpDeals_bidfloor = pmpdealsobjNode.path( "bidfloor" ).asDouble();
				// LiveRail Change GP - Sept 11 2014
				if (_req.Imp_bidfloorcur == null) {
					_req.Imp_bidfloorcur = pmpdealsobjNode.path( "bidfloorcur" ).textValue();
				}

				_req.PmpDeals_at = pmpdealsobjNode.path( "at" ).textValue();
			}
			_req.Pmp_Private = bidreqpmpNode.path( "private" ).asInt();
			_req.Pmp_PrivateAuction = bidreqpmpNode.path( "private_auction" ).asInt();
		}

	}

	/**
	 * Attempts to parse AerServ fields and values in <b>{@code bidRequest.ext}</b> to build {@link AerServRequestExt} as a field of the {@link AdThRequest}. If
	 * any of the minimum width / height fields are omitted, 0 is used.
	 * If any of the max width / height fields are omitted, MAX_INT / 2 is used (to protect against others retrieving
	 * its value and performing mathematically operations on it.
	 *
	 * @since 1.9.0
	 */
	private static void parseAerServExtNode( final AdThRequest bidRequest, final JsonNode theRootExtObjectNode ) {
		final boolean isTest = AerServRequestExt.parseAndConvertJsonIntNodeValueToBoolean( theRootExtObjectNode,
			AerServRequestExt.JSON_IS_TEST, AerServRequestExt.DEFAULT_IS_TEST );
		final boolean isPing = AerServRequestExt.parseAndConvertJsonIntNodeValueToBoolean( theRootExtObjectNode,
			AerServRequestExt.JSON_IS_PING, AerServRequestExt.DEFAULT_IS_PING );

		PlatformType platformType = AerServRequestExt.DEFAULT_PLATFORM_TYPE;
		if (theRootExtObjectNode.hasNonNull( AerServRequestExt.JSON_PLATFORM_TYPE )) {
			if (theRootExtObjectNode.get( AerServRequestExt.JSON_PLATFORM_TYPE ).canConvertToInt()) {
				final int platformTypeAsInt =
						theRootExtObjectNode.get( AerServRequestExt.JSON_PLATFORM_TYPE ).intValue();
				platformType = AerServRequestExt.PlatformType.getTypeFromIndex( platformTypeAsInt );
			} else {
				throw new IllegalArgumentException( "AerServ bidRequest.ext." + AerServRequestExt.JSON_PLATFORM_TYPE
					+ " must be an integer, but was ["
					+ theRootExtObjectNode.get( AerServRequestExt.JSON_PLATFORM_TYPE ).getNodeType() + "]" );
			}
		}

		final AerServRequestExt aerServRequestExt = new AerServRequestExt( isTest, isPing, platformType );
		bidRequest.setAerServExt( aerServRequestExt );
	}

	/**
	 * Attempts to parse AerServ fields and values in <b>{@code bidRequest.imp.banner.ext}</b> to build {@link AdThRequest#aerServExtension}. If any of the min
	 * width / height fields are omitted, 0 is used. If any of the max width / height fields are omitted, MAX_INT / 2 is used (to protect against others
	 * retrieving its value and performing mathematically operations on it.
	 *
	 * @since 1.9.0
	 */
	private static void parseAerServImpBannerExtNode( final AdThRequest bidRequest,
			final JsonNode impBannerExtNode ) {
		try {
			final int midWidth = impBannerExtNode.hasNonNull( AerServRequestImpBannerExt.JSON_MIN_WIDTH )
					&& impBannerExtNode.get( AerServRequestImpBannerExt.JSON_MIN_WIDTH ).canConvertToInt()
					? impBannerExtNode.get( AerServRequestImpBannerExt.JSON_MIN_WIDTH ).intValue()
						: 0;

					final int maxWidth = impBannerExtNode.hasNonNull( AerServRequestImpBannerExt.JSON_MAX_WIDTH )
							&& impBannerExtNode.get( AerServRequestImpBannerExt.JSON_MAX_WIDTH ).canConvertToInt()
							? impBannerExtNode.get( AerServRequestImpBannerExt.JSON_MAX_WIDTH ).intValue()
								: Integer.MAX_VALUE / 2;

							final int minHeight = impBannerExtNode.hasNonNull( AerServRequestImpBannerExt.JSON_MIN_HEIGHT )
									&& impBannerExtNode.get( AerServRequestImpBannerExt.JSON_MIN_HEIGHT ).canConvertToInt()
									? impBannerExtNode.get( AerServRequestImpBannerExt.JSON_MIN_HEIGHT ).intValue()
										: 0;

									final int maxHeight = impBannerExtNode.hasNonNull( AerServRequestImpBannerExt.JSON_MAX_HEIGHT )
											&& impBannerExtNode.get( AerServRequestImpBannerExt.JSON_MAX_HEIGHT ).canConvertToInt()
											? impBannerExtNode.get( AerServRequestImpBannerExt.JSON_MAX_HEIGHT ).intValue()
												: Integer.MAX_VALUE / 2;

											final AerServRequestImpBannerExt ext =
													new AerServRequestImpBannerExt( midWidth, maxWidth, minHeight, maxHeight );
											bidRequest.setAerServImpBannerExt( ext );

		} catch ( final Exception e ) {
			logger.log( Level.SEVERE, "Error parsing AerServ bidRequest.imp.banner.ext node: " + e.getMessage(), e );
		}
	}

	/**
	 * Attempts to parses TripleLift's <b>{@code bidRequest.imp.banner.ext.triplelift node}</b> to build a {@link TripleLiftRequestImpBannerExtTripleLift}
	 * object to assign to {@link AdThRequest#tripleLiftExtension}.
	 *
	 * @return true if the the bidRequest.imp.banner.ext.triplelift node was found and parsed, false otherwise
	 * @see {@link https://triplelift.atlassian.net/wiki/display/DSP/OpenRTB+2.2+Specs#OpenRTB2.2Specs-TripleLiftNativeExtension}
	 * @since 1.9.0
	 */
	private static void parseTripleLiftImpBannerExtTripleLiftNode( final AdThRequest bidRequest,
			final JsonNode impBannerExtNode ) {

		try {
			final JsonNode tripleLiftNode = impBannerExtNode.path( "triplelift" );
			if (tripleLiftNode.isMissingNode()) {
				throw new IllegalStateException(
						"TripleLift bid request detected but bidRequest.imp.banner.ext.triplelift node is missing" );
			}

			// Ensure required fields with valid values exist in the JSON bid request.
			if (!tripleLiftNode.hasNonNull( TripleLiftRequestImpBannerExtTripleLift.JSON_IMAGE_WIDTH )
					|| !tripleLiftNode.hasNonNull( TripleLiftRequestImpBannerExtTripleLift.JSON_IMAGE_HEIGHT )
					|| !tripleLiftNode.get( TripleLiftRequestImpBannerExtTripleLift.JSON_IMAGE_WIDTH ).canConvertToInt()
					|| !tripleLiftNode.get( TripleLiftRequestImpBannerExtTripleLift.JSON_IMAGE_HEIGHT ).canConvertToInt()) {
				throw new IllegalStateException(
						"[imgw] and [imgh] are required int values in the TripleLift bidRequest.imp.banner.ext.triplelift node, but are missing or malformed." );
			}

			final int imgWidth =
					tripleLiftNode.get( TripleLiftRequestImpBannerExtTripleLift.JSON_IMAGE_WIDTH ).intValue();
			final int imgHeight =
					tripleLiftNode.get( TripleLiftRequestImpBannerExtTripleLift.JSON_IMAGE_HEIGHT ).intValue();

			final int headingLength =
					tripleLiftNode.hasNonNull( TripleLiftRequestImpBannerExtTripleLift.JSON_HEADING_LENGTH )
					&& tripleLiftNode.get( TripleLiftRequestImpBannerExtTripleLift.JSON_HEADING_LENGTH )
					.canConvertToInt()
					? tripleLiftNode.get( TripleLiftRequestImpBannerExtTripleLift.JSON_HEADING_LENGTH ).intValue()
						: 0;

					final int captionLength =
							tripleLiftNode
							.hasNonNull( TripleLiftRequestImpBannerExtTripleLift.JSON_CAPTION_LENGTH )
							&& tripleLiftNode.get( TripleLiftRequestImpBannerExtTripleLift.JSON_CAPTION_LENGTH )
							.canConvertToInt()
							? tripleLiftNode.get( TripleLiftRequestImpBannerExtTripleLift.JSON_CAPTION_LENGTH ).intValue()
								: 0;

							bidRequest.setTripleLiftImpBannerExtTripleLift(
								new TripleLiftRequestImpBannerExtTripleLift( imgWidth, imgHeight, headingLength, captionLength ) );

		} catch ( final Exception e ) {
			logger.log( Level.SEVERE, "Error parsing TripleLift imp.banner.ext.triplelift node: " + e.getMessage(), e );
		}
	}

	// Internal Java MD5
	public static String createMD5( final String message ) {
		String generatedPassword = "";
		try {
			// Create MessageDigest instance for MD5
			final MessageDigest md = MessageDigest.getInstance( "MD5" );
			// Add password bytes to digest
			md.update( message.getBytes() );
			// Get the hash's bytes
			final byte[] bytes = md.digest();
			// This bytes[] has bytes in decimal format;
			// Convert it to hexadecimal format
			final StringBuilder sb = new StringBuilder();
			for ( final byte b : bytes ) {
				sb.append( Integer.toString( (b & 0xff) + 0x100, 16 ).substring( 1 ) );
			}
			// Get complete hashed password in hex format
			generatedPassword = sb.toString();
		} catch ( final NoSuchAlgorithmException ex ) {
			logger.log( Level.SEVERE, ex.getMessage(), ex );
			return "MD5Exception";
		}
		return generatedPassword;
	}

	// fixArrayedString // RSJ - 5 AUG 14
	// Removes '[', ']', and '"' characters from string and changes ',' character to '|'
	public static String fixArrayedString( final String input, final boolean ignoreQuote ) {
		final StringBuilder sb = new StringBuilder( input );
		int i = 0;
		while ( i < sb.length() ) {
			final char c = sb.charAt( i );
			if (c == '[' || c == ']') {
				sb.deleteCharAt( i );
				// String is shifted back when we delete the character
				// so we want to continue instead of increasing the index
				continue;
			}
			if (!ignoreQuote && c == '\"') {
				sb.deleteCharAt( i );
				continue;
			}
			if (c == ',') {
				sb.setCharAt( i, '|' );
				continue;
			}
			i++;
		}
		return sb.toString();
	}

	public static String assignProtocolString( final String theInput ) {
		final StringBuilder output = new StringBuilder();
		final String input = theInput.replace( "|", "," );
		final String[] ar = input.split( "," );
		boolean except = false;

		final int value[] = new int[ ar.length ];
		for ( int i = 0; i < ar.length; i++ ) {
			try {
				value[ i ] = Integer.parseInt( ar[ i ] );
			} catch ( final NumberFormatException ex ) {
				output.append( "UnMapped Value: " + ar[ i ] );
				except = true;
			}
			if (value[ i ] > 6 || value[ i ] < 1 && except != true) {
				output.append( "UnMapped Value: " + value[ i ] );
			} else {
				switch ( value[ i ] ) {
					case 1:
						output.append( "VAST 1.0" );
						break;
					case 2:
						output.append( "VAST 2.0" );
						break;
					case 3:
						output.append( "VAST 3.0" );
						break;
					case 4:
						output.append( "VAST 1.0 Wrapper" );
						break;
					case 5:
						output.append( "VAST 2.0 Wrapper" );
						break;
					case 6:
						output.append( "VAST 3.0 Wrapper" );
						break;
					default:
						break;
				}
			}
			if (i != ar.length - 1) {
				output.append( "|" );
			}
			except = false;
		}

		return output.toString();
	}

}

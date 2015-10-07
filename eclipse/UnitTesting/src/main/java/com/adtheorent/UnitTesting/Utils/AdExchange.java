package com.adtheorent.UnitTesting.Utils;

/**
 * All supported Advertising Exchanges
 *
 * @since 1.0.0
 */
public enum AdExchange {

	/**
	 * Internal ID: <b>3</b>
	 *
	 * @since 1.0.0
	 */
	SMAATO("Smaato"),

	/**
	 * Internal ID: <b>50</b>
	 *
	 * @since 1.0.0
	 */
	RUBICON("Rubicon"),

	/**
	 * Internal ID: <b>46</b>
	 *
	 * @since 1.0.0
	 */
	MOPUB("MoPub"),

	/**
	 * Internal ID: <b>2</b>
	 *
	 * @since 1.0.0
	 */
	NEXAGE("Nexage"),

	/**
	 * Internal ID: <b>47</b>
	 *
	 * @since 1.0.0
	 */
	BRIGHTROLL("BrightRoll"),

	/**
	 * Internal ID: <b>51</b>
	 *
	 * @since 1.0.0
	 */
	ADX("AdX"),

	/**
	 * Internal ID: <b>55</b>
	 *
	 * @since 1.0.0
	 */
	SPOTX("SpotX"),

	/**
	 * Internal ID: <b>52</b>
	 *
	 * @since 1.0.0
	 */
	OPENX("OpenX"),

	/**
	 * Internal ID: <b>58</b>
	 *
	 * @since 1.0.0
	 */
	YIELDMO("YieldMo"),

	/**
	 * Internal ID: <b>65</b>
	 *
	 * @since 1.0.0
	 */
	VDOPIA("Vdopia"),

	/**
	 * Internal ID: <b>61</b>
	 *
	 * @since 1.0.0
	 */
	ADAPTV("Adaptv"),

	/**
	 * Internal ID: <b>60</b>
	 *
	 * @since 1.0.0
	 */
	LIVERAIL("LiveRail"),

	/**
	 * Internal ID: <b>62</b>
	 *
	 * @since 1.0.0
	 */
	AMOBEE("Amobee"),

	/**
	 * Internal ID: <b>64</b>
	 *
	 * @since 1.0.0
	 */
	OMAX("OMAX"),

	/**
	 * Internal ID: <b>66</b>
	 *
	 * @since 1.9.0
	 */
	TRIPLELIFT("TripleLift"),

	/**
	 * Internal ID: <b>67</b>
	 *
	 * @since 1.9.0
	 */
	AERSERV("AerServ");
	


	private final String folderName;

	private AdExchange(final String theFolderName) {
		folderName = theFolderName;

	}

	final public String getFolderName() {
		return folderName;
	}

}

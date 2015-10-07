package com.adtheorent.UnitTesting.Utils.DEX;

// TODO see if we can remove this POJO entirely

/**
 * Holds unique aggregateKey for each bid log request message and the raw message. This is used during bidLogMessageQueue parsing of bid
 * log messages.
 */
public final class HoldData {

	private final String aggregateKey;
	private final String rawBidLogMessage;

	/**
	 * Creates an instance of this object with specified parameters.
	 *
	 * @param theAggregateKey
	 *            the aggregateKey is generated in the {@code Bidder} project's {@code BidEnd} {@code SendSummaryAndCount} method and is formatted as:
	 *            <p>
	 *            {@code BidLogMessageType | AdExchange | CampaignId | CreativeId | KinesisShardId | date |}
	 * @param theRawBidLogMessage
	 *            the raw bid log message, which will be one of the defined types in {@link BidLogMessageType}
	 */
	public HoldData(final String theAggregateKey, final String theRawBidLogMessage) {
		this.aggregateKey = theAggregateKey;
		this.rawBidLogMessage = theRawBidLogMessage;
	}

	public String getKey() {
		final String theKey = this.aggregateKey;
		return theKey;
	}

	public String getRecord() {
		final String theRecord = this.rawBidLogMessage;
		return theRecord;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.aggregateKey == null ? 0 : this.aggregateKey.hashCode());
		result = prime * result + (this.rawBidLogMessage == null ? 0 : this.rawBidLogMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals( final Object obj ) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final HoldData other = (HoldData) obj;
		if (this.aggregateKey == null) {
			if (other.aggregateKey != null) {
				return false;
			}
		} else if (!this.aggregateKey.equals( other.aggregateKey )) {
			return false;
		}
		if (this.rawBidLogMessage == null) {
			if (other.rawBidLogMessage != null) {
				return false;
			}
		} else if (!this.rawBidLogMessage.equals( other.rawBidLogMessage )) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "HoldData [aggregateKey=" + this.aggregateKey + ", rawBidLogMessage=" + this.rawBidLogMessage + "]";
	}

}

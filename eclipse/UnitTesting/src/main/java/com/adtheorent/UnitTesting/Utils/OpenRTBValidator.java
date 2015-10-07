package com.adtheorent.UnitTesting.Utils;
import java.io.IOException;

import org.openrtb.validator.OpenRtbInputType;
import org.openrtb.validator.OpenRtbValidator;
import org.openrtb.validator.OpenRtbValidatorFactory;
import org.openrtb.validator.OpenRtbVersion;
import org.openrtb.validator.ValidationResult;


public final class OpenRTBValidator {

	public static boolean isBidResponseRtbCompliant( final String bidResponse, final OpenRtbVersion rtbVersion ) {
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator( OpenRtbInputType.BID_RESPONSE,
			rtbVersion );
		ValidationResult validationResult;
		try {
			validationResult = validator.validate( bidResponse );
			// System.out.println( validationResult.getResult() );
			return validationResult.isValid();

		} catch ( final IOException e ) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isBidRequestRtbCompliant( final String bidRequest, final OpenRtbVersion rtbVersion ) {
		final OpenRtbValidator validator = OpenRtbValidatorFactory.getValidator( OpenRtbInputType.BID_REQUEST,
			rtbVersion );
		ValidationResult validationResult;
		try {
			validationResult = validator.validate( bidRequest );
			// System.out.println( validationResult.getResult() );
			return validationResult.isValid();

		} catch ( final IOException e ) {
			e.printStackTrace();
		}
		return false;
	}

}

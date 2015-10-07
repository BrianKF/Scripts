package com.adtheorent.UnitTesting.Utils;
import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;

public class Aws {
	public static AmazonS3Client s3Client; 
	
	public static void s3Login() {
		AWSCredentialsProvider credentialsProvider = new ClasspathPropertiesFileCredentialsProvider();
		try {
			credentialsProvider.getCredentials();
		} catch ( final Exception e ) {
			throw new AmazonClientException( "Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct ", e );
		}
		s3Client = new AmazonS3Client( credentialsProvider, new ClientConfiguration()
		.withMaxConnections( 100 )
		.withConnectionTimeout( 120 * 1000 )
		.withMaxConnections( 10 ) );
	}
}

package com.adtheorent.UnitTesting.Utils.Platform;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adtheorent.UnitTesting.Utils.*;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class DEFileMerger {

	private static final Properties properties = PropertiesUtils
			.getProperties( ConfigKeys.ENVIRONMENT_PROPERTIES_FILE_NAME_KEY );

	private static final Logger logger = Logger.getLogger( DEFileMerger.class.getName() );

	private final String existingBucketName = properties
			.getProperty( ConfigKeys.DE_BUCKET_NAME );
	private final String filePath = properties
			.getProperty( ConfigKeys.DE_FILE_PATH );

	private final String de04 = properties.getProperty( ConfigKeys.DE_04_FILE );
	private final String de08 = properties.getProperty( ConfigKeys.DE_08_FILE );
	private final String de09 = properties.getProperty( ConfigKeys.DE_09_FILE );
	private final String de24 = properties.getProperty( ConfigKeys.DE_24_FILE );
	private final String detz = properties.getProperty( ConfigKeys.DE_TZ_FILE );

	private final String deTargetFile = properties
			.getProperty( ConfigKeys.DE_TARGET_FILE );
	private final String deBackupFile = properties
			.getProperty( ConfigKeys.DE_BACKUP_FILE );
	private final String targetLocation = properties
			.getProperty( ConfigKeys.DE_TARGET_LOCATION );

	private Timer timer;
	private final AmazonS3 s3Client;
	private final MergeFiles mergeFiles;

	public DEFileMerger(final AmazonS3 s3Client, final MergeFiles mergeFiles) {

		this.s3Client = s3Client;
		this.mergeFiles = mergeFiles;

		scheduleNextRun();
	}

	private void scheduleNextRun() {

		final Calendar nextRun = Calendar.getInstance( TimeZone.getTimeZone( "EST5EDT" ) );

		nextRun.set( Calendar.SECOND, 0 );
		nextRun.set( Calendar.MINUTE, 0 );
		nextRun.set( Calendar.HOUR_OF_DAY, 3 );

		do {
			nextRun.add( Calendar.DAY_OF_MONTH, 1 );
		} while ( nextRun.get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY );

		final DateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm z", Locale.ENGLISH );
		df.setTimeZone( TimeZone.getTimeZone( "EST5EDT" ) );

		final Date scheduledRun = nextRun.getTime();
		System.out.println( "DE File Merge scheduled for " + df.format( scheduledRun ) );

		if (timer != null) {
			timer.cancel();
		}

		timer = new Timer( "DEFileMergeTimer" );

		timer.schedule( new DownloadFilesTask(), scheduledRun );
	}

	public void runNow() {

		System.out.println( "Manual DE File Merge executed" );

		new DownloadFilesTask().run();
	}

	private class DownloadFilesTask extends TimerTask {

		@Override
		public void run() {

			System.out.println( "Running DE File Merge..." );

			long start, end, totalStart, totalEnd;
			BufferedReader br = null;

			totalStart = System.currentTimeMillis();

			/* Download Files to the location */
			S3DownloadFile(existingBucketName, de04, filePath + de04);
			S3DownloadFile(existingBucketName, de08, filePath + de08);
			S3DownloadFile(existingBucketName, de09, filePath + de09);
			S3DownloadFile(existingBucketName, de24, filePath + de24);
			S3DownloadFile(existingBucketName, detz, filePath + detz);

			/* Take backup */
			System.out.println( "Creating backup file: " + filePath
				+ deBackupFile);

			start = System.currentTimeMillis();
			Path source = Paths.get(filePath + deTargetFile);
			Path target = Paths.get(filePath + deBackupFile);

			try {
				Files.deleteIfExists(target);
				final File f = new File(filePath + deTargetFile);
				if (f.exists() && !f.isDirectory()) {
					Files.copy(source, target);
				} else {
					System.out.println( "File: " + filePath + deTargetFile
						+ " doesn't exist. Backup not taken");

				}
				end = System.currentTimeMillis();
				System.out.println( "Time taken for backup : "
						+ TimeUnit.MILLISECONDS.toSeconds(end - start)
						+ " second(s)");

			} catch (final IOException e) {
				logger.log( Level.SEVERE, e.getMessage(), e );

			}

			/* Merge files */
			System.out.println( "Merging files " + de04 + " & " + de24);
			start = System.currentTimeMillis();
			mergeFiles.initMapperCarrier();
			mergeFiles.createMapperCarrierMergedFile();
			end = System.currentTimeMillis();
			System.out.println( "Time taken to merge " + de04 + " & " + de24
				+ " : " + TimeUnit.MILLISECONDS.toMinutes(end - start)
				+ " minute(s)");

			System.out.println( "Merging files " + de04 + "," + de09 + " & "
					+ de24);
			start = System.currentTimeMillis();
			mergeFiles.initHomeBiz();
			mergeFiles.createHomeBizMergedFile();
			end = System.currentTimeMillis();
			System.out.println(
				"Time taken to merge " + de04 + "," + de09 + " & " + de24
				+ " : " + TimeUnit.MILLISECONDS.toMinutes(end - start)
				+ " minute(s)");

			System.out.println( "Merging files " + de04 + "," + de08 + ","
					+ de09 + " & " + de24);
			start = System.currentTimeMillis();
			mergeFiles.initIsp();
			mergeFiles.createIspMergedFile();
			end = System.currentTimeMillis();
			System.out.println(
				"Time taken to merge " + de04 + "," + de08 + "," + de09 + " & "
						+ de24 + " : "
						+ TimeUnit.MILLISECONDS.toMinutes(end - start)
						+ " minute(s)");

			System.out.println( "Merging files " + de04 + "," + de08 + ","
					+ de09 + "," + de24 + " & " + detz);
			start = System.currentTimeMillis();
			mergeFiles.initTZ();
			mergeFiles.createTimeZoneMergedFile();
			end = System.currentTimeMillis();
			System.out.println( "Time taken to merge " + de04 + "," + de08 + ","
					+ de09 + "," + de24 + " & " + detz + " : "
					+ TimeUnit.MILLISECONDS.toMinutes(end - start) + " minute(s)");

			/* Copy to the IOPS volume */
			System.out.println( "Copying the final file to: " + targetLocation);
			start = System.currentTimeMillis();
			source = Paths.get(filePath + deTargetFile);
			target = Paths.get(targetLocation + deTargetFile);
			try {
				br = new BufferedReader(new FileReader(filePath + deTargetFile));
				if (br.readLine() == null) {
					System.out.println("File empty");
				} else {
					Files.copy( source, target, StandardCopyOption.REPLACE_EXISTING );
				}
				end = System.currentTimeMillis();
				System.out.println(
					"Time taken (in mins) to copy the file to IOPS volume: "
							+ TimeUnit.MILLISECONDS.toMinutes(end - start));
			} catch (final IOException e) {
				logger.log( Level.SEVERE, e.getMessage(), e );
			} finally {
				try {
					br.close();
				} catch (final IOException e) {
					logger.log( Level.SEVERE, e.getMessage(), e );
				}
			}

			totalEnd = System.currentTimeMillis();
			System.out.println( "Time taken for the entire process: "
					+ TimeUnit.MILLISECONDS.toMinutes(totalEnd - totalStart)
					+ " minutes");

			scheduleNextRun();
		}

		public void S3DownloadFile( final String bucketName,
				final String keyName, final String fullFilePath) {

			try {
				System.out.println("Downloading - " + keyName);
				final long start = System.currentTimeMillis();
				final S3Object s3object = s3Client.getObject(new GetObjectRequest(
					bucketName, keyName));
				System.out.println("Content Length: "
						+ s3object.getObjectMetadata().getContentLength() / 1024
						+ " KB");
				final InputStream reader = new BufferedInputStream(
					s3object.getObjectContent());
				final File file = new File(fullFilePath);
				final OutputStream writer = new BufferedOutputStream(
					new FileOutputStream(file));
				int read = -1;
				while ((read = reader.read()) != -1) {
					writer.write(read);
				}

				writer.flush();
				writer.close();
				reader.close();
				s3object.close();
				final long end = System.currentTimeMillis();
				final long total = end - start;
				System.out.println(
					"File " + keyName + " successfully downloaded in "
							+ TimeUnit.MILLISECONDS.toSeconds(total)
							+ " seconds(s)");

			} catch (final AmazonServiceException ase) {
				System.out.println("Caught an AmazonServiceException, which"
						+ " means your request made it "
						+ "to Amazon S3, but was rejected with an error response"
						+ " for some reason.");
				System.out.println("Error Message:    " + ase.getMessage());
				System.out.println("HTTP Status Code: " + ase.getStatusCode());
				System.out.println("AWS Error Code:   " + ase.getErrorCode());
				System.out.println("Error Type:       " + ase.getErrorType());
				System.out.println("Request ID:       " + ase.getRequestId());
			} catch (final AmazonClientException ace) {
				System.out.println("Caught an AmazonClientException, which means"
						+ " the client encountered "
						+ "an internal error while trying to "
						+ "communicate with S3, "
						+ "such as not being able to access the network.");
				System.out.println("Error Message: " + ace.getMessage());
			} catch (final FileNotFoundException e) {
				logger.log( Level.SEVERE, e.getMessage(), e );
			} catch (final IOException e) {
				logger.log( Level.SEVERE, e.getMessage(), e );
			}
		}
	}
}
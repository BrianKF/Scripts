package com.adtheorent.UnitTesting.Utils.Platform;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.adtheorent.AdThCommon_Dex.Utils.*;


public class MergeFiles {

	private static final Logger logger = Logger.getLogger( MergeFiles.class.getName() );

	private static final Properties properties = PropertiesUtils
			.getProperties(ConfigKeys.ENVIRONMENT_PROPERTIES_FILE_NAME_KEY);

	private final String filePath = properties
			.getProperty(ConfigKeys.DE_FILE_PATH);

	private String inputFile01;
	private String inputFile02;
	private String outputFile;
	// private final double MEG = Math.pow( 1024, 2 );
	private final Hashtable<String, String> ht = new Hashtable();

	private final String de04 = properties.getProperty( ConfigKeys.DE_04_FILE );
	private final String de08 = properties.getProperty( ConfigKeys.DE_08_FILE );
	private final String de09 = properties.getProperty( ConfigKeys.DE_09_FILE );
	private final String de24 = properties.getProperty( ConfigKeys.DE_24_FILE );
	private final String detz = properties.getProperty( ConfigKeys.DE_TZ_FILE );

	private final String de0424 = properties
			.getProperty(ConfigKeys.DE_04_24_FILE);
	private final String de042409 = properties
			.getProperty(ConfigKeys.DE_04_24_09_FILE);
	private final String de04240908 = properties
			.getProperty(ConfigKeys.DE_04_24_09_08_FILE);

	private final String deTargetFile = properties
			.getProperty(ConfigKeys.DE_TARGET_FILE);

	public void initMapperCarrier() {
		inputFile01 = filePath + de04;
		inputFile02 = filePath + de24;
		outputFile = filePath + de0424;
	}

	public void initHomeBiz() {
		inputFile01 = outputFile;
		inputFile02 = filePath + de09;
		outputFile = filePath + de042409;
	}

	public void initIsp() {
		inputFile01 = outputFile;
		inputFile02 = filePath + de08;
		outputFile = filePath + de04240908;
	}

	public void initTZ() {
		inputFile01 = outputFile;
		outputFile = filePath + deTargetFile;
		putTZ();
	}

	public void createMapperCarrierMergedFile() {

		final ArrayList<String> mobile_carrier = new ArrayList<String>(); // 10061 =
		// records
		// in mobile
		// carrier
		// file
		final ArrayList<double[]> mc_start_end = new ArrayList<double[]>();

		final double[] edge_start_end = new double[2];

		BufferedReader br_edgefile, br_mobilecarrier = null;
		BufferedWriter br_outfile = null;

		String rec[], line;
		try {
			br_edgefile = new BufferedReader(new FileReader(inputFile01));
			br_mobilecarrier = new BufferedReader(new FileReader(inputFile02));
			br_outfile = new BufferedWriter( new FileWriter( outputFile ) );

			// br_outfile = new FileOutputStream(outputFile, false);

			// Reading Mobile Carrier File
			line = br_mobilecarrier.readLine(); // passing by header
			while ((line = br_mobilecarrier.readLine()) != null) {
				line = line.trim();
				rec = line.split(",");

				final double[] from_to = new double[2];
				from_to[0] = Double.parseDouble(rec[0]);
				from_to[1] = Double.parseDouble(rec[1]);
				mc_start_end.add(from_to);
				mobile_carrier.add(line);

			}

			// Reading Edge File
			rec = br_edgefile.readLine().split(","); // passing be header
			br_outfile
			.write("start_num,end_num,edge-three-letter-country,edge-region,edge-city,edge-metro-code,edge-postal-code,mobile-carrier,mcc-mnc\n");
			boolean flag = false;
			double e1, e2, mc1, mc2;

			while ((line = br_edgefile.readLine()) != null) {
				line = line.trim();
				rec = line.split(",");

				edge_start_end[0] = Double.parseDouble(rec[0]);
				edge_start_end[1] = Double.parseDouble(rec[1]);
				e1 = edge_start_end[0];
				e2 = edge_start_end[1];

				for (int j = 0; j < mc_start_end.size(); j++) {

					final double[] from_to = mc_start_end.get(j);

					mc1 = from_to[0];
					mc2 = from_to[1];

					if (e1 >= mc1 && e2 <= mc2) {
						final String rec_2[] = mobile_carrier.get(j).split(",");
						br_outfile.write(rec[0] + "," + rec[1] + "," + rec[2]
								+ "," + rec[3] + "," + rec[4] + "," + rec[5]
										+ "," + rec[6] + "," + rec_2[2] + ","
										+ rec_2[3] + "-" + rec_2[4] + "\n");
						flag = true;

						break; // To break out from internal for loop

					} else if (mc1 >= e1 && mc2 <= e2) {
						final String rec_2[] = mobile_carrier.get(j).split(",");
						br_outfile.write(rec[0] + "," + (long) (mc1 - 1) + ","
								+ rec[2] + "," + rec[3] + "," + rec[4] + ","
								+ rec[5] + "," + rec[6] + ",,\n");
						br_outfile.write(rec_2[0] + "," + rec_2[1] + ","
								+ rec[2] + "," + rec[3] + "," + rec[4] + ","
								+ rec[5] + "," + rec[6] + "," + rec_2[2] + ","
								+ rec_2[3] + "-" + rec_2[4] + "\n");
						br_outfile.write((long) (mc2 + 1) + "," + rec[1] + ","
								+ rec[2] + "," + rec[3] + "," + rec[4] + ","
								+ rec[5] + "," + rec[6] + ",,\n");
						flag = true;

						break; // To break out from internal for loop

					} else if (mc1 >= e1 && mc1 <= e2 && mc2 >= e2) {
						final String rec_2[] = mobile_carrier.get(j).split(",");
						br_outfile.write(rec[0] + "," + (long) (mc1 - 1) + ","
								+ rec[2] + "," + rec[3] + "," + rec[4] + ","
								+ rec[5] + "," + rec[6] + ",,\n");
						br_outfile.write(rec_2[0] + "," + rec[1] + ","
								+ rec[2] + "," + rec[3] + "," + rec[4] + ","
								+ rec[5] + "," + rec[6] + "," + rec_2[2] + ","
								+ rec_2[3] + "-" + rec_2[4] + "\n");
						flag = true;

						break; // To break out from internal for loop

					} else if (mc1 <= e1 && mc2 >= e1 && mc2 <= e2) {
						final String rec_2[] = mobile_carrier.get(j).split(",");
						br_outfile.write(rec[0] + "," + rec_2[1] + ","
								+ rec[2] + "," + rec[3] + "," + rec[4] + ","
								+ rec[5] + "," + rec[6] + "," + rec_2[2] + ","
								+ rec_2[3] + "-" + rec_2[4] + "\n");
						br_outfile.write((long) (mc2 + 1) + "," + rec[1] + ","
								+ rec[2] + "," + rec[3] + "," + rec[4] + ","
								+ rec[5] + "," + rec[6] + ",,\n");
						flag = true;

						break; // To break out from internal for loop

					}

				} // for loop ends here

				// if Edge record can't be merged with any record in mobile
				// carrier file
				if (flag == false) {
					rec = line.split(",");
					br_outfile.write(rec[0] + "," + rec[1] + "," + rec[2]
							+ "," + rec[3] + "," + rec[4] + "," + rec[5] + ","
							+ rec[6] + ",,\n");
				}

				flag = false;

			} // while ends here

			br_outfile.flush();
			br_outfile.close();

		} catch (final Exception e) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		}

	} // Create method ends here

	@SuppressWarnings("resource")
	public void createHomeBizMergedFile() {

		final ArrayList<String> home_biz_rec = new ArrayList<String>(); // 90k approx.
		// = records
		// in
		// HomeBiz
		// file
		final ArrayList<double[]> hb_start_end = new ArrayList<double[]>();

		final double[] edge_start_end = new double[2];

		BufferedReader br_mc_merged_edgefile, br_homebiz = null;
		BufferedWriter br_outfile = null;

		String rec[], line;
		try {
			br_mc_merged_edgefile = new BufferedReader(new FileReader(
				inputFile01));
			br_homebiz = new BufferedReader(new FileReader(inputFile02));
			br_outfile = new BufferedWriter( new FileWriter( outputFile ) );

			// Reading Mobile Carrier File
			line = br_homebiz.readLine(); // passing by header
			while ((line = br_homebiz.readLine()) != null) {
				line = line.trim();
				rec = line.split(",");

				final double[] from_to = new double[2];
				from_to[0] = Double.parseDouble(rec[0]);
				from_to[1] = Double.parseDouble(rec[1]);
				hb_start_end.add(from_to);
				home_biz_rec.add(line);

			}

			// Reading MC Merged Edge File
			rec = br_mc_merged_edgefile.readLine().split(","); // passing be
			// header
			br_outfile
			.write("start_num,end_num,edge-three-letter-country,edge-region,edge-city,edge-metro-code,edge-postal-code,mobile-carrier,mcc-mnc,homebiz-type\n");
			boolean flag = false;
			double e1, e2, mc1, mc2;

			String ToOutput = "";

			while ((line = br_mc_merged_edgefile.readLine()) != null) {
				line = line.trim();
				rec = line.split(",");

				edge_start_end[0] = Double.parseDouble(rec[0]);
				edge_start_end[1] = Double.parseDouble(rec[1]);
				e1 = edge_start_end[0];
				e2 = edge_start_end[1];

				for (int j = 0; j < hb_start_end.size(); j++) {

					final double[] from_to = hb_start_end.get(j);

					mc1 = from_to[0];
					mc2 = from_to[1];

					if (e1 >= mc1 && e2 <= mc2) {
						final String rec_2[] = home_biz_rec.get(j).split(",");

						if (rec.length == 7) {
							ToOutput = rec[0] + "," + rec[1] + "," + rec[2]
									+ "," + rec[3] + "," + rec[4] + ","
									+ rec[5] + "," + rec[6] + ",,," + rec_2[2]
											+ "\n";
						} else if (rec.length == 8) {
							ToOutput = rec[0] + "," + rec[1] + "," + rec[2]
									+ "," + rec[3] + "," + rec[4] + ","
									+ rec[5] + "," + rec[6] + "," + rec[7]
											+ ",," + rec_2[2] + "\n";
						} else {
							ToOutput = rec[0] + "," + rec[1] + "," + rec[2]
									+ "," + rec[3] + "," + rec[4] + ","
									+ rec[5] + "," + rec[6] + "," + rec[7]
											+ "," + rec[8] + "," + rec_2[2] + "\n";
						}

						br_outfile.write(ToOutput);
						flag = true;
						break; // To break out from internal for loop
					}
				} // for loop ends here

				// if Edge record can't be merged with any record in mobile
				// carrier file
				if (flag == false) {
					rec = line.split(",");
					br_outfile.write(rec[0] + "," + rec[1] + "," + rec[2]
							+ "," + rec[3] + "," + rec[4] + "," + rec[5] + ","
							+ rec[6] + ",,,\n");

				}

				flag = false;
			} // while ends here

			br_outfile.flush();
			br_outfile.close();

		} catch (final Exception e) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		}

	} // Create method ends here

	public void createIspMergedFile() {

		final ArrayList<String> isp_rec = new ArrayList<String>(); // 90k approx. =
		// records in
		// HomeBiz file
		final ArrayList<double[]> isp_start_end = new ArrayList<double[]>();

		final double[] edge_start_end = new double[2];

		BufferedReader br_mc_merged_edgefile, br_isp = null;
		BufferedWriter br_outfile = null;

		String rec[], line;
		try {
			br_mc_merged_edgefile = new BufferedReader(new FileReader(
				inputFile01));
			br_isp = new BufferedReader(new FileReader(inputFile02));
			br_outfile = new BufferedWriter( new FileWriter( outputFile ) );

			// Reading File
			line = br_isp.readLine(); // passing by header
			while ((line = br_isp.readLine()) != null) {
				line = line.trim();;
				rec = line.split(",");

				final double[] from_to = new double[2];
				from_to[0] = Double.parseDouble(rec[0]);
				from_to[1] = Double.parseDouble(rec[1]);
				isp_start_end.add(from_to);
				isp_rec.add(line);

			}

			// Reading MC Merged Edge File
			rec = br_mc_merged_edgefile.readLine().split(","); // passing be
			// header
			br_outfile
			.write("start_num,end_num,edge-three-letter-country,edge-region,edge-city,edge-metro-code,edge-postal-code,mobile-carrier,mcc-mnc,homebiz-type,isp-name\n");
			boolean flag = false;
			double e1, e2, mc1, mc2;

			String ToOutput = "";

			while ((line = br_mc_merged_edgefile.readLine()) != null) {
				line = line.trim();
				rec = (line + ",1").split(",");

				edge_start_end[0] = Double.parseDouble(rec[0]);
				edge_start_end[1] = Double.parseDouble(rec[1]);
				e1 = edge_start_end[0];
				e2 = edge_start_end[1];

				for (int j = 0; j < isp_start_end.size(); j++) {

					final double[] from_to = isp_start_end.get(j);

					mc1 = from_to[0];
					mc2 = from_to[1];

					if (e1 >= mc1 && e2 <= mc2) {
						final String rec_2[] = isp_rec.get(j).split(",");

						ToOutput = rec[0] + "," + rec[1] + "," + rec[2] + ","
								+ rec[3] + "," + rec[4] + "," + rec[5] + ","
								+ rec[6] + "," + rec[7] + "," + rec[8] + ","
								+ rec[9] + "," + rec_2[2] + "\n";

						br_outfile.write(ToOutput);

						flag = true;

						break; // To break out from internal for loop

					}

				} // for loop ends here

				// if Edge record can't be merged with any record in mobile
				// carrier file
				if (flag == false) {
					br_outfile.write(line + ",\n");
				}

				flag = false;
			} // while ends here

			br_outfile.flush();
			br_outfile.close();

		} catch (final Exception e) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		}

	} // Create method ends here

	public void createTimeZoneMergedFile() {
		BufferedReader br_inputfile;
		BufferedWriter br_outputfile;
		try {
			br_inputfile = new BufferedReader(new FileReader(inputFile01));
			br_outputfile = new BufferedWriter( new FileWriter( outputFile ) );

			String line, tz, strArray[];
			br_outputfile
			.write("start_num,end_num,edge-three-letter-country,edge-region,edge-city,edge-metro-code,edge-postal-code,mobile-carrier,mcc-mnc,homebiz-type,isp-name,timezone\n");
			// Reading the header line so that it doesn't get written to the new
			// file
			br_inputfile.readLine();
			while ((line = br_inputfile.readLine()) != null) {
				line = line.trim().replaceAll( "'", "" );
				strArray = line.split(",");
				tz = getTZ(strArray[6]);

				if (!(tz == null || tz.isEmpty()) && strArray[2].equals("usa")) {
					br_outputfile.write(line + "," + tz
						+ System.lineSeparator());
				} else {
					br_outputfile.write(line + "," + System.lineSeparator());
				}
			}

			br_inputfile.close();
			br_outputfile.close();

		} catch (final IOException e) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		}

	}

	public void putTZ() {
		try{
			BufferedReader br = new BufferedReader(new FileReader(filePath
			+ detz));

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				final String[] splitLine = sCurrentLine.split(",");
				ht.put(splitLine[0], splitLine[1]);
			}
			System.out.println("Hashtable Populated - Total Size: " + ht.size());
		} catch (final IOException e) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		}
	}

	public String getTZ( final String zip ) {
		return ht.get(zip);
	}

	/*
	 * To Convert line with quotes in it to line without quotes Replaces commas
	 * within quotes with '|' Not using this method right now, However, if we
	 * find out that some lines in Edge file have quotes, then we can use it
	 */
	public String QuoteLineToCSV( final String line ) {

		String newline = "";
		final char[] array = line.toCharArray();
		boolean flag = false;

		if (line.contains("\"")) {
			for ( final char element : array ) {
				if (element == '"') {
					if (flag == false) {
						flag = true;
					} else {
						flag = false;
					}
				} else {
					if (flag == true && element == ',') {
						newline = newline + "|";
					} else {
						newline = newline + element;
					}
				}
			}

		} else {
			return line;
		}

		return newline;
	}

}

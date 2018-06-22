package io.spring.configuration;

import java.io.File;
import java.net.InetAddress;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;

import org.springframework.batch.item.ItemProcessor;

public class GeocodingItemProcessor implements ItemProcessor<LogEntry, LogEntry> {

	private DatabaseReader reader;

	public GeocodingItemProcessor(String databaseLocation) throws Exception {
		reader = new DatabaseReader(new File(databaseLocation));
	}

	@Override
	public LogEntry process(LogEntry item) throws Exception {
		try {
			item.setCountryCode(reader.country(InetAddress.getByName(item.getIpAddress())).getCountry().getIsoCode());
		}
		catch (AddressNotFoundException anfe) {
			return null;
		}

		return item;
	}
}

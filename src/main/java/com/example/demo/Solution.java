package com.example.demo;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootApplication
public class Solution {

	public static void main(String[] args) {
		File file = new File("CBOE.csv");

		try {
			FileWriter outputFile = new FileWriter(file);

			CSVWriter writer = new CSVWriter(outputFile);

			Document doc = Jsoup.connect("https://finance.yahoo.com/quote/%5EVIX/history?p=%5EVIX").get();

			Elements tables = doc.select("table");

			if(tables.size() > 0) {

				Elements rows = tables.get(0).select("tr");

				if(rows.size() > 1) {
					Elements headers = rows.get(0).select("th");

					String[] headerText = new String[headers.size()];
					for (int i = 0; i < headers.size(); i++) {
						headerText[i] = headers.get(i).text();
					}

					writer.writeNext(headerText);

					for (int i = 1; i < rows.size(); i++) {
						Elements cols = rows.get(i).select("td");

						String[] data = new String[cols.size()];
						for (int j = 0; j < cols.size(); j++) {
							data[j] = cols.get(j).text();
						}
						writer.writeNext(data);
					}

					writer.close();
				} else {
					throw new RuntimeException("No Data Found");
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}
